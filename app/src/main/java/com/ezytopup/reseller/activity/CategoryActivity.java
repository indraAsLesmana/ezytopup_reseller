package com.ezytopup.reseller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.adapter.RecyclerList_CategoryAdapter;
import com.ezytopup.reseller.api.CategoryResponse;
import com.ezytopup.reseller.utility.Helper;
import com.ezytopup.reseller.utility.PreferenceUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryActivity extends BaseActivity implements
        RecyclerList_CategoryAdapter.RecyclerList_CategoryAdapterlistener {

    private static final String CATEGORY_NAME = "CategoryActivity::productName";
    private static final String CATEGORY_ID = "CategoryActivity::productId";
    private RecyclerList_CategoryAdapter adapter;
    private ArrayList<CategoryResponse.Product> results;
    private static final String TAG = "CategoryActivity";
    private String mCategoryId;
    private View view_nodatafound;
    private ConstraintLayout container_layout;
    private View view_tryReload;
    private Button try_reload;
    private View progressbar;
    private CardView cv_generallist;

    public static void start(Activity caller, String categoryName, String categoryId) {
        Intent intent = new Intent(caller, CategoryActivity.class);
        intent.putExtra(CATEGORY_NAME, categoryName);
        intent.putExtra(CATEGORY_ID, categoryId);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra(CategoryActivity.CATEGORY_ID) == null) {
            Toast.makeText(this, "Caetgory id is null", Toast.LENGTH_SHORT).show();
            return;
        }
        results = new ArrayList<>();

        String mCategoryName = getIntent().getStringExtra(CategoryActivity.CATEGORY_NAME);
        mCategoryId = getIntent().getStringExtra(CategoryActivity.CATEGORY_ID);
        TextView categoryTitle = (TextView) findViewById(R.id.faq_titlequetion);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_generalmainrecycler);
        view_nodatafound = findViewById(R.id.view_nodatafound);
        container_layout = (ConstraintLayout) findViewById(R.id.container_layout);
        try_reload = (Button) findViewById(R.id.btn_tryreload);
        view_tryReload = findViewById(R.id.view_reload);
        progressbar = findViewById(R.id.progressbar);
        cv_generallist = (CardView) findViewById(R.id.cv_generallist);

        categoryTitle.setText(String.format("%s: %s", getString(R.string.category), mCategoryName));
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerList_CategoryAdapter(CategoryActivity.this, results, this);
        recyclerView.setAdapter(adapter);

        if (results.isEmpty() || results.size() == 0) getCategory();

        try_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategory();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getCategory() {
        cv_generallist.setVisibility(View.INVISIBLE);
        view_tryReload.setVisibility(View.GONE);

        Helper.showProgress(true, getApplicationContext(), null, progressbar);

        Call<CategoryResponse> category = Eztytopup.getsAPIService().getCategory(mCategoryId);
        category.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))) {
                    results.addAll(response.body().products);
                    adapter.notifyDataSetChanged();
                    if (results.size() == 0) {
                        view_nodatafound.setVisibility(View.VISIBLE);
                    } else {
                        cv_generallist.setVisibility(View.VISIBLE);
                        view_tryReload.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(CategoryActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
                Helper.showProgress(false, getApplicationContext(), null, progressbar);

            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                view_tryReload.setVisibility(View.VISIBLE);
                cv_generallist.setVisibility(View.INVISIBLE);
                Helper.apiSnacbarError(CategoryActivity.this, t, container_layout);
                Helper.showProgress(false, getApplicationContext(), null, progressbar);

            }
        });
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_generallist;
    }

    @Override
    public void onCardClick(CategoryResponse.Product product) {
        if (Eztytopup.getIsEnduser()) {
            /**
             * is for end user, prepare for future if request.
             * */
            /* BuyProductActivity.start(this,
                    product.getProductId(),
                    product.getProductName(),
                    product.getReviewUrl(),
                    product.getBackgroundImageUrl(),
                    product.getPrice());*/

            Toast.makeText(this, R.string.relogin_notasreseller, Toast.LENGTH_SHORT).show();
            PreferenceUtils.destroyUserSession(this);
            Login.start(this);
        } else {
            BuyResellerActivity.start(this,
                    product.getProductId(),
                    product.getProductName(),
                    product.getReviewUrl(),
                    product.getBackgroundImageUrl(),
                    product.getPrice());
        }
    }
}
