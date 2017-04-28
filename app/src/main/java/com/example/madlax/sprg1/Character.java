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
    int[] characterCoordinate;
    Bitmap characterBitmap;
    Rect characterRect;
    //constructor
    public Character(int hp, int mp, int ma, int itiX, int itiY, Bitmap bitmap){
        characterCoordinate = new int[2];
        hitPoint = hp;
        magicPoint = mp;
        MA = ma;
        characterCoordinate[0]=itiX;
        characterCoordinate[1]=itiY;
        characterBitmap = bitmap;
    }
    public int getCharaHP(){
        return hitPoint;
    }
    public int getCharaMP(){
        return magicPoint;
    }
    public int getCharaX(){
        return characterCoordinate[0];
    }
    public int getCharaY(){
        return characterCoordinate[1];
    }
    public Bitmap getCharacterBitmap(){
        return characterBitmap;
    }
    public Rect getCharaRect(int originX, int originY, int cellSize, int charaCoordinateX, int charaCoordinateY){
        characterRect = new Rect();
        characterRect.left = originX + charaCoordinateX * cellSize;
        characterRect.top = originY + charaCoordinateY * cellSize;
        characterRect.right = originX + ( 1 + charaCoordinateX ) * cellSize;
        characterRect.bottom = originY + ( 1 + charaCoordinateY ) * cellSize;
        return characterRect;
    }
}
