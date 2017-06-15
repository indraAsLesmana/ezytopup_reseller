package com.ezytopup.reseller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ezytopup.reseller.R;
import com.ezytopup.reseller.utility.Constant;
import com.ezytopup.reseller.utility.PreferenceUtils;

public class PaymentActivity extends BaseActivity {

    private WebView webview;
    private String email, device_id;
    private String paymentUrl;
    private static final String PAYMENT_EMAIL = "PaymentActivity::email";
    private static final String PAYMENT_DEVICEID = "PaymentActivity::deviceid";
    private static final String PAYMENT_URL = "PaymentActivity::paymentdetail";
    private String token;

    public static void start(Activity caller, String email, String deviceid, String paymentUrl) {
        Intent intent = new Intent(caller, PaymentActivity.class);
        intent.putExtra(PAYMENT_EMAIL, email);
        intent.putExtra(PAYMENT_DEVICEID, deviceid);
        intent.putExtra(PAYMENT_URL, paymentUrl);
        caller.startActivity(intent);
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra(PaymentActivity.PAYMENT_EMAIL) == null ||
                getIntent().getStringExtra(PaymentActivity.PAYMENT_DEVICEID) == null ||
                getIntent().getStringExtra(PaymentActivity.PAYMENT_URL) == null){
            finish();
            return;
        }
        device_id = getIntent().getStringExtra(PaymentActivity.PAYMENT_DEVICEID);
        email = getIntent().getStringExtra(PaymentActivity.PAYMENT_EMAIL);
        paymentUrl = getIntent().getStringExtra(PaymentActivity.PAYMENT_URL);
        // TODO : if i use this token, payment not work. for now just use temporary token
        token = PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_storeaccess_token_key);

        webview = (WebView) findViewById(R.id.pay_webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.setWebViewClient(new HelloWebViewClient());
        webview.loadUrl(String.format("%s?session_name=%s&device_id=%s&email=%s",
                paymentUrl, Constant.temporaryToken, device_id, email));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_payment;
    }
}
