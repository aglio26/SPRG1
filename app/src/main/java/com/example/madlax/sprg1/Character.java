package com.example.madlax.sprg1;
import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by madlax on 2017/04/23.
 */

public class Character {
    private int level;      //レベル
    private int hitPoint;   //HP
    private int strength;   //力
    private int magic;      //魔力
    private int skill;      //技
    private int speed;      //速さ
    private int luck;       //幸運
    private int deffence;   //守備
    private int resistance; //魔防
    private int movement;   //移動

    int charXcoord;
    int charYcoord;
    Bitmap charImage;
    Rect charDomain;

    private Weapon equipment;   //装備

    //コンストラクタ
    public Character(int lv, int hp, int str, int mag, int skl, int spd, int lck, int def, int res,
                     int move, int initX, int initY, Bitmap image, Weapon initWeapon){
        level = lv;
        hitPoint = hp;
        strength = str;
        magic = mag;
        skill = skl;
        speed = spd;
        luck = lck;
        deffence = def;
        resistance = res;
        movement = move;
        charXcoord = initX;
        charYcoord = initY;
        charImage = image;
        equipment = initWeapon;
    }

    //メソッド
    public int getHitPoint(){
        return hitPoint;
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
    public Rect getCharDomain(int originX, int originY, int cellSize, int charXcoord, int charYcoord){
        charDomain = new Rect();
        charDomain.left = originX + charXcoord * cellSize;
        charDomain.top = originY + charYcoord * cellSize;
        charDomain.right = originX + ( 1 + charXcoord ) * cellSize;
        charDomain.bottom = originY + ( 1 + charYcoord ) * cellSize;
        return charDomain;
    }
    public int getAttackPower() {
        return strength + equipment.getMight();
    }
    public int getDeffencePower() {
        return deffence;
    }
    public int getHitRate() {
        return  skill * 2 + luck + equipment.getHitRate();
    }
    public int getAvoid() {
        return speed * 2 + luck;
    }
    public int getCritical() {
        return skill / 2 + equipment.getCriticalRate();
    }
    public int getDodge() {
        return luck;
    }

    public void setHitPoint(int hp) {
        hitPoint = hp;
    }
}