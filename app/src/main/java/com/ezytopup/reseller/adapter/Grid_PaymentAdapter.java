package com.ezytopup.reseller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.api.PaymentResponse;
import com.ezytopup.reseller.utility.Constant;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/17/17.
 */

public class Grid_PaymentAdapter extends ArrayAdapter<PaymentResponse.PaymentMethod>{

    private Context mContext;
    private ArrayList<PaymentResponse.PaymentMethod> mGridData;
    private Grid_PaymentAdapterListener mListener;

    public Grid_PaymentAdapter(Context mContext, ArrayList<PaymentResponse.PaymentMethod> mGridData,
                               Grid_PaymentAdapterListener listener) {
        super(mContext, 0, mGridData);
        this.mContext = mContext;
        this.mGridData = mGridData;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final PaymentResponse.PaymentMethod bankItem = mGridData.get(position);
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.grid_itemcard, null);
        }
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.grid_item_image);
        final TextView textView = (TextView) convertView.findViewById(R.id.grid_item_title);
        final LinearLayout paymentOption = (LinearLayout) convertView.findViewById(R.id.container_paymentoption);
        if (bankItem != null){
            textView.setText(bankItem.getPaymentMethod());
            if (mGridData.get(position).getPaymentLogo() != null){
                Glide.with(mContext)
                        .load(bankItem.getPaymentLogo())
                        .error(R.drawable.ic_error_loadimage)
                        .crossFade(Constant.ITEM_CROSSFADEDURATION)
                        .into(imageView);
            }
        }
        paymentOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCardClick(bankItem);
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return mGridData.size();
    }

    public interface Grid_PaymentAdapterListener{
        void onCardClick(PaymentResponse.PaymentMethod optionPaymentItem);
    }
}
