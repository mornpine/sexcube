package com.pashkov.sexcubefull;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.*;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pashkov.sexcubefull.util.AppPreferences;
import com.pashkov.sexcubefull.util.RestClient;
import com.tapjoy.TJActionRequest;
import com.tapjoy.TJError;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

import cz.msebera.android.httpclient.Header;

public class Offerwall extends Activity implements View.OnClickListener {
    private static final int REMOVE_ADS_BALANCE = 1000;
    private Button tapjoy;
    private Button removeAds;
    private String userId;
    private ProgressDialog progressDialog;
    private static final Integer OID = 1306;
    private static final String TAG = "OfferwallActivity";
    private AdView mAdView;
    private TJPlacement p;

    private void enableTapjoyButton() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                tapjoy.setEnabled(true);
            }
        });
    }

    private void updateTapjoyUserId() {
        Tapjoy.setUserID(userId);
        enableTapjoyButton();

        p = new TJPlacement(this, "Offerwall", new TJPlacementListener() {
            @Override
            public void onRequestSuccess(TJPlacement placement) {
            }

            @Override
            public void onRequestFailure(TJPlacement placement, TJError error) {
            }

            @Override
            public void onContentReady(TJPlacement placement) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    p.showContent();
                }
            }

            @Override
            public void onContentShow(TJPlacement placement) {
            }

            @Override
            public void onContentDismiss(TJPlacement placement) {
                progressDialog = null;
                p.requestContent();
                fillInTotalPointControls();
            }

            @Override
            public void onPurchaseRequest(TJPlacement placement, TJActionRequest request, String productId) {
                Log.i("Tapjoy", "purchase");
            }

            @Override
            public void onRewardRequest(TJPlacement placement, TJActionRequest request, String itemId, int quantity) {
                Log.i("Tapjoy", "reward");
            }
        });

        if (Tapjoy.isConnected()) {
            p.requestContent();
        }
    }

    private void updateServerToken() {
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString(AppPreferences.TOKEN_KEY, "");
        if (userId == null || userId.isEmpty() || token.isEmpty()) {
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userId);
        requestParams.put("gcm", token);

        RestClient.get("update_gcm.php", requestParams, new JsonHttpResponseHandler() {
            @Override
            @TargetApi(11)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, "GCM Token updated for user.");
            }
        });
    }

    private void registerForUser() {
        final Context self = this;
        RestClient.get("register_user.php", null, new JsonHttpResponseHandler() {
            @Override
            @TargetApi(11)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    userId = response.getString("data");
                    AppPreferences.setUserId(self, userId);
                    Log.i(TAG, "User ID obtained: " + userId);

                    updateTapjoyUserId();
                    updateServerToken();

                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setText("text to clip");
                    } else {
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData clip = android.content.ClipData.newPlainText("User ID", userId);
                        clipboard.setPrimaryClip(clip);
                    }

                    Context context = getApplicationContext();
                    CharSequence text = "The User ID is copied to the clipboard";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    fillInUserIdControls(userId);
                } catch (JSONException e) {
                }
            }
        });
    }

    private void updateAdViews() {
        if (AppPreferences.areAdsRemoved(this)) {
            mAdView.setVisibility(View.INVISIBLE);
            removeAds.setEnabled(false);
        } else {
            removeAds.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offerwall);

        RelativeLayout adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);
        mAdView = new AdView(this, "373834849620122_373834962953444", AdSize.BANNER_320_50);
        adViewContainer.addView(mAdView);
        mAdView.loadAd();

        tapjoy = (Button) findViewById(R.id.tapjoy);
        tapjoy.setOnClickListener(this);
        tapjoy.setEnabled(false);

        removeAds = (Button) findViewById(R.id.removeAds);
        removeAds.setOnClickListener(this);
        removeAds.setEnabled(false);

        TextView neededPoints = (TextView) findViewById(R.id.neededPoints);
        neededPoints.setText(Integer.toString(REMOVE_ADS_BALANCE));

        userId = AppPreferences.getUserId(this);
        if (userId == null || userId.isEmpty()) {
            registerForUser();
        } else {
            updateTapjoyUserId();
            fillInUserIdControls(userId);
        }

        updateAdViews();
    }

    private void fillInUserIdControls(String userId) {
        TextView userIdView = (TextView) findViewById(R.id.userId);
        userIdView.setText(userId);
        fillInTotalPointControls();
    }

    private void fillInTotalPointControls() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userId);

        RestClient.get("get_total_points.php", requestParams, new JsonHttpResponseHandler() {
            @Override
            @TargetApi(11)
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int balance = response.getInt("data");
                    TextView totalPointView = (TextView) findViewById(R.id.totalPoints);
                    totalPointView.setText(Integer.toString(balance));
                } catch (JSONException e) {

                }

            }
        });
    }

    private void showShowOffers(Context context) {
        if (p.isContentReady()) {
            p.showContent();
        } else {
            progressDialog = ProgressDialog.show(context, "Please wait...", "Retrieving data ...");
            p.requestContent();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tapjoy: {
                final Context context = this;
                RequestParams requestParams = new RequestParams();
                requestParams.put("oid", OID);
                requestParams.put("uid", userId);
                RestClient.get("check_limit.php", requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            int returnedCode = Integer.parseInt(response.getString("status"));
                            if (returnedCode != 200) {
                                AlertDialog alert1 = new AlertDialog.Builder(context).create();
                                alert1.setTitle("Error:");
                                alert1.setMessage(AppPreferences.getErrorText(context, returnedCode));
                                alert1.show();
                                return;
                            }
                            showShowOffers(context);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        AlertDialog alert1 = new AlertDialog.Builder(context).create();
                        alert1.setTitle("Error:");
                        alert1.setMessage(AppPreferences.getErrorText(context, statusCode));
                        alert1.show();
                    }
                });
            }
            break;
            case R.id.removeAds: {
                final Context context = this;
                RequestParams requestParams = new RequestParams();
                requestParams.put("uid", userId);

                RestClient.get("get_total_points.php", requestParams, new JsonHttpResponseHandler() {
                    @Override
                    @TargetApi(11)
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            int balance = response.getInt("data");
                            if (balance >= REMOVE_ADS_BALANCE) {
                                AppPreferences.removeAds(context);
                                updateAdViews();
                                tapjoy.setEnabled(false);
                                registerForUser();
                                AlertDialog alert = new AlertDialog.Builder(context).create();
                                alert.setTitle("Success:");
                                alert.setMessage("Remove ads done! Please kill and restart the app.");
                                alert.show();
                            } else {
                                AlertDialog alert = new AlertDialog.Builder(context).create();
                                alert.setTitle("Error:");
                                alert.setMessage("Not enough points.");
                                alert.show();
                            }
                        } catch (JSONException e) {

                        }

                    }
                });


            }
        }

    }
}