package com.remindme.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.remindme.R;
import com.remindme.models.CategoryList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.Holder> {
    private Context mContext;
    private CategoryListAdapter.CategoryListAdapterCallback mCallback;
    private List<CategoryList> mDataSet;
    private int mWidth = 0;

    public CategoryListAdapter(Context context, List<CategoryList> dataSet, int width, CategoryListAdapterCallback callback) {
        mContext = context;
        mCallback = callback;
        mDataSet = dataSet;
        mWidth = width;
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
    public CategoryListAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryListAdapter.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_category_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final CategoryListAdapter.Holder holder, int position) {
        CategoryList business = mDataSet.get(position);
        holder.itemContainer.getLayoutParams().height = mWidth;
        holder.itemContainer.getLayoutParams().width = mWidth;
        holder.itemContainer.setBackgroundColor(Color.parseColor(business.getColor()));
        holder.imgCatIcon.setBackgroundResource(business.getImage());
        holder.txtName.setText(business.getName());
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    private boolean showLoadingView = false;

    public static final int VIEW_TYPE_DEFAULT = 1;
    public static final int VIEW_TYPE_LOADER = 2;

    @Override
    public int getItemViewType(int position) {
        if (showLoadingView && position == 0) {
            return VIEW_TYPE_LOADER;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id._layout_item_container)
        LinearLayout itemContainer;
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.img_cat_icon)
        ImageView imgCatIcon;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCallback.onClickItem(mDataSet.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface CategoryListAdapterCallback {
        void onClickItem(CategoryList item, int position);
    }

}
