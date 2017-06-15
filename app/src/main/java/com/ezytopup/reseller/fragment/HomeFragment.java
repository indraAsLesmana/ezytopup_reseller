package com.ezytopup.reseller.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.activity.BuyProductActivity;
import com.ezytopup.reseller.activity.BuyResellerActivity;
import com.ezytopup.reseller.activity.CategoryActivity;
import com.ezytopup.reseller.activity.Login;
import com.ezytopup.reseller.activity.ProfileActivity;
import com.ezytopup.reseller.adapter.RecyclerList_homeAdapter;
import com.ezytopup.reseller.adapter.SectionListDataAdapter;
import com.ezytopup.reseller.api.ProductResponse;
import com.ezytopup.reseller.utility.Helper;
import com.ezytopup.reseller.utility.PreferenceUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class HomeFragment extends Fragment implements
        RecyclerList_homeAdapter.RecyclerList_homeAdapterListener,
        SectionListDataAdapter.SectionListDataAdapterListener {

    private ArrayList<ProductResponse.Result> allProductdata;
    private RecyclerView my_recycler_view;
    private RecyclerList_homeAdapter adapter;
    private static final String TAG = "HomeFragment";
    private View rootView;
    private View view_tryReload;
    private Button try_reload;
    private View progressbar;
    private CardView cv_fragmentgenerallist;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allProductdata = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_generallist, container, false);

        try_reload = (Button) rootView.findViewById(R.id.btn_tryreload);
        view_tryReload = rootView.findViewById(R.id.view_reload);
        progressbar = rootView.findViewById(R.id.progressbar);
        cv_fragmentgenerallist = (CardView) rootView.findViewById(R.id.cv_fragmentgenerallist);

        cv_fragmentgenerallist.setVisibility(View.INVISIBLE);
        my_recycler_view = (RecyclerView) rootView.findViewById(R.id.home_recylerview);
        my_recycler_view.setNestedScrollingEnabled(false);
        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerList_homeAdapter(getContext(), allProductdata, this, this);
        my_recycler_view.setAdapter(adapter);
        getProduct();
        try_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_tryReload.setVisibility(View.GONE);
                getProduct();
            }
        });

        return rootView;
    }

    private void getProduct() {
        cv_fragmentgenerallist.setVisibility(View.INVISIBLE);
        view_tryReload.setVisibility(View.GONE);

        Helper.showProgress(true, getContext(), null, progressbar);

        Call<ProductResponse> call = Eztytopup.getsAPIService().getProduct();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call,
                                   Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    allProductdata.addAll(response.body().getResult());
                    adapter.notifyDataSetChanged();
                    cv_fragmentgenerallist.setVisibility(View.VISIBLE);
                    view_tryReload.setVisibility(View.GONE);
                }

                Helper.showProgress(false, getContext(), null, progressbar);

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Helper.apiSnacbarError(getContext(), t, rootView);
                view_tryReload.setVisibility(View.VISIBLE);
                cv_fragmentgenerallist.setVisibility(View.INVISIBLE);
                Helper.showProgress(false, getContext(), null, progressbar);

            }
        });
    }

    @Override
    public void onMoreClick(String categoryName, String categoryId) {
        CategoryActivity.start(getActivity(), categoryName, categoryId);
    }

    @Override
    public void onCardClick(ProductResponse.Product itemProduct) {
        if (Eztytopup.getIsEnduser()) {
           /**
            * is for end user, prepare for future if request.
            * */
            /* BuyProductActivity.start(getActivity(),
                    itemProduct.getProductId(),
                    itemProduct.getProductName(),
                    itemProduct.getImageUrl(),
                    itemProduct.getBackgroundImageUrl(),
                    itemProduct.getHargaToko());*/

            Toast.makeText(getContext(), R.string.relogin_notasreseller, Toast.LENGTH_SHORT).show();
            PreferenceUtils.destroyUserSession(getContext());
            Login.start(getActivity());

        } else {
            BuyResellerActivity.start(getActivity(),
                    itemProduct.getProductId(),
                    itemProduct.getProductName(),
                    itemProduct.getImageUrl(),
                    itemProduct.getBackgroundImageUrl(),
                    itemProduct.getHargaToko());
        }
    }
}
