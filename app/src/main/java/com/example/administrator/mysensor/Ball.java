package com.example.administrator.mysensor;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class Ball extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //没有titlebar，全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        //保持屏幕竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        WindowManager myManager = (WindowManager) this.getSystemService(this.WINDOW_SERVICE);
        DisplayMetrics mymeMetrics = new DisplayMetrics();
        myManager.getDefaultDisplay().getMetrics(mymeMetrics);
        int width = mymeMetrics.widthPixels;
        int height = mymeMetrics.heightPixels-125;

        MyView myview = new MyView(this,sensorManager,width,height);
        setContentView(myview);
    }
}
