package com.ezytopup.reseller.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.activity.BuyProductActivity;
import com.ezytopup.reseller.activity.BuyResellerActivity;
import com.ezytopup.reseller.activity.Login;
import com.ezytopup.reseller.adapter.RecyclerList_searchAdapter;
import com.ezytopup.reseller.api.BestSellerResponse;
import com.ezytopup.reseller.api.SearchResponse;
import com.ezytopup.reseller.utility.Helper;
import com.ezytopup.reseller.utility.PreferenceUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements
        RecyclerList_searchAdapter.RecyclerList_searchAdapterlistener {

    private ArrayList<BestSellerResponse.Product> AllSearchdata;
    private RecyclerList_searchAdapter adapter;
    private static final String TAG = "SearchFragment";
    private FrameLayout container_list;
    private View view_nodatafound;
    private View view_tryReload;
    private Button try_reload;
    private View progressbar;
    private CardView cv_fragmentgenerallist;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllSearchdata = new ArrayList<>();
//        getSearchProduct("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_generallist, container, false);

        try_reload = (Button) rootView.findViewById(R.id.btn_tryreload);
        view_tryReload = rootView.findViewById(R.id.view_reload);
        LinearLayout searchBar = (LinearLayout) rootView.findViewById(R.id.search_field);
        RecyclerView recycler_view = (RecyclerView) rootView.findViewById(R.id.home_recylerview);
        container_list = (FrameLayout) rootView.findViewById(R.id.container_list);
        view_nodatafound = rootView.findViewById(R.id.view_nodatafound);
        progressbar = rootView.findViewById(R.id.progressbar);
        cv_fragmentgenerallist = (CardView) rootView.findViewById(R.id.cv_fragmentgenerallist);

        cv_fragmentgenerallist.setVisibility(View.INVISIBLE);
        searchBar.setVisibility(View.VISIBLE);
        recycler_view.setNestedScrollingEnabled(false);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerList_searchAdapter(getContext(), AllSearchdata, this);

        recycler_view.setAdapter(adapter);
        final EditText seachBar = (EditText) rootView.findViewById(R.id.text_search);

        seachBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    getSearchProduct(seachBar.getText().toString());

                    Helper.hideSoftKeyboard(container_list);
                    return true;
                }
                return false;
            }
        });

        try_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_tryReload.setVisibility(View.GONE);
                getSearchProduct(seachBar.getText().toString());
            }
        });

        return rootView;
    }

    private void getSearchProduct(String productName) {
        cv_fragmentgenerallist.setVisibility(View.INVISIBLE);
        view_nodatafound.setVisibility(View.GONE);
        view_tryReload.setVisibility(View.GONE);

        AllSearchdata.clear();
        adapter.notifyDataSetChanged();

        Helper.showProgress(true, getContext(), null, progressbar);

        Call<SearchResponse> searchResult = Eztytopup.getsAPIService().getSearch(productName);
        searchResult.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()
                        && response.body().status.getCode()
                        .equals(String.valueOf(HttpURLConnection.HTTP_OK))) {

                    if (response.body().products.size() == 0) {

                        view_nodatafound.setVisibility(View.VISIBLE);
                    } else {
                        view_nodatafound.setVisibility(View.GONE);
                        cv_fragmentgenerallist.setVisibility(View.VISIBLE);
                        AllSearchdata.clear();
                        AllSearchdata.addAll(response.body().products);
                        adapter.notifyDataSetChanged();
                    }

                    view_tryReload.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

                Helper.showProgress(false, getContext(), null, progressbar);

            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Helper.apiSnacbarError(getContext(), t, container_list);
                view_tryReload.setVisibility(View.VISIBLE);
                cv_fragmentgenerallist.setVisibility(View.INVISIBLE);
                Helper.showProgress(false, getContext(), null, progressbar);
            }
        });
    }

    @Override
    public void onCardclick(BestSellerResponse.Product product) {
        if (Eztytopup.getIsEnduser()) {
            /**
             * is for end user, prepare for future if request.
             * */
            /*BuyProductActivity.start(getActivity(),
                    product.getProductId(),
                    product.getProductName(),
                    product.getReviewUrl(),
                    product.getBackgroundImageUrl(),
                    product.getPrice());*/

            Toast.makeText(getContext(), R.string.relogin_notasreseller, Toast.LENGTH_SHORT).show();
            PreferenceUtils.destroyUserSession(getContext());
            Login.start(getActivity());
        } else {
            BuyResellerActivity.start(getActivity(),
                    product.getProductId(),
                    product.getProductName(),
                    product.getReviewUrl(),
                    product.getBackgroundImageUrl(),
                    product.getPrice());
        }
    }
}
