package com.example.madlax.sprg1;

import android.graphics.Bitmap;

/**
 * Created by madlax on 2017/04/22.
 */

public class Terrain {
    //変数
    private int avoidance;
    private int moveCost;
    Bitmap terrainImage;

    //コンストラクタ
    public Terrain(int a, int mc, Bitmap image) {
        avoidance = a;
        moveCost = mc;
        terrainImage = image;
    }

    //メソッド
    public int getMoveCost(){
        return moveCost;
    }
    public int getAvoidance(){
        return avoidance;
    }
    public Bitmap getTerrainImage(){
        return terrainImage;
    }
}