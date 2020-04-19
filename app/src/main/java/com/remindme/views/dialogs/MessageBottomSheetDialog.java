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

public class MessageBottomSheetDialog extends BottomSheetDialog {

    private Context mContext;
    private static MessageBottomSheetDialog mInstance;
    @BindView(R.id.text_sub_title)
    TextView text_sub_title;

    @OnClick({R.id.btn_cancel,R.id.btn_cancel_action})
    public void onClickCancel(View v) {
        dismiss();
        if (mCallback != null) {
            mCallback.onClose();
        }
    }

    public static MessageBottomSheetDialog getInstance(@NonNull Context context) {
        return mInstance == null ? new MessageBottomSheetDialog(context) : mInstance;
    }

    public void setMessage(String message) {
        text_sub_title.setText(message);
    }

    public MessageBottomSheetDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        create();
    }

    public void create() {
        View view = getLayoutInflater().inflate(R.layout.dialog_message, null);
        ButterKnife.bind(this, view);
        setContentView(view);

    }

    private MessageBottomSheetDialogCallback mCallback;
    public void setCallback(MessageBottomSheetDialogCallback callbak) {
        mCallback = callbak;
    }

    public interface MessageBottomSheetDialogCallback {
        void onClose();
    }
}
