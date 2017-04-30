package com.example.madlax.sprg1;

import android.graphics.Rect;

/**
 * Created by madlax on 2017/04/22.
 */

public class Chapter {

    //変数
    private int numCellX;  //横方向セル数
    private int numCellY;  //縦方向セル数
    int[][] field;

    //コンストラクタ
    public Chapter(int f[][]) {
        numCellX = f.length;    //二次元配列fの要素数（x要素）
        numCellY = f[0].length; //二次元配列fの要素数（y要素）

        field = new int[numCellX][numCellY];
        for (int i=0; i < numCellX; i++){
            for (int j=0; j < numCellY; j++){
                field[i][j] = f[i][j];
            }
        }
    }

    //メソッド
    public int getNumCellX(){
        return numCellX;
    }
    public int getNumCellY(){
        return numCellY;
    }

}