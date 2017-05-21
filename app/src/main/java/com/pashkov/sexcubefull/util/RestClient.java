package com.pashkov.sexcubefull.util;

import com.loopj.android.http.*;

/**
 * Created by ralphchan on 11/12/2016.
 */

public class RestClient {
    private static final String BASE_URL = "http://lovecubepro.com/";

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static SyncHttpClient syncClient = new SyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static RequestHandle syncGet(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        return syncClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
