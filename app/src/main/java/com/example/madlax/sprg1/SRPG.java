package com.example.madlax.sprg1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by madlax on 2017/03/27.
 */

public class SRPG extends Activity {
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new SRPGView(this));
    }
}
