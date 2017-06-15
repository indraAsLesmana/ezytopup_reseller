package com.ezytopup.reseller.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezytopup.reseller.BuildConfig;
import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.api.Authrequest;
import com.ezytopup.reseller.utility.Constant;
import com.ezytopup.reseller.utility.Helper;
import com.ezytopup.reseller.utility.PermissionHelper;
import com.ezytopup.reseller.utility.PreferenceUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends BaseActivity implements LoaderCallbacks<Cursor> {

    private static final String TAG = "Login";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView mForgotPasswordView;
    private ConstraintLayout container_login;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, Login.class);
        caller.startActivity(intent);
        caller.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button mEmailSignUpButton = (Button) findViewById(R.id.btnSignup);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (PermissionHelper.isPermissionGranted(Login.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE})) {
                    setDeviceid();
                    attemptLogin();
                } else {
                    Toast.makeText(Login.this, R.string.telephony_request,
                            Toast.LENGTH_SHORT).show();
                    getDeviceimei();
                }
            }
        });

        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity.start(Login.this);
            }
        });

        mForgotPasswordView = (TextView) findViewById(R.id.tvForgotPassword);
        mForgotPasswordView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordActivity.start(Login.this);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        container_login = (ConstraintLayout) findViewById(R.id.container_login);
        TextView version = (TextView)findViewById(R.id.version_code);

        if (!PermissionHelper.isPermissionGranted(Login.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE})){
            PermissionHelper.requestStorage(Login.this);
        }

       getDeviceimei();

        if(version != null) {
            String versionStr = BuildConfig.VERSION_NAME;
            versionStr += "-" + BuildConfig.BUILD_TYPE;
            version.setText(String.format("%s: %s", getString(R.string.code_version), versionStr));
        }
    }

    private void getDeviceimei() {
        if (!PermissionHelper.isPermissionGranted(Login.this,
                new String[]{Manifest.permission.READ_PHONE_STATE})) {
            PermissionHelper.requestTelephony(Login.this);
        }
    }

    private void setDeviceid() {

        if (PermissionHelper.isPermissionGranted(Login.this,
                new String[]{Manifest.permission.READ_PHONE_STATE})) {

            TelephonyManager tManager = (TelephonyManager) getBaseContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("HardwareIds") String deviceIMEI = tManager.getDeviceId();

            if (PreferenceUtils.getSinglePrefrenceString(this,
                    R.string.settings_def_storeidevice_key).equals(Constant.PREF_NULL)) {
                PreferenceUtils.setDeviceId(this, deviceIMEI);
                Helper.log(TAG, "Deviceid --> " + deviceIMEI, null);
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_skipregistration) {
            Eztytopup.setIsEnduser(Boolean.TRUE);
            MainActivity.start(Login.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            loginUser(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void loginUser(String mEmail, String mPassword){
        Authrequest auth = new Authrequest
                (Constant.LOGIN_PROVIDER_EMAIL, mEmail, mPassword,
                        PreferenceUtils.getSinglePrefrenceString(this,
                                R.string.settings_def_storeidevice_key));

        Call<Authrequest> call = Eztytopup.getsAPIService().login_request(auth);
        call.enqueue(new Callback<Authrequest>() {
            @Override
            public void onResponse(Call<Authrequest> call, Response<Authrequest> response) {
                showProgress(false);
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))) {

                    PreferenceUtils.destroyUserSession(Login.this);
                    PreferenceUtils.setStoreDetail(Login.this,
                            response.body().getUser().getId(),
                            response.body().getUser().getFirstName(),
                            response.body().getUser().getLastName(),
                            response.body().getUser().getEmail(),
                            response.body().getUser().getPhoneNumber(),
                            response.body().getUser().getAccessToken(),
                            response.body().getUser().getImageUser(),
                            response.body().getUser().getSellerShopName(),
                            response.body().getUser().getSellerKasirName(),
                            response.body().getUser().getSellerPhone(),
                            response.body().getUser().getSellerAddress(),
                            response.body().getUser().getSellerWebsite(),
                            response.body().getUser().getSellerLogo(),
                            response.body().getUser().getSellerPrintLogo(),
                            response.body().getUser().getSellerWarnaBg(),
                            response.body().getUser().getSellerId());

                    Eztytopup.setIsEnduser
                            (PreferenceUtils.getSinglePrefrenceString(Login.this,
                                    R.string.settings_def_sellerid_key).equals(Constant.PREF_NULL));

                    MainActivity.start(Login.this);
                }else {
                    Toast.makeText(Login.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Authrequest> call, Throwable t) {
                Helper.apiSnacbarError(Login.this, t, container_login);
                showProgress(false);
            }
        });
    }
}

