package com.example.madlax.sprg1;
import android.graphics.Bitmap;


/**
 * Created by madlax on 2017/04/22.
 */

public class Terrain {
    //変数
    private int avoidance;
    public int moveCost;
    Bitmap terrainImage;
    Bitmap terrainImage_blue;
    Bitmap terrainImage_red;

    //コンストラクタ
    public Terrain(int a, int mc, Bitmap image1, Bitmap image2, Bitmap image3) {
        avoidance = a;
        moveCost = mc;
        terrainImage = image1;
        terrainImage_blue = image2;
        terrainImage_red = image3;
    }

    //メソッド
    public int getMoveCost(){
        return moveCost;
    }
    public int getAvoidance() {
        return avoidance;
    }
    public Bitmap getTerrainImage() {
        return terrainImage;
    }
}