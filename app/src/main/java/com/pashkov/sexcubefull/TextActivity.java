package com.pashkov.sexcubefull;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.pashkov.sexcubefull.R;

import com.facebook.ads.*;

import com.pashkov.sexcubefull.util.AppPreferences;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class TextActivity extends Activity {
    MyGLRenderer renderer;
    cube cubik;
    static Integer im = 0;

    private static final int PERIOD = 2000;
    private long lastPressedTime;

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


        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int hetgth = display.getHeight();


        setContentView(R.layout.paza);
        TextView textview = (TextView) findViewById(R.id.textRules);
        ImageView img = (ImageView) findViewById(R.id.imgSex);

        adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);
        mAdView = new AdView(this, "373834849620122_373834962953444", AdSize.BANNER_320_50);
        adViewContainer.addView(mAdView);
        mAdView.loadAd();


        ViewGroup.MarginLayoutParams paramsPoza = (ViewGroup.MarginLayoutParams) img.getLayoutParams();
        paramsPoza.setMargins((int) (width * 0.05f), (int) (width * 0.05f), (int) (width * 0.05f), (int) (width * 0.05f));
        paramsPoza.height = (int) (width * 0.9);
        paramsPoza.width = (int) (width * 0.9);
        img.setLayoutParams(paramsPoza);
        // textview.setText(im.toString());

        switch (im) {
            case 0:
                img.setBackgroundResource(R.drawable.k0);
                textview.setText(R.string.pos_0);
                break;
            case 1:
                img.setBackgroundResource(R.drawable.k1);
                textview.setText(R.string.pos_1);
                break;
            case 2:
                img.setBackgroundResource(R.drawable.k2);
                textview.setText(R.string.pos_2);
                break;
            case 3:
                img.setBackgroundResource(R.drawable.k3);
                textview.setText(R.string.pos_3);
                break;
            case 4:
                img.setBackgroundResource(R.drawable.k4);
                textview.setText(R.string.pos_4);
                break;
            case 5:
                img.setBackgroundResource(R.drawable.k5);
                textview.setText(R.string.pos_5);
                break;
            case 6:
                img.setBackgroundResource(R.drawable.k6);
                textview.setText(R.string.pos_6);
                break;
            case 7:
                img.setBackgroundResource(R.drawable.k7);
                textview.setText(R.string.pos_7);
                break;
            case 8:
                img.setBackgroundResource(R.drawable.k8);
                textview.setText(R.string.pos_8);
                break;
            case 9:
                img.setBackgroundResource(R.drawable.k9);
                textview.setText(R.string.pos_9);
                break;
            case 10:
                img.setBackgroundResource(R.drawable.k10);
                textview.setText(R.string.pos_10);
                break;
            case 11:
                img.setBackgroundResource(R.drawable.k11);
                textview.setText(R.string.pos_11);
                break;
            case 12:
                img.setBackgroundResource(R.drawable.k12);
                textview.setText(R.string.pos_12);
                break;
            case 13:
                img.setBackgroundResource(R.drawable.k13);
                textview.setText(R.string.pos_13);
                break;
            case 14:
                img.setBackgroundResource(R.drawable.k14);
                textview.setText(R.string.pos_14);
                break;
            case 15:
                img.setBackgroundResource(R.drawable.k15);
                textview.setText(R.string.pos_15);
                break;
            case 16:
                img.setBackgroundResource(R.drawable.k16);
                textview.setText(R.string.pos_16);
                break;
            case 17:
                img.setBackgroundResource(R.drawable.k17);
                textview.setText(R.string.pos_17);
                break;
            case 18:
                img.setBackgroundResource(R.drawable.k18);
                textview.setText(R.string.pos_18);
                break;
            case 19:
                img.setBackgroundResource(R.drawable.k19);
                textview.setText(R.string.pos_19);
                break;
            case 20:
                img.setBackgroundResource(R.drawable.k20);
                textview.setText(R.string.pos_20);
                break;
            case 21:
                img.setBackgroundResource(R.drawable.k21);
                textview.setText(R.string.pos_21);
                break;
            case 22:
                img.setBackgroundResource(R.drawable.k22);
                textview.setText(R.string.pos_22);
                break;
            case 23:
                img.setBackgroundResource(R.drawable.k23);
                textview.setText(R.string.pos_23);
                break;
            case 24:
                img.setBackgroundResource(R.drawable.k24);
                textview.setText(R.string.pos_24);
                break;
            case 25:
                img.setBackgroundResource(R.drawable.k25);
                textview.setText(R.string.pos_25);
                break;
            case 26:
                img.setBackgroundResource(R.drawable.k26);
                textview.setText(R.string.pos_26);
                break;
            case 27:
                img.setBackgroundResource(R.drawable.k27);
                textview.setText(R.string.pos_27);
                break;
            case 28:
                img.setBackgroundResource(R.drawable.k28);
                textview.setText(R.string.pos_28);
                break;
            case 29:
                img.setBackgroundResource(R.drawable.k29);
                textview.setText(R.string.pos_29);
                break;
            case 30:
                img.setBackgroundResource(R.drawable.k30);
                textview.setText(R.string.pos_30);
                break;
            case 31:
                img.setBackgroundResource(R.drawable.k31);
                textview.setText(R.string.pos_31);
                break;
            case 32:
                img.setBackgroundResource(R.drawable.k32);
                textview.setText(R.string.pos_32);
                break;
            case 33:
                img.setBackgroundResource(R.drawable.k33);
                textview.setText(R.string.pos_33);
                break;
            case 34:
                img.setBackgroundResource(R.drawable.k34);
                textview.setText(R.string.pos_34);
                break;
            case 35:
                img.setBackgroundResource(R.drawable.k35);
                textview.setText(R.string.pos_35);
                break;
            case 36:
                img.setBackgroundResource(R.drawable.k36);
                textview.setText(R.string.pos_36);
                break;
            case 37:
                img.setBackgroundResource(R.drawable.k37);
                textview.setText(R.string.pos_37);
                break;
            case 38:
                img.setBackgroundResource(R.drawable.k38);
                textview.setText(R.string.pos_38);
                break;
            case 39:
                img.setBackgroundResource(R.drawable.k39);
                textview.setText(R.string.pos_39);
                break;
            case 40:
                img.setBackgroundResource(R.drawable.k40);
                textview.setText(R.string.pos_40);
                break;
            case 41:
                img.setBackgroundResource(R.drawable.k41);
                textview.setText(R.string.pos_41);
                break;
            case 42:
                img.setBackgroundResource(R.drawable.k42);
                textview.setText(R.string.pos_42);
                break;
            default:
                MyGLRenderer.texture_num0 = 15;
                MyGLRenderer.texture_num1 = 18;
                MyGLRenderer.texture_num2 = 22;
                MyGLRenderer.texture_num3 = 19;
                MyGLRenderer.texture_num4 = 23;
                MyGLRenderer.texture_num5 = 21;
                Intent intent = new Intent(TextActivity.this, cube.class);
                startActivity(intent);
                finish();
                onStop();
                break;
        }


        Button btnCube = (Button) findViewById(R.id.butCube2);
        Button btnShare = (Button) findViewById(R.id.butShare2);

        // ScrollView scr = (ScrollView)findViewById(R.id.scrollView1);
        //scr.setBackgroundColor(50);
        btnCube.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                MyGLRenderer.texture_num0 = 15;
                MyGLRenderer.texture_num1 = 18;
                MyGLRenderer.texture_num2 = 22;
                MyGLRenderer.texture_num3 = 19;
                MyGLRenderer.texture_num4 = 23;
                MyGLRenderer.texture_num5 = 21;
                Intent intent = new Intent(TextActivity.this, cube.class);
                startActivity(intent);
                finish();
                onStop();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.sharetext));
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.shareapp)));
            }
        });

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        if (nInfo != null && nInfo.isConnected()) {

            return true;
        } else {

            return false;
        }
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
    protected void onResume() {
        super.onResume();
        updateAdViews();
    }
}
