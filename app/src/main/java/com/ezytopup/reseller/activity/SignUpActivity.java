package com.ezytopup.reseller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.ezytopup.reseller.R;

public class SignUpActivity extends BaseActivity {

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, SignUpActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

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
        return R.layout.activity_sign_up;
    }
}
