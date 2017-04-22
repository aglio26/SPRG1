package com.example.madlax.sprg1;

/**
 * Created by madlax on 2017/04/22.
 */

public class Chapter {

    //定数
    int x_cell;//横方向セル数
    int y_cell;//縦方向セル数
    int[][] MAP;

    //constructor
    public Chapter(int x, int y, int C[][]) {
        x_cell = x;
        y_cell = y;
        MAP = new int[x_cell][y_cell];
        for (int i=0;i<x;i++){
            for(int j=0;j<y;j++){
                MAP[i][j] = C[i][j];
            }
        }
    }


    //method
    public int getCellnumberX(){
        return x_cell;
    }
    public int getCellnumberY(){
        return y_cell;
    }

}
