package com.remindme.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.remindme.R;
import com.remindme.models.CategoryList;
import com.remindme.models.ItemList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchItemListAdapter extends RecyclerView.Adapter<SearchItemListAdapter.Holder>  {

    private List<ItemList> mDataSet;
    private Context mContext;
    private SearchItemListAdapter.SearchItemListAdapterCallback mCallback;

    public SearchItemListAdapter(Context context, List<ItemList> dataSet, SearchItemListAdapter.SearchItemListAdapterCallback callback) {
        mContext = context;
        mCallback = callback;
        mDataSet = dataSet;
    }

    public void addAll(final List<ItemList> dataSet) {
        mDataSet.clear();
        notifyDataSetChanged();

        mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void addItem(ItemList item) {
        mDataSet.add(item);
        notifyDataSetChanged();
    }

    public void addItemTopOfList(ItemList item) {
        mDataSet.add(0, item);
        notifyItemInserted(0);
    }

    public void remove(int index) {
        mDataSet.remove(index);
        notifyDataSetChanged();
    }

    public void updateItem(int position, ItemList item) {
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
    public SearchItemListAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchItemListAdapter.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_search_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchItemListAdapter.Holder holder, int position) {
        ItemList itemList = mDataSet.get(position);
        holder.txtTitle.setText(itemList.getName());
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView txtTitle;

        @OnClick(R.id.layout_item_container)
        public void onClickItem(View view) {
            mCallback.onClickItem(mDataSet.get(getAdapterPosition()));
        }

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface SearchItemListAdapterCallback {

        void onClickItem(ItemList item);
    }
}
