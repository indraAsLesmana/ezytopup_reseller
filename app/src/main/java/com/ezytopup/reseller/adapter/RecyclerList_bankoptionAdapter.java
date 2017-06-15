package com.ezytopup.reseller.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezytopup.reseller.R;
import com.ezytopup.reseller.api.PaymentResponse;
import com.ezytopup.reseller.utility.Constant;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/26/17.
 */

public class RecyclerList_bankoptionAdapter extends RecyclerView.Adapter<RecyclerList_bankoptionAdapter.Cardbank> {

    private Context mContext;
    private ArrayList<PaymentResponse.PaymentMethod> itemList;
    private RecyclerList_bankoptionAdapter.RecyclerList_bankoptionListener mListener;

    public RecyclerList_bankoptionAdapter(Context mContext, ArrayList<PaymentResponse.PaymentMethod> itemList,
                                          RecyclerList_bankoptionListener mListener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.mListener = mListener;
    }

    @Override
    public Cardbank onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_singlecard_bankoption, parent, false);
        return new RecyclerList_bankoptionAdapter.Cardbank(v);
    }

    @Override
    public void onBindViewHolder(final Cardbank holder, int position) {
        final PaymentResponse.PaymentMethod bankItem = itemList.get(position);
        if (bankItem == null) return;
        if (bankItem.getPaymentLogo() != null) {
            Glide.with(mContext)
                    .load(bankItem.getPaymentLogo())
                    .error(R.drawable.ic_error_loadimage)
                    .crossFade(Constant.ITEM_CROSSFADEDURATION)
                    .into(holder.itemImage);
        }
        holder.itemTitle.setText(bankItem.getPaymentMethod());
        holder.container_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCardClick(bankItem);
            }
        });
    }

    public static int getBackgroundColor(View view) {
        Drawable drawable = view.getBackground();
        if (drawable instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            if (Build.VERSION.SDK_INT >= 11) {
                return colorDrawable.getColor();
            }
            try {
                Field field = colorDrawable.getClass().getDeclaredField("mState");
                field.setAccessible(true);
                Object object = field.get(colorDrawable);
                field = object.getClass().getDeclaredField("mUseColor");
                field.setAccessible(true);
                return field.getInt(object);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        if (null != itemList) return itemList.size();
        else return 0;
    }


    class Cardbank extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemTitle;
        private RelativeLayout container_card;

        public Cardbank(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.bank_icon);
            itemTitle = (TextView) itemView.findViewById(R.id.bank_title);
            container_card = (RelativeLayout) itemView.findViewById(R.id.containter_bankoption);
        }
    }

    public interface RecyclerList_bankoptionListener {
        void onCardClick(PaymentResponse.PaymentMethod bankItem);
    }
}
