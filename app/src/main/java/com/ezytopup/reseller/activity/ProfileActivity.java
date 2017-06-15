package com.ezytopup.reseller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.api.UpdateProfileResponse;
import com.ezytopup.reseller.api.WalletbalanceResponse;
import com.ezytopup.reseller.utility.Constant;
import com.ezytopup.reseller.utility.Helper;
import com.ezytopup.reseller.utility.PreferenceUtils;
import com.sw926.imagefileselector.ImageCropper;
import com.sw926.imagefileselector.ImageFileSelector;

import java.io.File;
import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "ProfileActivity";
    private EditText mSaldo, mName, mPhone, mEmail;
    private ImageView mImageprofile;
    private LinearLayout container_view;
    private ImageFileSelector mImageFileSelector;
    private ImageCropper mImageCropper;
    private File mCurrentSelectFile;
    private Bitmap bitmap;
    private Button btnprofileUpdate, btnprofileCancel;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, ProfileActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSaldo = (EditText) findViewById(R.id.ed_profile_saldo);
        mName = (EditText) findViewById(R.id.ed_profile_name);
        mPhone = (EditText) findViewById(R.id.ed_profile_phone);
        mEmail = (EditText) findViewById(R.id.ed_profile_email);
        mImageprofile = (ImageView) findViewById(R.id.im_profile_image);
        container_view = (LinearLayout) findViewById(R.id.container_profileactivity);
        btnprofileUpdate = (Button) findViewById(R.id.btnprofileUpdate);
        btnprofileCancel = (Button) findViewById(R.id.btnprofileCancel);

        //make editext not editable
        mEmail.setKeyListener(null);
        mSaldo.setKeyListener(null);

        btnprofileUpdate.setOnClickListener(this);
        btnprofileCancel.setOnClickListener(this);

        String userName = PreferenceUtils.getSinglePrefrenceString
                (ProfileActivity.this, R.string.settings_def_storefirst_name_key);
        String userPhone = PreferenceUtils.getSinglePrefrenceString
                (ProfileActivity.this, R.string.settings_def_storephone_number_key);
        String userMail = PreferenceUtils.getSinglePrefrenceString
                (ProfileActivity.this, R.string.settings_def_storeemail_key);
        String imageUrl = PreferenceUtils.getSinglePrefrenceString
                (ProfileActivity.this, R.string.settings_def_storeimage_user_key);

        mName.setText(userName);
        mPhone.setText(userPhone);
        mEmail.setText(userMail);

        ImageFileSelector.setDebug(true);

        if (!imageUrl.equals(Constant.PREF_NULL)) {
            Glide.with(ProfileActivity.this)
                    .load(imageUrl).centerCrop()
                    .error(R.drawable.ic_user_image_null)
                    .into(mImageprofile);

            mImageprofile.setDrawingCacheEnabled(true);
        }

        String checkEmail = PreferenceUtils.
                getSinglePrefrenceString(ProfileActivity.this, R.string.settings_def_storeemail_key);
        if (!checkEmail.startsWith("autoemail") && !checkEmail.endsWith("@mail.com")
                || checkEmail.equals(Constant.PREF_NULL)) {

            if (Eztytopup.getIsEnduser()){
                getBalance();
            }else {
                getBalanceReseller();
            }
        }

        mImageFileSelector = new ImageFileSelector(this);
        mImageFileSelector.setCallback(new ImageFileSelector.Callback() {
            @Override
            public void onSuccess(String file) {
                if (!TextUtils.isEmpty(file)) {
                    loadImage(file);
                    mCurrentSelectFile = new File(file);
                } else
                    Toast.makeText(ProfileActivity.this, R.string.select_imageerror,
                            Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError() {
                Toast.makeText(ProfileActivity.this, R.string.select_imageerror,
                        Toast.LENGTH_LONG).show();
            }
        });

        mImageCropper = new ImageCropper(this);
        mImageCropper.setCallback(new ImageCropper.ImageCropperCallback() {
            @Override
            public void onCropperCallback(ImageCropper.CropperResult result, File srcFile, File outFile) {
                mCurrentSelectFile = null;
                if (result == ImageCropper.CropperResult.success) {
                    loadImage(outFile.getPath());
                } else if (result == ImageCropper.CropperResult.error_illegal_input_file) {
                    Toast.makeText(ProfileActivity.this, R.string.input_fileerror, Toast.LENGTH_LONG).show();
                } else if (result == ImageCropper.CropperResult.error_illegal_out_file) {
                    Toast.makeText(ProfileActivity.this, R.string.output_fileerror, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void loadImage(final String file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = BitmapFactory.decodeFile(file);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImageprofile.setImageBitmap(bitmap);
                        mImageprofile.getLayoutParams().width=250;
                        mImageprofile.getLayoutParams().height=250;

                        if (mCurrentSelectFile != null) {
                            mImageCropper.setOutPut(250, 250);
                            mImageCropper.setOutPutAspect(1, 1);
                            mImageCropper.cropImage(mCurrentSelectFile);
                        }

                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mImageFileSelector.onActivityResult(requestCode, resultCode, data);
        mImageCropper.onActivityResult(requestCode, resultCode, data);    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mImageFileSelector.onSaveInstanceState(outState);
        mImageCropper.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageFileSelector.onRestoreInstanceState(savedInstanceState);
        mImageCropper.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mImageFileSelector.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getBalance() {
        Helper.hideProgressDialog();

        String token = PreferenceUtils.getSinglePrefrenceString(ProfileActivity.this,
                R.string.settings_def_storeaccess_token_key);
        Call<WalletbalanceResponse> getBalace = Eztytopup.getsAPIService().getWalletbalance(token);
        getBalace.enqueue(new Callback<WalletbalanceResponse>() {
            @Override
            public void onResponse(Call<WalletbalanceResponse> call,
                                   Response<WalletbalanceResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_CREATED))) {

                    mSaldo.setText(Helper.formatCurrency(response.body().getBalance().toString()));
                } else {
                    Log.i(TAG, "onResponse: " + response.body().status.getMessage());
                }

                Helper.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<WalletbalanceResponse> call, Throwable t) {

                Helper.hideProgressDialog();
            }
        });
    }

    private void getBalanceReseller() {
        Helper.showProgressDialog(this, R.string.get_lastbalance);

        String token = PreferenceUtils.getSinglePrefrenceString(ProfileActivity.this,
                R.string.settings_def_storeaccess_token_key);
        Call<WalletbalanceResponse> getBalace =
                Eztytopup.getsAPIService().getWalletResellerbalance(token);
        getBalace.enqueue(new Callback<WalletbalanceResponse>() {
            @Override
            public void onResponse(Call<WalletbalanceResponse> call,
                                   Response<WalletbalanceResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_CREATED))) {

                    mSaldo.setText(Helper.formatCurrency(response.body().getBalance().toString()));

                } else if (response.body().status.code
                        .equals(String.valueOf(HttpURLConnection.HTTP_UNAUTHORIZED))) {

                    Toast.makeText(ProfileActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();

                    PreferenceUtils.destroyUserSession(ProfileActivity.this);
                    Login.start(ProfileActivity.this);

                } else {
                    Helper.snacbarError(response.body().status.getMessage(), container_view);
                }
                Helper.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<WalletbalanceResponse> call, Throwable t) {
                Helper.apiSnacbarError(ProfileActivity.this, t, container_view);
                Helper.hideProgressDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Helper.hideSoftKeyboard(container_view);
                finish();
                break;
            case R.id.fromCamera:
                initImageFileSelector();
                mImageFileSelector.takePhoto(this);
                break;
            case R.id.fromGallery:
                initImageFileSelector();
                mImageFileSelector.selectImage(this);
                break;
            case R.id.btnprofileCancel:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initImageFileSelector() {
        int w = 0;
        int h = 0;
        mImageFileSelector.setOutPutImageSize(w, h);
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    public void onClick(View v) {
        mImageprofile.destroyDrawingCache();

        switch (v.getId()){
            case R.id.btnprofileUpdate:
                String token = PreferenceUtils
                        .getSinglePrefrenceString(this, R.string.settings_def_storeaccess_token_key);
                String name = mName.getText().toString();
                String phone = mPhone.getText().toString();

                mImageprofile.setDrawingCacheEnabled(true);
                String image = Helper.convertStringImage(mImageprofile.getDrawingCache());

                updateProfile(token, name, phone, image);

                break;
            case R.id.btnprofileCancel:
                finish();
                break;
        }
    }

    private void updateProfile(String token, String name, String phone, String image) {
        Helper.showProgressDialog(this, R.string.updating_profile);

        Call<UpdateProfileResponse> setUpdateProfile = Eztytopup.getsAPIService()
                .setUpdateProfile(token, name, phone, null, image);
        setUpdateProfile.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call,
                                   Response<UpdateProfileResponse> response) {

                if (response.isSuccessful() && response.body()
                        .status.getCode().equals(String.valueOf(HttpURLConnection.HTTP_OK))) {

                    UpdateProfileResponse.User profileUpdate = response.body().user;

                    PreferenceUtils.setProfileUpdate(ProfileActivity.this,
                            profileUpdate.firstName,
                            profileUpdate.email,
                            profileUpdate.phoneNumber,
                            profileUpdate.avatar);

                    Toast.makeText(ProfileActivity.this, R.string.update_success,
                            Toast.LENGTH_SHORT).show();

                    btnprofileCancel.setText(R.string.done);

                } else if (response.isSuccessful() && response.body().status.code
                        .equals(String.valueOf(HttpURLConnection.HTTP_UNAUTHORIZED))) {

                    Toast.makeText(ProfileActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();

                    PreferenceUtils.destroyUserSession(ProfileActivity.this);
                    Login.start(ProfileActivity.this);

                }else {
                    Helper.snacbarError(response.body().status.getMessage(), container_view);
                }

                Helper.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                Helper.apiSnacbarError(ProfileActivity.this, t, container_view);
                Helper.hideProgressDialog();
            }
        });
    }
}
