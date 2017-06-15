package com.ezytopup.reseller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezytopup.reseller.R;
import com.ezytopup.reseller.api.TermResponse;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/8/17.
 */

public class RecyclerList_TermAdapter extends RecyclerView.Adapter
        <RecyclerList_TermAdapter.QuestionHolder> {

    private ArrayList<TermResponse.Result> dataList;
    private Context mContext;
    private static final String TAG = "RecyclerList_FaqAdapter";

    public RecyclerList_TermAdapter(Context mContext, ArrayList<TermResponse.Result> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_faq, parent, false);
        return new QuestionHolder(v);
    }

    @Override
    public void onBindViewHolder(QuestionHolder holder, int position) {
        if (dataList != null){
            holder.quetionAnswer.setText(dataList.get(position).term);
        }
    }

    @Override
    public int getItemCount() {
        if (null != dataList) return dataList.size();
        else return 0;
    }

    class QuestionHolder extends RecyclerView.ViewHolder {
        private TextView quetionTitle;
        private TextView quetionAnswer;

        QuestionHolder(View itemView) {
            super(itemView);
            quetionTitle = (TextView) itemView.findViewById(R.id.faq_titlequestion);
            quetionAnswer = (TextView) itemView.findViewById(R.id.faq_answer);
        }
    }
}
