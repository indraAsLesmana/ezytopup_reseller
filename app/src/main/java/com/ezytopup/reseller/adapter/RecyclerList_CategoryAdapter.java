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
import com.ezytopup.reseller.api.CategoryResponse;
import com.ezytopup.reseller.utility.Constant;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/9/17.
 */

public class RecyclerList_CategoryAdapter extends RecyclerView.Adapter
        <RecyclerList_CategoryAdapter.ItemProductHolder> {

    private ArrayList<CategoryResponse.Product> itemList;
    private Context mContext;
    private static final String TAG = "RecyclerList_CategoryAdapter";
    private RecyclerList_CategoryAdapterlistener mListener;

    public RecyclerList_CategoryAdapter(Context mContext,
                                        ArrayList<CategoryResponse.Product> itemList,
                                        RecyclerList_CategoryAdapterlistener listener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.mListener = listener;
    }

    @Override
    public RecyclerList_CategoryAdapter.ItemProductHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_singlecard_favorite, parent, false);
        return new ItemProductHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerList_CategoryAdapter.
                                             ItemProductHolder holder, int position) {
        final CategoryResponse.Product singleItem = itemList.get(position);
        if (singleItem != null){
            holder.cat_title.setText(singleItem.getProductName());
            holder.cat_category_value.setText(singleItem.getCategoryName());
            holder.cat_price_value.setText(singleItem.getPrice());

            if (singleItem.getReviewUrl() != null){
                Glide.with(mContext)
                        .load(singleItem.getReviewUrl()).centerCrop()
                        .error(R.drawable.ic_error_loadimage)
                        .crossFade(Constant.ITEM_CROSSFADEDURATION)
                        .into(holder.cat_image);
            }
            holder.card_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCardClick(singleItem);
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

    public class ItemProductHolder extends RecyclerView.ViewHolder {
        private TextView cat_title, cat_category_value, cat_price_value;
        private ImageView cat_image;
        private ConstraintLayout card_container;
        private CardView card_view;

        public ItemProductHolder(View itemView) {
            super(itemView);
            cat_title = (TextView) itemView.findViewById(R.id.fav_itemtitle);
            cat_category_value = (TextView) itemView.findViewById(R.id.fav_category_value);
            cat_price_value = (TextView) itemView.findViewById(R.id.fav_price_value);
            cat_image = (ImageView) itemView.findViewById(R.id.fav_image);
            card_container = (ConstraintLayout) itemView.findViewById(R.id.fav_cardcontainer);
            card_view = (CardView) itemView.findViewById(R.id.cardview_listfavorite);
        }
    }
    public interface RecyclerList_CategoryAdapterlistener{
        void onCardClick(CategoryResponse.Product product);
    }
}
