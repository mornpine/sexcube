package com.pashkov.sexcubefull;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.vending.billing.IInAppBillingService;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

import com.facebook.ads.*;

import com.pashkov.sexcubefull.R;
import com.pashkov.sexcubefull.util.AppPreferences;
import com.pashkov.sexcubefull.view_pager.CarouselPagerAdapter;
import com.pashkov.sexcubefull.view_pager.CarouselViewPager;
import com.pashkov.sexcubefull.view_pager.OnPageClickListener;
import com.pashkov.sexcubefull.view_pager.PageFragment;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
//import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
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
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

import com.pashkov.sexcubefull.util.IabBroadcastReceiver;
import com.pashkov.sexcubefull.util.IabBroadcastReceiver.IabBroadcastListener;
import com.pashkov.sexcubefull.util.IabHelper;
import com.pashkov.sexcubefull.util.IabHelper.IabAsyncInProgressException;
import com.pashkov.sexcubefull.util.IabResult;
import com.pashkov.sexcubefull.util.Inventory;
import com.pashkov.sexcubefull.util.Purchase;
import com.tapjoy.TJConnectListener;
import com.tapjoy.Tapjoy;


public class sexrule extends FragmentActivity implements IabBroadcastListener,
        OnClickListener, OnPageClickListener<MyPageItem> {
    MyGLRenderer renderer;
    cube cubik;

    private static final int PERIOD = 2000;
    private long lastPressedTime;

    public static SharedPreferences sPref;

    Display display;
    float width, hetgth;

    ImageView imgSexCube;
    ImageButton imgSecret, buyButton, installButton;
    ImageButton imgSettings;
    ImageButton imgPoza1, imgPoza2, imgPoza3;
    public static ImageButton imgOkBut;
    LinearLayout laySecret, laySettings, parentLay;

    int setPassOne = 0;
    int pass1, pass2, pass3, pass4;

    FrameLayout framePoza;

    public static Context contextApp;

    FrameLayout frame_levels;

    Button btnCube, butPassword;
    TextView textRules;

    ScrollView buyText;

    TableRow tableRow;

    TableLayout tableLay;
    int ij, ik = 0;

    public static MyPageFragment myPageFragment;
    public static CarouselPagerAdapter<MyPageItem> mPagerAdapter;
    public static CarouselViewPager mViewPager;
    public static ArrayList<MyPageItem> mItems;
    Bundle instanceState;

    Boolean butClick = true;

    AccelerateDecelerateInterpolator interpol;
    AnticipateOvershootInterpolator inter;
    OvershootInterpolator inter2;

    FrameLayout framepass, framebuy;
    public static ImageView imgCifra1, imgCifra2, imgCifra3, imgCifra4, buyBut, sumWayBut;
    public static ImageButton imgPass1, imgPass2, imgPass3, imgPass4, imgPass5, imgPass6, imgPass7, imgPass8, imgPass9, imgPass0, imgPassExit, imgPassClear;

    public static int nomercifra1, nomercifra2, nomercifra3, nomercifra4;

    CheckBox checkPassApp, checkPassSecret, checkIconChange, checkIconName, checkRules, checkInviz, checkRotateBut;
    EditText edittextRules;

    //String inappid = "android.test.purchased"; //replace this with your in-app product id

    // Debug tag, for logging
    static final String TAG = "SexCube";

    // Does the user have the premium upgrade?
    Boolean mIsPremium = false;


    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
    static final String SKU_PREMIUM = "premium_update";


    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;


    // The helper object
    IabHelper mHelper;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;

    private static final char[] symbols = new char[36];

    private boolean isServiceReady = false;

    static {
        for (int idx = 0; idx < 10; ++idx)
            symbols[idx] = (char) ('0' + idx);
        for (int idx = 10; idx < 36; ++idx)
            symbols[idx] = (char) ('a' + idx - 10);
    }

    Button offerwall;
    private boolean isTapjoyConnected = false;
    private boolean isOfferWallEnabled = false;

    private AdView mAdView;
    private AdView mAdView2;
    private AdView mAdView3;
    private RelativeLayout adViewContainer;

    private void updateAdViews() {
        if (AppPreferences.areAdsRemoved(this)) {
            adViewContainer.setVisibility(View.GONE);

            mAdView2.setVisibility(View.INVISIBLE);
            final ScrollView scrollSetting = (ScrollView) findViewById(R.id.scrollViewSettings);
            ViewGroup.MarginLayoutParams paramsSetttings = (ViewGroup.MarginLayoutParams) scrollSetting.getLayoutParams();
            paramsSetttings.height = (int) (hetgth - width * 0.32f);
            scrollSetting.setLayoutParams(paramsSetttings);

            mAdView3.setVisibility(View.INVISIBLE);
            frame_levels = (FrameLayout) findViewById(R.id.frame_levels2);
            ViewGroup.MarginLayoutParams paramsView = (ViewGroup.MarginLayoutParams) frame_levels.getLayoutParams();
            paramsView.setMargins(0, (int) ((hetgth - (width * 1.45f)) / 2), 0, 0);
            paramsView.height = (int) (width * 1f);
//            paramsView.height = (int) (width * 1f - width * 0.14f);
            frame_levels.setLayoutParams(paramsView);
        } else {
        }
    }

    private void enableOfferWallButton() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                isOfferWallEnabled = true;
                offerwall.setEnabled(isOfferWallEnabled);
            }
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.rule);

        contextApp = getApplicationContext();

        AdSettings.addTestDevice("65a2de33f08d4933ec5338ed8996a496");

        adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);
        mAdView = new AdView(this, "373834849620122_373834962953444", AdSize.BANNER_320_50);
        adViewContainer.addView(mAdView);
        mAdView.loadAd();

        RelativeLayout adViewContainer2 = (RelativeLayout) findViewById(R.id.adViewContainer2);
        mAdView2 = new AdView(this, "373834849620122_373834962953444", AdSize.BANNER_320_50);
        adViewContainer2.addView(mAdView2);
        mAdView2.loadAd();

        RelativeLayout adViewContainer3 = (RelativeLayout) findViewById(R.id.adViewContainer3);
        mAdView3 = new AdView(this, "373834849620122_373834962953444", AdSize.BANNER_320_50);
        adViewContainer3.addView(mAdView3);
        mAdView3.loadAd();
        // load game data

        //loadData();

	        /* base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY
             * (that you got from the Google Play developer console). This is not your
	         * developer public key, it's the *app-specific* public key.
	         *
	         * Instead of just storing the entire literal string here embedded in the
	         * program,  construct the key at runtime from pieces or
	         * use bit manipulation (for example, XOR with some other string) to hide
	         * the actual key.  The key itself is not secret information, but we don't
	         * want to make it easy for an attacker to replace the public key with one
	         * of their own and then fake messages from the server.
	         */
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzWomihSRNyH2TSOQZA6QaApHOYrNVkIpYfE1QVGkrOuB2OwXTiOhn6DTw9OWME9sULztiymNaNpWNLl68X4H1C7iqcNHAx8cX2IMGmVSrHUiVjSaJxhl7zMfRDtxj/1O5lClYJVlEoasYVj+eG2x1UKw1WYdYQ9lnbQwnaophnuZAHTIwJK08RSgW21vuggZzvsCla9ICSJKwWWDeamGRWXvSNIQe6VxxK1geqv51686ldev+1MeJMl3MUkRIXmHEOL1fnkGo/8p6PiesQqlQ5W9Nd/zKJrwdBu1j8G5zIRYG/orlU35a0drk+LJ4+1EPxAtC9P0bB6KKJ4nYRMZtwIDAQAB";

        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)

        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        //Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(false);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        //Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                //Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    isServiceReady = false;
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                isServiceReady = true;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(sexrule.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                //Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });


//NEW My Code
        display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();  // deprecated
        hetgth = display.getHeight();

        interpol = new AccelerateDecelerateInterpolator();
        inter = new AnticipateOvershootInterpolator();
        inter2 = new OvershootInterpolator();

        parentLay = (LinearLayout) findViewById(R.id.parentLay);
        laySecret = (LinearLayout) findViewById(R.id.layoutSecret);
        laySettings = (LinearLayout) findViewById(R.id.layoutSettings);

        framePoza = (FrameLayout) findViewById(R.id.framePoza);

        textRules = (TextView) findViewById(R.id.textRules);
        btnCube = (Button) findViewById(R.id.butCube);

        LinearLayout actionBar = (LinearLayout) findViewById(R.id.actionBar1);
        imgSexCube = (ImageView) findViewById(R.id.imgSex);
        imgSecret = (ImageButton) findViewById(R.id.imgSecret);
        imgSettings = (ImageButton) findViewById(R.id.imgSettings);

        tableRow = (TableRow) findViewById(R.id.tableRow1);
        tableLay = (TableLayout) findViewById(R.id.tableLayout1);

        offerwall = (Button) findViewById(R.id.butRemoveAds);
        offerwall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c = new Intent(sexrule.this, Offerwall.class);
                startActivity(c);
            }
        });
        offerwall.setEnabled(false);


        ViewGroup.MarginLayoutParams paramsFrame = (ViewGroup.MarginLayoutParams) framePoza.getLayoutParams();
        paramsFrame.setMargins(0, (int) ((hetgth - (width * 1.45f)) / 2), 0, 0);
        paramsFrame.height = (int) (width * 0.3f);
        framePoza.setLayoutParams(paramsFrame);

        imgPoza1 = (ImageButton) findViewById(R.id.imgPoza1);
        imgPoza2 = (ImageButton) findViewById(R.id.imgPoza2);
        imgPoza3 = (ImageButton) findViewById(R.id.imgPoza3);


        imgOkBut = (ImageButton) findViewById(R.id.imgOkBut);

        ViewGroup.MarginLayoutParams paramsPoza = (ViewGroup.MarginLayoutParams) imgPoza1.getLayoutParams();
        paramsPoza.setMargins((int) (width * 0.05f), (int) (width * 0.05f), 0, 0);
        paramsPoza.height = (int) (width / 5);
        paramsPoza.width = (int) (width / 5);
        imgPoza1.setLayoutParams(paramsPoza);

        paramsPoza = (ViewGroup.MarginLayoutParams) imgPoza2.getLayoutParams();
        paramsPoza.setMargins((int) (width * 0.3f), (int) (width * 0.05f), 0, 0);
        paramsPoza.height = (int) (width / 5);
        paramsPoza.width = (int) (width / 5);
        imgPoza2.setLayoutParams(paramsPoza);

        paramsPoza = (ViewGroup.MarginLayoutParams) imgPoza3.getLayoutParams();
        paramsPoza.setMargins((int) (width * 0.55f), (int) (width * 0.05f), 0, 0);
        paramsPoza.height = (int) (width / 5);
        paramsPoza.width = (int) (width / 5);
        imgPoza3.setLayoutParams(paramsPoza);

        paramsPoza = (ViewGroup.MarginLayoutParams) imgOkBut.getLayoutParams();
        paramsPoza.setMargins((int) (width * 0.8f), (int) (width * 0.075f), 0, 0);
        paramsPoza.height = (int) (width * 0.15f);
        paramsPoza.width = (int) (width * 0.15f);
        imgOkBut.setLayoutParams(paramsPoza);

        imgPoza1.setBackgroundResource(R.drawable.poza1w);
        imgPoza2.setBackgroundResource(R.drawable.poza2r);
        imgPoza3.setBackgroundResource(R.drawable.poza3r);
        imgPoza1.setTag(0);
        imgPoza2.setTag(1);
        imgPoza3.setTag(1);

        ViewGroup.MarginLayoutParams paramsPar = (ViewGroup.MarginLayoutParams) parentLay.getLayoutParams();
        paramsPar.setMargins(-(int) (width), (int) (width * 0.16f), 0, 0);
        //paramsPar.height = (int)(tableRow.getLayoutParams().height);
        paramsPar.width = (int) (width * 3);
        parentLay.setLayoutParams(paramsPar);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) actionBar.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        params.height = (int) (width * 0.15f);
        //params.width = (int)(width*0.6f);
        actionBar.setLayoutParams(params);

        params = (ViewGroup.MarginLayoutParams) imgSexCube.getLayoutParams();
        params.setMargins((int) (width * 0.05f), 0, 0, 0);
        params.height = (int) (width * 0.15f);
        params.width = (int) (width * 0.6f);
        imgSexCube.setLayoutParams(params);

        params = (ViewGroup.MarginLayoutParams) imgSecret.getLayoutParams();
        params.setMargins(-(int) (width * 0.85f), 0, 0, 0);
        params.height = (int) (width * 0.15f);
        params.width = (int) (width);
        imgSecret.setLayoutParams(params);

        params = (ViewGroup.MarginLayoutParams) imgSettings.getLayoutParams();
        params.setMargins((int) (width * 0.015f), 0, 0, 0);
        params.height = (int) (width * 0.15f);
        params.width = (int) (width);
        imgSettings.setLayoutParams(params);


        ViewGroup.MarginLayoutParams params1 = (ViewGroup.MarginLayoutParams) laySecret.getLayoutParams();
        params1.setMargins(0, 0, 0, 0);
        params1.height = (int) (tableRow.getLayoutParams().height);
        params1.width = (int) (width);
        laySecret.setLayoutParams(params1);

        params = (ViewGroup.MarginLayoutParams) tableLay.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        //params.height = (int)(width*0.65f-btn.getLayoutParams().height*2);
        params.width = (int) (width);
        tableLay.setLayoutParams(params);
        tableLay.getParent().requestLayout();

        ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) laySettings.getLayoutParams();
        params2.setMargins(0, 0, 0, 0);
        params2.height = (int) (tableRow.getLayoutParams().height);
        params2.width = (int) (width);
        laySettings.setLayoutParams(params2);


