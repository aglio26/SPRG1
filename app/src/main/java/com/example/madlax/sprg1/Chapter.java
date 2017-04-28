package com.example.madlax.sprg1;

/**
 * Created by madlax on 2017/04/22.
 */

public class Chapter {

    //定数
    int numberOfCellX;  //横方向セル数
    int numberOfCellY;  //縦方向セル数
    int[][] MAP;

    //constructor
    public Chapter(int x, int y, int C[][]) {
        numberOfCellX = x;
        numberOfCellY = y;
        MAP = new int[numberOfCellX][numberOfCellY];
        for (int i=0;i<x;i++){
            for(int j=0;j<y;j++){
                MAP[i][j] = C[i][j];
            }
        }
    }

    //method
    public int getNumberOfCellX(){
        return numberOfCellX;
    }
    public int getNumberOfCellY(){
        return numberOfCellY;
    }

}
