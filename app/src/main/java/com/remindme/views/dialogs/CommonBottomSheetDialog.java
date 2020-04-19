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
import com.remindme.utils.Constant;
import com.remindme.views.adapters.CommonAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonBottomSheetDialog extends BottomSheetDialog {

    @BindView(R.id.rv_data)
    RecyclerView mRvData;

    private Context mContext;
    private List<SelectableUserTitleType> selectableItems;
    private CommonAdapter commonAdapter;
    private String[]  priorityTypeId, priorityType, radiusType;
    private ArrayList<String> userTitleArray;
    private String  mSelected, mType;
    private static CommonBottomSheetDialog mInstance;
    private CommonBottomSheetDialog.CommonBottomSheetDialogCallback callback;

    @OnClick(R.id.btn_cancel)
    public void onClickCancel(View v) {
        dismiss();
    }

    public static CommonBottomSheetDialog getInstance(@NonNull Context context, String type, String selected, CommonBottomSheetDialog.CommonBottomSheetDialogCallback confirmationBottomSheetDialogCallback) {
        return mInstance == null ? new CommonBottomSheetDialog(context, type, selected, confirmationBottomSheetDialogCallback) : mInstance;
    }

    public CommonBottomSheetDialog(@NonNull Context context, String type, String selected, CommonBottomSheetDialog.CommonBottomSheetDialogCallback confirmationBottomSheetDialogCallback) {
        super(context);
        mContext = context;
        mSelected = selected;
        mType = type;
        callback = confirmationBottomSheetDialogCallback;
        create();
    }

    public void create() {
        View view = getLayoutInflater().inflate(R.layout.dialog_common, null);
        ButterKnife.bind(this, view);
        setContentView(view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRvData.setLayoutManager(layoutManager);
        commonAdapter = new CommonAdapter(mContext, new ArrayList<SelectableUserTitleType>(), new CommonAdapter.CommonAdapterCallback() {
            @Override
            public void onClickType(String id, String label) {
                dismiss();
                callback.onClickType(id, label);
            }
        });

      /*  userTitle = mContext.getResources().getStringArray(R.array.user_type);
        userTitleId = mContext.getResources().getStringArray(R.array.user_type_id);
        userTitleArray = new ArrayList<String>(Arrays.asList(userTitle));*/
        priorityTypeId =  mContext.getResources().getStringArray(R.array.priority_type_id);
        priorityType =  mContext.getResources().getStringArray(R.array.priority_type);
        radiusType =  mContext.getResources().getStringArray(R.array.radius_type);
        selectableItems = new ArrayList<>();


        if(mType.equals(Constant.PRIORITY)){

            for (int x = 0; x < priorityType.length; x++) {
                if (x == 0) {

                } else {

                    if(priorityType[x].equals(mSelected)){
                        selectableItems.add(new SelectableUserTitleType(new UserType(priorityTypeId[x], priorityType[x]), true));
                    }else{
                        selectableItems.add(new SelectableUserTitleType(new UserType(priorityTypeId[x], priorityType[x]), false));
                    }
                }
            }
        }else if(mType.equals(Constant.RADIUS)){

            for (int x = 0; x < radiusType.length; x++) {
                if (x == 0) {

                } else {

                    if(radiusType[x].equals(mSelected)){
                        selectableItems.add(new SelectableUserTitleType(new UserType("", radiusType[x]), true));
                    }else{
                        selectableItems.add(new SelectableUserTitleType(new UserType("", radiusType[x]), false));
                    }
                }
            }
        }



        mRvData.setAdapter(commonAdapter);
        commonAdapter.addAll(selectableItems);
    }

    public interface CommonBottomSheetDialogCallback {
        void onClickType(String id, String label);
    }

}
