package com.remindme.views.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.remindme.R;
import com.remindme.models.CategoryList;
import com.remindme.utils.ConstantExtras;
import com.remindme.views.adapters.SearchCategoryListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.rv_search_result)
    RecyclerView rvSearchResult;
    @BindView(R.id.edt_search_box)
    EditText edtSearchBox;

    private int mSearchCode;
    private ArrayList<CategoryList> mCategoryLists;
    private SearchCategoryListAdapter mSearchListAdapter;

    @OnClick(R.id.btn_left_back)
    public void onClickBack(View view) {
        onBackPressed();
    }

    TextWatcher searchTextWatcher = new TextWatcher() {
        private Timer timer = new Timer();
        private final long DELAY = 500;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(final Editable s) {
            List<CategoryList> List = new ArrayList<>();

                for (int x = 0; x < mCategoryLists.size(); x++) {
                    if (mCategoryLists.get(x).getName().toLowerCase().startsWith(s.toString().trim().toLowerCase())) {
                        List.add(mCategoryLists.get(x));
                    }
                }
                mSearchListAdapter.addAll(List);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        init();
        initSearchListRecyclerView();
    }

    private void init(){
        Intent intent = getIntent();
        mCategoryLists = ((ArrayList<CategoryList>) getIntent().getSerializableExtra(ConstantExtras.ARG_CATEGORY_LIST));
        edtSearchBox.addTextChangedListener(searchTextWatcher);
        mSearchCode = intent.getIntExtra(ConstantExtras.ARG_SEARCH_CODE, -1);
    }

    private void initSearchListRecyclerView() {

        mSearchListAdapter = new SearchCategoryListAdapter(this, new ArrayList<CategoryList>() , new SearchCategoryListAdapter.SearchListAdapterCallback() {

            @Override
            public void onClickItem(CategoryList item) {
                setResult(item);
            }
        });
        rvSearchResult.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        rvSearchResult.setHasFixedSize(true);
        rvSearchResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSearchResult.setAdapter(mSearchListAdapter);
        mSearchListAdapter.addAll(mCategoryLists);
    }

    private void setResult(CategoryList item) {
        Intent intent = new Intent();
        intent.putExtra(ConstantExtras.ARG_SEARCH_RESULT, item);
        setResult(mSearchCode, intent);
        onBackPressed();
    }
}