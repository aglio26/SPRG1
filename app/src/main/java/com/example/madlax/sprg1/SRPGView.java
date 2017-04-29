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
    //field variable
    int screenWidth;    //画面横幅
    int screenHeight;   //画面縦幅
    int cellSize;       //セルサイズ
    int numCellX;       //セル数横
    int numCellY;       //セル数縦
    int originX;        //座標原点X
    int originY;        //座標原点Y
    float touchX;       //タッチ座標X
    float touchY;       //タッチ座標Y
    int touchXcoord;   //タッチしたセルの座標X
    int touchYcoord;   //タッチしたセルの座標Y
    int charaXcoord;   //キャラクターの座標X
    int charaYcoord;   //キャラクターの座標Y
    int charaTouchDistance;

    //地形情報
    Rect src;
    Rect[][] drawDomain;//描画領域
    Chapter chapter1;
    int[][] C1 = {
            {1,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,2,0,0,0,0,0,0,0,0},
            {0,0,0,1,1,0,0,0,0,0},
            {0,0,1,1,1,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0}};
    Terrain[][] terrain1;

    //キャラクター
    Character Reimu;
    //SystemConstant
    public SurfaceHolder holder;
    public Thread thread;
    public Canvas canvas;
    //SceneConstant
    int SCENE = -1;
    int NEXT_SCENE = -1;
    static int SC_OP = 0;
    static int SC_MAP = 1;
    static int SC_MOVEREADY = 2;
    static int SC_BATTLE = 3;
    static int SC_GAMEOVER = 9;
    //CharactorConstant
    int Reachable = 4;
    //MapConstant
    int[][] Map;
    private Bitmap map0;
    private Bitmap map1;
    private Bitmap map2;
    public Bitmap reimu;

    //user defined method //todo:タッチ入力待ち受け関数の実装 画像読み込み関数の実装
    public void lock(){
        canvas = holder.lockCanvas();
    }//surface lock
    public void unlock(){
        holder.unlockCanvasAndPost(canvas);
    }//sruface unlock
    public void sleep(int time){//delay method
        try {
            Thread.sleep(time);
        }catch(Exception e){
        }
    }
    public void moveChara(Character character, int idougoX, int idougoY){
        character.characterCoordinate[0]=idougoX;
        character.characterCoordinate[1]=idougoY;
    }

    public int get_TouchMapID(int coordinate_x,int coordinate_y){
        return Map[coordinate_x][coordinate_y];
    }
    //地形情報生成
    public void generateTerrain(int n,int m,int originX,int originY,int cell,int chapterMap[][],Terrain terrain[][]){
        for(int i=0;i<n;i++) {
            for (int j=0;j<m;j++) {
                terrain[i][j].terrainCoordinate[0] = i;
                terrain[i][j].terrainCoordinate[1] = j;
                terrain[i][j].terrainDomain.left = originX + i * cell;
                terrain[i][j].terrainDomain.top = originY + j * cell;
                terrain[i][j].terrainDomain.right = originX + (1 + i) * cell;
                terrain[i][j].terrainDomain.bottom = originY + (1 + j) * cell;

                if (chapterMap[i][j] == 0) {
                    terrain[i][j].avoid = 10;
                    terrain[i][j].moveCost = -1;
                    terrain[i][j].terrainPicture = map0;
                }
                if (chapterMap[i][j] == 1) {
                    terrain[i][j].avoid = 10;
                    terrain[i][j].moveCost = -1;
                    terrain[i][j].terrainPicture = map1;
                }
                if (chapterMap[i][j] == 2){
                    terrain[i][j].avoid = 10;
                    terrain[i][j].moveCost = -1;
                    terrain[i][j].terrainPicture = map2;
                }
            }
        }
    }
    //地形情報初期化
    public void initTerrain(Terrain terrain){
        terrain = new Terrain();
    }
    //画像の描画元領域の取得
    public Rect getSrc(Bitmap bitmap){
        src = new Rect();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        src.bottom = height;
        src.right = width;
        src.left = 0;
        src.top = 0;
        return src;
    }

    //Constructor
    public SRPGView(Context context) {
        super(context);
        //generate surface holder
        holder = getHolder();
        holder.addCallback(this);
        //get cellSize number
        chapter1 = new Chapter(8,10,C1);
        numCellX = chapter1.getNumCellX();
        numCellY = chapter1.getNumCellY();
        //配列の初期化
        drawDomain = new Rect[numCellX][numCellY];
        Map = new int[numCellX][numCellY];
        terrain1 = new Terrain[numCellX][numCellY];
        for(int i = 0; i< numCellX; i++) {
            for (int j = 0; j < numCellY; j++) {
                terrain1[i][j] = new Terrain();
            }
        }
        //get screen size and get origin,cellSize
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        screenWidth = p.x;
        screenHeight = p.y;
        cellSize = Math.min(screenWidth / numCellX, screenHeight / numCellY)-20;
        originX = (screenWidth - numCellX * cellSize)/2;
        originY = (screenHeight - numCellY * cellSize)/2;
        //initialize each array
        touchXcoord = 0;
        touchYcoord = 0;
        charaXcoord = 0;
        charaYcoord = 0;
        //画像の読込
        Resources r = context.getResources();                                   //リソースのインスタンス生成
        map0 = BitmapFactory.decodeResource(r, R.drawable.terrain_flatland);    //Bitmapクラスオブジェクトの生成
        map1 = BitmapFactory.decodeResource(r, R.drawable.terrain_grass);
        map2 = BitmapFactory.decodeResource(r, R.drawable.terrain_lake);
        reimu = BitmapFactory.decodeResource(r, R.drawable.char_reimu);
        //地形情報生成
        generateTerrain(numCellX, numCellY, originX, originY, cellSize, chapter1.MAP, terrain1);
        //キャラクター情報生成
        Reimu = new Character(20,5,5,0,0,reimu);
        //scene constant
        NEXT_SCENE = 0;
    }

    //Surface method
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new Thread(this);
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        thread = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    }


    public void run() {
        while (thread != null) {
            SCENE = NEXT_SCENE;
            if (SCENE == SC_OP) {//todo:OP画像の表示及びBGMの実装
                lock();
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setTextSize(48);
                canvas.drawText("OP"+"X:"+ screenWidth +"Y:"+ screenHeight +"CELL:"+ cellSize,50,50,paint);
                Paint paint1 = new Paint();
                paint1.setColor(Color.BLUE);
                paint1.setTextSize(48);
                canvas.drawText("Mapdata origin:" + terrain1[0][0].terrainDomain.left + ","
                        + terrain1[0][0].terrainDomain.top, 50, 100, paint1);
                Paint paint2 = new Paint();
                paint2.setColor(Color.BLUE);
                paint2.setTextSize(48);
                canvas.drawText("touchX:"+(int)touchX+"touchY:"+(int)touchY,50,150,paint);
                Paint paint3 = new Paint();
                paint3.setColor(Color.RED);
                paint3.setStyle(Paint.Style.STROKE);
                for(int i = 0; i< numCellX; i++){
                    for(int j = 0; j< numCellY; j++){
                        canvas.drawRect(terrain1[i][j].terrainDomain,paint3);
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
                canvas.drawText("MAP",60,50,paint);
                Paint paint1 = new Paint();
                paint1.setTextSize(48);
                canvas.drawText("Touch:"+ touchXcoord + "," + touchYcoord + "Character:"
                        + charaXcoord + "," + charaYcoord, 60, 100, paint1);

                for(int i = 0; i< numCellX; i++){
                    for(int j = 0; j< numCellY; j++) {
                        canvas.drawBitmap(terrain1[i][j].terrainPicture, getSrc(terrain1[i][j].terrainPicture),
                                terrain1[i][j].terrainDomain, null);
                    }
                }
                canvas.drawBitmap(Reimu.characterBitmap, getSrc(Reimu.characterBitmap),
                        Reimu.getCharaRect(originX, originY, cellSize, Reimu.characterCoordinate[0],Reimu.characterCoordinate[1]),
                        null);
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
                        + charaXcoord + "," + charaYcoord, 60, 100, paint1);
                for(int i = 0; i< numCellX; i++){
                    for(int j = 0; j< numCellY; j++) {
                        canvas.drawBitmap(terrain1[i][j].terrainPicture, getSrc(terrain1[i][j].terrainPicture),
                                terrain1[i][j].terrainDomain, null);
                    }
                }
                canvas.drawBitmap(Reimu.characterBitmap, getSrc(Reimu.characterBitmap),
                        Reimu.getCharaRect(originX,originY, cellSize,Reimu.characterCoordinate[0],
                                Reimu.characterCoordinate[1]), null);
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
        if(originX<touchX&&touchX< numCellX * cellSize +originX&&originY<touchY&&touchY< numCellY * cellSize +originY) {
            touchXcoord = ((int) touchX - originX) / cellSize;
            touchYcoord = ((int) touchY - originY) / cellSize;
            charaTouchDistance = Math.abs(touchXcoord - charaXcoord)
                    + Math.abs(touchYcoord - charaYcoord);
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
                    if(charaTouchDistance == 0 && originX < touchX && touchX < numCellX * cellSize
                            + originX&&originY < touchY && touchY < numCellY * cellSize + originY){//todo:ifタッチ箇所==キャラクター
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
                    if (charaTouchDistance <= Reachable) {//todo:ifタッチ箇所==キャラクター
                        charaXcoord = touchXcoord;
                        charaYcoord = touchYcoord;
                        moveChara(Reimu, touchXcoord, touchYcoord);
                        NEXT_SCENE = SC_MAP;
                    }
                    if (charaTouchDistance > Reachable){
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