package com.remindme.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.remindme.R;
import com.remindme.models.PendingList;
import com.remindme.utils.Constant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PendingListAdapter extends RecyclerView.Adapter<PendingListAdapter.Holder> {

    private Context mContext;
    private ArrayList<PendingList> mDataSet;
    private int selectedItem;
    private boolean mClick = false;

    public PendingListAdapter(Context context, ArrayList<PendingList> pendingLists) {
        mContext = context;
        mDataSet = pendingLists;
        selectedItem = 0;
    }

    public void addAll(final ArrayList<PendingList> dataSet) {
        if (dataSet == null) return;
        if (dataSet.size() == 0) return;

        mDataSet.clear();
        notifyDataSetChanged();

        mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void addItem(PendingList item) {
        mDataSet.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mDataSet != null) {
            mDataSet.clear();
            notifyDataSetChanged();
        }
    }

    public  ArrayList<PendingList> getDataSet(){
        return mDataSet;
    }

    public void remove(int index) {
        mDataSet.remove(index);
        notifyDataSetChanged();
    }

    public void updateItem(int position, PendingList item) {
        mDataSet.set(position, item);
        notifyItemChanged(position);
    }

    @Override
    public PendingListAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pending_list, parent, false);
        return new PendingListAdapter.Holder(rowItem);
    }

    @Override
    public void onBindViewHolder(PendingListAdapter.Holder holder, int i) {
        PendingList reminders = mDataSet.get(i);
        holder.txtCategory.setText(reminders.getCategory());
        holder.txtProduct.setText(reminders.getName());
        holder.txtRemark.setText(reminders.getRemarks());
        holder.txtDateTime.setText(reminders.getFrom());

    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 2 : mDataSet.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_category)
        TextView txtCategory;
        @BindView(R.id.txt_product)
        TextView txtProduct;
        @BindView(R.id.txt_remark)
        TextView txtRemark;
        @BindView(R.id.txt_date_time)
        TextView txtDateTime;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
