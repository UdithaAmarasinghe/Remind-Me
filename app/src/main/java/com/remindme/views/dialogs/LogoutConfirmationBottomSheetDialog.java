package com.remindme.views.dialogs;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.remindme.R;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogoutConfirmationBottomSheetDialog extends BottomSheetDialog {

    private Context mContext;
    private static LogoutConfirmationBottomSheetDialog mInstance;
    private LogoutConfirmationBottomSheetDialog.LogoutConfirmationBottomSheetDialogCallback callback;

    @OnClick(R.id.btn_cancel)
    public void onClickCancel(View v) {
        dismiss();
    }

    @OnClick(R.id.btn_cancel_log_out)
    public void onClickHide(View v) {
        dismiss();
    }

    @OnClick(R.id.btn_log_out)
    public void onClickLogout(View v) {
        dismiss();
        callback.onClickLogOut();
    }

    public static LogoutConfirmationBottomSheetDialog getInstance(@NonNull Context context, LogoutConfirmationBottomSheetDialog.LogoutConfirmationBottomSheetDialogCallback logoutConfirmationBottomSheetDialogCallback) {
        return mInstance == null ? new LogoutConfirmationBottomSheetDialog(context, logoutConfirmationBottomSheetDialogCallback) : mInstance;
    }

    public LogoutConfirmationBottomSheetDialog(@NonNull Context context, LogoutConfirmationBottomSheetDialog.LogoutConfirmationBottomSheetDialogCallback logoutConfirmationBottomSheetDialogCallback) {
        super(context);
        mContext = context;
        callback = logoutConfirmationBottomSheetDialogCallback;
        create();
    }

    public void create() {
        View view = getLayoutInflater().inflate(R.layout.dialog_log_out_dialog, null);
        ButterKnife.bind(this, view);
        setContentView(view);
    }

    public interface LogoutConfirmationBottomSheetDialogCallback {
        void onClickLogOut();
    }

}
