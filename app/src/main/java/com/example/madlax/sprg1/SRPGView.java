package com.example.madlax.sprg1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.view.MotionEvent;
import android.graphics.Paint;

import android.graphics.Canvas;

/**
 * Created by madlax on 2017/03/27.
 */

public class SRPGView extends SurfaceView
    implements SurfaceHolder.Callback, Runnable {
    //field variable
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
    public SRPGView(Activity activity) {
        super(activity);
        //generate surfeceholder
        holder = getHolder();
        holder.addCallback(this);
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
                canvas.drawText("OP",50,50,paint);
                unlock();
                sleep(200);
                SCENE = MAP;

            }
            if (SCENE == MAP) {
                lock();
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setTextSize(48);
                canvas.drawText("MAP",60,50,paint);
                unlock();
                sleep(200);
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
                sleep(200);
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
                sleep(200);
                SCENE = OP;

            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}


