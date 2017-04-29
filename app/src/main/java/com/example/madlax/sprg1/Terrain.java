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
    int[] terrainCoordinate;
    Rect terrainDomain;
    Bitmap terrainPicture;

    //constructor
    public Terrain() {
        terrainCoordinate = new int[2];
        avoid = 0;
        moveCost = 0;
        terrainDomain = new Rect();
        terrainCoordinate[0]=0;
        terrainCoordinate[1]=0;
    }
    //method
    public int getMoveCost(){
        return moveCost;
    }
    public int getAvoid(){
        return avoid;
    }
    public int getTerrainCorrdinateX(){
        return terrainCoordinate[0];
    }
    public int getTerrainCorrdinateY(){
        return terrainCoordinate[1];
    }
    public Rect getTerrainDomain(){
        return terrainDomain;
    }
    public Bitmap getTerrainPicture(){
        return terrainPicture;
    }
}