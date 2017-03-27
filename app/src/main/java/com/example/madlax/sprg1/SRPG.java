package com.example.madlax.sprg1;

import android.app.Activity;
import android.os.Bundle;
/**
 * Created by madlax on 2017/03/27.
 */

public class SRPG extends Activity {
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(new SRPGView(this));
    }
}
