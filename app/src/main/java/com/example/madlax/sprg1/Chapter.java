package com.example.madlax.sprg1;

import android.graphics.Rect;

/**
 * Created by madlax on 2017/04/22.
 */

public class Chapter {

    //定数
    int numCellX;  //横方向セル数
    int numCellY;  //縦方向セル数
    int[][] field;
    Rect[][] domain;

    //constructor
    public Chapter(int x, int y, int f[][]) {
        numCellX = x;
        numCellY = y;
        field = new int[numCellX][numCellY];
        for (int i=0;i<x;i++){
            for (int j=0;j<y;j++){
                field[i][j] = f[i][j];
            }
        }
    }

    //method
    public int getNumCellX(){
        return numCellX;
    }
    public int getNumCellY(){
        return numCellY;
    }

}