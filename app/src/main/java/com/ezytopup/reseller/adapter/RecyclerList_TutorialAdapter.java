package com.ezytopup.reseller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezytopup.reseller.R;
import com.ezytopup.reseller.api.TutorialStepResponse;

import java.util.ArrayList;

/**
 * Created by luteh on 17/05/17.
 */

public class RecyclerList_TutorialAdapter extends RecyclerView.Adapter
        <RecyclerList_TutorialAdapter.TutorialHolder> {

    private ArrayList<TutorialStepResponse.Result> dataList;
    private Context mContext;
    private static final String TAG = "RecyclerList_TutorialAdapter";

    public RecyclerList_TutorialAdapter(Context mContext, ArrayList<TutorialStepResponse.Result> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }


    @Override
    public TutorialHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tutorial, parent, false);
        return new TutorialHolder(v);
    }

    @Override
    public void onBindViewHolder(TutorialHolder holder, int position) {
        holder.tutorialJudul.setVisibility(View.VISIBLE);

        holder.tutorialJudul.setText(dataList.get(position).judul);
        holder.tutorialIsi.setText(dataList.get(position).isi);
    }

    @Override
    public int getItemCount() {
        if (null != dataList) return dataList.size();
        else return 0;    }

    class TutorialHolder extends RecyclerView.ViewHolder {
        private TextView tutorialJudul;
        private TextView tutorialIsi;

        TutorialHolder(View itemView) {
            super(itemView);
            tutorialJudul = (TextView) itemView.findViewById(R.id.tutorial_title);
            tutorialIsi = (TextView) itemView.findViewById(R.id.tutorial_answer);
        }
    }
}
