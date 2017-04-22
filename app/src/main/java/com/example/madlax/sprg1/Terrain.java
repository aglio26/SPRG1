package com.example.madlax.sprg1;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by madlax on 2017/04/22.
 */

public class Terrain {
    //field variable
    int avoid;
    int move_cost;
    int[] Terrain_Coordinate;
    Rect Terrain_Domain;
    Bitmap Terrain_pic;


    //constructor
    public Terrain() {
        Terrain_Coordinate = new int[2];
        avoid = 0;
        move_cost = 0;
        Terrain_Domain = new Rect();
        Terrain_Coordinate[0]=0;
        Terrain_Coordinate[1]=0;
    }
    //method
    public int getMove_cost(){
        return move_cost;
    }
    public int getAvoid(){
        return avoid;
    }
    public int getTerrain_CorrdinateX(){
        return Terrain_Coordinate[0];
    }
    public int getTerrain_CorrdinateY(){
        return Terrain_Coordinate[1];
    }
    public Rect getTerrain_Domain(){
        return Terrain_Domain;
    }
    public Bitmap getTerrain_pic(){
        return Terrain_pic;
    }

}
