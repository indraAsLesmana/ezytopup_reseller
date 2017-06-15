package com.ezytopup.reseller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.api.SendFeedBackResponse;
import com.ezytopup.reseller.api.ContactUsResponse;
import com.ezytopup.reseller.utility.Helper;
import com.ezytopup.reseller.utility.PreferenceUtils;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends BaseActivity {

    private TextView tvTitle, tvAddress, tvCity, tvCountry, tvFacebook,
            tvTwitter, tvInstagram, tvWeb, tvEmail, tvPhone, tvMessage,
            tvYourName, tvYourEmail, tvYourPhone, tvYourSubject, tvYourMessage;

    private RelativeLayout container_layout;
    private String token, yourName, yourEmail, yourPhone, yourSubject, yourMessage;
    private static final String TAG = "ContactUsActivity";

    private LinearLayout loadingBar;
    private ScrollView contentLayout;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, ContactUsActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        container_layout = (RelativeLayout) findViewById(R.id.container_contactus_layout);
        loadingBar = (LinearLayout) findViewById(R.id.loadingBar);
        contentLayout = (ScrollView) findViewById(R.id.contentLayout);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvCountry = (TextView) findViewById(R.id.tvCountry);
        tvFacebook = (TextView) findViewById(R.id.tvFacebook);
        tvTwitter = (TextView) findViewById(R.id.tvTwitter);
        tvInstagram = (TextView) findViewById(R.id.tvInstagram);
        tvWeb = (TextView) findViewById(R.id.tvWeb);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvYourName = (TextView) findViewById(R.id.tvYourName);
        tvYourEmail = (TextView) findViewById(R.id.tvYourEmail);
        tvYourPhone = (TextView) findViewById(R.id.tvYourPhone);
        tvYourSubject = (TextView) findViewById(R.id.tvYourSubject);
        tvYourMessage = (TextView) findViewById(R.id.tvYourMessage);

        Button btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvYourName.getText().toString().equals("")) {

                    Toast.makeText(v.getContext(), String.format("%d name", R.string.please_fill),
                            Toast.LENGTH_LONG).show();

                } else if (tvYourEmail.getText().toString().equals("")) {

                    Toast.makeText(v.getContext(), String.format("%d email", R.string.please_fill),
                            Toast.LENGTH_LONG).show();

                } else if (tvYourPhone.getText().toString().equals("")) {

                    Toast.makeText(v.getContext(), String.format("%d phone", R.string.please_fill),
                            Toast.LENGTH_LONG).show();

                }else if (tvYourSubject.getText().toString().equals("")) {

                    Toast.makeText(v.getContext(), String.format("%d subject", R.string.please_fill),
                            Toast.LENGTH_LONG).show();

                }else if (tvYourMessage.getText().toString().equals("")) {
                    Toast.makeText(v.getContext(), String.format("%d message", R.string.please_fill),
                            Toast.LENGTH_LONG).show();
                }
                else {
                    feedBack();
                    finish();
                }
            }
        });

        getContactUs();

    }

    public void getContactUs() {
        loadingBar.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);

        Call<ContactUsResponse> contactUsResponseCall = Eztytopup.getsAPIService().getContactUs();
        contactUsResponseCall.enqueue(new Callback<ContactUsResponse>() {
            @Override
            public void onResponse(Call<ContactUsResponse> call, Response<ContactUsResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))) {

                    tvTitle.setText(response.body().alamat.nama);
                    tvAddress.setText(response.body().alamat.alamat);
                    tvCity.setText(response.body().alamat.kota);
                    tvCountry.setText(response.body().alamat.negara);
                    tvFacebook.setText(response.body().sosmed.fb);
                    tvTwitter.setText(response.body().sosmed.twitter);
                    tvInstagram.setText(response.body().sosmed.instagram);
                    tvWeb.setText(response.body().support.web);
                    tvEmail.setText(response.body().support.email);
                    tvPhone.setText(response.body().support.telp);
                    tvMessage.setText(response.body().pesanku);

                    loadingBar.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(ContactUsActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();

                    loadingBar.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ContactUsResponse> call, Throwable t) {
                Helper.apiSnacbarError(ContactUsActivity.this, t, container_layout);

                loadingBar.setVisibility(View.GONE);
                contentLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void feedBack() {
        token = PreferenceUtils.getSinglePrefrenceString(ContactUsActivity.this,
                R.string.settings_def_storeaccess_token_key);
        yourName = tvYourName.getText().toString();
        yourEmail = tvYourEmail.getText().toString();
        yourPhone = tvYourPhone.getText().toString();
        yourSubject = tvYourSubject.getText().toString();
        yourMessage = tvYourMessage.getText().toString();

        Helper.log(TAG, "Feed Back Token " + token, null);

        sendFeedback(token, yourName, yourEmail, yourPhone, yourSubject, yourMessage);
        
    }

    private void sendFeedback(String token, String yourName, String yourEmail,
                              String yourPhone, String yourSubject, String yourMessage) {
        Call<SendFeedBackResponse> changePassword = Eztytopup.getsAPIService()
                .sendFeedBack(
                        token, token, yourName, yourEmail, yourPhone, yourSubject, yourMessage);

        changePassword.enqueue(new Callback<SendFeedBackResponse>() {
            @Override
            public void onResponse(Call<SendFeedBackResponse> call,
                                   Response<SendFeedBackResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){

                    Toast.makeText(ContactUsActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ContactUsActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SendFeedBackResponse> call, Throwable t) {
                Helper.apiSnacbarError(ContactUsActivity.this, t, container_layout);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Helper.hideSoftKeyboard(container_layout);
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
        return R.layout.activity_contact_us;
    }


}
