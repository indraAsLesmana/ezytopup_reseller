package com.ezytopup.reseller.activity;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ezytopup.reseller.BuildConfig;
import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.api.TokencheckResponse;
import com.ezytopup.reseller.utility.Constant;
import com.ezytopup.reseller.utility.Helper;
import com.ezytopup.reseller.utility.PreferenceUtils;
import java.net.HttpURLConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroActivity extends AppCompatActivity{
    private static final String TAG = "IntroActivity";
    private View mProgressBar;
    private ConstraintLayout intro_container;
    private ConstraintLayout intro_errorcontainer;
    private Button btn_reload;
    private TextView msg_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        btn_reload = (Button) findViewById(R.id.btn_intro_reload);
        intro_container = (ConstraintLayout) findViewById(R.id.container_intro);
        intro_errorcontainer = (ConstraintLayout) findViewById(R.id.container_introerror);
        msg_info = (TextView) findViewById(R.id.intro_message);
        mProgressBar = findViewById(R.id.intro_progress_bar);
        TextView version = (TextView)findViewById(R.id.version_code);
        final String lastToken = PreferenceUtils.getSinglePrefrenceString(IntroActivity.this,
                R.string.settings_def_storeaccess_token_key);

        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokenCheck(lastToken);
            }
        });

        if(version != null) {
            String versionStr = BuildConfig.VERSION_NAME;
            versionStr += "-" + BuildConfig.BUILD_TYPE;
            version.setText(String.format("%s: %s", getString(R.string.code_version), versionStr));
        }

        if (Eztytopup.getIsSunmiDevice()) {
            mProgressBar.setVisibility(View.GONE);
            intro_errorcontainer.setVisibility(View.VISIBLE);
            msg_info.setText(R.string.device_dsupport);
            btn_reload.setVisibility(View.INVISIBLE);
        }else {
            tokenCheck(lastToken);
        }
    }

    private void tokenCheck(String lastToken){
        intro_errorcontainer.setVisibility(View.GONE);
        if (!lastToken.equals(Constant.PREF_NULL)){
            tokenValidityCheck(lastToken);
        }else {
            PreferenceUtils.destroyUserSession(IntroActivity.this);
            Login.start(IntroActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Eztytopup.setIsEnduser(false);
    }

    private void tokenValidityCheck(String token){
        mProgressBar.setVisibility(View.VISIBLE);

        Call<TokencheckResponse> tokenResponse = Eztytopup.getsAPIService().checkToken(token);
        tokenResponse.enqueue(new Callback<TokencheckResponse>() {
            @Override
            public void onResponse(Call<TokencheckResponse> call,
                                   Response<TokencheckResponse> response) {
                if (response.isSuccessful()
                        && response.body().status.getCode()
                        .equals(String.valueOf(HttpURLConnection.HTTP_OK))
                        && response.body().tokenValidity == Boolean.TRUE) {

                    mProgressBar.setVisibility(View.GONE);
                    MainActivity.start(IntroActivity.this);
                    finish();

                } else {

                    Helper.log(TAG, String.format("onResponse: %s",
                            response.body().status.getMessage()), null);

                    Helper.snacbarError(response.body().status.getMessage(), intro_container);
                    mProgressBar.setVisibility(View.GONE);
                    PreferenceUtils.destroyUserSession(IntroActivity.this);

                    Login.start(IntroActivity.this);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<TokencheckResponse> call, Throwable t) {

                Helper.apiSnacbarError(IntroActivity.this, t, intro_container);
                mProgressBar.setVisibility(View.GONE);
                intro_errorcontainer.setVisibility(View.VISIBLE);
            }
        });
    }

}
