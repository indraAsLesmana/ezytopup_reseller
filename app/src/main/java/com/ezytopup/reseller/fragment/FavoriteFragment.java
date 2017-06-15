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
import com.ezytopup.reseller.activity.Login;
import com.ezytopup.reseller.adapter.RecyclerList_favoriteAdapter;
import com.ezytopup.reseller.api.BestSellerResponse;
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
public class FavoriteFragment extends Fragment implements
        RecyclerList_favoriteAdapter.RecyclerList_favoriteAdapterlistener{

    private ArrayList<BestSellerResponse.Product> AllFavoritedata;
    private RecyclerList_favoriteAdapter adapter;
    private static final String TAG = "FavoriteFragment";
    private View rootView;
    private View view_tryReload;
    private Button try_reload;
    private View progressbar;
    private CardView cv_fragmentgenerallist;


    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllFavoritedata = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_generallist, container, false);

        try_reload = (Button) rootView.findViewById(R.id.btn_tryreload);
        view_tryReload = rootView.findViewById(R.id.view_reload);
        progressbar = rootView.findViewById(R.id.progressbar);
        cv_fragmentgenerallist = (CardView) rootView.findViewById(R.id.cv_fragmentgenerallist);

        cv_fragmentgenerallist.setVisibility(View.INVISIBLE);
        RecyclerView recycler_view = (RecyclerView) rootView.findViewById(R.id.home_recylerview);
        recycler_view.setNestedScrollingEnabled(false);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerList_favoriteAdapter(getContext(), AllFavoritedata, FavoriteFragment.this);
        recycler_view.setAdapter(adapter);
        getFavProduct();

        try_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_tryReload.setVisibility(View.GONE);
                getFavProduct();
            }
        });

        return  rootView;
    }

    private void getFavProduct() {
        cv_fragmentgenerallist.setVisibility(View.INVISIBLE);
        view_tryReload.setVisibility(View.GONE);

        Helper.showProgress(true, getContext(), null, progressbar);

        Call<BestSellerResponse> call = Eztytopup.getsAPIService().getBestSeller();
        call.enqueue(new Callback<BestSellerResponse>() {
            @Override
            public void onResponse(Call<BestSellerResponse> call, Response<BestSellerResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                    AllFavoritedata.addAll(response.body().getProducts());
                    adapter.notifyDataSetChanged();

                    cv_fragmentgenerallist.setVisibility(View.VISIBLE);
                    view_tryReload.setVisibility(View.GONE);
                }else {
                    Toast.makeText(getContext(), response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
                Helper.showProgress(false, getContext(), null, progressbar);            }

            @Override
            public void onFailure(Call<BestSellerResponse> call, Throwable t) {
               Helper.apiSnacbarError(getContext(), t, rootView);
                view_tryReload.setVisibility(View.VISIBLE);
                cv_fragmentgenerallist.setVisibility(View.INVISIBLE);
                Helper.showProgress(false, getContext(), null, progressbar);
            }
        });
    }

    @Override
    public void onCardclick(BestSellerResponse.Product product) {
       if (Eztytopup.getIsEnduser()){
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

       }else {
           BuyResellerActivity.start(getActivity(),
                   product.getProductId(),
                   product.getProductName(),
                   product.getReviewUrl(),
                   product.getBackgroundImageUrl(),
                   product.getPrice());
       }
    }
}
