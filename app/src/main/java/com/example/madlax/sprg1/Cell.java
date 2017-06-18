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
        moveVariable = -10;
        rootX = -1;
        rootY = -1;
    }
    //メソッド
    public void writecell(int i,int j,Cell cell[][],Chapter chapter,Terrain[] terrain,boolean loop){
        //自マスの処理
        cell[i][j].tansakuzumi = true;
        //周囲4マスの処理
        if(0<=i&&i<=chapter.getNumCellX()-2) {
            if (cell[i][j].moveVariable - terrain[chapter.field[i+1][j]].moveCost>=0) {
                if(cell[i+1][j].tansakuzumi==false||cell[i+1][j].moveVariable < cell[i][j].moveVariable - terrain[chapter.field[i+1][j]].moveCost){
                cell[i+1][j].moveVariable = cell[i][j].moveVariable - terrain[chapter.field[i+1][j]].moveCost;
                    loop = true;
                }
                cell[i+1][j].rootX = i;
                cell[i+1][j].rootY = j;

                if (cell[i+1][j].moveVariable >= 0) {
                    cell[i+1][j].tansakuzumi = true;
            }}
        }

        if(0<=j&&j<=chapter.getNumCellY()-2){

            if(cell[i][j].moveVariable -terrain[chapter.field[i][j+1]].moveCost>=0) {
                if(cell[i][j+1].tansakuzumi==false||cell[i][j+1].moveVariable < cell[i][j].moveVariable - terrain[chapter.field[i][j+1]].moveCost){
                    cell[i][j + 1].moveVariable = cell[i][j].moveVariable - terrain[chapter.field[i][j + 1]].moveCost;
                    loop = true;
                }
                    cell[i][j + 1].rootX = i;
                    cell[i][j + 1].rootY = j;
                    if (cell[i][j + 1].moveVariable >= 0) {

                        cell[i][j + 1].tansakuzumi = true;
                    }


        }}

        if(1<=i&&i<=chapter.getNumCellX()-1) {

            if (cell[i][j].moveVariable -terrain[chapter.field[i-1][j]].moveCost>=0) {
                if(cell[i-1][j].tansakuzumi==false||cell[i-1][j].moveVariable < cell[i][j].moveVariable - terrain[chapter.field[i-1][j]].moveCost){
                    cell[i - 1][j].moveVariable = cell[i][j].moveVariable - terrain[chapter.field[i - 1][j]].moveCost;
                    loop = true;
                }
                cell[i-1][j].rootX = i;
                cell[i-1][j].rootY = j;
                if (cell[i-1][j].moveVariable >= 0) {

                    cell[i-1][j].tansakuzumi = true;
                }

        }}

        if(1<=j&&j<=chapter.getNumCellY()-1) {
            if (cell[i][j].moveVariable -terrain[chapter.field[i][j-1]].moveCost>=0) {
                if(cell[i][j-1].tansakuzumi==false||cell[i][j-1].moveVariable < cell[i][j].moveVariable - terrain[chapter.field[i][j-1]].moveCost){
                    cell[i][j - 1].moveVariable = cell[i][j].moveVariable - terrain[chapter.field[i][j - 1]].moveCost;
                    loop = true;
                }
                cell[i][j - 1].rootX = i;
                cell[i][j - 1].rootY = j;
                if (cell[i][j - 1].moveVariable >= 0) {
                    cell[i][j - 1].tansakuzumi = true;
                }

        }
        }
    }



}
