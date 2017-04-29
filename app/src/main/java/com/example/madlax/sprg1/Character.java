package com.example.madlax.sprg1;
import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by madlax on 2017/04/23.
 */

public class Character {
    int hitPoint;
    int magicPoint;
    int MA;
    int charXcoord;
    int charYcoord;
    Bitmap characterBitmap;
    Rect characterRect;
    //constructor
    public Character(int hp, int mp, int ma, int itiX, int itiY, Bitmap bitmap){
        hitPoint = hp;
        magicPoint = mp;
        MA = ma;
        charXcoord=itiX;
        charYcoord=itiY;
        characterBitmap = bitmap;
    }
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
    public Bitmap getCharacterBitmap(){
        return characterBitmap;
    }
    public Rect getCharaRect(int originX, int originY, int cellSize, int charCoordX, int charCoordY){
        characterRect = new Rect();
        characterRect.left = originX + charCoordX * cellSize;
        characterRect.top = originY + charCoordY * cellSize;
        characterRect.right = originX + ( 1 + charCoordX ) * cellSize;
        characterRect.bottom = originY + ( 1 + charCoordY ) * cellSize;
        return characterRect;
    }
}