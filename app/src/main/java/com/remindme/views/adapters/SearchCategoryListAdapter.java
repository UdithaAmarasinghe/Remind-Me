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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchCategoryListAdapter extends RecyclerView.Adapter<SearchCategoryListAdapter.Holder>  {

    private List<CategoryList> mDataSet;
    private Context mContext;
    private SearchCategoryListAdapter.SearchListAdapterCallback mCallback;

    public SearchCategoryListAdapter(Context context, List<CategoryList> dataSet, SearchListAdapterCallback callback) {
        mContext = context;
        mCallback = callback;
        mDataSet = dataSet;
    }

    public void addAll(final List<CategoryList> dataSet) {
        mDataSet.clear();
        notifyDataSetChanged();

        mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void addItem(CategoryList item) {
        mDataSet.add(item);
        notifyDataSetChanged();
    }

    public void addItemTopOfList(CategoryList item) {
        mDataSet.add(0, item);
        notifyItemInserted(0);
    }

    public void remove(int index) {
        mDataSet.remove(index);
        notifyDataSetChanged();
    }

    public void updateItem(int position, CategoryList item) {
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
    public SearchCategoryListAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchCategoryListAdapter.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_search_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchCategoryListAdapter.Holder holder, int position) {
        CategoryList categoryList = mDataSet.get(position);
        holder.txtTitle.setText(categoryList.getName());
        holder.img.setImageResource(categoryList.getImage());
        holder.layoutItemContainer.setBackgroundColor(Color.parseColor(categoryList.getColor()));
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.layout_item_container)
        RelativeLayout layoutItemContainer;

        @OnClick(R.id.layout_item_container)
        public void onClickItem(View view) {
            mCallback.onClickItem(mDataSet.get(getAdapterPosition()));
        }

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface SearchListAdapterCallback {

        void onClickItem(CategoryList item);
    }
}
