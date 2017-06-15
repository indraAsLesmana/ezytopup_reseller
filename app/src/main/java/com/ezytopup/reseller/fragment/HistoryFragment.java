package com.ezytopup.reseller.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
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
import com.ezytopup.reseller.activity.BuyResellerActivity;
import com.ezytopup.reseller.activity.DeviceListActivity;
import com.ezytopup.reseller.adapter.Recyclerlist_HistoryAdapter;
import com.ezytopup.reseller.api.ServertimeResponse;
import com.ezytopup.reseller.api.TransactionHistoryResponse;
import com.ezytopup.reseller.printhelper.ThreadPoolManager;
import com.ezytopup.reseller.utility.Constant;
import com.ezytopup.reseller.utility.Helper;
import com.ezytopup.reseller.utility.PermissionHelper;
import com.ezytopup.reseller.utility.PreferenceUtils;
import com.zj.btsdk.PrintPic;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements
        Recyclerlist_HistoryAdapter.Recyclerlist_HistoryAdapterlistener,
        BuyResellerActivity.BuynowInterface {

    private ArrayList<TransactionHistoryResponse.Result> Allhistory;
    private Recyclerlist_HistoryAdapter adapter;
    private static final String TAG = "FavoriteFragment";
    private View rootView;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private String uid, token;
    private View view_nodatafound;
    private View view_tryReload;
    private Button try_reload;
    private View progressbar;
    private CardView cv_fragmentgenerallist;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Allhistory = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_generallist, container, false);

        try_reload = (Button) rootView.findViewById(R.id.btn_tryreload);
        view_tryReload = rootView.findViewById(R.id.view_reload);
        view_nodatafound = rootView.findViewById(R.id.view_nodatafound);
        progressbar = rootView.findViewById(R.id.progressbar);
        cv_fragmentgenerallist = (CardView) rootView.findViewById(R.id.cv_fragmentgenerallist);

        cv_fragmentgenerallist.setVisibility(View.INVISIBLE);
        RecyclerView recycler_view = (RecyclerView) rootView.findViewById(R.id.home_recylerview);
        recycler_view.setNestedScrollingEnabled(false);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        adapter = new Recyclerlist_HistoryAdapter(getContext(), Allhistory, HistoryFragment.this);
        recycler_view.setAdapter(adapter);

        uid = PreferenceUtils.getSinglePrefrenceString(getContext(),
                R.string.settings_def_uid_key);
        token = PreferenceUtils.getSinglePrefrenceString(getContext(),
                R.string.settings_def_storeaccess_token_key);

        if (!uid.equals(Constant.PREF_NULL) && !token.equals(Constant.PREF_NULL)){
            if (Eztytopup.getIsEnduser()) {
                getHistory(token, uid);
            } else {
                getHistoryreseller(token, uid);
            }
        }

        BuyResellerActivity.buynowListener(this);

        try_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Eztytopup.getIsEnduser()){
                    getHistory(token, uid);
                }else {
                    view_tryReload.setVisibility(View.GONE);
                    getHistoryreseller(token, uid);
                }
            }
        });

        return rootView;
    }

    private void getHistory(String token, String customerId) {
        Helper.showProgressDialog(getContext(), R.string.get_history);

        Call<TransactionHistoryResponse> history = Eztytopup.getsAPIService().getHistory(token,
                customerId);
        history.enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(Call<TransactionHistoryResponse> call,
                                   Response<TransactionHistoryResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){

                    if (response.body().result.size() == 0){
                        Allhistory.clear();
                        adapter.notifyDataSetChanged();
                        view_nodatafound.setVisibility(View.VISIBLE);
                    }else {
                        view_nodatafound.setVisibility(View.GONE);
                        Allhistory.clear();
                        Allhistory.addAll(response.body().result);
                        adapter.notifyDataSetChanged();
                    }

                    view_tryReload.setVisibility(View.GONE);
                }else {
                    Toast.makeText(getContext(), response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

                Helper.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {
                Helper.apiSnacbarError(getContext(), t, rootView);
                view_tryReload.setVisibility(View.VISIBLE);
                Helper.hideProgressDialog();
            }
        });
    }

    private void getHistoryreseller(String token, String customerId) {
        view_nodatafound.setVisibility(View.GONE);
        view_tryReload.setVisibility(View.GONE);
        cv_fragmentgenerallist.setVisibility(View.INVISIBLE);

        Allhistory.clear();
        adapter.notifyDataSetChanged();

        Helper.showProgress(true, getContext(), null, progressbar);

        Call<TransactionHistoryResponse> history =
                Eztytopup.getsAPIService().getHistoryReseller(token, customerId);

        history.enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(Call<TransactionHistoryResponse> call,
                                   Response<TransactionHistoryResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))) {

                    if (response.body().result.size() == 0) {

                        view_nodatafound.setVisibility(View.VISIBLE);
                    } else {
                        cv_fragmentgenerallist.setVisibility(View.VISIBLE);
                        view_nodatafound.setVisibility(View.GONE);
                        Allhistory.clear();
                        Allhistory.addAll(response.body().result);
                        adapter.notifyDataSetChanged();
                    }

                    view_tryReload.setVisibility(View.GONE);
                    Helper.showProgress(false, getContext(), null, progressbar);

                } else {
                    Toast.makeText(getContext(), response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {
                Helper.apiSnacbarError(getContext(), t, rootView);
                view_tryReload.setVisibility(View.VISIBLE);
                cv_fragmentgenerallist.setVisibility(View.INVISIBLE);
                Helper.showProgress(false, getContext(), null, progressbar);

            }
        });
    }


    @Override
    public void onReprintClick(TransactionHistoryResponse.Result historyItem) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getContext(), R.string.bluetooth_open,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), R.string.failed_open_bluetooth,
                            Toast.LENGTH_LONG).show();
                }
                break;
            case  REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    Eztytopup.setCon_dev(Eztytopup.getmBTprintService().getDevByMac(address));
                    Eztytopup.getmBTprintService().connect(Eztytopup.getCon_dev());
                }
                break;
        }
    }

    @Override
    public void buyNowClick() {
        if (Eztytopup.getIsEnduser()){
            getHistory(token, uid);
        }else {
            getHistoryreseller(token, uid);
        }
    }
}
