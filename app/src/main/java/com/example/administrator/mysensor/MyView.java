package com.example.administrator.mysensor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.Map;

public class MyView extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap ball,BACK;
    private int width,height,areaWidth,areaHeight;
    private float gx=0,gy=0,gz=0,lastx,lasty;
    private float x,y,vx=0,vy=0;
    private boolean running=false;
    public Canvas mycanvas;
    public Paint mypaint;
    private SurfaceHolder myviewHolder;
    private SensorManager mysensorManager;
    private SoundPool soundpool;//声音池
    private static Map<String,Object> soundIds;
    private static AudioManager audioManager;//音频服务管理器
    private static Context mcontext;

    public MyView(Context context,SensorManager sensorManager,int w,int h) {
        super(context);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        mcontext = context;
        width = w;
        height = h;
        soundpool=new SoundPool(2, AudioManager.STREAM_MUSIC,0);//参数分别为：声音池中最多可同时存在的声音个数；
                                                                // AudioManager.STREAM_MUSIC；第三个参数现在无意义默认0
        int soundId1 = soundpool.load(context,R.raw.bong,2);
        int soundId2 = soundpool.load(context, R.raw.a, 1);
        soundIds = new HashMap<>();
        soundIds.put("soundId1",soundId1);
        soundIds.put("soundId2",soundId2);
        myviewHolder = this.getHolder();
        myviewHolder.addCallback(this);
        mysensorManager = sensorManager;
        mycanvas = new Canvas();
        mypaint = new Paint();
        mypaint.setColor(Color.WHITE);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        running = true;

        x = width / 2;
        y = height / 2;
        lastx = x;
        lasty = y;

        ball = BitmapFactory.decodeResource(this.getResources(),R.drawable.ball);
        BACK = BitmapFactory.decodeResource(this.getResources(),R.drawable.back);

        areaWidth = width - ball.getWidth();
        areaHeight = height - ball.getHeight();
        Sensor myoritationSensor = mysensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mysensorManager.registerListener(new mySensorEventlistener(), myoritationSensor, SensorManager.SENSOR_DELAY_GAME);
        soundpool.load(mcontext,R.raw.bong,2);
        mythread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
            running = false;
        }
    private class mySensorEventlistener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            gz =event.values[2];
            gx =(event.values[0])-(float)0.75;//定x坐标轴上的位置
            gy =(event.values[1])-(float)0.105;//定x坐标轴上的位置
            Log.d("gz", String.valueOf(gz));

            vx = vx - gx/2;
            vy = vy+ gy/2;
            vx = vx*199/200;
            vy = vy*199/200;

            x = x+vx/2;
            y = y+vy/2;
            if (x < 0){
                x = 0;
                if (lastx==x){
                    vx=0;
                }else {
                    vx = -vx*3/4;
                    soundpool.play((int)soundIds.get("soundId1"),1,1,1,0,1);//音频地址，音量，音量，优先级，声音播放速率
                }
            }else if (x>areaWidth){
                x = areaWidth;
                if (lastx==x){
                    vx=0;
                }else {
                    vx = -vx*3/4;
                    soundpool.play((int)soundIds.get("soundId1"),1,1,1,0,1);//音频地址，音量，音量，优先级，声音播放速率
                }
            }
            if (y<0){
                y = 0;
                if (lasty==y){
                    vy=0;
                }else {
                    vy = -vy*3/4;
                    soundpool.play((int)soundIds.get("soundId1"),1,1,1,0,1);//音频地址，音量，音量，优先级，声音播放速率
                }
            }else if (y>areaHeight){
                y = areaHeight;
                if (lasty==y){
                    vy=0;
                }else {
                    vy = -vy*3/4;
                    soundpool.play((int)soundIds.get("soundId1"),1,1,1,0,1);//音频地址，音量，音量，优先级，声音播放速率
                }
            }
            if (gz >19){
                Thread thread = new Thread(){
                    public void run() {
                        super.run();
                        Log.d("gz", String.valueOf(gz));
                        soundpool.play((int)soundIds.get("soundId2"),1,1,1,0,1);
                    }
                };
                thread.start();
            }
            lastx = x;
            lasty = y;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
    private Thread mythread = new Thread(){
        @Override
        public void run() {
            super.run();
            while (running){
                long starttime = System.currentTimeMillis();
                synchronized (myviewHolder) {
                    mycanvas = myviewHolder.lockCanvas();
                    draw();
                    myviewHolder.unlockCanvasAndPost(mycanvas);

                }
                long endtime = System.currentTimeMillis();
                int difftime =(int) (endtime - starttime);
                while (difftime<20){
                    difftime = (int)(System.currentTimeMillis()-starttime);
                    Thread.yield();
                }
            }
        }
    };
    private void draw(){
        mycanvas.drawBitmap(BACK,0,0,mypaint);
        mycanvas.drawBitmap(ball,x,y,mypaint);

    }
}