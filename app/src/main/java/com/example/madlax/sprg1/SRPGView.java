package com.example.madlax.sprg1;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Display;
import android.graphics.Point;
import android.view.MotionEvent;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Rect;
import java.util.Random;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
/**
 * Created by madlax on 2017/03/27.
 */
//comment from lab 6/14
public class SRPGView extends SurfaceView
        implements SurfaceHolder.Callback, Runnable {
    //フィールド変数
    int screenWidth;    //画面横幅
    int screenHeight;   //画面縦幅
    int cellSize;       //セルサイズ
    int numCellX;       //セル数横
    int numCellY;       //セル数縦
    int originX;        //座標原点X
    int originY;        //座標原点Y
    float touchX;       //タッチ座標X
    float touchY;       //タッチ座標Y
    int touchXcoord;    //タッチしたセルの座標X
    int touchYcoord;    //タッチしたセルの座標Y
    int charXcoord;     //キャラクターの座標X
    int charYcoord;     //キャラクターの座標Y
    int charTouchDistance = 0;
    Rect charAnime;
    int dx;
    int dy;
    //地形情報
    Terrain[] terrain = new Terrain[3];
    Rect[][] drawingDomain;//描画領域

    //キャラクター
    Character Reimu;
    Character Marisa;
    Character Sakuya;
    Character moveScheduleChar;

    //武器
    Weapon ironSword;

    //章
    Chapter chapter1;

    //画像
    Rect src;

    //MapConstant
    private Bitmap imageFlatland;
    private Bitmap imageFlatland_blue;
    private Bitmap imageFlatland_red;
    private Bitmap imageGrass;
    private Bitmap imageGrass_blue;
    private Bitmap imageGrass_red;
    private Bitmap imageLake;
    private Bitmap imageLake_blue;
    private Bitmap imageLake_red;
    public Bitmap imageReimu;
    public Bitmap imageMarisa;
    public Bitmap imageSakuya;
    public Bitmap imageStatus;

    //システム
    public SurfaceHolder holder;
    public Thread thread;
    public Canvas canvas;
    public Canvas canvasMap;
    public Canvas canvasMOVEREADY;

    //シーン
    int SCENE = -1;
    int NEXT_SCENE = -1;
    static int SC_OP = 0;
    static int SC_MAP = 1;
    static int SC_MOVEREADY = 2;
    static int SC_MOVE = 3;
    static int SC_ACT = 4;
    static int SC_BATTLE = 5;
    static int SC_CLEAR = 6;
    static int SC_STATUS = 7;
    static int SC_GAMEOVER = 9;
    //frag 管理
    private boolean touchCharFlag = false;
    private boolean chapterLoop =true;
    private boolean OP = true;
    private boolean MAP = false;
    private boolean MOVEREADY = false;
    private boolean MOVE = false;

    /**
     * コンストラクタ（長い）
     */
    public SRPGView(Context context) {
        super(context);

        //generate surface holder
        holder = getHolder();
        holder.addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setFocusable(true);
        setZOrderOnTop(true);

        //画像の読込
        Resources r = context.getResources();   //リソースのインスタンス生成
        //Bitmapクラスオブジェクトの生成
        imageFlatland = BitmapFactory.decodeResource(r, R.drawable.terrain_flatland);
        imageFlatland_blue = BitmapFactory.decodeResource(r, R.drawable.terrain_flatland_blue);
        imageFlatland_red = BitmapFactory.decodeResource(r, R.drawable.terrain_flatland_red);
        imageGrass = BitmapFactory.decodeResource(r, R.drawable.terrain_grass);
        imageGrass_blue = BitmapFactory.decodeResource(r, R.drawable.terrain_grass_blue);
        imageGrass_red = BitmapFactory.decodeResource(r, R.drawable.terrain_grass_red);
        imageLake = BitmapFactory.decodeResource(r, R.drawable.terrain_lake);
        imageLake_blue = BitmapFactory.decodeResource(r, R.drawable.terrain_lake_blue);
        imageLake_red = BitmapFactory.decodeResource(r, R.drawable.terrain_lake_red);
        imageReimu = BitmapFactory.decodeResource(r, R.drawable.char_reimu);
        imageMarisa = BitmapFactory.decodeResource(r, R.drawable.char_marisa);
        imageSakuya = BitmapFactory.decodeResource(r, R.drawable.char_sakuya);
        imageStatus = BitmapFactory.decodeResource(r, R.drawable.status);

        //地形情報生成
        terrain[0] = new Terrain(0, 1, imageFlatland, imageFlatland_blue, imageFlatland_red);
        terrain[1] = new Terrain(10, 2, imageGrass, imageGrass_blue, imageGrass_red);
        terrain[2] = new Terrain(10, 99, imageLake, imageLake_blue, imageLake_red);

        //武器情報生成
        ironSword = new Weapon(5, 90, 0, 1);

        //1章情報生成
        int[][] FIELD1 = {
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 2, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
                {0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        chapter1 = new Chapter(FIELD1);
        numCellX = chapter1.getNumCellX();
        numCellY = chapter1.getNumCellY();
        //キャラクター情報生成
        Reimu = new Character(1, 20, 7, 7, 7, 7, 7, 7, 7, 5, 0, 0, imageReimu, ironSword, chapter1);
        Marisa = new Character(1, 20, 7, 7, 7, 7, 7, 7, 7, 5, 6, 0, imageMarisa, ironSword, chapter1);
        Sakuya = new Character(1, 20, 5, 5, 5, 5, 5, 5, 5, 5, 3, 0, imageSakuya, ironSword, chapter1);

        //セルのサイズ、原点を計算
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        screenWidth = p.x;
        screenHeight = p.y;
        cellSize = Math.min(screenWidth / numCellX, screenHeight / numCellY) - 20;
        originX = (screenWidth - numCellX * cellSize) / 2;
        originY = (screenHeight - numCellY * cellSize) / 2;

        //描画領域を計算
        drawingDomain = new Rect[numCellX][numCellY];
        for (int i = 0; i < numCellX; i++) {
            for (int j = 0; j < numCellY; j++) {
                drawingDomain[i][j] = new Rect();
                drawingDomain[i][j].left = originX + i * cellSize;
                drawingDomain[i][j].top = originY + j * cellSize;
                drawingDomain[i][j].right = originX + (1 + i) * cellSize;
                drawingDomain[i][j].bottom = originY + (1 + j) * cellSize;
            }
        }

        //初期化
        touchXcoord = 0;
        touchYcoord = 0;
        charXcoord = 0;
        charYcoord = 0;

        //初期シーン（オープニング）
        NEXT_SCENE = 0;
        //アニメーション初期化
        charAnime = new Rect();
    }

    /**
     * 　メソッド
     */
    //キャラクターの移動（character.javaに置くべきメソッド？）
    public void moveChar(Character character, int idougoX, int idougoY) {
        character.charXcoord = idougoX;
        character.charYcoord = idougoY;
    }
    //キャラクター座標の特定
    public Character touchChar(int touchX,int touchY){
        if(Reimu.getCharaX()-touchX==0&&Reimu.getCharaY()-touchY==0){
            return Reimu;
        }
        else if (Sakuya.getCharaX()-touchX==0&&Sakuya.getCharaY()-touchY==0) {
            return Sakuya;
        }
        else if  (Marisa.getCharaX()-touchX==0&&Marisa.getCharaY()-touchY==0)
            return Marisa;
        else
            return null;
    }

    //対象キャラクターの移動情報書き込み
    public void judgeCell(Character character, Chapter chapter, Terrain[] terrain) {
        boolean loop = true;
        character.cell[character.getCharaX()][character.getCharaY()].tansakuzumi = true;
        character.cell[character.getCharaX()][character.getCharaY()].moveVariable = character.getMovement();
        while (loop == true) {
            loop = false;
            for (int i = 0; i < chapter.getNumCellX(); i++) {
                for (int j = 0; j < chapter.getNumCellY(); j++) {
                    if (character.cell[i][j].tansakuzumi == true) {
                        character.cell[i][j].writecell(i, j, character.cell, chapter, terrain, loop);
                    }
                }
            }
        }
    }

    //画像の描画元領域の取得
    public Rect getSrc(Bitmap bitmap) {
        src = new Rect();
        src.bottom = bitmap.getHeight();
        src.right = bitmap.getWidth();
        src.left = 0;
        src.top = 0;
        return src;
    }


    //画面のロック、アンロック、スリープメソッド //todo:タッチ入力待ち受け関数の実装 画像読み込み関数の実装
    public void lock() {
        canvas = holder.lockCanvas();
    }

    public void unlock(Canvas canvas) {
        holder.unlockCanvasAndPost(canvas);
    }

    public void sleep(int time) {//delay method
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }

    //キャラクター描画メソッド
    public void drawingCharacter(Character character) {
        canvas.drawBitmap(character.charImage, getSrc(character.charImage),
                character.getCharDomain(originX, originY, cellSize, character.charXcoord, character.charYcoord), null);
    }

    //謎のメソッド集
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new Thread(this);
        thread.start();
    }


    public void surfaceDestroyed(SurfaceHolder holder) {
        thread = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    }

    /**
     * メイン処理
     */
    public void run() {
        //OP　処理
        lock();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Paint paint_DefaultChar = new Paint();
        paint_DefaultChar.setColor(Color.BLUE);
        paint_DefaultChar.setTextSize(48);
        canvas.drawText("OP" + "X:" + screenWidth + "Y:" + screenHeight + "CELL:" + cellSize, 50, 50, paint_DefaultChar);
        canvas.drawText("Mapdata origin:" + drawingDomain[0][0].left + "," + drawingDomain[0][0].top, 50, 100, paint_DefaultChar);
        canvas.drawText("touchX:" + (int) touchX + "touchY:" + (int) touchY, 50, 150, paint_DefaultChar);
        Paint paint_CellTest = new Paint();
        paint_CellTest.setColor(Color.RED);
        paint_CellTest.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < numCellX; i++) {
            for (int j = 0; j < numCellY; j++) {
                canvas.drawRect(drawingDomain[i][j], paint_CellTest);
            }
        }
        canvas.drawText("cellX:" + touchXcoord + "cellY:" + touchYcoord, 50, 200, paint_DefaultChar);
        unlock(canvas);
        canvas.drawColor(0, Mode.CLEAR);
        while (OP == true) {
            sleep(1000);
        }//待機処理


        while(chapterLoop == true) {
            //MAP処理
            canvasMap = holder.lockCanvas();
            canvasMap.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            for (int i = 0; i < numCellX; i++) {
                for (int j = 0; j < numCellY; j++) {
                    canvasMap.drawBitmap(terrain[chapter1.field[i][j]].terrainImage,
                            getSrc(terrain[chapter1.field[i][j]].terrainImage), drawingDomain[i][j], null);
                }
            }
            canvasMap.drawText("SC_MAP", 60, 50, paint_DefaultChar);
            canvasMap.drawText("Touch:" + touchXcoord + "," + touchYcoord + "Character:" + charXcoord + "," + charYcoord, 60, 100, paint_DefaultChar);
            drawingCharacter(Reimu);
            drawingCharacter(Marisa);
            unlock(canvasMap);
            canvasMap.drawColor(0, Mode.CLEAR);
            while (MAP == true) {
                sleep(10);
            }//待機処理

            //MoveReady処理
            if(MOVEREADY ==true) {
                canvasMOVEREADY = holder.lockCanvas();
                canvasMOVEREADY.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                judgeCell(moveScheduleChar, chapter1, terrain);
                canvasMap.drawText("MOVE_READY", 70, 50, paint_DefaultChar);
                canvasMap.drawText("Touch:" + touchXcoord + "," + touchYcoord + "Character:"
                        + charXcoord + "," + charYcoord, 60, 100, paint_DefaultChar);
                    for (int i = 0; i < numCellX; i++) {
                        for (int j = 0; j < numCellY; j++) {
                            canvasMOVEREADY.drawBitmap(terrain[chapter1.field[i][j]].terrainImage,
                                    getSrc(terrain[chapter1.field[i][j]].terrainImage), drawingDomain[i][j], null);
                            if (moveScheduleChar.cell[i][j].moveVariable >= 0) {
                                canvasMOVEREADY.drawBitmap(terrain[chapter1.field[i][j]].terrainImage_blue,
                                        getSrc(terrain[chapter1.field[i][j]].terrainImage_blue), drawingDomain[i][j], null);
                            }
                        }
                    }
                drawingCharacter(Reimu);
                drawingCharacter(Marisa);
                unlock(canvasMOVEREADY);
                while (MOVEREADY == true) {
                    sleep(10);
                }
            }
        }
    }


//
//            if (SCENE == SC_ACT) {
//                lock();
//                canvas.drawColor(Color.WHITE);
//                Paint paint = new Paint();
//                paint.setColor(Color.BLUE);
//                paint.setTextSize(48);
//                canvas.drawText("ACT", 80, 50, paint);
//                Paint paint1 = new Paint();
//                paint1.setTextSize(48);
//                canvas.drawText("Touch:"+ touchXcoord + "," + touchYcoord + "Character:"
//                        + charXcoord + "," + charYcoord, 60, 100, paint1);
//                for(int i = 0; i< numCellX; i++){
//                    for(int j = 0; j< numCellY; j++) {
//                        canvas.drawBitmap(terrain[chapter1.field[i][j]].terrainImage,
//                                getSrc(terrain[chapter1.field[i][j]].terrainImage), drawingDomain[i][j], null);
//                    }
//                }
//                drawingCharacter(Reimu);
//                drawingCharacter(Marisa);
//                drawingCharacter(Sakuya);
//                if (Math.abs(Reimu.charXcoord - Sakuya.charXcoord) + Math.abs(Reimu.charYcoord - Sakuya.charYcoord) == 1) {
//                    Paint paint2 = new Paint();
//                    paint2.setTextSize(48);
//                    canvas.drawText("Attack:", 80, 150, paint2);
//                    int hit1;
//                    hit1 = Reimu.getHitRate() - Sakuya.getAvoid();
//                    int dmg1;
//                    dmg1 = Reimu.getAttackPower() - Sakuya.getDeffencePower();
//                    int crt1;
//                    crt1 = Reimu.getCritical() - Sakuya.getDodge();
//                    Paint paint3 = new Paint();
//                    paint3.setTextSize(48);
//                    canvas.drawText("Reimu    HP:" + Reimu.getHitPoint() + " HIT:" + hit1 + " DMG:" + dmg1
//                            + " CRT:" + crt1, 80, 200, paint3);
//                    int hit2;
//                    hit2 = Sakuya.getHitRate() - Reimu.getAvoid();
//                    int dmg2;
//                    dmg2 = Sakuya.getAttackPower() - Reimu.getDeffencePower();
//                    int crt2;
//                    crt2 = Sakuya.getCritical() - Reimu.getDodge();
//                    Paint paint4 = new Paint();
//                    paint4.setTextSize(48);
//                    canvas.drawText("Sakuya  HP:"+ Sakuya.getHitPoint() + " HIT:" + hit2 + " DMG:" + dmg2
//                            + " CRT:" + crt2, 80, 250, paint4);
//                }
//                unlock();
//                sleep(600);
//            }
//
//            if (SCENE == SC_BATTLE) {
//                lock();
//                Random rand = new Random();
//                Paint paint1 = new Paint();
//                canvas.drawColor(Color.WHITE);
//                paint1.setTextSize(48);
//                Paint paint2 = new Paint();
//                paint2.setTextSize(48);
//                if (Reimu.getHitRate() - Sakuya.getAvoid() >= rand.nextInt(100)) {
//                    int damage;
//                    damage = Math.max(Reimu.getAttackPower() - Sakuya.getDeffencePower(), 0);
//                    Sakuya.setHitPoint(Sakuya.getHitPoint() - damage);
//                    canvas.drawText("Reimu -> Sakuya: HIT!  Sakuya:" + Sakuya.getHitPoint(), 80, 200, paint1);
//                }
//                else{
//                    canvas.drawText("Reimu -> Sakuya: MISS! Sakuya:" + Sakuya.getHitPoint(), 80, 200, paint1);
//                }
//                if (Sakuya.getHitPoint() > 0 && Sakuya.getHitRate() - Reimu.getAvoid() >= rand.nextInt(100)) {
//                    int damage;
//                    damage = Math.max(Sakuya.getAttackPower() - Reimu.getDeffencePower(), 0);
//                    Reimu.setHitPoint(Reimu.getHitPoint() - damage);
//                    canvas.drawText("Sakuya -> Reimu: HIT!  Reimu:" + Reimu.getHitPoint(), 80, 250, paint2);
//                }
//                else{
//                    canvas.drawText("Sakuya -> Reimu: MISS! Reimu:" + Reimu.getHitPoint(), 80, 250, paint2);
//                }
//                Paint paint3 = new Paint();
//                paint3.setColor(Color.BLUE);
//                paint3.setTextSize(48);
//                canvas.drawText("BATTLE", 80, 100, paint3);
//                unlock();
//                sleep(2400);
//                if(Reimu.getHitPoint() <= 0 ) {
//                    NEXT_SCENE = SC_GAMEOVER;
//                }
//                else if(Sakuya.getHitPoint() <= 0){
//                    NEXT_SCENE = SC_CLEAR;
//                }
//                else{
//                    NEXT_SCENE = SC_MAP;
//                }
//            }
//            if (SCENE == SC_CLEAR) {
//                lock();
//                canvas.drawColor(Color.WHITE);
//                Paint paint = new Paint();
//                paint.setColor(Color.BLUE);
//                paint.setTextSize(48);
//                canvas.drawText("CLEAR",80,50,paint);
//                Rect dst = new Rect();
//                dst.left = originX;
//                dst.top = originY;
//                dst.right = originX + cellSize * numCellX;
//                dst.bottom = originY + cellSize * numCellY;
//                canvas.drawBitmap(imageStatus, getSrc(imageStatus), dst, null);
//                unlock();
//                sleep(0);
//                SCENE = SC_OP;
//            }
//            if (SCENE == SC_GAMEOVER) {
//                lock();
//                canvas.drawColor(Color.WHITE);
//                Paint paint = new Paint();
//                paint.setColor(Color.BLUE);
//                paint.setTextSize(48);
//                canvas.drawText("GAMEOVER",80,50,paint);
//                unlock();
//                sleep(0);
//                SCENE = SC_OP;
//            }
//        }
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //タッチ場所端末ディスプレイ座標取得
        touchX = event.getX();
        touchY = event.getY();
        //タッチ場所ゲーム座標の導出
        if (originX < touchX && touchX < numCellX * cellSize + originX && originY < touchY
                && touchY < numCellY * cellSize + originY) {
            touchXcoord = ((int) touchX - originX) / cellSize;
            touchYcoord = ((int) touchY - originY) / cellSize;
        }


        if (OP == true) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    //画面がタッチされたときの動作
                    OP = false;
                    MAP = true;
                    break;

                case MotionEvent.ACTION_MOVE:
                    //タッチしたまま移動したときの動作
                    break;

                case MotionEvent.ACTION_UP:
                    //タッチが離されたときの動作
                    break;

                case MotionEvent.ACTION_CANCEL:
                    //他の要因によってタッチがキャンセルされたときの動作
                    break;

            }
        }
        else if (MAP == true) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //画面がタッチされたときの動作
                        if(touchChar(touchXcoord,touchYcoord)!=null){
                            touchCharFlag = true;
                        }
                        if (touchCharFlag == true && originX < touchX && touchX < numCellX * cellSize
                                + originX && originY < touchY && touchY < numCellY * cellSize + originY) {
                            moveScheduleChar = touchChar(touchXcoord,touchYcoord);
                            touchCharFlag = false;
                            MAP = false;
                            MOVEREADY = true;
                        }
                        else{
                            touchCharFlag = false;
                            moveScheduleChar = null;
                            MAP = true;
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        //タッチしたまま移動したときの動作
                        break;

                    case MotionEvent.ACTION_UP:
                        //タッチが離されたときの動作
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        //他の要因によってタッチがキャンセルされたときの動作
                        break;
                }
        }
        else if (MOVEREADY == true){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //画面がタッチされたときの動作
                    if (moveScheduleChar.cell[touchXcoord][touchYcoord].moveVariable >= 0) {
                        moveChar(moveScheduleChar, touchXcoord, touchYcoord);
                        moveScheduleChar.resetCell(chapter1);
                        moveScheduleChar = null;
                        touchCharFlag = false;
                        MOVEREADY = false;
                        MAP = true;

                    }
                    else if (moveScheduleChar.cell[touchXcoord][touchYcoord].moveVariable < 0){
                        moveScheduleChar = null;
                        touchCharFlag = false;
                        MOVEREADY = false;
                        MAP = true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    //タッチしたまま移動したときの動作
                    break;

                case MotionEvent.ACTION_UP:
                    //タッチが離されたときの動作
                    break;

                case MotionEvent.ACTION_CANCEL:
                    //他の要因によってタッチがキャンセルされたときの動作
                    break;
            }
        }

        //todo:タッチ待ち受け　タッチによるキャラクターの移動の実装  アニメーション　SE 戦闘
        return true;
    }
}