//TODO frame buy
        int heigthfrag = (int) ((hetgth - width * 0.9f) / 1.5f);

        framebuy = (FrameLayout) findViewById(R.id.framebuy);
        ViewGroup.MarginLayoutParams paramsbuy = (ViewGroup.MarginLayoutParams) framebuy.getLayoutParams();
        paramsbuy.setMargins((int) (width * 0.1f), heigthfrag, 0, 0);
        paramsbuy.height = (int) (width * 0.87f);
        paramsbuy.width = (int) (width * 0.83f);
        framebuy.setLayoutParams(paramsbuy);

        final ImageButton closeBut = (ImageButton) findViewById(R.id.closebuys);
        paramsbuy = (ViewGroup.MarginLayoutParams) closeBut.getLayoutParams();
        paramsbuy.setMargins((int) (width * 0.705f), 0, 0, 0);
        paramsbuy.height = (int) (width * 0.12f);
        paramsbuy.width = (int) (width * 0.12f);
        ;
        closeBut.setLayoutParams(paramsbuy);

        framebuy.setVisibility(View.GONE);
        framebuy.animate().translationX(width).setStartDelay(0).setDuration(50).setInterpolator(interpol);
        framebuy.animate().alpha(0).setStartDelay(0).setDuration(50).setInterpolator(interpol);

        buyText = (ScrollView) findViewById(R.id.scrollViewBuy);
        paramsbuy = (ViewGroup.MarginLayoutParams) buyText.getLayoutParams();
        paramsbuy.setMargins(0, (int) (width * 0.05f), 0, 0);
        paramsbuy.height = (int) (width * 0.6f);
        //paramsbuy.width = (int)(width*0.8f);
        buyText.setLayoutParams(paramsbuy);

        int butwigth = (int) (width * 0.7f);

        ImageButton buyButton = (ImageButton) findViewById(R.id.butBuy);
        paramsbuy = (ViewGroup.MarginLayoutParams) buyButton.getLayoutParams();
        paramsbuy.setMargins((int) (width * 0.067f), (int) (width * 0.675f), 0, 0);
        paramsbuy.height = (int) (width * 0.15f);
        paramsbuy.width = butwigth;
        buyButton.setLayoutParams(paramsbuy);


