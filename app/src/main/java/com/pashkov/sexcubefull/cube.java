package com.pashkov.sexcubefull;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.pashkov.sexcubefull.R;

import com.facebook.ads.*;

import com.pashkov.sexcubefull.util.AppPreferences;
import com.pashkov.sexcubefull.view_pager.MyAnimation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.opengl.GLSurfaceView;


public class cube extends Activity implements SensorEventListener {
    private GLSurfaceView glView;  // Use subclass of GLSurfaceView (NEW)

    private static final int PERIOD = 2000;
    private long lastPressedTime;

    Display display;
    float width, hetgth;

    int counters = 0;
    int doAction = 0;

    int secInt = 0;
    int whatText = 0;
    CountDownTimer timers;

    Timer timer;

    ImageView imageHand;

    MyGLRenderer renderer;
    TextActivity textac;


    SharedPreferences sPref;
    //acselerometr
    SensorManager mSensorManager;
    Sensor mAccelerometerSensor;
    TextView mForceValueText;
    TextView mXValueText;
    TextView mYValueText;
    TextView mZValueText;
    Integer vi = 0;
    static Integer x1 = 0;
    static Integer y1 = 0;
    public static Integer z1;
    public static Integer time = 0;
    Integer c = 2, prov = 0;
    String texts;
    Integer process;
    //acselerometr

    //proverka taymera
    int[] masx = new int[10];
    int[] masy = new int[10];
    Integer q = 0;
    //proverka taymera
    //timer
    TimerTask mTimerTask;
    TimerTask mTimerTask2;
    Handler handler = new Handler();
    Handler handler2 = new Handler();
    Timer t = new Timer();
    Timer t2 = new Timer();
    Integer nCounter = 0;
    Integer nCounter2 = 0;
    Random random = new Random();

    ImageButton btnRotate;
    //timer

    private AdView mAdView;
    private RelativeLayout adViewContainer;
    private void updateAdViews() {
        if (AppPreferences.areAdsRemoved(this)) {
            adViewContainer.setVisibility(View.GONE);
        } else {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.cube);

        adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);

        mAdView = new AdView(this, "373834849620122_373834962953444", AdSize.BANNER_320_50);
        adViewContainer.addView(mAdView);
        mAdView.loadAd();

        display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();  // deprecated
        hetgth = display.getHeight();

        final LinearLayout lay = (LinearLayout) findViewById(R.id.linearLayouGl);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lay.getLayoutParams();
        params.setMargins(0, (int) (width * 0.125f), 0, 0);
        params.height = (int) (hetgth * 0.82f);
        params.width = (int) (width);
        lay.setLayoutParams(params);


        imageHand = (ImageView) findViewById(R.id.imageHand);
        params = (ViewGroup.MarginLayoutParams) imageHand.getLayoutParams();
        params.height = (int) (width * 0.3f);
        params.width = (int) (width * 0.3f);
        imageHand.setLayoutParams(params);
        imageHand.setVisibility(View.GONE);

        btnRotate = (ImageButton) findViewById(R.id.btnrotate);


        final AccelerateDecelerateInterpolator interpol = new AccelerateDecelerateInterpolator();
        final OvershootInterpolator inter2 = new OvershootInterpolator();

        sPref = getSharedPreferences("pozes", MODE_PRIVATE);
        int butRot = sPref.getInt("butRotate", 0);


