package com.example.madlax.sprg1;

/**
 * Created by k-watanabe on 5/21/17.
 */

public class Weapon {
    //武器レベル
    private int might;          //威力
    private int hitRate;        //命中
    private int criticalRate;   //必殺
    //重さ
    //耐久
    private int range;          //射程
    //価格
    //武器経験

    public Weapon(int mt, int hit, int crt, int rng) {
        might = mt;
        hitRate = hit;
        criticalRate = crt;
        range = rng;
    }

    public int getMight() {
        return might;
    }
    public int getHitRate() {
        return hitRate;
    }
    public int getCriticalRate() {
        return criticalRate;
    }
    public int getRange() {
        return range;
    }
}
