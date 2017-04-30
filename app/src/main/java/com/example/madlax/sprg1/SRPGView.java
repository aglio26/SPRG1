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

/**
 * Created by madlax on 2017/03/27.
 */

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
    int charTouchDistance;

    //地形情報
    Terrain[] terrain = new Terrain[3];
    Rect[][] drawingDomain;//描画領域

    //キャラクター
    Character Reimu;
    int Reachable = 4;

    //章
    Chapter chapter1;

    //画像
    Rect src;

    //MapConstant
    private Bitmap imageFlatland;
    private Bitmap imageGrass;
    private Bitmap imageLake;
    public Bitmap imageReimu;

    //システム
    public SurfaceHolder holder;
    public Thread thread;
    public Canvas canvas;

    //シーン
    int SCENE = -1;
    int NEXT_SCENE = -1;
    static int SC_OP = 0;
    static int SC_MAP = 1;
    static int SC_MOVEREADY = 2;
    static int SC_BATTLE = 3;
    static int SC_GAMEOVER = 9;

    /**
     * コンストラクタ（長い）
     */
    public SRPGView(Context context) {
        super(context);

        //generate surface holder
        holder = getHolder();
        holder.addCallback(this);

        //画像の読込
        Resources r = context.getResources();   //リソースのインスタンス生成
        //Bitmapクラスオブジェクトの生成
        imageFlatland = BitmapFactory.decodeResource(r, R.drawable.terrain_flatland);
        imageGrass = BitmapFactory.decodeResource(r, R.drawable.terrain_grass);
        imageLake = BitmapFactory.decodeResource(r, R.drawable.terrain_lake);
        imageReimu = BitmapFactory.decodeResource(r, R.drawable.char_reimu);

        //地形情報生成
        terrain[0] = new Terrain(0, -1, imageFlatland);
        terrain[1] = new Terrain(10, -1, imageGrass);
        terrain[2] = new Terrain(10, -2, imageLake);

        //キャラクター情報生成
        Reimu = new Character(20, 5, 5, 0, 0, imageReimu);

        //1章情報生成
        int[][] FIELD1 = {
                {1,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,2,0,0,0,0,0,0,0,0},
                {0,0,0,1,1,0,0,0,0,0},
                {0,0,1,1,1,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0}};
        Chapter chapter1 = new Chapter(FIELD1);
        numCellX = chapter1.getNumCellX();
        numCellY = chapter1.getNumCellY();

        //セルのサイズ、原点を計算
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        screenWidth = p.x;
        screenHeight = p.y;
        cellSize = Math.min(screenWidth / numCellX, screenHeight / numCellY) - 20;
        originX = (screenWidth - numCellX * cellSize) / 2;
        originY = (screenHeight - numCellY * cellSize) / 2;

        //描画領域を計算
        drawingDomain = new Rect[numCellX][numCellY];
        for(int i=0; i < numCellX; i++) {
            for (int j=0; j < numCellY; j++) {
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
    }

    /**
     *　メソッド
     */
    //キャラクターの移動（character.javaに置くべきメソッド）
    public void moveChar(Character character, int idougoX, int idougoY){
        character.charXcoord = idougoX;
        character.charYcoord = idougoY;
    }

    //画像の描画元領域の取得
    public Rect getSrc(Bitmap bitmap){
        src = new Rect();
        src.bottom = bitmap.getHeight();
        src.right = bitmap.getWidth();
        src.left = 0;
        src.top = 0;
        return src;
    }

    //画面のロック、アンロック、スリープメソッド //todo:タッチ入力待ち受け関数の実装 画像読み込み関数の実装
    public void lock(){
        canvas = holder.lockCanvas();
    }
    public void unlock(){
        holder.unlockCanvasAndPost(canvas);
    }
    public void sleep(int time){//delay method
        try {
            Thread.sleep(time);
        }catch(Exception e){
        }
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
        while (thread != null) {
            SCENE = NEXT_SCENE;
            if (SCENE == SC_OP) {//todo:OP画像の表示及びBGMの実装
                lock();
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setTextSize(48);
                canvas.drawText("OP" + "X:" + screenWidth + "Y:" + screenHeight + "CELL:" + cellSize, 50, 50, paint);
                Paint paint1 = new Paint();
                paint1.setColor(Color.BLUE);
                paint1.setTextSize(48);
                canvas.drawText("Mapdata origin:" + drawingDomain[0][0].left + "," + drawingDomain[0][0].top, 50, 100, paint1);
                Paint paint2 = new Paint();
                paint2.setColor(Color.BLUE);
                paint2.setTextSize(48);
                canvas.drawText("touchX:" + (int)touchX + "touchY:" + (int)touchY, 50, 150, paint);
                Paint paint3 = new Paint();
                paint3.setColor(Color.RED);
                paint3.setStyle(Paint.Style.STROKE);
                for(int i = 0; i < numCellX; i++){
                    for(int j = 0; j < numCellY; j++){
                        canvas.drawRect(drawingDomain[i][j],paint3);
                    }
                }
                Paint paint4 = new Paint();
                paint2.setColor(Color.BLUE);
                paint2.setTextSize(48);
                canvas.drawText("cellX:"+ touchXcoord + "cellY:" + touchYcoord, 50, 200, paint);
                unlock();
                sleep(0);
            }
            if (SCENE == SC_MAP) {//todo：MAP描画の実装　キャラクター描画（カズさん担当）
                lock();
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setTextSize(48);
                canvas.drawText("field", 60, 50, paint);
                Paint paint1 = new Paint();
                paint1.setTextSize(48);
                canvas.drawText("Touch:"+ touchXcoord + "," + touchYcoord + "Character:"
                        + charXcoord + "," + charYcoord, 60, 100, paint1);
                for(int i = 0; i< numCellX; i++){
                    for(int j = 0; j< numCellY; j++) {
                        canvas.drawBitmap(terrain[chapter1.field[i][j]].terrainImage,
                                getSrc(terrain[chapter1.field[i][j]].terrainImage), drawingDomain[i][j], null);
                    }
                }
                canvas.drawBitmap(Reimu.charImage, getSrc(Reimu.charImage),
                        Reimu.getCharDomain(originX, originY, cellSize, Reimu.charXcoord, Reimu.charYcoord), null);
                unlock();
                sleep(600);
            }
            if (SCENE == SC_MOVEREADY) {// todo:移動可能領域の取得、及び領域の描画の実装　
                lock();
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setTextSize(48);
                canvas.drawText("MOVE_READY",70,50,paint);
                Paint paint1 = new Paint();
                paint1.setTextSize(48);
                canvas.drawText("Touch:"+ touchXcoord + "," + touchYcoord + "Character:"
                        + charXcoord + "," + charYcoord, 60, 100, paint1);
                for(int i = 0; i< numCellX; i++){
                    for(int j = 0; j< numCellY; j++) {
                        canvas.drawBitmap(terrain[chapter1.field[i][j]].terrainImage,
                                getSrc(terrain[chapter1.field[i][j]].terrainImage), drawingDomain[i][j], null);
                    }
                }
                canvas.drawBitmap(Reimu.charImage, getSrc(Reimu.charImage),
                        Reimu.getCharDomain(originX, originY, cellSize, Reimu.charXcoord, Reimu.charYcoord), null);
                unlock();
                sleep(0);
            }
            if (SCENE == SC_GAMEOVER) {
                lock();
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setTextSize(48);
                canvas.drawText("GAMEOVER",80,50,paint);
                unlock();
                sleep(0);
                SCENE = SC_OP;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();
        if(originX < touchX && touchX < numCellX * cellSize + originX && originY < touchY
                && touchY < numCellY * cellSize + originY) {
            touchXcoord = ((int) touchX - originX) / cellSize;
            touchYcoord = ((int) touchY - originY) / cellSize;
            charTouchDistance = Math.abs(touchXcoord - charXcoord)
                    + Math.abs(touchYcoord - charYcoord);
        }
        if(SCENE == SC_OP){
            switch ( event.getAction() ) {

                case MotionEvent.ACTION_DOWN:
                    //画面がタッチされたときの動作

                    NEXT_SCENE = SC_MAP;
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
        else if(SCENE == SC_MAP){
            switch ( event.getAction() ) {

                case MotionEvent.ACTION_DOWN:
                    //画面がタッチされたときの動作
                    if(charTouchDistance == 0 && originX < touchX && touchX < numCellX * cellSize
                            + originX && originY < touchY && touchY < numCellY * cellSize + originY){//todo:ifタッチ箇所==キャラクター
                        NEXT_SCENE = SC_MOVEREADY;
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
        else if(SCENE == SC_MOVEREADY) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    //画面がタッチされたときの動作
                    if (charTouchDistance <= Reachable) {//todo:ifタッチ箇所==キャラクター
                        charXcoord = touchXcoord;
                        charYcoord = touchYcoord;
                        moveChar(Reimu, touchXcoord, touchYcoord);
                        NEXT_SCENE = SC_MAP;
                    }
                    if (charTouchDistance > Reachable){
                        NEXT_SCENE = SC_MAP;
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
        return true;
    }//todo:タッチ待ち受け　タッチによるキャラクターの移動の実装  アニメーション　SE 戦闘
}