package com.ezytopup.reseller.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.api.BestSellerResponse;
import com.ezytopup.reseller.utility.Constant;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/5/17.
 */

public class RecyclerList_favoriteAdapter extends RecyclerView.Adapter
        <RecyclerList_favoriteAdapter.SingleItemFavHolder> {

    private ArrayList<BestSellerResponse.Product> itemList;
    private Context mContext;
    private RecyclerList_favoriteAdapterlistener mListener;

    public RecyclerList_favoriteAdapter(Context mContext, ArrayList<BestSellerResponse.Product> itemList,
                                        RecyclerList_favoriteAdapterlistener listener) {
        this.itemList = itemList;
        this.mContext = mContext;
        this.mListener = listener;
    }

    @Override
    public SingleItemFavHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_singlecard_favorite, parent, false);
        return new SingleItemFavHolder(v);
    }

    @Override
    public void onBindViewHolder(SingleItemFavHolder holder, int position) {
        final BestSellerResponse.Product singleItem = itemList.get(position);
        if (singleItem != null){
            holder.fav_title.setText(singleItem.getProductName());
            holder.fav_category_value.setText(singleItem.getCategoryName());
            holder.fav_price_value.setText(singleItem.getPrice());

            if (singleItem.getReviewUrl() != null){
                Glide.with(mContext)
                        .load(singleItem.getReviewUrl()).centerCrop()
                        .error(R.drawable.ic_error_loadimage)
                        .crossFade(Constant.ITEM_CROSSFADEDURATION)
                        .into(holder.fav_image);
            }
            holder.card_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCardclick(singleItem);
                }
            });

            if (position == itemList.size() - 1) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                        holder.card_view.getLayoutParams();
                params.bottomMargin = 0;
                holder.card_view.setLayoutParams(params);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (null != itemList) return itemList.size();
        else return 0;
    }

    public class SingleItemFavHolder extends RecyclerView.ViewHolder {
        private TextView fav_title, fav_category_value, fav_price_value;
        private ImageView fav_image;
        private ConstraintLayout card_container;
        private CardView card_view;

        public SingleItemFavHolder(View itemView) {
            super(itemView);
            fav_title = (TextView) itemView.findViewById(R.id.fav_itemtitle);
            fav_category_value = (TextView) itemView.findViewById(R.id.fav_category_value);
            fav_price_value = (TextView) itemView.findViewById(R.id.fav_price_value);
            fav_image = (ImageView) itemView.findViewById(R.id.fav_image);
            card_container = (ConstraintLayout) itemView.findViewById(R.id.fav_cardcontainer);
            card_view = (CardView) itemView.findViewById(R.id.cardview_listfavorite);

        }
    }

    public interface RecyclerList_favoriteAdapterlistener{
        void onCardclick(BestSellerResponse.Product product);
    }
}
