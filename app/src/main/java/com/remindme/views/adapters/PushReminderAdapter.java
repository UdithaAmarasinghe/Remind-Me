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
import com.remindme.models.PushReminderList;
import com.remindme.utils.Constant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PushReminderAdapter extends RecyclerView.Adapter<PushReminderAdapter.Holder> {

    private Context mContext;
    private ArrayList<PushReminderList> mDataSet;
    private int selectedItem;
    private String mType;
    private boolean mClick = false;
    private PushReminderAdapter.PushReminderAdapterCallback mCallback;

    public PushReminderAdapter(Context context, ArrayList<PushReminderList> pendingLists, String type, PushReminderAdapter.PushReminderAdapterCallback callback) {
        mContext = context;
        mDataSet = pendingLists;
        mType = type;
        mCallback = callback;
        selectedItem = 0;
    }

    public void addAll(final ArrayList<PushReminderList> dataSet) {
        if (dataSet == null) return;
        if (dataSet.size() == 0) return;

        mDataSet.clear();
        notifyDataSetChanged();

        mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void addItem(PushReminderList item) {
        mDataSet.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mDataSet != null) {
            mDataSet.clear();
            notifyDataSetChanged();
        }
    }

    public  ArrayList<PushReminderList> getDataSet(){
        return mDataSet;
    }

    public void remove(int index) {
        mDataSet.remove(index);
        notifyDataSetChanged();
    }

    public void updateItem(int position, PushReminderList item) {
        mDataSet.set(position, item);
        notifyItemChanged(position);
    }

    @Override
    public PushReminderAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_push_list, parent, false);
        return new PushReminderAdapter.Holder(rowItem);
    }

    @Override
    public void onBindViewHolder(PushReminderAdapter.Holder holder, int i) {
        PushReminderList reminders = mDataSet.get(i);
        holder.txtCategory.setText(reminders.getCategory());
        holder.txtProduct.setText(reminders.getName() + " ("+reminders.getShop().getName()+")");
        holder.txtRemark.setText(reminders.getRemarks());
        holder.txtDateTime.setText(reminders.getFrom());

        if(mType.equals(Constant.PENDING_LIST)){
            holder.layoutBtn.setVisibility(View.GONE);
        }else{
            holder.layoutBtn.setVisibility(View.VISIBLE);
        }
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
        @BindView(R.id.layout_btn)
        LinearLayout layoutBtn;

        @OnClick(R.id.btn_snooze)
        public void onClickSnooze(View view) {
            mCallback.onClickSnooze(mDataSet.get(getAdapterPosition()), getAdapterPosition());
        }

        @OnClick(R.id.btn_done)
        public void onClickDone(View view) {
            mCallback.onClickDone(mDataSet.get(getAdapterPosition()), getAdapterPosition());
        }

        @OnClick(R.id.btn_navigate)
        public void onClickNavigation(View view) {
            mCallback.onClickNavigation(mDataSet.get(getAdapterPosition()), getAdapterPosition());
        }

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface PushReminderAdapterCallback {

        void onClickSnooze(PushReminderList pushReminderList, int adapterPosition);
        void onClickDone(PushReminderList pushReminderList, int position);
        void onClickNavigation(PushReminderList pushReminderList, int position);
    }
}
