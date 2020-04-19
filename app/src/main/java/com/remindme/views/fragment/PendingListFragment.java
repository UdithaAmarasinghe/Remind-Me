package com.remindme.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.remindme.R;
import com.remindme.models.PendingList;
import com.remindme.services.responses.PendingRemindersResponse;
import com.remindme.services.sync.UserSync;
import com.remindme.utils.Constant;
import com.remindme.views.adapters.PendingListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PendingListFragment extends BaseFragment {

    @BindView(R.id.rv_pending_list)
    RecyclerView mRvPendingList;

    private View view;
    private UserSync mUserSync;
    private Context mContext;
    private PendingListAdapter mPendingListAdapter;

    public PendingListFragment() {
        // Required empty public constructor
    }

    public static PendingListFragment newInstance() {
        PendingListFragment fragment = new PendingListFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pending_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void init() {
        mUserSync = new UserSync(mContext, mRestAPI);
        initPendingListRecyclerView();
        loadPendingList();
    }

    private void initPendingListRecyclerView() {

        mPendingListAdapter = new PendingListAdapter(mContext, new ArrayList<PendingList>());
        mRvPendingList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRvPendingList.setAdapter(mPendingListAdapter);

    }

    private void loadPendingList() {
        showProgress();
        mUserSync.pendingList(mAccessToken, new UserSync.UserSyncListeners.UserPendingListCallback() {
            @Override
            public void onSuccessPendingList(PendingRemindersResponse response) {
                dismissProgress();
                mPendingListAdapter.addAll(response.getData());
            }

            @Override
            public void onFailedPendingList(String message, int code) {
                if (message != null) {
                    showMessageDialog(message);
                }
                dismissProgress();
            }
        });

    }

}