//TODO frame pass
        int heigthfrag2 = (int) ((hetgth - width * 1.2f) / 2f);

        framepass = (FrameLayout) findViewById(R.id.framepass);
        ViewGroup.MarginLayoutParams paramspass = (ViewGroup.MarginLayoutParams) framepass.getLayoutParams();
        paramspass.setMargins((int) (width * 0.1f), heigthfrag2, 0, 0);
        paramspass.height = (int) (width * 1.225f);
        paramspass.width = (int) (width * 0.8f);
        framepass.setLayoutParams(paramspass);

        framepass.setVisibility(View.GONE);
        framepass.animate().translationX(width).setStartDelay(0).setDuration(50).setInterpolator(interpol);
        framepass.animate().alpha(0).setStartDelay(0).setDuration(50).setInterpolator(interpol);

        ImageView imgPass = (ImageView) findViewById(R.id.imgPass);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass.getLayoutParams();
        paramspass.setMargins(0, (int) (width * 0.01f), 0, 0);
        paramspass.height = (int) (width * 0.16f);
        paramspass.width = (int) (width * 0.79f);
        imgPass.setLayoutParams(paramspass);


        imgCifra1 = (ImageView) findViewById(R.id.imgCifa1);
        paramspass = (ViewGroup.MarginLayoutParams) imgCifra1.getLayoutParams();
        paramspass.setMargins((int) (width * 0.095f), (int) (width * 0.19f), 0, 0);
        paramspass.height = (int) (width * 0.13f);
        paramspass.width = (int) (width * 0.13f);
        imgCifra1.setLayoutParams(paramspass);
        imgCifra1.setTag(10);

        imgCifra2 = (ImageView) findViewById(R.id.imgCifa2);
        paramspass = (ViewGroup.MarginLayoutParams) imgCifra2.getLayoutParams();
        paramspass.setMargins((int) (width * 0.255f), (int) (width * 0.19f), 0, 0);
        paramspass.height = (int) (width * 0.13f);
        paramspass.width = (int) (width * 0.13f);
        imgCifra2.setLayoutParams(paramspass);
        imgCifra2.setTag(10);

        imgCifra3 = (ImageView) findViewById(R.id.imgCifa3);
        paramspass = (ViewGroup.MarginLayoutParams) imgCifra3.getLayoutParams();
        paramspass.setMargins((int) (width * 0.415f), (int) (width * 0.19f), 0, 0);
        paramspass.height = (int) (width * 0.13f);
        paramspass.width = (int) (width * 0.13f);
        imgCifra3.setLayoutParams(paramspass);
        imgCifra3.setTag(10);

        imgCifra4 = (ImageView) findViewById(R.id.imgCifa4);
        paramspass = (ViewGroup.MarginLayoutParams) imgCifra4.getLayoutParams();
        paramspass.setMargins((int) (width * 0.575f), (int) (width * 0.19f), 0, 0);
        paramspass.height = (int) (width * 0.13f);
        paramspass.width = (int) (width * 0.13f);
        imgCifra4.setLayoutParams(paramspass);
        imgCifra4.setTag(10);


        imgPass1 = (ImageButton) findViewById(R.id.imgpass1);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass1.getLayoutParams();
        paramspass.setMargins((int) (width * 0.07f), (int) (width * 0.35f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass1.setLayoutParams(paramspass);

        imgPass2 = (ImageButton) findViewById(R.id.imgpass2);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass2.getLayoutParams();
        paramspass.setMargins((int) (width * 0.295f), (int) (width * 0.35f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass2.setLayoutParams(paramspass);

        imgPass3 = (ImageButton) findViewById(R.id.imgpass3);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass3.getLayoutParams();
        paramspass.setMargins((int) (width * 0.52f), (int) (width * 0.35f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass3.setLayoutParams(paramspass);

        imgPass4 = (ImageButton) findViewById(R.id.imgpass4);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass4.getLayoutParams();
        paramspass.setMargins((int) (width * 0.07f), (int) (width * 0.555f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass4.setLayoutParams(paramspass);

        imgPass5 = (ImageButton) findViewById(R.id.imgpass5);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass5.getLayoutParams();
        paramspass.setMargins((int) (width * 0.295f), (int) (width * 0.555f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass5.setLayoutParams(paramspass);

        imgPass6 = (ImageButton) findViewById(R.id.imgpass6);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass6.getLayoutParams();
        paramspass.setMargins((int) (width * 0.52f), (int) (width * 0.555f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass6.setLayoutParams(paramspass);

        imgPass7 = (ImageButton) findViewById(R.id.imgpass7);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass7.getLayoutParams();
        paramspass.setMargins((int) (width * 0.07f), (int) (width * 0.760f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass7.setLayoutParams(paramspass);

        imgPass8 = (ImageButton) findViewById(R.id.imgpass8);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass8.getLayoutParams();
        paramspass.setMargins((int) (width * 0.295f), (int) (width * 0.760f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass8.setLayoutParams(paramspass);

        imgPass9 = (ImageButton) findViewById(R.id.imgpass9);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass9.getLayoutParams();
        paramspass.setMargins((int) (width * 0.52f), (int) (width * 0.760f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass9.setLayoutParams(paramspass);

        imgPassExit = (ImageButton) findViewById(R.id.imgpassExit);
        paramspass = (ViewGroup.MarginLayoutParams) imgPassExit.getLayoutParams();
        paramspass.setMargins((int) (width * 0.07f), (int) (width * 0.965f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPassExit.setLayoutParams(paramspass);

        imgPass0 = (ImageButton) findViewById(R.id.imgpass0);
        paramspass = (ViewGroup.MarginLayoutParams) imgPass0.getLayoutParams();
        paramspass.setMargins((int) (width * 0.295f), (int) (width * 0.965f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPass0.setLayoutParams(paramspass);

        imgPassClear = (ImageButton) findViewById(R.id.imgpassClear);
        paramspass = (ViewGroup.MarginLayoutParams) imgPassClear.getLayoutParams();
        paramspass.setMargins((int) (width * 0.52f), (int) (width * 0.965f), 0, 0);
        paramspass.height = (int) (width * 0.19f);
        paramspass.width = (int) (width * 0.21f);
        imgPassClear.setLayoutParams(paramspass);

//TODO Settings menu
        butPassword = (Button) findViewById(R.id.butSetPass);
        ViewGroup.MarginLayoutParams paramsSetttings = (ViewGroup.MarginLayoutParams) butPassword.getLayoutParams();
        paramsSetttings.setMargins(0, (int) (width * 0.02f), 0, 0);
        paramsSetttings.height = (int) (width * 0.135f);
        paramsSetttings.width = (int) (width * 0.75f);
        butPassword.setLayoutParams(paramsSetttings);

        final ScrollView scrollSetting = (ScrollView) findViewById(R.id.scrollViewSettings);
        paramsSetttings = (ViewGroup.MarginLayoutParams) scrollSetting.getLayoutParams();
//        paramsSetttings.height = (int) (hetgth - width * 0.32f);
        paramsSetttings.height = (int) (hetgth - width * 0.44f);
        scrollSetting.setLayoutParams(paramsSetttings);

        //
        final TextView textPassApp = (TextView) findViewById(R.id.textPassApp);
        paramsSetttings = (ViewGroup.MarginLayoutParams) textPassApp.getLayoutParams();
        paramsSetttings.setMargins((int) (width * 0.01f), 0, 0, 0);

        paramsSetttings.width = (int) (width * 0.8f);
        textPassApp.setLayoutParams(paramsSetttings);

        checkPassApp = (CheckBox) findViewById(R.id.checkBoxPassApp);
        paramsSetttings = (ViewGroup.MarginLayoutParams) checkPassApp.getLayoutParams();
        paramsSetttings.setMargins((int) (width * 0.02f), 0, 0, 0);
        checkPassApp.setLayoutParams(paramsSetttings);
        //

        TextView textPassSecret = (TextView) findViewById(R.id.textPassSecret);
        paramsSetttings = (ViewGroup.MarginLayoutParams) textPassSecret.getLayoutParams();
        paramsSetttings.setMargins((int) (width * 0.01f), 0, 0, 0);
        paramsSetttings.width = (int) (width * 0.8f);
        textPassSecret.setLayoutParams(paramsSetttings);

        checkPassSecret = (CheckBox) findViewById(R.id.checkBoxPassSecret);
        paramsSetttings = (ViewGroup.MarginLayoutParams) checkPassSecret.getLayoutParams();
        paramsSetttings.setMargins((int) (width * 0.02f), 0, 0, 0);
        checkPassSecret.setLayoutParams(paramsSetttings);
        //

        TextView textIconChange = (TextView) findViewById(R.id.textIconChange);
        paramsSetttings = (ViewGroup.MarginLayoutParams) textIconChange.getLayoutParams();
        paramsSetttings.setMargins((int) (width * 0.01f), 0, 0, 0);
        paramsSetttings.width = (int) (width * 0.8f);
        textIconChange.setLayoutParams(paramsSetttings);

        checkIconChange = (CheckBox) findViewById(R.id.checkBoxIconChange);
        paramsSetttings = (ViewGroup.MarginLayoutParams) checkIconChange.getLayoutParams();
        paramsSetttings.setMargins((int) (width * 0.02f), 0, 0, 0);
        checkIconChange.setLayoutParams(paramsSetttings);
        //

        TextView textIconName = (TextView) findViewById(R.id.textIconName);
        paramsSetttings = (ViewGroup.MarginLayoutParams) textIconName.getLayoutParams();
        paramsSetttings.setMargins((int) (width * 0.01f), 0, 0, 0);
        paramsSetttings.width = (int) (width * 0.8f);
        textIconName.setLayoutParams(paramsSetttings);

        checkIconName = (CheckBox) findViewById(R.id.checkBoxIconName);
        paramsSetttings = (ViewGroup.MarginLayoutParams) checkIconName.getLayoutParams();
        paramsSetttings.setMargins((int) (width * 0.02f), 0, 0, 0);
        checkIconName.setLayoutParams(paramsSetttings);

        ImageView imgChangeIcon = (ImageView) findViewById(R.id.imgChangeIcon);
        paramsSetttings = (ViewGroup.MarginLayoutParams) imgChangeIcon.getLayoutParams();
        paramsSetttings.setMargins(0, 0, 0, 0);
        paramsSetttings.height = (int) (width * 0.28f);
        paramsSetttings.width = (int) (width * 0.65f);
        imgChangeIcon.setLayoutParams(paramsSetttings);
        //

        TextView textRotateBut = (TextView) findViewById(R.id.textRotateBut);
        paramsSetttings = (ViewGroup.MarginLayoutParams) textRotateBut.getLayoutParams();
        paramsSetttings.setMargins((int) (width * 0.01f), 0, 0, 0);
        paramsSetttings.width = (int) (width * 0.8f);
        textRotateBut.setLayoutParams(paramsSetttings);

        checkRotateBut = (CheckBox) findViewById(R.id.checkBoxRotateBut);
        paramsSetttings = (ViewGroup.MarginLayoutParams) checkRotateBut.getLayoutParams();
        paramsSetttings.setMargins((int) (width * 0.02f), 0, 0, 0);
        checkRotateBut.setLayoutParams(paramsSetttings);

        //


        TextView textviewRules = (TextView) findViewById(R.id.textViewRules);
        paramsSetttings = (ViewGroup.MarginLayoutParams) textviewRules.getLayoutParams();
        paramsSetttings.setMargins((int) (width * 0.01f), 0, 0, 0);
        paramsSetttings.width = (int) (width * 0.8f);
        textviewRules.setLayoutParams(paramsSetttings);

        checkRules = (CheckBox) findViewById(R.id.checkBoxRules);
        paramsSetttings = (ViewGroup.MarginLayoutParams) checkRules.getLayoutParams();
        paramsSetttings.setMargins((int) (width * 0.02f), 0, 0, 0);
        checkRules.setLayoutParams(paramsSetttings);


        edittextRules = (EditText) findViewById(R.id.edittextRules);
        paramsSetttings = (ViewGroup.MarginLayoutParams) edittextRules.getLayoutParams();
        paramsSetttings.setMargins(0, 0, 0, 0);
        paramsSetttings.height = (int) (width * 0.4f);
        edittextRules.setLayoutParams(paramsSetttings);

        edittextRules.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edittextRules) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });


        //
        TextView textInviz = (TextView) findViewById(R.id.textInviz);
        paramsSetttings = (ViewGroup.MarginLayoutParams) textInviz.getLayoutParams();
        paramsSetttings.setMargins((int) (width * 0.01f), 0, 0, 0);
        paramsSetttings.width = (int) (width * 0.8f);
        textInviz.setLayoutParams(paramsSetttings);

        checkInviz = (CheckBox) findViewById(R.id.checkBoxInviz);
        paramsSetttings = (ViewGroup.MarginLayoutParams) checkInviz.getLayoutParams();
        paramsSetttings.setMargins((int) (width * 0.02f), 0, 0, 0);
        checkInviz.setLayoutParams(paramsSetttings);

        ImageView imgInviz = (ImageView) findViewById(R.id.imgInviz);
        paramsSetttings = (ViewGroup.MarginLayoutParams) imgInviz.getLayoutParams();
        paramsSetttings.setMargins(0, 0, 0, 0);
        paramsSetttings.height = (int) (width * 0.33f);
        paramsSetttings.width = (int) (width * 0.85f);
        imgInviz.setLayoutParams(paramsSetttings);


        //

        final LinearLayout laySale = (LinearLayout) findViewById(R.id.linearSale);
        paramsSetttings = (ViewGroup.MarginLayoutParams) laySale.getLayoutParams();
        paramsSetttings.setMargins(0, 0, 0, 0);
        paramsSetttings.height = (int) (hetgth);
        paramsSetttings.width = (int) (width);
        laySale.setLayoutParams(paramsSetttings);
        laySale.setVisibility(View.INVISIBLE);

        ImageView imgSale = (ImageView) findViewById(R.id.imgsale);
        paramsSetttings = (ViewGroup.MarginLayoutParams) imgSale.getLayoutParams();
        paramsSetttings.setMargins(0, 0, 0, 0);
        paramsSetttings.height = (int) (width * 1.178f);
        paramsSetttings.width = (int) (width);
        imgSale.setLayoutParams(paramsSetttings);

        sPref = getSharedPreferences("pozes", MODE_PRIVATE);
        Editor ed = sPref.edit();

        Integer reklama = sPref.getInt("reklama", 0);
        int okSale = sPref.getInt("saleview", 0);

        String langs = getResources().getConfiguration().locale.getLanguage();
        if (langs.contains("ru")) {
            imgSale.setBackgroundResource(R.drawable.reklamarus);
        } else if (langs.contains("uk")) {
            imgSale.setBackgroundResource(R.drawable.reklamarus);
        } else {
            imgSale.setBackgroundResource(R.drawable.reklama);
        }

        if ((reklama != 1) & (okSale == 1)) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    laySale.animate().alpha(0).setStartDelay(0).setDuration(400).setInterpolator(interpol);
                }
            }, 12550);
        } else {
            laySale.setVisibility(View.INVISIBLE);
            laySale.setEnabled(false);
            ed.putInt("saleview", 1).commit();
        }


        ImageView imgSalefon = (ImageView) findViewById(R.id.imgsalepolosa);
        paramsSetttings = (ViewGroup.MarginLayoutParams) imgSalefon.getLayoutParams();
        paramsSetttings.setMargins(0, 0, 0, 0);
        paramsSetttings.height = (int) (hetgth - width * 1.178f);
        paramsSetttings.width = (int) (width);
        imgSalefon.setLayoutParams(paramsSetttings);

        Button butSaveSettings = (Button) findViewById(R.id.butSaveSettings);
        paramsSetttings = (ViewGroup.MarginLayoutParams) butSaveSettings.getLayoutParams();
        paramsSetttings.setMargins(0, (int) (width * 0.015f), 0, (int) (width * 0.01f));
        paramsSetttings.height = (int) (width * 0.135f);
        paramsSetttings.width = (int) (width * 0.975f);
        butSaveSettings.setLayoutParams(paramsSetttings);

//TODO some changes


        String strPref1 = sPref.getString("poza1", "");
        String strPref2 = sPref.getString("poza2", "");
        String strPref3 = sPref.getString("poza3", "");
        if ((strPref1 == "") | (strPref2 == "") | (strPref3 == "") | (strPref3 == "null") | (strPref2 == "null") | (strPref2 == "null")) {
            ed.putString("poza1", "q");
            ed.putString("poza2", "q");
            ed.putString("poza3", "q");
            ed.commit();
        }

        String iconName = sPref.getString("iconapp", "");

        if (iconName.contains("cubename")) {
            checkIconChange.setChecked(false);
            checkIconName.setChecked(true);
        } else if (iconName.contains("cubeicon")) {
            checkIconChange.setChecked(true);
            checkIconName.setChecked(false);
        } else if (iconName.contains("sexcube")) {
            checkIconChange.setChecked(false);
            checkIconName.setChecked(false);
        } else if (iconName.contains("cube")) {
            checkIconChange.setChecked(true);
            checkIconName.setChecked(true);
        }


        int butsVizible = sPref.getInt("butsVizible", 0);

        if (butsVizible == 1) {
            imgSecret.setBackgroundResource(R.drawable.but_secret2);
            imgSettings.setBackgroundResource(R.drawable.but_settings2);
            checkInviz.setChecked(true);
        } else {
            imgSecret.setBackgroundResource(R.drawable.but_secret);
            imgSettings.setBackgroundResource(R.drawable.but_settings);
            checkInviz.setChecked(false);
        }

        int changeRules = sPref.getInt("changeRule", 0);
        String textsRules = sPref.getString("textRule", "");

        if (changeRules == 1) {
            textRules.setText(textsRules);
            edittextRules.setText(textsRules);
            checkRules.setChecked(true);
        } else {
            textRules.setText(getResources().getText(R.string.using));
            edittextRules.setText(getResources().getText(R.string.using));
            checkRules.setChecked(false);
        }

        int passApp = sPref.getInt("passApp", 0);
        int passButs = sPref.getInt("passButs", 0);

        int butRotate = sPref.getInt("butRotate", 0);

        if (butRotate == 1) {
            checkRotateBut.setChecked(true);
        } else {
            checkRotateBut.setChecked(false);
        }

        if (passApp == 1) {
            checkPassApp.setChecked(true);
        } else {
            checkPassApp.setChecked(false);
        }

        if (passButs == 1) {
            checkPassSecret.setChecked(true);
        } else {
            checkPassSecret.setChecked(false);
        }

        Button btnRateUs = (Button) findViewById(R.id.butRate);
        Integer IntPref = sPref.getInt("rateOk", 0);
        if (IntPref == 0) {
            LinearLayout.LayoutParams params_1 = (LinearLayout.LayoutParams) btnRateUs.getLayoutParams();
            params_1.weight = 1.0f;
            btnRateUs.setLayoutParams(params_1);
            params_1 = (LinearLayout.LayoutParams) btnCube.getLayoutParams();
            params_1.weight = 1.0f;
            btnCube.setLayoutParams(params_1);
            btnRateUs.setText(getResources().getText(R.string.rateus));
        } else {
            LinearLayout.LayoutParams params_1 = (LinearLayout.LayoutParams) btnRateUs.getLayoutParams();
            params_1.weight = 3.0f;
            btnRateUs.setLayoutParams(params_1);
            params_1 = (LinearLayout.LayoutParams) btnCube.getLayoutParams();
            params_1.weight = 2.0f;
            btnCube.setLayoutParams(params_1);
            btnRateUs.setText(getResources().getText(R.string.share));
        }

        //������ ������ ���������

        mViewPager = (CarouselViewPager) findViewById(R.id.carousel_pager);


        //frame_levels - ����� � ������� ���������� ������ - ��������� ����������� ��� ������� ���
        frame_levels = (FrameLayout) findViewById(R.id.frame_levels2);
        ViewGroup.MarginLayoutParams paramsView = (ViewGroup.MarginLayoutParams) frame_levels.getLayoutParams();
        paramsView.setMargins(0, (int) ((hetgth - (width * 1.45f)) / 2), 0, 0);
//        paramsView.height = (int) (width * 1f);
        paramsView.height = (int) (width * 1f - width * 0.14f);
        frame_levels.setLayoutParams(paramsView);
        //������ ������ � ������ ����� ������
        myPageFragment = new MyPageFragment();
        myPageFragment.setPxHeightWeight((int) (width * 0.95f), (int) (width * 0.8f));

        CarouselViewPager.heigth_frag = (int) (width * 1f);
        CarouselViewPager.width_frag = (int) (width * 0.8f);

        Drawable image;
        if (instanceState == null) {
            int size = 43;
            mItems = new ArrayList<MyPageItem>(size);
            for (int i = 0; i < size; i++) {
                //image = (Drawable) getResources().getDrawable(R.drawable.k12);
                mItems.add(new MyPageItem("Item " + i, null, i));
            }
        } else {
            mItems = instanceState.getParcelableArrayList("items");
        }

        mPagerAdapter = new CarouselPagerAdapter<MyPageItem>(getSupportFragmentManager(),
                MyPageFragment.class, R.layout.page_layout, mItems, this);

        mPagerAdapter.setOnPageClickListener(this);

        mViewPager.setAdapter(mPagerAdapter);


        mViewPager.setCurrentItem(0);

        imgSecret.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (butClick == true) {
                    sPref = getSharedPreferences("pozes", MODE_PRIVATE);
                    Editor ed = sPref.edit();
                    int passOk = sPref.getInt("passOk", 0);
                    int passOnButs = sPref.getInt("passButs", 0);
                    Integer reklama = sPref.getInt("reklama", 0);
                    if (reklama != 1) {
                        laySale.animate().alpha(0).setStartDelay(0).setDuration(200).setInterpolator(interpol);
                        ed.putInt("reklama", 1);
                        ed.commit();
                    }
                    ed.putString("passDo", "passsecret");
                    ed.commit();
                    if ((passOk == 1) && (passOnButs == 1)) {
                        framepass.setVisibility(View.VISIBLE);
                        framepass.animate().translationX(0).alpha(1).setStartDelay(0).setDuration(300).setInterpolator(interpol);

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), getResources().getText(R.string.enterpass), Toast.LENGTH_SHORT).show();
                            }
                        }, 550);
                    } else {
                        imgPoza1.setBackgroundResource(R.drawable.poza1w);
                        imgPoza2.setBackgroundResource(R.drawable.poza2r);
                        imgPoza3.setBackgroundResource(R.drawable.poza3r);
                        imgPoza1.setTag(0);
                        imgPoza2.setTag(1);
                        imgPoza3.setTag(1);
                        imgSecret.animate().translationX((int) (width * 0.85f)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        imgSettings.animate().translationX((int) (width * 0.85f)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        imgSexCube.animate().translationX((int) (width * 0.85f)).setStartDelay(0).setDuration(450).setInterpolator(interpol);

                        laySecret.animate().translationX((int) (width)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        tableLay.animate().translationX((int) (width)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        imgOkBut.animate().scaleX(0f).scaleY(0f).setStartDelay(0).setDuration(100).setInterpolator(interpol);
                        butClick = false;
                    }
                } else {

                    imgSecret.animate().translationX(0).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                    imgSettings.animate().translationX(0).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                    imgSexCube.animate().translationX(0).setStartDelay(0).setDuration(450).setInterpolator(interpol);

                    laySecret.animate().translationX(0).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                    tableLay.animate().translationX(0).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                    butClick = true;

                }
            }
        });

        imgSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //TODO Settigs mode
                if (butClick == true) {
                    sPref = getSharedPreferences("pozes", MODE_PRIVATE);
                    Editor ed = sPref.edit();
                    int passOk = sPref.getInt("passOk", 0);
                    int passOnButs = sPref.getInt("passButs", 0);
                    ed.putString("passDo", "passsettings");
                    ed.commit();
                    if ((passOk == 1) && (passOnButs == 1)) {
                        framepass.setVisibility(View.VISIBLE);
                        framepass.animate().translationX(0).alpha(1).setStartDelay(0).setDuration(300).setInterpolator(interpol);

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), getResources().getText(R.string.enterpass), Toast.LENGTH_SHORT).show();
                            }
                        }, 550);
                    } else {
                        imgSecret.animate().translationX(-(int) (width * 0.85f)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        imgSettings.animate().translationX(-(int) (width * 0.815f)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        imgSexCube.animate().translationX(-(int) (width * 0.85f)).setStartDelay(0).setDuration(450).setInterpolator(interpol);

                        laySettings.animate().translationX(-(int) (width)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        tableLay.animate().translationX(-(int) (width)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        butClick = false;
                    }

                } else {
                    imgSecret.animate().translationX(0).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                    imgSettings.animate().translationX(0).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                    imgSexCube.animate().translationX(0).setStartDelay(0).setDuration(450).setInterpolator(interpol);

                    laySettings.animate().translationX(0).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                    tableLay.animate().translationX(0).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                    butClick = true;

                }
            }
        });


        ScrollView scr = (ScrollView) findViewById(R.id.scrollView1);
        scr.setScrollBarStyle(10);
        //scr.setBackgroundColor(50);
        btnCube.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
				/*
				 for (int nomer = 0; nomer <= 42; nomer++) {
    				 sPref.edit().putBoolean("checkP"+nomer, true).commit();
    			 }
    			*/
                Intent intent = new Intent(sexrule.this, cube.class);
                startActivity(intent);
                MyGLRenderer.texture_num0 = 15;
                MyGLRenderer.texture_num1 = 18;
                MyGLRenderer.texture_num2 = 22;
                MyGLRenderer.texture_num3 = 19;
                MyGLRenderer.texture_num4 = 23;
                MyGLRenderer.texture_num5 = 21;
                finish();
                onStop();
            }
        });

        butPassword.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sPref = getSharedPreferences("pozes", MODE_PRIVATE);
                Editor ed = sPref.edit();
                ed.putString("passDo", "setpass");
                ed.commit();
                laySettings.animate().alpha(0).setStartDelay(0).setDuration(300).setInterpolator(interpol);
                for (int i = 0; i < laySettings.getChildCount(); i++) {
                    View child = laySettings.getChildAt(i);
                    child.setEnabled(false);
                }
                edittextRules.setEnabled(false);
                checkIconChange.setEnabled(false);
                checkIconName.setEnabled(false);
                checkInviz.setEnabled(false);
                checkPassApp.setEnabled(false);
                checkPassSecret.setEnabled(false);
                checkRules.setEnabled(false);
                checkRotateBut.setEnabled(false);
                framepass.setVisibility(View.VISIBLE);
                framepass.animate().translationX(0).alpha(1).setStartDelay(0).setDuration(300).setInterpolator(interpol);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.newpass), Toast.LENGTH_SHORT).show();
                    }
                }, 550);

            }
        });

        butSaveSettings.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

