package com.example.madlax.sprg1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Display;
import android.graphics.Point;
import android.view.MotionEvent;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Map;

/**
 * Created by madlax on 2017/03/27.
 */

public class SRPGView extends SurfaceView
    implements SurfaceHolder.Callback, Runnable {
    //field variable
    int w;//画面横幅
    int h;//画面縦幅
    int cell;//セルサイズ
    int n = 24;//セル数横
    int m = 32;//セル数縦
    int originX;//座標原点X
    int originY;//座標原点Y
    Rect[][] Map_data = new Rect[n][m];;//描画領域
    //SystemConstant
    public SurfaceHolder holder;
    public Thread thread;
    public Paint paint;
    public Canvas canvas;
    //SceneConstant
    int SCENE = -1;
    int NEXT_SCENE = -1;
    int OP = 0;
    int MAP = 1;
    int MOVE_READY = 2;
    int BATTLE = 3;
    int GAMEOVER = 9;


    //user defined method
    public void lock(){
        canvas = holder.lockCanvas();
    }
    public void unlock(){
        holder.unlockCanvasAndPost(canvas);
    }
    public void sleep(int time){
        try {
            Thread.sleep(time);
        }catch(Exception e){
        }
    }


    //Constructor
    public SRPGView(Context context) {
        super(context);
        //generate surfeceholder
        holder = getHolder();
        holder.addCallback(this);
        //get screen size and get origin,cell
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        w = p.x;
        h = p.y;
        cell = Math.min(w/n,h/m);
        originX = (w-n*cell)/2;
        originY = (h-m*cell)/2;
        //initialize Map_data
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                Map_data[i][j] = new Rect(originX+i*cell,originY+j*cell,originX+(1+i)*cell,originY+(1+j)*cell);
            }
        }




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
            if (SCENE == OP) {
                lock();
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setTextSize(48);
                canvas.drawText("OP"+"X:"+w+"Y:"+h+"CELL:"+cell,50,50,paint);
                Paint paint1 = new Paint();
                paint1.setColor(Color.BLUE);
                paint1.setTextSize(48);
                canvas.drawText("Mapdata origin:"+Map_data[0][0].left+","+Map_data[0][0].top,50,100,paint1);

                Paint paint2 = new Paint();
                paint2.setColor(Color.RED);
                paint2.setStyle(Paint.Style.STROKE);
                for(int i=0;i<n;i++){
                    for(int j=0;j<m;j++){
                        canvas.drawRect(Map_data[i][j],paint2);
                    }
                }

                unlock();
                sleep(0);

            }
            if (SCENE == MAP) {
                lock();
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setTextSize(48);
                canvas.drawText("MAP",60,50,paint);
                unlock();
                sleep(0);
                SCENE = MOVE_READY;

            }
            if (SCENE == MOVE_READY) {
                lock();
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setTextSize(48);
                canvas.drawText("MOVE_READY",70,50,paint);
                unlock();
                sleep(0);
                SCENE = GAMEOVER;
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
        return true;
    }
}