        if (butRot == 1) {
            params = (ViewGroup.MarginLayoutParams) btnRotate.getLayoutParams();
            params.height = 0;
            params.width = 0;
            btnRotate.setLayoutParams(params);

            btnRotate.setVisibility(View.INVISIBLE);
            btnRotate.setEnabled(false);
        } else {
            params = (ViewGroup.MarginLayoutParams) btnRotate.getLayoutParams();
            params.setMargins((int) (width * 0.8f), (int) (hetgth - width * 0.35f), 0, 0);
            params.height = (int) (width * 0.18f);
            params.width = (int) (width * 0.18f);
            btnRotate.setLayoutParams(params);
            btnRotate.animate().scaleX(0f).scaleY(0f).setStartDelay(0).setDuration(0).setInterpolator(interpol);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnRotate.animate().scaleX(1f).scaleY(1f).setStartDelay(0).setDuration(200).setInterpolator(inter2);
                }
            }, 800);
        }

        glView = new MyGLSurfaceView(this);
        lay.addView(glView);
        //acselerometr
        //timer zapushen
        //    stopTask();
        //     doTimerTask();
        mForceValueText = (TextView) findViewById(R.id.textControl);
        mForceValueText.setTextColor(Color.WHITE);
        mForceValueText.setText(R.string.textcontsec2);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        if (sensors.size() > 0) {
            for (Sensor sensor : sensors) {
                switch (sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        if (mAccelerometerSensor == null) mAccelerometerSensor = sensor;
                        break;
                    default:
                        break;
                }
            }
        }


        //acselerometr
        Button btnhow = (Button) findViewById(R.id.btnhow);
        btnhow.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Animation anim = new MyAnimation(imageHand, (int) (width * 0.35f));
                anim.setDuration(2000);
                anim.setRepeatCount(1);
                anim.setFillAfter(true);
                anim.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                imageHand.setVisibility(View.VISIBLE);
                            }
                        }, 150);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // TODO Auto-generated method stub
                        imageHand.setVisibility(View.GONE);

                    }
                });
                imageHand.startAnimation(anim);

	
		            /*
					// TODO Auto-generated method stub
					if (isOnline()){
					Intent intent = new Intent(cube.this, Reklama.class);
				     startActivity(intent);	
				     finish();
				     onStop();
					} else {
						finish();
					     onStop();
					}            
				       */
            }
        });
        final Button btnback = (Button) findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                //	 stopTask();
                //	 onStop();
                Intent intent = new Intent(cube.this, sexrule.class);
                startActivity(intent);
                finish();
            }
        });

        glView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                counters = 0;
                doAction = 0;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // �������
                        secInt = 0;
                        whatText = 0;
                        mForceValueText.setText(R.string.textcontsec2);
                        break;
                    case MotionEvent.ACTION_MOVE: // ��������
                        if (secInt == 0) {
                            timers = new CountDownTimer(2000, 500) {

                                public void onTick(long millisUntilFinished) {
                                    mForceValueText.setTextColor(Color.RED);

                                    if (millisUntilFinished <= 1000) {
                                        mForceValueText.setText(R.string.textcontsec1);
                                        whatText = 1;
                                        btnRotate.setEnabled(false);
                                    }
                                }

                                public void onFinish() {
                                    if (whatText == 1) {
                                        sPref = getSharedPreferences("pozes", MODE_PRIVATE);

                                        String savedPoza1 = sPref.getString("poza1", "q");
                                        String savedPoza2 = sPref.getString("poza2", "q");
                                        String savedPoza3 = sPref.getString("poza3", "q");

                                        if ((savedPoza1.contains("q")) && (savedPoza2.contains("q")) && (savedPoza3.contains("q"))) {
                                            int massiveLengs = 0;
                                            for (int nomer = 0; nomer <= 42; nomer++) {
                                                Boolean isChecks = sPref.getBoolean("checkP" + nomer, true);
                                                if (isChecks == true) {
                                                    massiveLengs = massiveLengs + 1;
                                                }
                                            }
                                            Integer mass[] = new Integer[massiveLengs];
                                            int nomermass = 0;
                                            for (int nomer = 0; nomer <= 42; nomer++) {
                                                Boolean isChecks = sPref.getBoolean("checkP" + nomer, true);
                                                if (isChecks == true) {
                                                    mass[nomermass] = nomer;
                                                    nomermass = nomermass + 1;
                                                }
                                            }
                                            if (mass.length == 0) {
                                                for (int nomer = 0; nomer <= 42; nomer++) {
                                                    sPref.edit().putBoolean("checkP" + nomer, true).commit();
                                                }
                                                c = random.nextInt(42);
                                            } else {
                                                c = mass[random.nextInt(mass.length)];
                                            }
                                            vi = c;
                                        } else if (savedPoza1.contains("p")) {
                                            c = getPozaNomer(savedPoza1);
                                            vi = c;
                                            Editor ed = sPref.edit();
                                            ed.putString("poza1", "q");
                                            ed.commit();
                                        } else if (savedPoza2.contains("p")) {
                                            c = getPozaNomer(savedPoza2);
                                            vi = c;
                                            Editor ed = sPref.edit();
                                            ed.putString("poza2", "q");
                                            ed.commit();
                                        } else if (savedPoza3.contains("p")) {
                                            c = getPozaNomer(savedPoza3);
                                            vi = c;
                                            Editor ed = sPref.edit();
                                            ed.putString("poza3", "q");
                                            ed.commit();
                                        }


                                        mForceValueText.setTextColor(Color.WHITE);
                                        mForceValueText.setText(R.string.textcontdone);

                                        MyGLRenderer.texture_num0 = c;
                                        MyGLRenderer.texture_num1 = c;
                                        MyGLRenderer.texture_num2 = c;
                                        MyGLRenderer.texture_num3 = c;
                                        MyGLRenderer.texture_num4 = c;
                                        MyGLRenderer.texture_num5 = c;

                                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                                        long milliseconds = 100;
                                        v.vibrate(milliseconds);

                                    } else {
                                        mForceValueText.setTextColor(Color.WHITE);
                                        mForceValueText.setText(R.string.textcontsec2);
                                    }
                                    timers.cancel();
                                }
                            }.start();
                            secInt = 1;
                        }
                        break;
                    case MotionEvent.ACTION_UP: // ����������
                    case MotionEvent.ACTION_CANCEL:
                        if (whatText == 0) {
                            if (timers != null) timers.onFinish();
                        } else {
                            btnback.setEnabled(false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(cube.this, TextActivity.class);
                                    startActivity(intent);
                                    if (c != -1) {
                                        TextActivity.im = c;
                                    }
                                    finish();
                                }
                            }, 1800);
                        }
                        break;
                }
                return false;
            }
        });


        btnRotate.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                btnRotate.animate().scaleX(0f).scaleY(0f).setStartDelay(200).setDuration(200).setInterpolator(interpol);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MyGLRenderer.coficient = 0.1f;
                        btnback.setEnabled(false);
                    }
                }, 200);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sPref = getSharedPreferences("pozes", MODE_PRIVATE);

                        String savedPoza1 = sPref.getString("poza1", "q");
                        String savedPoza2 = sPref.getString("poza2", "q");
                        String savedPoza3 = sPref.getString("poza3", "q");

                        if ((savedPoza1.contains("q")) && (savedPoza2.contains("q")) && (savedPoza3.contains("q"))) {
                            int massiveLengs = 0;
                            for (int nomer = 0; nomer <= 42; nomer++) {
                                Boolean isChecks = sPref.getBoolean("checkP" + nomer, true);
                                if (isChecks == true) {
                                    massiveLengs = massiveLengs + 1;
                                }
                            }
                            Integer mass[] = new Integer[massiveLengs];
                            int nomermass = 0;
                            for (int nomer = 0; nomer <= 42; nomer++) {
                                Boolean isChecks = sPref.getBoolean("checkP" + nomer, true);
                                if (isChecks == true) {
                                    mass[nomermass] = nomer;
                                    nomermass = nomermass + 1;
                                }
                            }
                            if (mass.length == 0) {
                                for (int nomer = 0; nomer <= 42; nomer++) {
                                    sPref.edit().putBoolean("checkP" + nomer, true).commit();
                                }
                                c = random.nextInt(42);
                            } else {
                                c = mass[random.nextInt(mass.length)];
                            }
                            vi = c;
                        } else if (savedPoza1.contains("p")) {
                            c = getPozaNomer(savedPoza1);
                            vi = c;
                            Editor ed = sPref.edit();
                            ed.putString("poza1", "q");
                            ed.commit();
                        } else if (savedPoza2.contains("p")) {
                            c = getPozaNomer(savedPoza2);
                            vi = c;
                            Editor ed = sPref.edit();
                            ed.putString("poza2", "q");
                            ed.commit();
                        } else if (savedPoza3.contains("p")) {
                            c = getPozaNomer(savedPoza3);
                            vi = c;
                            Editor ed = sPref.edit();
                            ed.putString("poza3", "q");
                            ed.commit();
                        }


                        mForceValueText.setTextColor(Color.WHITE);
                        mForceValueText.setText(R.string.textcontdone);

                        MyGLRenderer.texture_num0 = c;
                        MyGLRenderer.texture_num1 = c;
                        MyGLRenderer.texture_num2 = c;
                        MyGLRenderer.texture_num3 = c;
                        MyGLRenderer.texture_num4 = c;
                        MyGLRenderer.texture_num5 = c;

                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                        long milliseconds = 100;
                        v.vibrate(milliseconds);

                        MyGLRenderer.coficient = 2;
                    }
                }, 1800);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(cube.this, TextActivity.class);
                        startActivity(intent);
                        if (c != -1) {
                            TextActivity.im = c;
                        }
                        finish();
                    }
                }, 3200);
                MyGLRenderer.coficient = 1;


            }
        });
    }


    //acselerometr
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        Sensor mMagneticFieldSensor = null;
        mSensorManager.registerListener(this, mMagneticFieldSensor, SensorManager.SENSOR_DELAY_GAME);
        updateAdViews();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: {
                //mXValueText.setText(String.format("%1.0f",
                //   event.values[SensorManager.DATA_X]));

                x1 = (int) Math.round(event.values[SensorManager.DATA_X]);
                y1 = (int) Math.round(event.values[SensorManager.DATA_Y]);
                z1 = (int) Math.round(event.values[SensorManager.DATA_Z]);

                // mYValueText.setText(String.format("%1.0f",
                //     event.values[SensorManager.DATA_Y]));
                // mZValueText.setText(String.format("%1.0f",
                //     event.values[SensorManager.DATA_Z]));
	 
	                /*
	                double totalForce = 0.0f;
	                totalForce += Math.pow(
	                    values[SensorManager.DATA_X]/SensorManager.GRAVITY_EARTH, 1.0);
	                totalForce += Math.pow(
	                    values[SensorManager.DATA_Y]/SensorManager.GRAVITY_EARTH, 1.0);
	                totalForce += Math.pow(
	                    values[SensorManager.DATA_Z]/SensorManager.GRAVITY_EARTH, 1.0);
	                totalForce = Math.sqrt(totalForce);
	                //mForceValueText.setText(String.format("%2.0f", totalForce));
	                */
            }
            break;
        }
    }

    //acselerometr
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        if (nInfo != null && nInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public Integer getPozaNomer(String poza) {
        if (poza == "p0") {
            c = 0;
        } else if (poza == "p1") {
            c = 1;
        } else if (poza == "p2") {
            c = 2;
        } else if (poza == "p3") {
            c = 3;
        } else if (poza == "p4") {
            c = 4;
        } else if (poza == "p5") {
            c = 5;
        } else if (poza == "p6") {
            c = 6;
        } else if (poza == "p7") {
            c = 7;
        } else if (poza == "p8") {
            c = 8;
        } else if (poza == "p9") {
            c = 9;
        } else if (poza == "p10") {
            c = 10;
        } else if (poza == "p11") {
            c = 11;
        } else if (poza == "p12") {
            c = 12;
        } else if (poza == "p13") {
            c = 13;
        } else if (poza == "p14") {
            c = 14;
        } else if (poza == "p15") {
            c = 15;
        } else if (poza == "p16") {
            c = 16;
        } else if (poza == "p17") {
            c = 17;
        } else if (poza == "p18") {
            c = 18;
        } else if (poza == "p19") {
            c = 19;
        } else if (poza == "p20") {
            c = 20;
        } else if (poza == "p21") {
            c = 21;
        } else if (poza == "p22") {
            c = 22;
        } else if (poza == "p23") {
            c = 23;
        } else if (poza == "p24") {
            c = 24;
        } else if (poza == "p25") {
            c = 25;
        } else if (poza == "p26") {
            c = 26;
        } else if (poza == "p27") {
            c = 27;
        } else if (poza == "p28") {
            c = 28;
        } else if (poza == "p29") {
            c = 29;
        } else if (poza == "p30") {
            c = 30;
        } else if (poza == "p31") {
            c = 31;
        } else if (poza == "p32") {
            c = 32;
        } else if (poza == "p33") {
            c = 33;
        } else if (poza == "p34") {
            c = 34;
        } else if (poza == "p35") {
            c = 35;
        } else if (poza == "p36") {
            c = 36;
        } else if (poza == "p37") {
            c = 37;
        } else if (poza == "p38") {
            c = 38;
        } else if (poza == "p39") {
            c = 39;
        } else if (poza == "p40") {
            c = 40;
        } else if (poza == "p41") {
            c = 41;
        } else if (poza == "p42") {
            c = 42;
        }
        return c;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        finish();
                    } else {
                        String langs = getResources().getConfiguration().locale.getLanguage();
                        if (langs.contains("ru")) {
                            Toast.makeText(getApplicationContext(), "������� ����� ��� ������.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (langs.contains("uk")) {
                            Toast.makeText(getApplicationContext(), "�������� ����� ��� ������.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Press again to exit.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}