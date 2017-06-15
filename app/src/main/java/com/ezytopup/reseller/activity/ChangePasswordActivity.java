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
import com.ezytopup.reseller.api.ChangepasswordResponse;
import com.ezytopup.reseller.utility.Helper;
import com.ezytopup.reseller.utility.PreferenceUtils;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText mOldPasswordView, mNewPasswordView, mConfirmNewPasswordView;
    private Button mChangePasswordButton, mCancelButton;
    private static final String TAG = "ChangePasswordActivity";
    private String token, newPassword, confirmPassword, oldPassword;
    private ConstraintLayout container_view;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, ChangePasswordActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mOldPasswordView = (EditText) findViewById(R.id.oldPassword);
        mNewPasswordView = (EditText) findViewById(R.id.newPassword);
        mConfirmNewPasswordView = (EditText) findViewById(R.id.confirmNewpassword);

        mChangePasswordButton = (Button) findViewById(R.id.change_password_button);
        mCancelButton = (Button) findViewById(R.id.btnCancel);
        container_view = (ConstraintLayout) findViewById(R.id.container_changepassword);

        mCancelButton.setOnClickListener(this);
        mChangePasswordButton.setOnClickListener(this);
    }

    private void setChangepassword(String token, String newPassword, String oldPassword,
                                   String confirmPassword){

        Call<ChangepasswordResponse> changePassword = Eztytopup.getsAPIService()
                .setChangePassword(token, oldPassword, newPassword, confirmPassword, token);
        changePassword.enqueue(new Callback<ChangepasswordResponse>() {
            @Override
            public void onResponse(Call<ChangepasswordResponse> call,
                                   Response<ChangepasswordResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){

                    Toast.makeText(ChangePasswordActivity.this, R.string.password_change,
                            Toast.LENGTH_SHORT).show();
                    finish();

                } else if (response.isSuccessful() && response.body().status.code
                        .equals(String.valueOf(HttpURLConnection.HTTP_UNAUTHORIZED))) {

                    Toast.makeText(ChangePasswordActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();

                    PreferenceUtils.destroyUserSession(ChangePasswordActivity.this);
                    Login.start(ChangePasswordActivity.this);

                }else {
                    Toast.makeText(ChangePasswordActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangepasswordResponse> call, Throwable t) {
                    Helper.apiSnacbarError(ChangePasswordActivity.this, t, container_view);
            }
        });
    }
    private void changePassword(){
        token = PreferenceUtils.getSinglePrefrenceString(ChangePasswordActivity.this,
                R.string.settings_def_storeaccess_token_key);
        oldPassword = mOldPasswordView.getText().toString();
        newPassword = mNewPasswordView.getText().toString();
        confirmPassword = mConfirmNewPasswordView.getText().toString();

        Helper.log(TAG, "change passwordToken " + token, null);

        if (oldPassword.isEmpty()) {
            Toast.makeText(this, R.string.please_fill_password, Toast.LENGTH_SHORT).show();
        } else if (newPassword.length() < 8 || confirmPassword.length() < 8 ||
                oldPassword.length() < 8) {
            Toast.makeText(this, R.string.password_toshort, Toast.LENGTH_SHORT).show();
        } else if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, R.string.password_notmatch, Toast.LENGTH_SHORT).show();
        } else {
            setChangepassword(token, newPassword, oldPassword, confirmPassword);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Helper.hideSoftKeyboard(container_view);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_password_button:
                changePassword();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_change_password;
    }

}
