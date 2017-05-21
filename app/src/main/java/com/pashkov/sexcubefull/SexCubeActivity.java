package com.pashkov.sexcubefull;

import java.security.PublicKey;
import java.util.Timer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;

import java.util.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.pashkov.sexcubefull.R;
import com.pashkov.sexcubefull.gcm.RegistrationIntentService;
import com.pashkov.sexcubefull.util.AppPreferences;


public class SexCubeActivity extends Activity {

    FrameLayout framepass;
    public static ImageView imgCifra1, imgCifra2, imgCifra3, imgCifra4, buyBut, sumWayBut;
    public static ImageButton imgPass1, imgPass2, imgPass3, imgPass4, imgPass5, imgPass6, imgPass7, imgPass8, imgPass9, imgPass0, imgPassExit, imgPassClear;

    AccelerateDecelerateInterpolator interpol;
    AnticipateOvershootInterpolator inter;
    OvershootInterpolator inter2;

    int width;
    int hetgth;

    LinearLayout layImgSexCube;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private boolean isReceiverRegistered;

    public static int nomercifra1, nomercifra2, nomercifra3, nomercifra4;

    ProgressBar bar;
    int total = 0;
    boolean isRunning = false;
    // handler for the background updating
    Handler handler;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppPreferences.initialize(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//				if (isTapjoyConnected) {
//					enableOfferWallButton();
//				}
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();  // deprecated
        hetgth = display.getHeight();

        interpol = new AccelerateDecelerateInterpolator();
        inter = new AnticipateOvershootInterpolator();
        inter2 = new OvershootInterpolator();

        LinearLayout layImgSex = (LinearLayout) findViewById(R.id.linearLayoutSexImg);
        ViewGroup.MarginLayoutParams paramsLay = (ViewGroup.MarginLayoutParams) layImgSex.getLayoutParams();
        paramsLay.setMargins((int) (width * 0.05f), (int) (width * 0.3f), 0, 0);
        paramsLay.height = (int) (width * 0.9f);
        paramsLay.width = (int) (width * 0.9f);
        layImgSex.setLayoutParams(paramsLay);

        layImgSexCube = (LinearLayout) findViewById(R.id.linearLayoutSexCube);
        paramsLay = (ViewGroup.MarginLayoutParams) layImgSexCube.getLayoutParams();
        paramsLay.setMargins(0, (int) (hetgth - width * 0.2f), 0, 0);
        paramsLay.height = (int) (width * 0.17f);
        //paramsLay.width = (int)(width*0.9f);
        layImgSexCube.setLayoutParams(paramsLay);

        LinearLayout layBar = (LinearLayout) findViewById(R.id.linearLayoutBar);
        paramsLay = (ViewGroup.MarginLayoutParams) layBar.getLayoutParams();
        paramsLay.setMargins(0, (int) (hetgth - width * 0.15f), 0, 0);
        //paramsLay.height = (int)(width*0.17f);
        //paramsLay.width = (int)(width*0.9f);
        layBar.setLayoutParams(paramsLay);
        //TODO frame pass

        int heigthfrag = (int) ((hetgth - width * 1.2f) / 2);

        framepass = (FrameLayout) findViewById(R.id.framepassApp);
        ViewGroup.MarginLayoutParams paramspass = (ViewGroup.MarginLayoutParams) framepass.getLayoutParams();
        paramspass.setMargins((int) (width * 0.1f), heigthfrag, 0, 0);
        paramspass.height = (int) (width * 1.225f);
        paramspass.width = (int) (width * 0.8f);
        framepass.setLayoutParams(paramspass);

        //framepass.setVisibility(View.GONE);
        //framepass.animate().translationX(width).setStartDelay(0).setDuration(50).setInterpolator(interpol);
        //framepass.animate().alpha(0).setStartDelay(0).setDuration(50).setInterpolator(interpol);

        ImageView imgPass = (ImageView) findViewById(R.id.imgPassApp);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass.getLayoutParams();
        paramspass.setMargins(0, (int) (width * 0.01f), 0, 0);
        paramspass.height = (int) (width * 0.16f);
        paramspass.width = (int) (width * 0.79f);
        imgPass.setLayoutParams(paramspass);


        imgCifra1 = (ImageView) findViewById(R.id.imgCifa1App);
        paramspass = (ViewGroup.MarginLayoutParams) imgCifra1.getLayoutParams();
        paramspass.setMargins((int) (width * 0.095f), (int) (width * 0.19f), 0, 0);
        paramspass.height = (int) (width * 0.13f);
        paramspass.width = (int) (width * 0.13f);
        imgCifra1.setLayoutParams(paramspass);
        imgCifra1.setTag(10);

        imgCifra2 = (ImageView) findViewById(R.id.imgCifa2App);
        paramspass = (ViewGroup.MarginLayoutParams) imgCifra2.getLayoutParams();
        paramspass.setMargins((int) (width * 0.255f), (int) (width * 0.19f), 0, 0);
        paramspass.height = (int) (width * 0.13f);
        paramspass.width = (int) (width * 0.13f);
        imgCifra2.setLayoutParams(paramspass);
        imgCifra2.setTag(10);

        imgCifra3 = (ImageView) findViewById(R.id.imgCifa3App);
        paramspass = (ViewGroup.MarginLayoutParams) imgCifra3.getLayoutParams();
        paramspass.setMargins((int) (width * 0.415f), (int) (width * 0.19f), 0, 0);
        paramspass.height = (int) (width * 0.13f);
        paramspass.width = (int) (width * 0.13f);
        imgCifra3.setLayoutParams(paramspass);
        imgCifra3.setTag(10);

        imgCifra4 = (ImageView) findViewById(R.id.imgCifa4App);
        paramspass = (ViewGroup.MarginLayoutParams) imgCifra4.getLayoutParams();
        paramspass.setMargins((int) (width * 0.575f), (int) (width * 0.19f), 0, 0);
        paramspass.height = (int) (width * 0.13f);
        paramspass.width = (int) (width * 0.13f);
        imgCifra4.setLayoutParams(paramspass);
        imgCifra4.setTag(10);


        imgPass1 = (ImageButton) findViewById(R.id.imgpass1App);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass1.getLayoutParams();
        paramspass.setMargins((int) (width * 0.07f), (int) (width * 0.35f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass1.setLayoutParams(paramspass);

        imgPass2 = (ImageButton) findViewById(R.id.imgpass2App);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass2.getLayoutParams();
        paramspass.setMargins((int) (width * 0.295f), (int) (width * 0.35f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass2.setLayoutParams(paramspass);

        imgPass3 = (ImageButton) findViewById(R.id.imgpass3App);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass3.getLayoutParams();
        paramspass.setMargins((int) (width * 0.52f), (int) (width * 0.35f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass3.setLayoutParams(paramspass);

        imgPass4 = (ImageButton) findViewById(R.id.imgpass4App);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass4.getLayoutParams();
        paramspass.setMargins((int) (width * 0.07f), (int) (width * 0.555f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass4.setLayoutParams(paramspass);

        imgPass5 = (ImageButton) findViewById(R.id.imgpass5App);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass5.getLayoutParams();
        paramspass.setMargins((int) (width * 0.295f), (int) (width * 0.555f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass5.setLayoutParams(paramspass);

        imgPass6 = (ImageButton) findViewById(R.id.imgpass6App);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass6.getLayoutParams();
        paramspass.setMargins((int) (width * 0.52f), (int) (width * 0.555f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass6.setLayoutParams(paramspass);

        imgPass7 = (ImageButton) findViewById(R.id.imgpass7App);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass7.getLayoutParams();
        paramspass.setMargins((int) (width * 0.07f), (int) (width * 0.760f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass7.setLayoutParams(paramspass);

        imgPass8 = (ImageButton) findViewById(R.id.imgpass8App);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass8.getLayoutParams();
        paramspass.setMargins((int) (width * 0.295f), (int) (width * 0.760f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass8.setLayoutParams(paramspass);

        imgPass9 = (ImageButton) findViewById(R.id.imgpass9App);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass9.getLayoutParams();
        paramspass.setMargins((int) (width * 0.52f), (int) (width * 0.760f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass9.setLayoutParams(paramspass);

        imgPassExit = (ImageButton) findViewById(R.id.imgpassExitApp);
        paramspass = (ViewGroup.MarginLayoutParams) imgPassExit.getLayoutParams();
        paramspass.setMargins((int) (width * 0.07f), (int) (width * 0.965f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPassExit.setLayoutParams(paramspass);

        imgPass0 = (ImageButton) findViewById(R.id.imgpass0App);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass0.getLayoutParams();
        paramspass.setMargins((int) (width * 0.295f), (int) (width * 0.965f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass0.setLayoutParams(paramspass);

        imgPassClear = (ImageButton) findViewById(R.id.imgpassClearApp);
        paramspass = (ViewGroup.MarginLayoutParams) imgPassClear.getLayoutParams();
        paramspass.setMargins((int) (width * 0.52f), (int) (width * 0.965f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPassClear.setLayoutParams(paramspass);

        imgCifra1.setTag(10);
        imgCifra2.setTag(10);
        imgCifra3.setTag(10);
        imgCifra4.setTag(10);

        nomercifra1 = (Integer) imgCifra1.getTag();
        nomercifra2 = (Integer) imgCifra2.getTag();
        nomercifra3 = (Integer) imgCifra3.getTag();
        nomercifra4 = (Integer) imgCifra4.getTag();

        imgPass0.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (nomercifra4 == 10) {
                    passButOnClick(0);
                    animButPass(imgPass0);
                }

            }
        });

        imgPass1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (nomercifra4 == 10) {
                    passButOnClick(1);
                    animButPass(imgPass1);
                }
            }
        });

        imgPass2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (nomercifra4 == 10) {
                    passButOnClick(2);
                    animButPass(imgPass2);
                }
            }
        });

        imgPass3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (nomercifra4 == 10) {
                    passButOnClick(3);
                    animButPass(imgPass3);
                }
            }
        });

        imgPass4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (nomercifra4 == 10) {
                    passButOnClick(4);
                    animButPass(imgPass4);
                }
            }
        });

        imgPass5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (nomercifra4 == 10) {
                    passButOnClick(5);
                    animButPass(imgPass5);
                }
            }
        });

        imgPass6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (nomercifra4 == 10) {
                    passButOnClick(6);
                    animButPass(imgPass6);
                }
            }
        });

        imgPass7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (nomercifra4 == 10) {
                    passButOnClick(7);
                    animButPass(imgPass7);
                }
            }
        });

        imgPass8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (nomercifra4 == 10) {
                    passButOnClick(8);
                    animButPass(imgPass8);
                }
            }
        });

        imgPass9.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (nomercifra4 == 10) {
                    passButOnClick(9);
                    animButPass(imgPass9);
                }
            }
        });

        imgPassClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                passButOnClick(10);
                animButPass(imgPassClear);
            }
        });

        imgPassExit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //framepass.animate().translationX(width*0.9f).alpha(0).setStartDelay(450).setDuration(300).setInterpolator(interpol);
                //animButPass (imgPassExit);

                imgCifra1.setTag(10);
                imgCifra2.setTag(10);
                imgCifra3.setTag(10);
                imgCifra4.setTag(10);

                animImgCigrPass(imgCifra1, R.drawable.cifrano);
                animImgCigrPass(imgCifra2, R.drawable.cifrano);
                animImgCigrPass(imgCifra3, R.drawable.cifrano);
                animImgCigrPass(imgCifra4, R.drawable.cifrano);

                nomercifra1 = (Integer) imgCifra1.getTag();
                nomercifra2 = (Integer) imgCifra2.getTag();
                nomercifra3 = (Integer) imgCifra3.getTag();
                nomercifra4 = (Integer) imgCifra4.getTag();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 600);
            }
        });


        SharedPreferences sPref = getSharedPreferences("pozes", MODE_PRIVATE);
        int passApp = sPref.getInt("passApp", 0);

        bar = (ProgressBar) findViewById(R.id.progressBar1);

        if (passApp == 1) {
            layImgSexCube.animate().scaleX(0).scaleY(0).setStartDelay(0).setDuration(0).setInterpolator(interpol);
        } else {
            framepass.animate().translationX(width * 0.9f).alpha(0).setStartDelay(0).setDuration(0).setInterpolator(interpol);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startProgress();
                }
            }, 400);
        }

    }

    public void onStart() {
        super.onStart();
        // reset the bar to the default value of 0
        bar.setProgress(0);
        // create a thread for updating the progress bar
        Thread background = new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < 100 && isRunning; i++) {
                        // wait 1000ms between each update
                        Thread.sleep(10);
                        handler.sendMessage(handler.obtainMessage());
                    }
                } catch (Throwable t) {

                }
            }
        });

        isRunning = true;
        // start the background thread
        background.start();
    }

    public void onStop() {
        super.onStop();
        isRunning = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    public void onPause() {
        unregisterReceiver();
        super.onPause();
    }

    //TODO one
    public void animImgCigrPass(final ImageView imgCifr, final int idDravable) {
        imgCifr.animate().scaleX(0.5f).scaleY(0.5f).setStartDelay(50).setDuration(200).setInterpolator(inter2);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                imgCifr.setBackgroundResource(idDravable);
            }
        }, 200);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                imgCifr.animate().scaleX(1f).scaleY(1f).setStartDelay(0).setDuration(200).setInterpolator(inter2);
            }
        }, 250);
    }

    //TODO two
    public void animButPass(final ImageButton imgBut) {
        imgBut.animate().scaleX(0.85f).scaleY(0.85f).setStartDelay(50).setDuration(200).setInterpolator(inter2);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                imgBut.animate().scaleX(1f).scaleY(1f).setStartDelay(0).setDuration(200).setInterpolator(inter2);
            }
        }, 250);
    }

    public void passButOnClick(int nomerCifra) {
        nomercifra1 = (Integer) imgCifra1.getTag();
        nomercifra2 = (Integer) imgCifra2.getTag();
        nomercifra3 = (Integer) imgCifra3.getTag();
        nomercifra4 = (Integer) imgCifra4.getTag();

        switch (nomerCifra) {
            case 0: {
                if (nomercifra1 == 10) {
                    imgCifra1.setTag(0);
                    animImgCigrPass(imgCifra1, R.drawable.cifra0);
                } else if (nomercifra2 == 10) {
                    imgCifra2.setTag(0);
                    animImgCigrPass(imgCifra2, R.drawable.cifra0);
                } else if (nomercifra3 == 10) {
                    imgCifra3.setTag(0);
                    animImgCigrPass(imgCifra3, R.drawable.cifra0);
                } else if (nomercifra4 == 10) {
                    imgCifra4.setTag(0);
                    animImgCigrPass(imgCifra4, R.drawable.cifra0);
                    //�������� �� ������������ ������
                    doSomething();
                }
                break;
            }
            case 1: {
                if (nomercifra1 == 10) {
                    imgCifra1.setTag(1);
                    animImgCigrPass(imgCifra1, R.drawable.cifra1);
                } else if (nomercifra2 == 10) {
                    imgCifra2.setTag(1);
                    animImgCigrPass(imgCifra2, R.drawable.cifra1);
                } else if (nomercifra3 == 10) {
                    imgCifra3.setTag(1);
                    animImgCigrPass(imgCifra3, R.drawable.cifra1);
                } else if (nomercifra4 == 10) {
                    imgCifra4.setTag(1);
                    animImgCigrPass(imgCifra4, R.drawable.cifra1);
                    //�������� �� ������������ ������
                    doSomething();
                }
                break;
            }
            case 2: {
                if (nomercifra1 == 10) {
                    imgCifra1.setTag(2);
                    animImgCigrPass(imgCifra1, R.drawable.cifra2);
                } else if (nomercifra2 == 10) {
                    imgCifra2.setTag(2);
                    animImgCigrPass(imgCifra2, R.drawable.cifra2);
                } else if (nomercifra3 == 10) {
                    imgCifra3.setTag(2);
                    animImgCigrPass(imgCifra3, R.drawable.cifra2);
                } else if (nomercifra4 == 10) {
                    imgCifra4.setTag(2);
                    animImgCigrPass(imgCifra4, R.drawable.cifra2);
                    //�������� �� ������������ ������
                    doSomething();
                }
                break;
            }
            case 3: {
                if (nomercifra1 == 10) {
                    imgCifra1.setTag(3);
                    animImgCigrPass(imgCifra1, R.drawable.cifra3);
                } else if (nomercifra2 == 10) {
                    imgCifra2.setTag(3);
                    animImgCigrPass(imgCifra2, R.drawable.cifra3);
                } else if (nomercifra3 == 10) {
                    imgCifra3.setTag(3);
                    animImgCigrPass(imgCifra3, R.drawable.cifra3);
                } else if (nomercifra4 == 10) {
                    imgCifra4.setTag(3);
                    animImgCigrPass(imgCifra4, R.drawable.cifra3);
                    //�������� �� ������������ ������
                    doSomething();
                }
                break;
            }
            case 4: {
                if (nomercifra1 == 10) {
                    imgCifra1.setTag(4);
                    animImgCigrPass(imgCifra1, R.drawable.cifra4);
                } else if (nomercifra2 == 10) {
                    imgCifra2.setTag(4);
                    animImgCigrPass(imgCifra2, R.drawable.cifra4);
                } else if (nomercifra3 == 10) {
                    imgCifra3.setTag(4);
                    animImgCigrPass(imgCifra3, R.drawable.cifra4);
                } else if (nomercifra4 == 10) {
                    imgCifra4.setTag(4);
                    animImgCigrPass(imgCifra4, R.drawable.cifra4);
                    //�������� �� ������������ ������
                    doSomething();
                }
                break;
            }
            case 5: {
                if (nomercifra1 == 10) {
                    imgCifra1.setTag(5);
                    animImgCigrPass(imgCifra1, R.drawable.cifra5);
                } else if (nomercifra2 == 10) {
                    imgCifra2.setTag(5);
                    animImgCigrPass(imgCifra2, R.drawable.cifra5);
                } else if (nomercifra3 == 10) {
                    imgCifra3.setTag(5);
                    animImgCigrPass(imgCifra3, R.drawable.cifra5);
                } else if (nomercifra4 == 10) {
                    imgCifra4.setTag(5);
                    animImgCigrPass(imgCifra4, R.drawable.cifra5);
                    //�������� �� ������������ ������
                    doSomething();
                }
                break;
            }
            case 6: {
                if (nomercifra1 == 10) {
                    imgCifra1.setTag(6);
                    animImgCigrPass(imgCifra1, R.drawable.cifra6);
                } else if (nomercifra2 == 10) {
                    imgCifra2.setTag(6);
                    animImgCigrPass(imgCifra2, R.drawable.cifra6);
                } else if (nomercifra3 == 10) {
                    imgCifra3.setTag(6);
                    animImgCigrPass(imgCifra3, R.drawable.cifra6);
                } else if (nomercifra4 == 10) {
                    imgCifra4.setTag(6);
                    animImgCigrPass(imgCifra4, R.drawable.cifra6);
                    //�������� �� ������������ ������
                    doSomething();
                }
                break;
            }
            case 7: {
                if (nomercifra1 == 10) {
                    imgCifra1.setTag(7);
                    animImgCigrPass(imgCifra1, R.drawable.cifra7);
                } else if (nomercifra2 == 10) {
                    imgCifra2.setTag(7);
                    animImgCigrPass(imgCifra2, R.drawable.cifra7);
                } else if (nomercifra3 == 10) {
                    imgCifra3.setTag(7);
                    animImgCigrPass(imgCifra3, R.drawable.cifra7);
                } else if (nomercifra4 == 10) {
                    imgCifra4.setTag(7);
                    animImgCigrPass(imgCifra4, R.drawable.cifra7);
                    //�������� �� ������������ ������
                    doSomething();
                }
                break;
            }
            case 8: {
                if (nomercifra1 == 10) {
                    imgCifra1.setTag(8);
                    animImgCigrPass(imgCifra1, R.drawable.cifra8);
                } else if (nomercifra2 == 10) {
                    imgCifra2.setTag(8);
                    animImgCigrPass(imgCifra2, R.drawable.cifra8);
                } else if (nomercifra3 == 10) {
                    imgCifra3.setTag(8);
                    animImgCigrPass(imgCifra3, R.drawable.cifra8);
                } else if (nomercifra4 == 10) {
                    imgCifra4.setTag(8);
                    animImgCigrPass(imgCifra4, R.drawable.cifra8);
                    //�������� �� ������������ ������
                    doSomething();
                }
                break;
            }
            case 9: {
                if (nomercifra1 == 10) {
                    imgCifra1.setTag(9);
                    animImgCigrPass(imgCifra1, R.drawable.cifra9);
                } else if (nomercifra2 == 10) {
                    imgCifra2.setTag(9);
                    animImgCigrPass(imgCifra2, R.drawable.cifra9);
                } else if (nomercifra3 == 10) {
                    imgCifra3.setTag(9);
                    animImgCigrPass(imgCifra3, R.drawable.cifra9);
                } else if (nomercifra4 == 10) {
                    imgCifra4.setTag(9);
                    animImgCigrPass(imgCifra4, R.drawable.cifra9);
                    //�������� �� ������������ ������
                    doSomething();
                }
                break;
            }

            case 10: {

                imgCifra1.setTag(10);
                animImgCigrPass(imgCifra1, R.drawable.cifrano);

                imgCifra2.setTag(10);
                animImgCigrPass(imgCifra2, R.drawable.cifrano);

                imgCifra3.setTag(10);
                animImgCigrPass(imgCifra3, R.drawable.cifrano);

                imgCifra4.setTag(10);
                animImgCigrPass(imgCifra4, R.drawable.cifrano);

                break;
            }

            default: {
                break;
            }
        }

        nomercifra1 = (Integer) imgCifra1.getTag();
        nomercifra2 = (Integer) imgCifra2.getTag();
        nomercifra3 = (Integer) imgCifra3.getTag();
        nomercifra4 = (Integer) imgCifra4.getTag();
    }

    public void doSomething() {
        SharedPreferences sPref = getSharedPreferences("pozes", MODE_PRIVATE);
        Editor ed = sPref.edit();

        int pass1 = sPref.getInt("pass1", 0);
        int pass2 = sPref.getInt("pass2", 0);
        int pass3 = sPref.getInt("pass3", 0);
        int pass4 = sPref.getInt("pass4", 0);
        if ((pass1 == (Integer) imgCifra1.getTag()) && (pass2 == (Integer) imgCifra2.getTag()) && (pass3 == (Integer) imgCifra3.getTag()) && (pass4 == (Integer) imgCifra4.getTag())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    framepass.animate().translationX(width * 0.9f).alpha(0).setStartDelay(0).setDuration(300).setInterpolator(interpol);
                }
            }, 400);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgCifra1.setTag(10);
                    imgCifra2.setTag(10);
                    imgCifra3.setTag(10);
                    imgCifra4.setTag(10);

                    animImgCigrPass(imgCifra1, R.drawable.cifrano);
                    animImgCigrPass(imgCifra2, R.drawable.cifrano);
                    animImgCigrPass(imgCifra3, R.drawable.cifrano);
                    animImgCigrPass(imgCifra4, R.drawable.cifrano);

                    nomercifra1 = (Integer) imgCifra1.getTag();
                    nomercifra2 = (Integer) imgCifra2.getTag();
                    nomercifra3 = (Integer) imgCifra3.getTag();
                    nomercifra4 = (Integer) imgCifra4.getTag();

                    startProgress();

                }
            }, 850);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                    long milliseconds = 300;
                    v.vibrate(milliseconds);
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.badpass), Toast.LENGTH_SHORT).show();

                    imgCifra1.setTag(10);
                    imgCifra2.setTag(10);
                    imgCifra3.setTag(10);
                    imgCifra4.setTag(10);

                    animImgCigrPass(imgCifra1, R.drawable.cifrano);
                    animImgCigrPass(imgCifra2, R.drawable.cifrano);
                    animImgCigrPass(imgCifra3, R.drawable.cifrano);
                    animImgCigrPass(imgCifra4, R.drawable.cifrano);

                    nomercifra1 = (Integer) imgCifra1.getTag();
                    nomercifra2 = (Integer) imgCifra2.getTag();
                    nomercifra3 = (Integer) imgCifra3.getTag();
                    nomercifra4 = (Integer) imgCifra4.getTag();
                }
            }, 400);
        }
    }

    public void startProgress() {

        layImgSexCube.animate().scaleX(1).scaleY(1).setStartDelay(0).setDuration(300).setInterpolator(inter2);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                total = total + 1;
                bar.incrementProgressBy(1);
            }
        };
        onStart();


        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SexCubeActivity.this, sexrule.class);
                startActivity(intent);
                finish();
                onStop();

                //����� ����� ������������
                //}
            }
        };

        timer.schedule(task, 1000);


    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(AppPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
            isReceiverRegistered = false;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}

