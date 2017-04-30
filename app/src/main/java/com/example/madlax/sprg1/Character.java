package com.example.madlax.sprg1;
import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by madlax on 2017/04/23.
 */

public class Character {
    //変数
    private int hitPoint;
    private int magicPoint;
    private int MA;
    int charXcoord;
    int charYcoord;
    Bitmap charImage;
    Rect charDomain;

    //コンストラクタ
    public Character(int hp, int mp, int ma, int initX, int initY, Bitmap image){
        hitPoint = hp;
        magicPoint = mp;
        MA = ma;
        charXcoord = initX;
        charYcoord = initY;
        charImage = image;
    }

    //メソッド
    public int getCharaHP(){
        return hitPoint;
    }
    public int getCharaMP(){
        return magicPoint;
    }
    public int getCharaX(){
        return charXcoord;
    }
    public int getCharaY(){
        return charYcoord;
    }
    public Bitmap getCharBitmap(){
        return charImage;
    }
    public Rect getCharDomain(int originX, int originY, int cellSize, int charCoordX, int charCoordY){
        charDomain = new Rect();
        charDomain.left = originX + charCoordX * cellSize;
        charDomain.top = originY + charCoordY * cellSize;
        charDomain.right = originX + ( 1 + charCoordX ) * cellSize;
        charDomain.bottom = originY + ( 1 + charCoordY ) * cellSize;
        return charDomain;
    }
}