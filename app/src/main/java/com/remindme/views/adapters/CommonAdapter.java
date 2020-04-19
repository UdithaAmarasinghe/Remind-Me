package com.remindme.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.remindme.R;
import com.remindme.models.SelectableUserTitleType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.UserTypeAdapterHolder> {

    private Context mContext;
    private List<SelectableUserTitleType> mDataSet;
    private CommonAdapter.CommonAdapterCallback mCallback;
    public SelectableUserTitleType mSelectedValue = null;

    public CommonAdapter(Context context, ArrayList<SelectableUserTitleType> selectableUserTitleTypes, CommonAdapter.CommonAdapterCallback userTypeAdapterCallback) {
        mContext = context;
        mDataSet = selectableUserTitleTypes;
        mCallback = userTypeAdapterCallback;
    }

    public void addAll(final List<SelectableUserTitleType> dataSet) {
        if (dataSet == null) return;
        if (dataSet.size() == 0) return;

        mDataSet.clear();
        notifyDataSetChanged();

        mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void addItem(SelectableUserTitleType item) {
        mDataSet.add(item);
        notifyDataSetChanged();
    }

    public void updateItem(int position, SelectableUserTitleType item) {
        mDataSet.set(position, item);
        notifyItemChanged(position);
    }

    public void clear() {
        if (mDataSet != null) {
            mDataSet.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public CommonAdapter.UserTypeAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_user_type, parent, false);
        return new CommonAdapter.UserTypeAdapterHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(final UserTypeAdapterHolder holder, int position) {
        SelectableUserTitleType userType = mDataSet.get(position);
        holder.txt_type.setText(userType.getLabel());

        holder.layoutParentPanel.setBackgroundColor(Color.parseColor("#ffffff"));
        if (userType.isSelected()) {
            holder.layoutParentPanel.setBackgroundColor(Color.parseColor("#ccced0"));
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }


    public class UserTypeAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txt_type)
        TextView txt_type;
        @BindView(R.id.layout_main)
        LinearLayout layoutParentPanel;

        public UserTypeAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCallback.onClickType(mDataSet.get(getAdapterPosition()).getId(), mDataSet.get(getAdapterPosition()).getLabel());
        }
    }

    public interface CommonAdapterCallback {
        void onClickType(String id, String label);
    }
}
