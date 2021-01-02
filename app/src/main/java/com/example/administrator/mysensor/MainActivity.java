package com.example.administrator.mysensor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Button btnball = (Button)findViewById(R.id.ball);
        btnball.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        switch (vid){
            case R.id.ball:
                Intent intent = new Intent(MainActivity.this,Ball.class);
                startActivity(intent);
                break;
        }
    }
}
