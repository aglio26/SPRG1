package com.example.madlax.sprg1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.method.Touch;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Display;
import android.graphics.Point;
import android.view.MotionEvent;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.text.TextPaint;
import android.view.TouchDelegate;

/**
 * Created by madlax on 2017/03/27.
 */

public class SRPGView extends SurfaceView
    implements SurfaceHolder.Callback, Runnable {
    //field variable
    int w;//画面横幅
    int h;//画面縦幅
    int cell;//セルサイズ
    int n = 8;//セル数横
    int m = 10;//セル数縦
    int originX;//座標原点X
    int originY;//座標原点Y
    float touchX;//タッチ座標
    float touchY;//タッチ座標
    int Touch_Coordinate[] = new int[2];
    int Chara_Coordinate[] = new int[2];
    int Chara_Touch_Distance;
    Rect[][] Map_domain = new Rect[n][m];;//描画領域
    //SystemConstant
    public SurfaceHolder holder;
    public Thread thread;
    public Canvas canvas;
    //SceneConstant
    int SCENE = -1;
    int NEXT_SCENE = -1;
    static int OP = 0;
    static int MAP = 1;
    static int MOVE_READY = 2;
    static int BATTLE = 3;
    static int GAMEOVER = 9;
    //CharactorConstant
    int Reachable = 4;
    //MapConstant
    int[][] Map = new int[n][m];
    int MapID = 0;



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
    public void generateMapDomain(int n,int m,int originX,int originY,int cell){
        for(int i=0;i<n;i++) {
            for (int j = 0; j < m; j++) {
                Map_domain[i][j] = new Rect(originX + i * cell+i, originY + j * cell+j, originX + (1 + i) * cell+i, originY + (1 + j) * cell+j);
            }
        }
    }
    public int get_TouchMapID(int coordinate_x,int coordinate_y){
        return Map[coordinate_x][coordinate_y];
    }

    //Constructor
    public SRPGView(Context context) {
        super(context);
        //generate surface holder
        holder = getHolder();
        holder.addCallback(this);
        //get screen size and get origin,cell
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        w = p.x;
        h = p.y;
        cell = Math.min(w/n,h/m)-20;
        originX = (w-n*cell)/2;
        originY = (h-m*cell)/2;
        //initialize Map_data
        generateMapDomain(n,m,originX,originY,cell);
        //initialize each array
        Touch_Coordinate[0] = 0;
        Touch_Coordinate[1] = 0;
        Chara_Coordinate[0] = 0;
        Chara_Coordinate[1] = 0;
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
            if (SCENE == OP) {//todo:OP画像の表示及びBGMの実装
                lock();
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setTextSize(48);
                canvas.drawText("OP"+"X:"+w+"Y:"+h+"CELL:"+cell,50,50,paint);
                Paint paint1 = new Paint();
                paint1.setColor(Color.BLUE);
                paint1.setTextSize(48);
                canvas.drawText("Mapdata origin:"+Map_domain[0][0].left+","+Map_domain[0][0].top,50,100,paint1);
                Paint paint2 = new Paint();
                paint2.setColor(Color.BLUE);
                paint2.setTextSize(48);
                canvas.drawText("touchX:"+(int)touchX+"touchY:"+(int)touchY,50,150,paint);
                Paint paint3 = new Paint();
                paint3.setColor(Color.RED);
                paint3.setStyle(Paint.Style.STROKE);
                for(int i=0;i<n;i++){
                    for(int j=0;j<m;j++){
                        canvas.drawRect(Map_domain[i][j],paint3);
                    }
                }
                Paint paint4 = new Paint();
                paint2.setColor(Color.BLUE);
                paint2.setTextSize(48);
                canvas.drawText("cellX:"+Touch_Coordinate[0]+"cellY:"+Touch_Coordinate[1],50,200,paint);
                unlock();
                sleep(0);

            }
            if (SCENE == MAP) {//todo：MAP描画の実装　キャラクター描画（カズさん担当）
                lock();
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setTextSize(48);
                canvas.drawText("MAP",60,50,paint);
                Paint paint1 = new Paint();
                paint1.setTextSize(48);
                canvas.drawText("Touch:"+Touch_Coordinate[0]+","+Touch_Coordinate[1]+"Chara:"+Chara_Coordinate[0]+","+Chara_Coordinate[1],60,100,paint1);
                unlock();
                sleep(600);

            }
            if (SCENE == MOVE_READY) {// todo:移動可能領域の取得、及び領域の描画の実装　
                lock();
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setTextSize(48);
                canvas.drawText("MOVE_READY",70,50,paint);
                Paint paint1 = new Paint();
                paint1.setTextSize(48);
                canvas.drawText("Touch:"+Touch_Coordinate[0]+","+Touch_Coordinate[1]+"Chara:"+Chara_Coordinate[0]+","+Chara_Coordinate[1],60,100,paint1);
                unlock();
                sleep(0);
            }
            if (SCENE == GAMEOVER) {
                lock();
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setTextSize(48);
                canvas.drawText("GAMEOVER",80,50,paint);
                unlock();
                sleep(0);
                SCENE = OP;

            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();
        Touch_Coordinate[0] = ((int)touchX-originX)/cell;
        Touch_Coordinate[1] = ((int)touchY-originY)/cell;
        Chara_Touch_Distance = Math.abs(Touch_Coordinate[0]-Chara_Coordinate[0])+Math.abs(Touch_Coordinate[1]-Chara_Coordinate[1]);
        MapID = get_TouchMapID(Touch_Coordinate[0], Touch_Coordinate[1]);

        if(SCENE == OP){
            switch ( event.getAction() ) {

                case MotionEvent.ACTION_DOWN:
                    //画面がタッチされたときの動作

                    NEXT_SCENE = MAP;
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
        else if(SCENE == MAP){
            switch ( event.getAction() ) {

                case MotionEvent.ACTION_DOWN:
                    //画面がタッチされたときの動作
                    if(Chara_Touch_Distance == 0){//todo:ifタッチ箇所==キャラクター
                        NEXT_SCENE = MOVE_READY;
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
        else if(SCENE == MOVE_READY) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    //画面がタッチされたときの動作
                    if (Chara_Touch_Distance <= Reachable) {//todo:ifタッチ箇所==キャラクター
                        Chara_Coordinate[0] = Touch_Coordinate[0];
                        Chara_Coordinate[1] = Touch_Coordinate[1];
                        NEXT_SCENE = MAP;
                    }
                    if (Chara_Touch_Distance > Reachable){
                        NEXT_SCENE = MAP;
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


