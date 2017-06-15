package com.ezytopup.reseller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.adapter.RecyclerList_ListCategoryAdapter;
import com.ezytopup.reseller.api.ListCategoryResponse;
import com.ezytopup.reseller.utility.Helper;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListCategoryActivity extends BaseActivity implements
        RecyclerList_ListCategoryAdapter.RecyclerList_ListCategoryAdapterlistener{

    private ArrayList<ListCategoryResponse.Category> allCategoryProduct;
    private RecyclerList_ListCategoryAdapter adapter;
    private static final String TAG = "HomeFragment";
    private ConstraintLayout container_layout;
    private View progressbar;
    private View view_tryReload;
    private Button try_reload;
    private View view_nodatafound;
    private CardView cv_generallist;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, ListCategoryActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        allCategoryProduct = new ArrayList<>();

        RecyclerView list_recyclerview = (RecyclerView)
                findViewById(R.id.activity_generalmainrecycler);
        container_layout = (ConstraintLayout)
                findViewById(R.id.container_layout);
        progressbar = findViewById(R.id.progressbar);
        view_nodatafound = findViewById(R.id.view_nodatafound);
        try_reload = (Button) findViewById(R.id.btn_tryreload);
        view_tryReload = findViewById(R.id.view_reload);
        cv_generallist = (CardView) findViewById(R.id.cv_generallist);

        list_recyclerview.setHasFixedSize(true);
        list_recyclerview.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerList_ListCategoryAdapter(ListCategoryActivity.this,
                allCategoryProduct, this);
        list_recyclerview.setAdapter(adapter);

        try_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlistCategory();
            }
        });

        getlistCategory();
    }

    private void getlistCategory() {
        cv_generallist.setVisibility(View.INVISIBLE);
        view_tryReload.setVisibility(View.GONE);

        Helper.showProgress(true, getApplicationContext(), null, progressbar);

        Call<ListCategoryResponse> listcategory = Eztytopup.getsAPIService().getListCategory();
        listcategory.enqueue(new Callback<ListCategoryResponse>() {
            @Override
            public void onResponse(Call<ListCategoryResponse> call,
                                   Response<ListCategoryResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))) {
                    allCategoryProduct.addAll(response.body()._0.categories);
                    adapter.notifyDataSetChanged();
                    if (allCategoryProduct.size() == 0) {
                        view_nodatafound.setVisibility(View.VISIBLE);
                    }else {
                        cv_generallist.setVisibility(View.VISIBLE);
                        view_tryReload.setVisibility(View.GONE);
                    }
                }else {
                    Toast.makeText(ListCategoryActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

                Helper.showProgress(false, getApplicationContext(), null, progressbar);
            }

            @Override
            public void onFailure(Call<ListCategoryResponse> call, Throwable t) {
                view_tryReload.setVisibility(View.VISIBLE);
                cv_generallist.setVisibility(View.INVISIBLE);
                Helper.apiSnacbarError(ListCategoryActivity.this, t, container_layout);
                Helper.showProgress(false, getApplicationContext(), null, progressbar);
            }
        });
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
    public void onCardClick(ListCategoryResponse.Category singleItem) {
        CategoryActivity.start(ListCategoryActivity.this, singleItem.getName(), singleItem.getId());
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_generallist;
    }
}
