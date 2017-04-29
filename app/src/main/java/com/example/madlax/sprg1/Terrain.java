package com.example.madlax.sprg1;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by madlax on 2017/04/22.
 */

public class Terrain {
    //field variable
    int avoid;
    int moveCost;
    int terrainXcoord;
    int terrainYcoord;
    Rect terrainDomain;
    Bitmap terrainPicture;

    //constructor
    public Terrain() {
        avoid = 0;
        moveCost = 0;
        terrainDomain = new Rect();
        terrainXcoord = 0;
        terrainYcoord = 0;
    }
    //method
    public int getMoveCost(){
        return moveCost;
    }
    public int getAvoid(){
        return avoid;
    }
    public int getTerrainCorrdinateX(){
        return terrainXcoord;
    }
    public int getTerrainCorrdinateY(){
        return terrainYcoord;
    }
    public Rect getTerrainDomain(){
        return terrainDomain;
    }
    public Bitmap getTerrainPicture(){
        return terrainPicture;
    }
}