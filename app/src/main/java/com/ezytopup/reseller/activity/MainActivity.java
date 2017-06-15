package com.ezytopup.reseller.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.adapter.CustomViewPager;
import com.ezytopup.reseller.adapter.RegisterFragment_Adapter;
import com.ezytopup.reseller.api.HeaderimageResponse;
import com.ezytopup.reseller.api.TutorialResponse;
import com.ezytopup.reseller.utility.Constant;
import com.ezytopup.reseller.utility.Helper;
import com.ezytopup.reseller.utility.PermissionHelper;
import com.ezytopup.reseller.utility.PreferenceUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static TextView nav_user_name, nav_user_email;
    public static RoundedImageView nav_image_view;

    private static final String TAG = "MainActivity";
    private ArrayList<TutorialResponse.Result> tutorialImage;
    private DrawerLayout drawer;
    private File file = new File(Constant.DEF_PATH_IMAGEPRINT);

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, MainActivity.class);
        caller.startActivity(intent);
        caller.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setElevation(0);
        actionBar.setDisplayShowTitleEnabled(false);
        tutorialImage = new ArrayList<>();

        if (PreferenceUtils.getSinglePrefrenceString(MainActivity.this,
                R.string.settings_def_storeaccess_token_key).equals(Constant.PREF_NULL)
                || PreferenceUtils.getSinglePrefrenceString(MainActivity.this,
                R.string.settings_def_storeemail_key).equals(Constant.PREF_NULL)) {
            Helper.synchronizeFCMRegToken(this, null);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navView = navigationView.getHeaderView(0);
        nav_user_name = (TextView) navView.findViewById(R.id.nav_user_name);
        nav_user_email = (TextView) navView.findViewById(R.id.nav_user_email);
        nav_image_view = (RoundedImageView) navView.findViewById(R.id.nav_image_view);

        String checkEmail = PreferenceUtils.
                getSinglePrefrenceString(MainActivity.this, R.string.settings_def_storeemail_key);
        if (checkEmail.startsWith("autoemail") && checkEmail.endsWith("@mail.com")
                || checkEmail.equals(Constant.PREF_NULL)) {
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_signup).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_changepassword).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);

            nav_user_name.setText(R.string.welcome_guest);
            nav_user_email.setText(R.string.your_email_here);
        } else {
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_signup).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_changepassword).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);

        }

        if (!file.exists()
                && !PreferenceUtils.getSinglePrefrenceString(this,
                        R.string.settings_def_sellerprintlogo_key).equals(Constant.PREF_NULL)
                && !Eztytopup.getIsEnduser()
                && PermissionHelper.isPermissionGranted(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE})) {

            Helper.downloadFile(this, PreferenceUtils.getSinglePrefrenceString(this,
                    R.string.settings_def_sellerprintlogo_key));
        }

        set_navdrawer();
        initTabMenu();

        Helper.log(TAG, "token: " + PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_storeaccess_token_key), null);
        Helper.log(TAG, "setDeviceId: " + PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_storeidevice_key), null);
        Helper.log(TAG, "sellerId: " + PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_sellerid_key), null);
        Helper.log(TAG, "shopName: " + PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_sellershopname_key), null);
        Helper.log(TAG, "customerId: " + PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_uid_key), null);
        Helper.log(TAG, "kasirName: " + PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_sellerkasirname_key), null);
        Helper.log(TAG, "sellerLogo: " + PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_sellerlogo_key), null);

    }

    private void set_navdrawer() {
        String firstName = PreferenceUtils.getSinglePrefrenceString
                (this, R.string.settings_def_storefirst_name_key);
        String lastName = PreferenceUtils.getSinglePrefrenceString
                (this, R.string.settings_def_storelast_name_key);
        String userMail = PreferenceUtils.getSinglePrefrenceString
                (this, R.string.settings_def_storeemail_key);
        String imageUrl = PreferenceUtils.getSinglePrefrenceString
                (this, R.string.settings_def_storeimage_user_key);

        nav_user_name.setText(firstName + " " + lastName);
        nav_user_email.setText(userMail);

        if (!imageUrl.equals(Constant.PREF_NULL)) {
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .error(R.drawable.ic_user_image_null)
                    .into(nav_image_view);
        }
    }

    private void initTabMenu() {
        final CustomViewPager mMain_Pagger = (CustomViewPager) findViewById(R.id.main_pagger);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        RegisterFragment_Adapter adapter = new RegisterFragment_Adapter(
                getSupportFragmentManager(), this);

        mMain_Pagger.setOffscreenPageLimit(4);
        mMain_Pagger.setAdapter(adapter);
        tabLayout.setupWithViewPager(mMain_Pagger);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_category) {
            ListCategoryActivity.start(MainActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_guide:

                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_tutorial);

                final SliderLayout slider_tutorial = (SliderLayout)
                        dialog.findViewById(R.id.slider_tutorial);

                if (tutorialImage.isEmpty()) {
                    Call<TutorialResponse> tutorial = Eztytopup.getsAPIService().getTutorial();
                    tutorial.enqueue(new Callback<TutorialResponse>() {
                        @Override
                        public void onResponse(Call<TutorialResponse> call,
                                               Response<TutorialResponse> response) {
                            if (response.isSuccessful()) {
                                tutorialImage.addAll(response.body().result);

                                initSlider(slider_tutorial, tutorialImage);
                            }
                        }

                        @Override
                        public void onFailure(Call<TutorialResponse> call, Throwable t) {
                            Helper.apiSnacbarError(MainActivity.this, t, drawer);
                            dialog.dismiss();
                        }
                    });
                } else {
                    initSlider(slider_tutorial, tutorialImage);
                }

                slider_tutorial.setDuration(Constant.TUTORIAL_DURATION);
                slider_tutorial.setPresetTransformer(SliderLayout.Transformer.Default);
                TextView tvClose = (TextView) dialog.findViewById(R.id.tvClose);
                tvClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;
            case R.id.nav_faq:
                FaqActivity.start(MainActivity.this);
                break;

            case R.id.nav_contactus:
                ContactUsActivity.start(MainActivity.this);
                break;

            case R.id.nav_tutorial:
                TutorialActivity.start(MainActivity.this);
                break;

            case R.id.nav_term:
                TermActivity.start(MainActivity.this);
                break;
            case R.id.nav_login:
                Login.start(MainActivity.this);

                break;
            case R.id.nav_logout:
                PreferenceUtils.destroyUserSession(MainActivity.this);
                Login.start(MainActivity.this);

                break;
            case R.id.nav_changepassword:
                ChangePasswordActivity.start(MainActivity.this);

                break;
            case R.id.nav_profile:
                ProfileActivity.start(MainActivity.this);

                break;
            case R.id.nav_print:
                PrintDemo.start(MainActivity.this);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    private TextSliderView initSlider(SliderLayout slider,
                                      ArrayList<TutorialResponse.Result> result) {
        TextSliderView textSliderView = null;
        for (int i = 0; i < result.size(); i++) {
            textSliderView = new TextSliderView(MainActivity.this);
            String description = result.get(i).judul;
            String image = result.get(i).image;

            textSliderView
                    .description(description)
                    .image(image)
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", description);

            slider.addSlider(textSliderView);

        }
        return textSliderView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        set_navdrawer();
    }

}
