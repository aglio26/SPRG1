package com.example.madlax.sprg1;

/**
 * Created by k-watanabe on 5/21/17.
 */

public class Weapon {
    private int might;
    private int hitRate;
    private int criticalRate;
    private int range;

    public Weapon(int mt, int hit, int crt, int rng) {
        might = mt;
        hitRate = hit;
        criticalRate = crt;
        range = rng;
    }
}
