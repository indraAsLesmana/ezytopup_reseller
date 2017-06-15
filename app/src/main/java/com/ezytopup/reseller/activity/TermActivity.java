package com.ezytopup.reseller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.adapter.RecyclerList_TermAdapter;
import com.ezytopup.reseller.api.TermResponse;
import com.ezytopup.reseller.utility.Helper;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermActivity extends BaseActivity {

    private RecyclerList_TermAdapter adapter;
    private ArrayList<TermResponse.Result> results;
    private LinearLayout loadingBar;
    private RecyclerView recyclerView;
    private ConstraintLayout container_layout;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, TermActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        loadingBar = (LinearLayout) findViewById(R.id.loadingBar);
        TextView titleText = (TextView) findViewById(R.id.tutorial_titlequetion);
        titleText.setVisibility(View.VISIBLE);
        titleText.setText(R.string.term_and_condition);
        container_layout = (ConstraintLayout) findViewById(R.id.container_tutorial_layout);

        results = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.activity_tutorialrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerList_TermAdapter(TermActivity.this, results);
        recyclerView.setAdapter(adapter);

        if (results.isEmpty() || results.size() == 0) getTerm();
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

    private void getTerm() {
        loadingBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        Call<TermResponse> term = Eztytopup.getsAPIService().getTerm();
        term.enqueue(new Callback<TermResponse>() {
            @Override
            public void onResponse(Call<TermResponse> call, Response<TermResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                    results.addAll(response.body().result);
                    adapter.notifyDataSetChanged();

                    loadingBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(TermActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();

                    loadingBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TermResponse> call, Throwable t) {
                Helper.apiSnacbarError(TermActivity.this, t, container_layout);
                loadingBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_tutorial;
    }
}
