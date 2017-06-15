package com.ezytopup.reseller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezytopup.reseller.Eztytopup;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.api.TransactionHistoryResponse;
import com.ezytopup.reseller.utility.Constant;
import com.ezytopup.reseller.utility.PreferenceUtils;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/15/17.
 */

public class Recyclerlist_HistoryAdapter extends RecyclerView.Adapter
        <Recyclerlist_HistoryAdapter.SingleItemFavHolder> {

    private ArrayList<TransactionHistoryResponse.Result> itemList;
    private Context mContext;
    private Recyclerlist_HistoryAdapterlistener mListener;

    public Recyclerlist_HistoryAdapter(Context mContext,
                                       ArrayList<TransactionHistoryResponse.Result> itemList,
                                       Recyclerlist_HistoryAdapterlistener mListener) {
        this.itemList = itemList;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @Override
    public SingleItemFavHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_itemhistory, parent, false);
        return new SingleItemFavHolder(v);
    }

    @Override
    public void onBindViewHolder(final SingleItemFavHolder holder, int position) {
        final TransactionHistoryResponse.Result result = itemList.get(position);
        if (result != null) {
            holder.hist_deviderdate.setText(result.getCreatedDate());
            holder.hist_title.setText(result.getProductName());
            holder.hist_price.setText(result.getPrice());
            holder.hist_adminfee.setText(result.getServiceFee());
            holder.hist_total.setText(result.getTotal());
            holder.hist_payment.setText(result.getPaymentMethod());
            holder.hist_date.setText(result.getCreatedDate());
            holder.hist_delivery.setText(result.getStatusDelivery());
            holder.hist_deliverydate.setText(result.getDeliveryTime());
            holder.hist_email.setText(result.getEmail());
            if (result.getReviewUrl() != null) {
                Glide.with(mContext)
                        .load(result.getReviewUrl()).centerCrop()
                        .error(R.drawable.ic_error_loadimage)
                        .crossFade(Constant.ITEM_CROSSFADEDURATION)
                        .into(holder.hist_image);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (null != itemList) return itemList.size();
        else return 0;
    }

    public class SingleItemFavHolder extends RecyclerView.ViewHolder {
        private TextView hist_title, hist_price, hist_adminfee, hist_total,
                hist_payment, hist_date, hist_delivery, hist_deliverydate, hist_email,
                hist_deviderdate, reprint;
        private ImageView hist_image;

        public SingleItemFavHolder(View itemView) {
            super(itemView);
            hist_deviderdate = (TextView) itemView.findViewById(R.id.devider_date);
            hist_title = (TextView) itemView.findViewById(R.id.hist_title);
            hist_price = (TextView) itemView.findViewById(R.id.hist_price);
            hist_adminfee = (TextView) itemView.findViewById(R.id.hist_adminfee);
            hist_total = (TextView) itemView.findViewById(R.id.hist_total);
            hist_payment = (TextView) itemView.findViewById(R.id.hist_payment);
            hist_date = (TextView) itemView.findViewById(R.id.hist_date);
            hist_delivery = (TextView) itemView.findViewById(R.id.hist_status);
            hist_deliverydate = (TextView) itemView.findViewById(R.id.hist_datestaus);
            hist_email = (TextView) itemView.findViewById(R.id.hist_email);
            hist_image = (ImageView) itemView.findViewById(R.id.itemImage);
            reprint = (TextView) itemView.findViewById(R.id.reprint);
        }
    }
    public interface Recyclerlist_HistoryAdapterlistener{
        void onReprintClick(TransactionHistoryResponse.Result historyItem);
    }

}
