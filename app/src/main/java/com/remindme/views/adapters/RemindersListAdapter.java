package com.remindme.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.remindme.R;
import com.remindme.models.Reminders;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RemindersListAdapter extends RecyclerView.Adapter<RemindersListAdapter.Holder> {

    private Context mContext;
    private ArrayList<Reminders> mDataSet;
    RemindersListAdapter.RemindersListAdapterCallback mCallback;
    private int selectedItem;
    private boolean mClick = false;

    public RemindersListAdapter(Context context, ArrayList<Reminders> reminders, RemindersListAdapterCallback callback) {
        mContext = context;
        mDataSet = reminders;
        mCallback = callback;
        selectedItem = 0;
    }

    public void addAll(final ArrayList<Reminders> dataSet) {
        if (dataSet == null) return;
        if (dataSet.size() == 0) return;

        mDataSet.clear();
        notifyDataSetChanged();

        mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void addItem(Reminders item) {
        mDataSet.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mDataSet != null) {
            mDataSet.clear();
            notifyDataSetChanged();
        }
    }

    public  ArrayList<Reminders> getDataSet(){
        return mDataSet;
    }

    public void remove(int index) {
        mDataSet.remove(index);
        notifyDataSetChanged();
    }

    public void updateItem(int position, Reminders item) {
        mDataSet.set(position, item);
        notifyItemChanged(position);
    }

    @Override
    public RemindersListAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_reminder, parent, false);
        return new RemindersListAdapter.Holder(rowItem);
    }

    @Override
    public void onBindViewHolder(RemindersListAdapter.Holder holder, int i) {
        Reminders reminders = mDataSet.get(i);
        holder.txtCategory.setText(reminders.getCategory());
        holder.txtItems.setText(reminders.getItem());
        /*holder.txtTime.setText(reminders.getTime());
        holder.txtRadius.setText(reminders.getRadius());*/
        holder.txtPriority.setText(reminders.getPriority());
        /*holder.txtRemark.setText(reminders.getRemark());*/

    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 2 : mDataSet.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txt_category)
        TextView txtCategory;
        @BindView(R.id.txt_items)
        TextView txtItems;
        @BindView(R.id.txt_priority)
        TextView txtPriority;

        @OnClick(R.id.img_remove)
        public void onClickRemove(View view) {
            mCallback.onClickRemove(getAdapterPosition(), mDataSet.get(getAdapterPosition()));
        }


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public interface RemindersListAdapterCallback {

        void onClickRemove(int position, Reminders product);
    }
}
