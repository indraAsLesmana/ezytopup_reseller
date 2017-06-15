package com.ezytopup.reseller.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.base.ActivityInterface;
import com.ezytopup.reseller.utility.Constant;
import com.ezytopup.reseller.utility.Helper;
import com.ezytopup.reseller.utility.PreferenceUtils;

import static com.ezytopup.reseller.utility.Helper.enableImmersiveMode;

/**
 * Created by indraaguslesmana on 3/31/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements ActivityInterface {

    private static final String TAG = "BaseActivity";
    ImageView toolbar_centerImage;
    Toolbar toolbar;
    ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        configureToolbar();
    }

    private void configureToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
                toolbar_centerImage = (ImageView) findViewById(R.id.toolbar_centered_logo);
                
                if (!PreferenceUtils.getSinglePrefrenceString(this,
                        R.string.settings_def_sellerlogo_key)
                        .equals(Constant.PREF_NULL)) {

                    toolbar_centerImage.setBackgroundResource(0);
                    Glide.with(this)
                            .load(PreferenceUtils.getSinglePrefrenceString(this,
                                    R.string.settings_def_sellerlogo_key))
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                    // Do something with bitmap here.
                                    BitmapDrawable bitmap1 = new
                                            BitmapDrawable(getResources(), bitmap);
                                    toolbar_centerImage.setImageDrawable(bitmap1);
                                }
                            });
                }

            }
        }

    }
}
