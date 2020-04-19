package com.remindme.views.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.remindme.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageValidationBottomSheetDialog extends BottomSheetDialog {

    private Context mContext;
    private static MessageValidationBottomSheetDialog mInstance;
    @BindView(R.id.text_sub_title)
    TextView text_sub_title;
    @BindView(R.id.text_data)
    TextView text_data;

    @OnClick({R.id.btn_cancel,R.id.btn_cancel_action})
    public void onClickCancel(View v) {
        dismiss();
    }

    public static MessageValidationBottomSheetDialog getInstance(@NonNull Context context) {
        return mInstance == null ? new MessageValidationBottomSheetDialog(context) : mInstance;
    }

    public void setTitle(String title) {
        text_data.setText(title);
    }

    public void setMessage(String message) {
        text_sub_title.setText(message);
    }

    public MessageValidationBottomSheetDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        create();
    }

    public void create() {
        View view = getLayoutInflater().inflate(R.layout.dialog_validation_message, null);
        ButterKnife.bind(this, view);
        setContentView(view);

    }
}