//
//        if(SCENE == SC_OP){
//            switch ( event.getAction() ) {
//
//                case MotionEvent.ACTION_DOWN:
//                    //画面がタッチされたときの動作
//
//                    NEXT_SCENE = SC_MAP;
//                    break;
//
//                case MotionEvent.ACTION_MOVE:
//                    //タッチしたまま移動したときの動作
//                    break;
//
//                case MotionEvent.ACTION_UP:
//                    //タッチが離されたときの動作
//                    break;
//
//                case MotionEvent.ACTION_CANCEL:
//                    //他の要因によってタッチがキャンセルされたときの動作
//                    break;
//
//            }
//        }
//        else if(SCENE == SC_MAP){
//            switch ( event.getAction() ) {
//
//                case MotionEvent.ACTION_DOWN:
//                    //画面がタッチされたときの動作
//                    if(charTouchDistance == 0 && originX < touchX && touchX < numCellX * cellSize
//                            + originX && originY < touchY && touchY < numCellY * cellSize + originY){
//                        NEXT_SCENE = SC_MOVEREADY;
//                    }
//                    break;
//
//                case MotionEvent.ACTION_MOVE:
//                    //タッチしたまま移動したときの動作
//                    break;
//
//                case MotionEvent.ACTION_UP:
//                    //タッチが離されたときの動作
//                    break;
//
//                case MotionEvent.ACTION_CANCEL:
//                    //他の要因によってタッチがキャンセルされたときの動作
//                    break;
//
//            }
//
//        }
//        else if(SCENE == SC_MOVEREADY) {
//            switch (event.getAction()) {
//
//                case MotionEvent.ACTION_DOWN:
//                    //画面がタッチされたときの動作
//                    if (Reimu.cell[touchXcoord][touchYcoord].moveVariable >= 0) {//todo:ifタッチ箇所==キャラクター
//                        charXcoord = touchXcoord;
//                        charYcoord = touchYcoord;
//                        charAnime = Reimu.getCharDomain(originX, originY, cellSize, Reimu.charXcoord, Reimu.charYcoord);
//                        dx = ((originX + cellSize * touchXcoord) - (originX + cellSize * Reimu.charXcoord));
//                        dy = ((originY + cellSize * touchYcoord) - (originY + cellSize * Reimu.charYcoord));
//                        NEXT_SCENE = SC_MOVE;
//                    }
//                    if (Reimu.cell[touchXcoord][touchYcoord].moveVariable < 0){
//                        NEXT_SCENE = SC_MAP;
//                    }
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    //タッチしたまま移動したときの動作
//                    break;
//
//                case MotionEvent.ACTION_UP:
//                    //タッチが離されたときの動作
//                    break;
//
//                case MotionEvent.ACTION_CANCEL:
//                    //他の要因によってタッチがキャンセルされたときの動作
//                    break;
//
//            }
//        }
//        else  if (SCENE == SC_ACT){
//            switch ( event.getAction() ) {
//
//                case MotionEvent.ACTION_DOWN:
//                    //画面がタッチされたときの動作
//                    if (Math.abs(Reimu.charXcoord - Sakuya.charXcoord)
//                            + Math.abs(Reimu.charYcoord - Sakuya.charYcoord) == 1) {
//                        NEXT_SCENE = SC_BATTLE;
//                    }
//                    else {
//                        NEXT_SCENE = SC_MAP;
//                    }
//                    break;
//
//                case MotionEvent.ACTION_MOVE:
//                    //タッチしたまま移動したときの動作
//                    break;
//
//                case MotionEvent.ACTION_UP:
//                    //タッチが離されたときの動作
//                    break;
//
//                case MotionEvent.ACTION_CANCEL:
//                    //他の要因によってタッチがキャンセルされたときの動作
//                    break;
//            }
//        }
//        else  if (SCENE == SC_BATTLE){
//            switch ( event.getAction() ) {
//
//                case MotionEvent.ACTION_DOWN:
//                    //画面がタッチされたときの動作
//                    break;
//
//                case MotionEvent.ACTION_MOVE:
//                    //タッチしたまま移動したときの動作
//                    break;
//
//                case MotionEvent.ACTION_UP:
//                    //タッチが離されたときの動作
//                    break;
//
//                case MotionEvent.ACTION_CANCEL:
//                    //他の要因によってタッチがキャンセルされたときの動作
//                    break;
//            }
//        }
//        else if(SCENE == SC_CLEAR){
//            switch ( event.getAction() ) {
//
//                case MotionEvent.ACTION_DOWN:
//                    //画面がタッチされたときの動作
//                    NEXT_SCENE = SC_OP;
//                    break;
//
//                case MotionEvent.ACTION_MOVE:
//                    //タッチしたまま移動したときの動作
//                    break;
//
//                case MotionEvent.ACTION_UP:
//                    //タッチが離されたときの動作
//                    break;
//
//                case MotionEvent.ACTION_CANCEL:
//                    //他の要因によってタッチがキャンセルされたときの動作
//                    break;
//            }
//        }
//        else if(SCENE == SC_GAMEOVER){
//            switch ( event.getAction() ) {
//
//                case MotionEvent.ACTION_DOWN:
//                    //画面がタッチされたときの動作
//                    NEXT_SCENE = SC_OP;
//                    break;
//
//                case MotionEvent.ACTION_MOVE:
//                    //タッチしたまま移動したときの動作
//                    break;
//
//                case MotionEvent.ACTION_UP:
//                    //タッチが離されたときの動作
//                    break;
//
//                case MotionEvent.ACTION_CANCEL:
//                    //他の要因によってタッチがキャンセルされたときの動作
//                    break;
//            }
//        }
//        return true;
//    }//todo:タッチ待ち受け　タッチによるキャラクターの移動の実装  アニメーション　SE 戦闘