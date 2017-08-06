package com.example.madlax.sprg1;

/**
 * Created by madlax on 2017/06/04.
 */

public class Cell {
    //変数
    public boolean marker;
    public boolean tansakuyotei;
    public int joudgeLoop;
    public int moveVariable;
    public int rootX;
    public int rootY;

    //コンストラクタ
    public Cell(){
        marker = false;
        tansakuyotei= false;
        moveVariable = -10;
        rootX = -1;
        rootY = -1;
    }

    //メソッド
    public void writecell(int i,int j,Cell cell[][],Chapter chapter,Terrain[] terrain){
        //周囲4マスの処理
        if(0<=i&&i<=chapter.getNumCellX()-2) {
            if (cell[i][j].moveVariable - terrain[chapter.field[i+1][j]].moveCost>=0) {
                if(cell[i+1][j].marker == false
                        || cell[i+1][j].moveVariable < cell[i][j].moveVariable - terrain[chapter.field[i+1][j]].moveCost){
                    cell[i+1][j].moveVariable = cell[i][j].moveVariable - terrain[chapter.field[i+1][j]].moveCost;
                    cell[i+1][j].marker = true;
                }
                cell[i+1][j].rootX = i;
                cell[i+1][j].rootY = j;
            }
        }

        if(0<=j&&j<=chapter.getNumCellY()-2){

            if(cell[i][j].moveVariable -terrain[chapter.field[i][j+1]].moveCost>=0) {
                if(cell[i][j+1].marker == false
                        || cell[i][j+1].moveVariable < cell[i][j].moveVariable - terrain[chapter.field[i][j+1]].moveCost){
                    cell[i][j + 1].moveVariable = cell[i][j].moveVariable - terrain[chapter.field[i][j + 1]].moveCost;
                    cell[i][j + 1].marker = true;

                }
                cell[i][j + 1].rootX = i;
                cell[i][j + 1].rootY = j;

            }
        }

        if(1<=i&&i<=chapter.getNumCellX()-1) {

            if (cell[i][j].moveVariable -terrain[chapter.field[i-1][j]].moveCost>=0) {
                if(cell[i-1][j].marker == false
                        || cell[i-1][j].moveVariable < cell[i][j].moveVariable - terrain[chapter.field[i-1][j]].moveCost){
                    cell[i - 1][j].moveVariable = cell[i][j].moveVariable - terrain[chapter.field[i - 1][j]].moveCost;
                    cell[i-1][j].marker = true;
                }
                cell[i-1][j].rootX = i;
                cell[i-1][j].rootY = j;
            }
        }

        if(1<=j&&j<=chapter.getNumCellY()-1) {
            if (cell[i][j].moveVariable -terrain[chapter.field[i][j-1]].moveCost>=0) {
                if(cell[i][j-1].marker==false
                        || cell[i][j-1].moveVariable < cell[i][j].moveVariable - terrain[chapter.field[i][j-1]].moveCost){
                    cell[i][j - 1].moveVariable = cell[i][j].moveVariable - terrain[chapter.field[i][j - 1]].moveCost;
                    cell[i][j - 1].marker = true;
                    joudgeLoop += 1;
                }
                cell[i][j - 1].rootX = i;
                cell[i][j - 1].rootY = j;

            }
        }
    }
}
