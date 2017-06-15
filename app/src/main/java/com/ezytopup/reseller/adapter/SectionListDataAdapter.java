package com.ezytopup.reseller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.api.ProductResponse;
import com.ezytopup.reseller.utility.Constant;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/3/17.
 */

public class SectionListDataAdapter extends RecyclerView.Adapter
        <SectionListDataAdapter.SingleItemHolder> {

    private ArrayList<ProductResponse.Product> itemsList;
    private Context mContext;
    private static final String TAG = "SectionListDataAdapter";
    private SectionListDataAdapterListener mListener;

    public SectionListDataAdapter(Context mContext, ArrayList<ProductResponse.Product> itemsList,
                                  SectionListDataAdapterListener listener) {
        this.itemsList = itemsList;
        this.mContext = mContext;
        this.mListener = listener;
    }

    @Override
    public SingleItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_singlecard_home, parent, false);
        return new SingleItemHolder(v);
    }

    @Override
    public void onBindViewHolder(SingleItemHolder holder, int position) {
        final ProductResponse.Product singleItem = itemsList.get(position);

        holder.itemTitle.setText(singleItem.getProductName());
        holder.itemPrice.setText(singleItem.getHargaToko());

        if (singleItem.getImageUrl() != null){
            Glide.with(mContext)
                    .load(singleItem.getImageUrl()).centerCrop()
                    .error(R.drawable.ic_error_loadimage)
                    .crossFade(Constant.ITEM_CROSSFADEDURATION)
                    .into(holder.itemImage);
        }
        holder.container_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCardClick(singleItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null != itemsList) return itemsList.size();
        else return 0;
    }

    class SingleItemHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemTitle, itemPrice;
        private LinearLayout container_card;

        public SingleItemHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemPrice = (TextView) itemView.findViewById(R.id.item_price);
            container_card = (LinearLayout) itemView.findViewById(R.id.cn_cardview);
        }
    }
    public interface SectionListDataAdapterListener{
        void onCardClick(ProductResponse.Product itemProduct);
    }

}
