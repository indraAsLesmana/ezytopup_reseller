package com.ezytopup.reseller.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.api.ForgotpasswordResponse;
import com.ezytopup.reseller.utility.Helper;
import com.ezytopup.reseller.utility.PreferenceUtils;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEmailView, mPhoneView;
    private Button mGetYourPasswordButton, mBackButton;
    private ConstraintLayout container_forgotpassword;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, ForgotPasswordActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mEmailView = (EditText) findViewById(R.id.emailAddress);
        mPhoneView = (EditText) findViewById(R.id.phone);

        mGetYourPasswordButton = (Button) findViewById(R.id.btnGetyourpassword);
        mBackButton = (Button) findViewById(R.id.btnBack);
        container_forgotpassword = (ConstraintLayout) findViewById(R.id.container_forgotpassword);

        mGetYourPasswordButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);

    }

    private void setForgotpassword(String email, String phone){
        String token = PreferenceUtils.getSinglePrefrenceString(ForgotPasswordActivity.this,
                R.string.settings_def_storeaccess_token_key);

        Call<ForgotpasswordResponse> forgotPassword = Eztytopup.getsAPIService()
                .setForgotpassword(token, email, phone);
        forgotPassword.enqueue(new Callback<ForgotpasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotpasswordResponse> call,
                                   Response<ForgotpasswordResponse> response) {

                if (response.isSuccessful() && response.body().status.getCode()
                        .equals(String.valueOf(HttpURLConnection.HTTP_OK))) {

                    Toast.makeText(ForgotPasswordActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(ForgotPasswordActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForgotpasswordResponse> call, Throwable t) {
                Helper.apiSnacbarError(ForgotPasswordActivity.this, t, container_forgotpassword);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGetyourpassword:
                if (mEmailView.getText().toString().isEmpty() ||
                        mPhoneView.getText().toString().isEmpty()){
                    Toast.makeText(this, R.string.forgot_password,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                setForgotpassword(mEmailView.getText().toString(), mPhoneView.getText().toString());
                break;
            case R.id.btnBack:
                Helper.hideSoftKeyboard(container_forgotpassword);
                finish();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
        return R.layout.activity_forgot_password;
    }
}
