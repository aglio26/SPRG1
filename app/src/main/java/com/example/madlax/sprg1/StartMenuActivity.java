package com.example.madlax.sprg1;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.widget.Button;

/**
 * Created by madlax on 2017/03/27.
 */

public class StartMenuActivity extends Activity {
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_startmenu);

        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), StoryActivity.class);
                startActivity(intent);
            }
        });

        Button endButton = (Button) findViewById(R.id.end_button);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
