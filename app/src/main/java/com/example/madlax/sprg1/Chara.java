package com.example.madlax.sprg1;
import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by madlax on 2017/04/23.
 */

public class Chara {
    int HP;
    int MP;
    int MA;
    int AttackRange;
    int[] chara_coordinate;
    Bitmap chara_bitmap;
    Rect chara_rect;
    //constructor
    public Chara(int hp,int mp,int ma,int itiX,int itiY,Bitmap bitmap){
        chara_coordinate = new int[2];
        HP = hp;
        MP = mp;
        MA = ma;
        chara_coordinate[0]=itiX;
        chara_coordinate[1]=itiY;
        chara_bitmap = bitmap;
    }
    public int getCharaHP(){
        return HP;
    }
    public int getCharaMP(){
        return MP;
    }
    public int getCharaX(){
        return chara_coordinate[0];
    }
    public int getCharaY(){
        return chara_coordinate[1];
    }
    public Bitmap getCharaBitmap(){
        return chara_bitmap;
    }
    public Rect getCharaRect(int originX,int originY,int cell,int chara_coordinateX,int chara_coordinateY){
        chara_rect = new Rect();
        chara_rect.left = originX + chara_coordinateX* cell;
        chara_rect.top = originY + chara_coordinateY * cell;
        chara_rect.right = originX + (1 + chara_coordinateX) * cell;
        chara_rect.bottom = originY + (1 + chara_coordinateY) * cell;
        return chara_rect;
    }
}
