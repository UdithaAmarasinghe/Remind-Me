package com.remindme.views.dialogs;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.remindme.R;
import com.remindme.models.SelectableUserTitleType;
import com.remindme.models.UserType;
import com.remindme.views.adapters.CommonAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserTypeBottomSheetDialog extends BottomSheetDialog {

    private Context mContext;
    private static UserTypeBottomSheetDialog mInstance;
    @BindView(R.id.rv_user_type)
    RecyclerView rv_user_type;

    private List<SelectableUserTitleType> selectableItems;
    private CommonAdapter commonAdapter;
    private String[] userTitle, userTitleId;
    private ArrayList<String> userTitleArray;
    private UserTypeBottomSheetDialogCallback callback;
    private String mSelected;

    @OnClick(R.id.btn_cancel)
    public void onClickCancel(View v) {
        dismiss();
    }


    public static UserTypeBottomSheetDialog getInstance(@NonNull Context context, String selectedTitle, UserTypeBottomSheetDialog.UserTypeBottomSheetDialogCallback userTypeBottomSheetDialogCallback) {
        return mInstance == null ? new UserTypeBottomSheetDialog(context, selectedTitle, userTypeBottomSheetDialogCallback) : mInstance;
    }

    public UserTypeBottomSheetDialog(@NonNull Context context, String selectedTitle, UserTypeBottomSheetDialog.UserTypeBottomSheetDialogCallback userTypeBottomSheetDialogCallback) {
        super(context);
        mContext = context;
        callback =  userTypeBottomSheetDialogCallback;
        mSelected = selectedTitle;
        create();
    }

    public void create() {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_user_type, null);
        ButterKnife.bind(this, view);
        setContentView(view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rv_user_type.setLayoutManager(layoutManager);
        commonAdapter = new CommonAdapter(mContext, new ArrayList<SelectableUserTitleType>(), new CommonAdapter.CommonAdapterCallback() {
            @Override
            public void onClickType(String id, String label) {
                dismiss();
                callback.onClickType(id, label);
            }
        });

        userTitle = mContext.getResources().getStringArray(R.array.user_type);
        userTitleId = mContext.getResources().getStringArray(R.array.user_type_id);
        userTitleArray = new ArrayList<String>(Arrays.asList(userTitle));
        selectableItems = new ArrayList<>();

        for (int x = 0; x < userTitleArray.size(); x++) {
            if (x == 0) {

            } else {

                if(userTitle[x].equals(mSelected)){
                    selectableItems.add(new SelectableUserTitleType(new UserType(userTitleId[x], userTitle[x]), true));
                }else{
                    selectableItems.add(new SelectableUserTitleType(new UserType(userTitleId[x], userTitle[x]), false));
                }
            }
        }
        rv_user_type.setAdapter(commonAdapter);
        commonAdapter.addAll(selectableItems);

    }

    public interface UserTypeBottomSheetDialogCallback {
        void onClickType(String id, String label);
    }

}