// TODO Change icon
                sPref = getSharedPreferences("pozes", MODE_PRIVATE);
                Editor ed = sPref.edit();

                Boolean premiumOk = sPref.getBoolean("allOk", true);
//TODO debugs set in true if
                if ((premiumOk == false) | (mIsPremium == false)) {
                    framebuy.setVisibility(View.VISIBLE);
                    laySettings.animate().alpha(0).setStartDelay(0).setDuration(300).setInterpolator(interpol);
                    for (int i = 0; i < laySettings.getChildCount(); i++) {
                        View child = laySettings.getChildAt(i);
                        child.setEnabled(false);
                    }
                    edittextRules.setEnabled(false);
                    checkIconChange.setEnabled(false);
                    checkIconName.setEnabled(false);
                    checkInviz.setEnabled(false);
                    checkPassApp.setEnabled(false);
                    checkPassSecret.setEnabled(false);
                    checkRules.setEnabled(false);
                    checkRotateBut.setEnabled(false);

                    framebuy.animate().translationX(0).alpha(1).setStartDelay(0).setDuration(300).setInterpolator(interpol);

                } else {
                    Boolean passOks = sPref.getBoolean("passTrue", false);
                    if (((checkPassApp.isChecked()) & (passOks != true)) || ((checkPassSecret.isChecked()) & (passOks != true))) {
                        String langs = getResources().getConfiguration().locale.getLanguage();
                        if (langs.contains("ru")) {
                            Toast.makeText(getApplicationContext(), "���������� ������ ����� ������.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (langs.contains("uk")) {
                            Toast.makeText(getApplicationContext(), "��������� ������ ����� ������.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "You need SET NEW PASSWORD.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if ((checkIconChange.isChecked()) && (checkIconName.isChecked() == false)) {
                            ed.putString("iconapp", "cubeicon");
                            ed.commit();
                        } else if ((checkIconChange.isChecked() == false) && (checkIconName.isChecked())) {
                            ed.putString("iconapp", "cubename");
                            ed.commit();
                        } else if ((checkIconChange.isChecked()) && (checkIconName.isChecked())) {
                            ed.putString("iconapp", "cube");
                            ed.commit();
                        } else if ((checkIconChange.isChecked() == false) && (checkIconName.isChecked() == false)) {
                            ed.putString("iconapp", "sexcube");
                            ed.commit();
                        }

                        String iconName = sPref.getString("iconapp", "");

                        if (iconName == "cube") {
                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-Cube"),
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                    PackageManager.DONT_KILL_APP);

                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-SexCube"),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP);

                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-CubeName"),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP);

                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-CubeIconSex"),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP);
                        } else if (iconName == "sexcube") {
                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-Cube"),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP);

                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-SexCube"),
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                    PackageManager.DONT_KILL_APP);

                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-CubeName"),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP);

                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-CubeIconSex"),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP);
                        } else if (iconName == "cubeicon") {
                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-Cube"),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP);


                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-SexCube"),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP);

                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-CubeName"),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP);

                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-CubeIconSex"),
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                    PackageManager.DONT_KILL_APP);
                        } else if (iconName == "cubename") {
                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-Cube"),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP);


                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-SexCube"),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP);

                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-CubeIconSex"),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP);

                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("com.pashkov.sexcubefull", "com.pashkov.sexcubefull.MainActivity-CubeName"),
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                    PackageManager.DONT_KILL_APP);
                        }

                        if (checkInviz.isChecked()) {
                            imgSecret.setBackgroundResource(R.drawable.but_secret2);
                            imgSettings.setBackgroundResource(R.drawable.but_settings2);
                            ed.putInt("butsVizible", 1);
                            ed.commit();
                        } else {
                            imgSecret.setBackgroundResource(R.drawable.but_secret);
                            imgSettings.setBackgroundResource(R.drawable.but_settings);
                            ed.putInt("butsVizible", 0);
                            ed.commit();
                        }

                        if (checkRules.isChecked()) {
                            textRules.setText(edittextRules.getText().toString());
                            ed.putString("textRule", edittextRules.getText().toString());
                            ed.putInt("changeRule", 1);
                            ed.commit();
                        } else {
                            textRules.setText(getResources().getText(R.string.using));
                            edittextRules.setText(getResources().getText(R.string.using));
                            ed.putInt("changeRule", 0);
                            ed.commit();
                        }


                        if (checkPassApp.isChecked()) {
                            ed.putInt("passApp", 1);
                            ed.commit();
                        } else {
                            ed.putInt("passApp", 0);
                            ed.commit();
                        }

                        if (checkPassSecret.isChecked()) {
                            ed.putInt("passButs", 1);
                            ed.commit();
                        } else {
                            ed.putInt("passButs", 0);
                            ed.commit();
                        }

                        if (checkRotateBut.isChecked()) {
                            ed.putInt("butRotate", 1);
                            ed.commit();
                        } else {
                            ed.putInt("butRotate", 0);
                            ed.commit();
                        }

                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.apliedsettings), Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                imgSecret.animate().translationX(0).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                                imgSettings.animate().translationX(0).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                                imgSexCube.animate().translationX(0).setStartDelay(0).setDuration(450).setInterpolator(interpol);

                                laySettings.animate().translationX(0).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                                tableLay.animate().translationX(0).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                                butClick = true;
                            }
                        }, 700);
                    }
                }
            }
        });

        buyButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        laySettings.animate().alpha(1).setStartDelay(0).setDuration(300).setInterpolator(interpol);
                        for (int i = 0; i < laySettings.getChildCount(); i++) {
                            View child = laySettings.getChildAt(i);
                            child.setEnabled(true);
                        }

                        laySecret.animate().alpha(1).setStartDelay(0).setDuration(300).setInterpolator(interpol);
                        for (int i = 0; i < laySecret.getChildCount(); i++) {
                            View child = laySecret.getChildAt(i);
                            child.setEnabled(true);
                        }

                        frame_levels.setEnabled(true);

                        edittextRules.setEnabled(true);
                        checkIconChange.setEnabled(true);
                        checkIconName.setEnabled(true);
                        checkInviz.setEnabled(true);
                        checkPassApp.setEnabled(true);
                        checkPassSecret.setEnabled(true);
                        checkRules.setEnabled(true);
                        checkRotateBut.setEnabled(true);
                        framebuy.animate().translationX(width * 0.9f).alpha(0).setStartDelay(0).setDuration(300).setInterpolator(interpol);
                    }
                }, 600);


                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        framebuy.setVisibility(View.GONE);
                        onUpgradeAppButtonClicked(findViewById(R.id.butBuy));
                    }
                }, 950);

            }
        });

        closeBut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        closeBut.animate().scaleX(0.85f).scaleY(0.85f).setStartDelay(0).setDuration(200).setInterpolator(inter2);
                    }
                }, 100);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        closeBut.animate().scaleX(1).scaleY(1).setStartDelay(0).setDuration(200).setInterpolator(inter2);
                    }
                }, 300);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        laySettings.animate().alpha(1).setStartDelay(0).setDuration(300).setInterpolator(interpol);
                        for (int i = 0; i < laySettings.getChildCount(); i++) {
                            View child = laySettings.getChildAt(i);
                            child.setEnabled(true);
                        }

                        laySecret.animate().alpha(1).setStartDelay(0).setDuration(300).setInterpolator(interpol);
                        for (int i = 0; i < laySecret.getChildCount(); i++) {
                            View child = laySecret.getChildAt(i);
                            child.setEnabled(true);
                        }

                        frame_levels.setEnabled(true);
                        edittextRules.setEnabled(true);
                        checkIconChange.setEnabled(true);
                        checkIconName.setEnabled(true);
                        checkInviz.setEnabled(true);
                        checkPassApp.setEnabled(true);
                        checkPassSecret.setEnabled(true);
                        checkRules.setEnabled(true);
                        checkRotateBut.setEnabled(true);
                        framebuy.animate().translationX(width * 0.9f).alpha(0).setStartDelay(0).setDuration(300).setInterpolator(interpol);
                    }
                }, 600);


                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        framebuy.setVisibility(View.GONE);
                    }
                }, 950);
            }
        });


        btnRateUs.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (isOnline()) {
                    sPref = getSharedPreferences("pozes", MODE_PRIVATE);
                    Editor ed = sPref.edit();

                    Integer IntPref = sPref.getInt("rateOk", 0);
                    if (IntPref == 0) {
                        ed.putInt("rateOk", 1);
                        ed.commit();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        //Try Google play
                        intent.setData(Uri.parse("market://details?id=com.pashkov.sexcubefull"));
                        if (!MyStartActivity(intent)) {
                            //Market (Google play) app seems not installed, let's try to open a webbrowser
                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?com.pashkov.sexcubefull"));
                            if (!MyStartActivity(intent)) {
                                //Well if this also fails, we have run out of options, inform the user.
                                Toast.makeText(getApplicationContext(), "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (IntPref == 1) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.sharetext));
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.shareapp)));
                    }
                }
            }
        });
		   /*
		    sumWayButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					new Handler().postDelayed(new Runnable() {
			             @Override
			             public void run() {
			            	 framebuy.animate().translationX(width*0.9f).alpha(0).setStartDelay(0).setDuration(300).setInterpolator(interpol);
			             }
			        }, 150);

					 new Handler().postDelayed(new Runnable() {
			             @Override
			             public void run() {
			            	 Intent intent = new Intent(Intent.ACTION_VIEW);
			            	    //Try Google play
			            	    intent.setData(Uri.parse("market://details?id=com.bouncy.bball"));
			            	    if (!MyStartActivity(intent)) {
			            	        //Market (Google play) app seems not installed, let's try to open a webbrowser
			            	        intent.setData(Uri.parse("https://play.google.com/store/apps/details?com.bouncy.bball"));
			            	        if (!MyStartActivity(intent)) {
			            	            //Well if this also fails, we have run out of options, inform the user.
			            	            Toast.makeText(getApplicationContext(), "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
			            	        }
			            	    }

			             }
			        }, 600);
				}
			});

		    */
        imgPoza1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int poza2Img = (Integer) imgPoza2.getTag();
                int poza3Img = (Integer) imgPoza3.getTag();
                if ((poza2Img != 0) && (poza3Img != 0)) {
                    imgPoza1.setBackgroundResource(R.drawable.poza1w);
                    imgPoza1.setTag(0);
                }
            }
        });

        imgPoza2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int poza1Img = (Integer) imgPoza1.getTag();
                int poza3Img = (Integer) imgPoza3.getTag();
                if ((poza1Img != 0) && (poza3Img != 0)) {
                    imgPoza2.setBackgroundResource(R.drawable.poza2w);
                    imgPoza2.setTag(0);
                }
            }
        });
        imgPoza3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int poza2Img = (Integer) imgPoza2.getTag();
                int poza1Img = (Integer) imgPoza1.getTag();
                if ((poza2Img != 0) && (poza1Img != 0)) {
                    imgPoza3.setBackgroundResource(R.drawable.poza3w);
                    imgPoza3.setTag(0);
                }
            }
        });

        imgOkBut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sPref = getSharedPreferences("pozes", MODE_PRIVATE);
                Editor ed = sPref.edit();

                Boolean premiumOk = sPref.getBoolean("allOk", true);
                //TODO debugs set in true if

                if ((premiumOk == false) | (mIsPremium == false)) {
                    framebuy.setVisibility(View.VISIBLE);
                    laySecret.animate().alpha(0).setStartDelay(0).setDuration(300).setInterpolator(interpol);
                    for (int i = 0; i < laySecret.getChildCount(); i++) {
                        View child = laySecret.getChildAt(i);
                        child.setEnabled(false);
                    }
                    frame_levels.setEnabled(false);
	 					/*
	 					edittextRules.setEnabled(false);
	 					checkIconChange.setEnabled(false);
	 					checkIconName.setEnabled(false);
	 					checkInviz.setEnabled(false);
	 					checkPassApp.setEnabled(false);
	 					checkPassSecret.setEnabled(false);
	 					checkRules.setEnabled(false);
	 					*/
                    framebuy.animate().translationX(0).alpha(1).setStartDelay(0).setDuration(300).setInterpolator(interpol);

                } else {

                    imgOkBut.animate().scaleX(0f).scaleY(0f).setStartDelay(0).setDuration(600).setInterpolator(inter2);

                    imgSecret.animate().translationX(0).setStartDelay(600).setDuration(450).setInterpolator(interpol);
                    imgSettings.animate().translationX(0).setStartDelay(600).setDuration(450).setInterpolator(interpol);
                    imgSexCube.animate().translationX(0).setStartDelay(600).setDuration(450).setInterpolator(interpol);

                    laySecret.animate().translationX(0).setStartDelay(600).setDuration(450).setInterpolator(interpol);
                    tableLay.animate().translationX(0).setStartDelay(600).setDuration(450).setInterpolator(interpol);
                    butClick = true;
                }
            }
        });

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
                framepass.animate().translationX(width * 0.9f).alpha(0).setStartDelay(500).setDuration(300).setInterpolator(interpol);
                animButPass(imgPassExit);

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

                laySettings.animate().alpha(1).setStartDelay(450).setDuration(300).setInterpolator(interpol);
                for (int i = 0; i < laySettings.getChildCount(); i++) {
                    View child = laySettings.getChildAt(i);
                    child.setEnabled(true);
                }
                edittextRules.setEnabled(true);
                checkIconChange.setEnabled(true);
                checkIconName.setEnabled(true);
                checkInviz.setEnabled(true);
                checkPassApp.setEnabled(true);
                checkPassSecret.setEnabled(true);
                checkRules.setEnabled(true);
                checkRotateBut.setEnabled(true);

            }
        });

        Hashtable<String, Object> connectFlags = new Hashtable<String, Object>();
        Tapjoy.connect(getApplicationContext(), getResources().getString(R.string.tapjoy_sdk_kKey), connectFlags, new TJConnectListener() {
            @Override
            public void onConnectSuccess() {
                isTapjoyConnected = true;
                enableOfferWallButton();
            }

            @Override
            public void onConnectFailure() {
            }
        });



		/*
		    Button testbut = (Button) findViewById(R.id.buttest);
		    testbut.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					framebuy.setVisibility(View.VISIBLE);
					laySettings.animate().alpha(0).setStartDelay(0).setDuration(300).setInterpolator(interpol);
					for (int i = 0; i < laySettings.getChildCount(); i++) {
					    View child = laySettings.getChildAt(i);
					    child.setEnabled(false);
					}
					edittextRules.setEnabled(false);
					checkIconChange.setEnabled(false);
					checkIconName.setEnabled(false);
					checkInviz.setEnabled(false);
					checkPassApp.setEnabled(false);
					checkPassSecret.setEnabled(false);
					checkRules.setEnabled(false);

					framebuy.animate().translationX(0).alpha(1).setStartDelay(0).setDuration(300).setInterpolator(interpol);

				}
			});
			*/
    }

    private boolean MyStartActivity(Intent aIntent) {
        try {
            startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
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

    //TODO two
    public void doSomething() {

        sPref = getSharedPreferences("pozes", MODE_PRIVATE);
        Editor ed = sPref.edit();
        String whatDo = sPref.getString("passDo", "");

        if ((whatDo.contains("setpass")) && (setPassOne == 0)) {
            setPassOne = 1;
            pass1 = (Integer) imgCifra1.getTag();
            pass2 = (Integer) imgCifra2.getTag();
            pass3 = (Integer) imgCifra3.getTag();
            pass4 = (Integer) imgCifra4.getTag();

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
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.repeatpass), Toast.LENGTH_SHORT).show();
                }
            }, 400);
        } else if ((whatDo.contains("setpass")) && (setPassOne == 1)) {
            setPassOne = 0;

            if ((pass1 == (Integer) imgCifra1.getTag()) && (pass2 == (Integer) imgCifra2.getTag()) && (pass3 == (Integer) imgCifra3.getTag()) && (pass4 == (Integer) imgCifra4.getTag())) {
                ed.putInt("pass1", pass1);
                ed.putInt("pass2", pass2);
                ed.putInt("pass3", pass3);
                ed.putInt("pass4", pass4);
                ed.putBoolean("passTrue", true);
                ed.commit();

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

                ed.putInt("passOk", 1);
                ed.commit();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        laySettings.animate().alpha(1).setStartDelay(500).setDuration(300).setInterpolator(interpol);
                        for (int i = 0; i < laySettings.getChildCount(); i++) {
                            View child = laySettings.getChildAt(i);
                            child.setEnabled(true);
                        }
                        edittextRules.setEnabled(true);
                        checkIconChange.setEnabled(true);
                        checkIconName.setEnabled(true);
                        checkInviz.setEnabled(true);
                        checkPassApp.setEnabled(true);
                        checkPassSecret.setEnabled(true);
                        checkRules.setEnabled(true);
                        checkRotateBut.setEnabled(true);
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.savepass), Toast.LENGTH_SHORT).show();
                        framepass.animate().translationX(width * 0.9f).alpha(0).setStartDelay(450).setDuration(300).setInterpolator(interpol);
                    }
                }, 400);
            } else {

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
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                        long milliseconds = 300;
                        v.vibrate(milliseconds);
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.nopass), Toast.LENGTH_SHORT).show();
                    }
                }, 400);

            }
        }


        if (whatDo.contains("passsecret")) {
            pass1 = sPref.getInt("pass1", 0);
            pass2 = sPref.getInt("pass2", 0);
            pass3 = sPref.getInt("pass3", 0);
            pass4 = sPref.getInt("pass4", 0);
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
                        imgPoza1.setBackgroundResource(R.drawable.poza1w);
                        imgPoza2.setBackgroundResource(R.drawable.poza2r);
                        imgPoza3.setBackgroundResource(R.drawable.poza3r);
                        imgPoza1.setTag(0);
                        imgPoza2.setTag(1);
                        imgPoza3.setTag(1);
                        imgSecret.animate().translationX((int) (width * 0.85f)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        imgSettings.animate().translationX((int) (width * 0.85f)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        imgSexCube.animate().translationX((int) (width * 0.85f)).setStartDelay(0).setDuration(450).setInterpolator(interpol);

                        laySecret.animate().translationX((int) (width)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        tableLay.animate().translationX((int) (width)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        imgOkBut.animate().scaleX(0f).scaleY(0f).setStartDelay(0).setDuration(100).setInterpolator(interpol);
                        butClick = false;

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

        if (whatDo.contains("passsettings")) {
            pass1 = sPref.getInt("pass1", 0);
            pass2 = sPref.getInt("pass2", 0);
            pass3 = sPref.getInt("pass3", 0);
            pass4 = sPref.getInt("pass4", 0);
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

                        imgSecret.animate().translationX(-(int) (width * 0.85f)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        imgSettings.animate().translationX(-(int) (width * 0.815f)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        imgSexCube.animate().translationX(-(int) (width * 0.85f)).setStartDelay(0).setDuration(450).setInterpolator(interpol);

                        laySettings.animate().translationX(-(int) (width)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        tableLay.animate().translationX(-(int) (width)).setStartDelay(0).setDuration(450).setInterpolator(interpol);
                        butClick = false;

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
    }

    //TODO two
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


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        if (nInfo != null && nInfo.isConnected()) {

            return true;
        } else {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.netok), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //PAGEVIEW
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("items", mItems);
    }

    @Override
    public void onSingleTap(View view, MyPageItem item) {
        // Toast.makeText(getApplicationContext(), "Single tap: " + item.getTitle(),
        //         Toast.LENGTH_SHORT).show();
        int poza1Img = (Integer) imgPoza1.getTag();
        int poza2Img = (Integer) imgPoza2.getTag();
        int poza3Img = (Integer) imgPoza3.getTag();
        if ((poza1Img != 2) & (poza2Img != 2) & (poza3Img != 2) & (poza2Img != 1) & (poza3Img != 1)) {
            Toast.makeText(getApplicationContext(), R.string.singleTap,
                    Toast.LENGTH_SHORT).show();
        } else {
            if ((poza1Img != 0) | (poza2Img != 0) | (poza3Img != 0)) {
                Toast.makeText(getApplicationContext(), R.string.singleTap,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.singleTapDone,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDoubleTap(View view, MyPageItem item) {

        int poza1Img = (Integer) imgPoza1.getTag();
        int poza2Img = (Integer) imgPoza2.getTag();
        int poza3Img = (Integer) imgPoza3.getTag();

        if ((poza1Img == 2) & (poza2Img == 2) & (poza3Img == 2)) {
            Toast.makeText(getApplicationContext(), R.string.singleTapDone,
                    Toast.LENGTH_SHORT).show();
        }

        if (poza1Img == 0) {
            ChangePoza(item.getmLevelTitle(), imgPoza1, "poza1");
            imgPoza1.setTag(2);
            imgOkBut.animate().scaleX(1f).scaleY(1f).setStartDelay(0).setDuration(600).setInterpolator(inter2);
            if (poza2Img != 2) {
                imgPoza2.setTag(0);
                imgPoza2.setBackgroundResource(R.drawable.poza2w);
            }
        } else if (poza2Img == 0) {
            ChangePoza(item.getmLevelTitle(), imgPoza2, "poza2");
            imgPoza2.setTag(2);
            if (poza3Img != 2) {
                imgPoza3.setTag(0);
                imgPoza3.setBackgroundResource(R.drawable.poza3w);
            }
        } else if (poza3Img == 0) {
            ChangePoza(item.getmLevelTitle(), imgPoza3, "poza3");
            imgPoza3.setTag(2);
        }


    }

    public void ChangePoza(int itemNomer, ImageView imgPoza, String namePerfs) {
        sPref = getSharedPreferences("pozes", MODE_PRIVATE);
        Editor ed = sPref.edit();
        if (namePerfs.contains("poza1")) {
            ed.putString("poza2", "q");
            ed.putString("poza3", "q");
        } else if (namePerfs.contains("poza2")) {
            ed.putString("poza3", "q");
        }
        Boolean premiumOk = sPref.getBoolean("allOk", true);
        switch (itemNomer) {
            case 0: {
                imgPoza.setBackgroundResource(R.drawable.k0);
                if (premiumOk) {
                    ed.putString(namePerfs, "p0");
                    ed.commit();
                }
                break;
            }
            case 1: {
                imgPoza.setBackgroundResource(R.drawable.k1);
                if (premiumOk) {
                    ed.putString(namePerfs, "p1");
                    ed.commit();
                }
                break;
            }
            case 2: {
                imgPoza.setBackgroundResource(R.drawable.k2);
                if (premiumOk) {
                    ed.putString(namePerfs, "p2");
                    ed.commit();
                }
                break;
            }
            case 3: {
                imgPoza.setBackgroundResource(R.drawable.k3);
                if (premiumOk) {
                    ed.putString(namePerfs, "p3");
                    ed.commit();
                }
                break;
            }
            case 4: {
                imgPoza.setBackgroundResource(R.drawable.k4);
                if (premiumOk) {
                    ed.putString(namePerfs, "p4");
                    ed.commit();
                }
                break;
            }
            case 5: {
                imgPoza.setBackgroundResource(R.drawable.k5);
                if (premiumOk) {
                    ed.putString(namePerfs, "p5");
                    ed.commit();
                }
                break;
            }
            case 6: {
                imgPoza.setBackgroundResource(R.drawable.k6);
                if (premiumOk) {
                    ed.putString(namePerfs, "p6");
                    ed.commit();
                }
                break;
            }
            case 7: {
                imgPoza.setBackgroundResource(R.drawable.k7);
                if (premiumOk) {
                    ed.putString(namePerfs, "p7");
                    ed.commit();
                }
                break;
            }
            case 8: {
                imgPoza.setBackgroundResource(R.drawable.k8);
                if (premiumOk) {
                    ed.putString(namePerfs, "p8");
                    ed.commit();
                }
                break;
            }
            case 9: {
                imgPoza.setBackgroundResource(R.drawable.k9);
                if (premiumOk) {
                    ed.putString(namePerfs, "p9");
                    ed.commit();
                }
                break;
            }
            case 10: {
                imgPoza.setBackgroundResource(R.drawable.k10);
                if (premiumOk) {
                    ed.putString(namePerfs, "p10");
                    ed.commit();
                }
                break;
            }
            case 11: {
                imgPoza.setBackgroundResource(R.drawable.k11);
                if (premiumOk) {
                    ed.putString(namePerfs, "p11");
                    ed.commit();
                }
                break;
            }
            case 12: {
                imgPoza.setBackgroundResource(R.drawable.k12);
                if (premiumOk) {
                    ed.putString(namePerfs, "p12");
                    ed.commit();
                }
                break;
            }
            case 13: {
                imgPoza.setBackgroundResource(R.drawable.k13);
                if (premiumOk) {
                    ed.putString(namePerfs, "p13");
                    ed.commit();
                }
                break;
            }
            case 14: {
                imgPoza.setBackgroundResource(R.drawable.k14);
                if (premiumOk) {
                    ed.putString(namePerfs, "p14");
                    ed.commit();
                }
                break;
            }
            case 15: {
                imgPoza.setBackgroundResource(R.drawable.k15);
                if (premiumOk) {
                    ed.putString(namePerfs, "p15");
                    ed.commit();
                }
                break;
            }
            case 16: {
                imgPoza.setBackgroundResource(R.drawable.k16);
                if (premiumOk) {
                    ed.putString(namePerfs, "p16");
                    ed.commit();
                }
                break;
            }
            case 17: {
                imgPoza.setBackgroundResource(R.drawable.k17);
                if (premiumOk) {
                    ed.putString(namePerfs, "p17");
                    ed.commit();
                }
                break;
            }
            case 18: {
                imgPoza.setBackgroundResource(R.drawable.k18);
                if (premiumOk) {
                    ed.putString(namePerfs, "p18");
                    ed.commit();
                }
                break;
            }
            case 19: {
                imgPoza.setBackgroundResource(R.drawable.k19);
                if (premiumOk) {
                    ed.putString(namePerfs, "p19");
                    ed.commit();
                }
                break;
            }
            case 20: {
                imgPoza.setBackgroundResource(R.drawable.k20);
                if (premiumOk) {
                    ed.putString(namePerfs, "p20");
                    ed.commit();
                }
                break;
            }
            case 21: {
                imgPoza.setBackgroundResource(R.drawable.k21);
                if (premiumOk) {
                    ed.putString(namePerfs, "p21");
                    ed.commit();
                }
                break;
            }
            case 22: {
                imgPoza.setBackgroundResource(R.drawable.k22);
                if (premiumOk) {
                    ed.putString(namePerfs, "p22");
                    ed.commit();
                }
                break;
            }
            case 23: {
                imgPoza.setBackgroundResource(R.drawable.k23);
                if (premiumOk) {
                    ed.putString(namePerfs, "p23");
                    ed.commit();
                }
                break;
            }
            case 24: {
                imgPoza.setBackgroundResource(R.drawable.k24);
                if (premiumOk) {
                    ed.putString(namePerfs, "p24");
                    ed.commit();
                }
                break;
            }
            case 25: {
                imgPoza.setBackgroundResource(R.drawable.k25);
                if (premiumOk) {
                    ed.putString(namePerfs, "p25");
                    ed.commit();
                }
                break;
            }
            case 26: {
                imgPoza.setBackgroundResource(R.drawable.k26);
                if (premiumOk) {
                    ed.putString(namePerfs, "p26");
                    ed.commit();
                }
                break;
            }
            case 27: {
                imgPoza.setBackgroundResource(R.drawable.k27);
                if (premiumOk) {
                    ed.putString(namePerfs, "p27");
                    ed.commit();
                }
                break;
            }
            case 28: {
                imgPoza.setBackgroundResource(R.drawable.k28);
                if (premiumOk) {
                    ed.putString(namePerfs, "p28");
                    ed.commit();
                }
                break;
            }
            case 29: {
                imgPoza.setBackgroundResource(R.drawable.k29);
                if (premiumOk) {
                    ed.putString(namePerfs, "p29");
                    ed.commit();
                }
                break;
            }
            case 30: {
                imgPoza.setBackgroundResource(R.drawable.k30);
                if (premiumOk) {
                    ed.putString(namePerfs, "p30");
                    ed.commit();
                }
                break;
            }
            case 31: {
                imgPoza.setBackgroundResource(R.drawable.k31);
                if (premiumOk) {
                    ed.putString(namePerfs, "p31");
                    ed.commit();
                }
                break;
            }
            case 32: {
                imgPoza.setBackgroundResource(R.drawable.k32);
                if (premiumOk) {
                    ed.putString(namePerfs, "p32");
                    ed.commit();
                }
                break;
            }
            case 33: {
                imgPoza.setBackgroundResource(R.drawable.k33);
                if (premiumOk) {
                    ed.putString(namePerfs, "p33");
                    ed.commit();
                }
                break;
            }
            case 34: {
                imgPoza.setBackgroundResource(R.drawable.k34);
                if (premiumOk) {
                    ed.putString(namePerfs, "p34");
                    ed.commit();
                }
                break;
            }
            case 35: {
                imgPoza.setBackgroundResource(R.drawable.k35);
                if (premiumOk) {
                    ed.putString(namePerfs, "p35");
                    ed.commit();
                }
                break;
            }
            case 36: {
                imgPoza.setBackgroundResource(R.drawable.k36);
                if (premiumOk) {
                    ed.putString(namePerfs, "p36");
                    ed.commit();
                }
                break;
            }
            case 37: {
                imgPoza.setBackgroundResource(R.drawable.k37);
                if (premiumOk) {
                    ed.putString(namePerfs, "p37");
                    ed.commit();
                }
                break;
            }
            case 38: {
                imgPoza.setBackgroundResource(R.drawable.k38);
                if (premiumOk) {
                    ed.putString(namePerfs, "p38");
                    ed.commit();
                }
                break;
            }
            case 39: {
                imgPoza.setBackgroundResource(R.drawable.k39);
                if (premiumOk) {
                    ed.putString(namePerfs, "p39");
                    ed.commit();
                }
                break;
            }
            case 40: {
                imgPoza.setBackgroundResource(R.drawable.k40);
                if (premiumOk) {
                    ed.putString(namePerfs, "p40");
                    ed.commit();
                }
                break;
            }
            case 41: {
                imgPoza.setBackgroundResource(R.drawable.k41);
                if (premiumOk) {
                    ed.putString(namePerfs, "p41");
                    ed.commit();
                }
                break;
            }
            case 42: {
                imgPoza.setBackgroundResource(R.drawable.k42);
                if (premiumOk) {
                    ed.putString(namePerfs, "p42");
                    ed.commit();
                }
                break;
            }

            default: {
                break;
            }
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


    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            //Log.d(TAG, "Query inventory was successful.");

	            /*
	             * Check for items we own. Notice that for each purchase, we check
	             * the developer payload to see if it's correct! See
	             * verifyDeveloperPayload().
	             */

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            if (mIsPremium) {
                sPref = getSharedPreferences("pozes", MODE_PRIVATE);
                sPref.edit().putBoolean("allOk", true).commit();
            } else {
                sPref = getSharedPreferences("pozes", MODE_PRIVATE);
                sPref.edit().putBoolean("allOk", false).commit();
            }
            ;

            updateUi();
            //setWaitScreen(false);
            //Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    // User clicked the "Upgrade to Premium" button.
    public void onUpgradeAppButtonClicked(View arg0) {
        // Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
        //setWaitScreen(true);

	        /* TODO: for security, generate your payload here for verification. See the comments on
	         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
	         *        an empty string, but on a production app you should carefully generate this. */
        RandomString randomString = new RandomString(36);
        String payload = randomString.nextString();

        try {
            mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            // setWaitScreen(false);
            sPref = getSharedPreferences("pozes", MODE_PRIVATE);
            sPref.edit().putBoolean("allOk", false).commit();
        }
    }


    @Override
    public void onClick(DialogInterface dialog, int id) {
        if (id != DialogInterface.BUTTON_NEGATIVE) {
            // There are only four buttons, this should not happen
            //Log.e(TAG, "Unknown button clicked in subscription dialog: " + id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            // Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

	        /*
	         * TODO: verify that the developer payload of the purchase is correct. It will be
	         * the same one that you sent when initiating the purchase.
	         *
	         * WARNING: Locally generating a random string when starting a purchase and
	         * verifying it here might seem like a good approach, but this will fail in the
	         * case where the user purchases an item on one device and then uses your app on
	         * a different device, because on the other device you will not have access to the
	         * random string you originally generated.
	         *
	         * So a good developer payload has these characteristics:
	         *
	         * 1. If two different users purchase an item, the payload is different between them,
	         *    so that one user's purchase can't be replayed to another user.
	         *
	         * 2. The payload must be such that you can verify it even when the app wasn't the
	         *    one who initiated the purchase flow (so that items purchased by the user on
	         *    one device work on other devices owned by the user).
	         *
	         * Using your own server to store and verify developer payloads across app
	         * installations is recommended.
	         */

        return true;
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                //complain("Error purchasing: " + result);
                sPref = getSharedPreferences("pozes", MODE_PRIVATE);
                sPref.edit().putBoolean("allOk", false).commit();
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                sPref = getSharedPreferences("pozes", MODE_PRIVATE);
                sPref.edit().putBoolean("allOk", false).commit();
                return;
            }

            //Log.d(TAG, "Purchase successful.");
            if (purchase.getSku().equals(SKU_PREMIUM)) {
                // bought the premium upgrade!
                //Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                String langs = getResources().getConfiguration().locale.getLanguage();
                if (langs.contains("ru")) {
                    alert("���������� �� ���������� �� Premium Sex Cube!");
                } else if (langs.contains("uk")) {
                    alert("������ �� ��������� �� premium Sex Cube!");
                } else {
                    alert("Thank you for upgrading to Premium Sex Cube!");
                }
                sPref = getSharedPreferences("pozes", MODE_PRIVATE);
                sPref.edit().putBoolean("allOk", true).commit();
                mIsPremium = true;
                updateUi();
                setWaitScreen(false);
            }
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                //Log.d(TAG, "Consumption successful. Provisioning.");
                saveData();
            } else {
                complain("Error while consuming: " + result);
            }
            updateUi();
            //setWaitScreen(false);
            //Log.d(TAG, "End consumption flow.");
        }
    };

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (isServiceReady) {
            if (mHelper != null) {
                mHelper.disposeWhenFinished();
                mHelper = null;
            }
        }

    }

    // updates UI to reflect model
    public void updateUi() {
        // update the car color to reflect premium status or lack thereof
        //((ImageView)findViewById(R.id.free_or_premium)).setImageResource(mIsPremium ? R.drawable.premium : R.drawable.free);

        // "Upgrade" button is only visible if the user is not premium
        //findViewById(R.id.upgrade_button).setVisibility(mIsPremium ? View.GONE : View.VISIBLE);

    }

    // Enables or disables the "please wait" screen.
    void setWaitScreen(boolean set) {
        //findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
        // findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);
    }

    void complain(String message) {
        //Log.e(TAG, "**** SexCube Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    void saveData() {

	        /*
	         * WARNING: on a real application, we recommend you save data in a secure way to
	         * prevent tampering. For simplicity in this sample, we simply store the data using a
	         * SharedPreferences.
	         */

        SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();
        //spe.putInt("tank", mTank);
        //spe.apply();
        Log.d(TAG, "Saved data: tank = " + 1);
    }

    void loadData() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        //mTank = sp.getInt("tank", 2);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    public class RandomString {

	        /*
	         * static { for (int idx = 0; idx < 10; ++idx) symbols[idx] = (char)
	         * ('0' + idx); for (int idx = 10; idx < 36; ++idx) symbols[idx] =
	         * (char) ('a' + idx - 10); }
	         */

        private final Random random = new Random();

        private final char[] buf;

        public RandomString(int length) {
            if (length < 1)
                throw new IllegalArgumentException("length < 1: " + length);
            buf = new char[length];
        }

        public String nextString() {
            for (int idx = 0; idx < buf.length; ++idx)
                buf[idx] = symbols[random.nextInt(symbols.length)];
            return new String(buf);
        }

    }

    public final class SessionIdentifierGenerator {

        private SecureRandom random = new SecureRandom();

        public String nextSessionId() {
            return new BigInteger(130, random).toString(32);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdViews();
    }

}
