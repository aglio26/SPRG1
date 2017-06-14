package com.example.madlax.sprg1;

/**
 * Created by madlax on 2017/06/04.
 */

public class Cell {
    //変数
    public boolean tansakuzumi;
    public boolean tansakuyotei;
    public int moveVariable;
    public int rootX;
    public int rootY;
    //コンストラクタ
    public Cell(){
        tansakuzumi = false;
        tansakuyotei= false;
        moveVariable = -5;
        rootX = 0;
        rootY = 0;
    }
    //メソッド
    public void writeCell(int i, int j, Cell cell[][], Chapter chapter, Terrain[] terrain){
        //自マスの処理
        cell[i][j].tansakuyotei = false;
        cell[i][j].tansakuzumi = true;
        //周囲4マスの処理
        if(0<=i&&i<=chapter.getNumCellX()-1) {
            if (cell[i+1][j].moveVariable > cell[i][j].moveVariable - terrain[chapter.field[i+1][j]].moveCost) {
                cell[i+1][j].moveVariable = cell[i][j].moveVariable - terrain[chapter.field[i+1][j]].moveCost;
                rootX = i;
                rootY = j;
                if (cell[i+1][j].moveVariable > 0) {
                    cell[i+1][j].tansakuyotei = true;
                }
            }
        }
        if(0<=j&&j<=chapter.getNumCellY()-1){
            if(cell[i][j+1].moveVariable > cell[i][j].moveVariable -terrain[chapter.field[i][j+1]].moveCost) {
                    cell[i][j + 1].moveVariable = cell[i][j].moveVariable - terrain[chapter.field[i][j + 1]].moveCost;
                    rootX = i;
                    rootY = j;
                    if (cell[i][j + 1].moveVariable > 0) {
                        cell[i][j + 1].tansakuyotei = true;
                    }

            }
        }
        if(1<=i&&i<=chapter.getNumCellX()) {
            if (cell[i-1][j].moveVariable > cell[i][j].moveVariable - terrain[chapter.field[i-1][j]].moveCost) {
                cell[i-1][j].moveVariable = cell[i][j].moveVariable - terrain[chapter.field[i-1][j]].moveCost;
                rootX = i;
                rootY = j;
                if (cell[i-1][j].moveVariable > 0) {
                    cell[i-1][j].tansakuyotei = true;
                }
            }
        }
        if(1<=j&&j<=chapter.getNumCellY()) {
            if (cell[i][j - 1].moveVariable > cell[i][j].moveVariable - terrain[chapter.field[i][j-1]].moveCost) {
                cell[i][j - 1].moveVariable = cell[i][j].moveVariable - terrain[chapter.field[i][j - 1]].moveCost;
                rootX = i;
                rootY = j;
                if (cell[i][j - 1].moveVariable > 0) {
                    cell[i][j - 1].tansakuyotei = true;
                }
            }
        }
    }



}
