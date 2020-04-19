package com.remindme.views.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.remindme.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("ValidFragment")
public class ConfirmationBottomSheetDialog extends BottomSheetDialog {

        private Context mContext;
        private static ConfirmationBottomSheetDialog mInstance;
        private ConfirmationBottomSheetDialog.ConfirmationBottomSheetDialogCallback callback;

        @OnClick(R.id.btn_cancel)
        public void onClickCancel(View v) {
        dismiss();
    }

        @OnClick(R.id.btn_yes)
        public void onClickYes(View v) {
        dismiss();
        callback.onClickYes();
    }

        @OnClick(R.id.btn_no)
        public void onClickNo(View v) {
        dismiss();
        callback.onClickNo();
    }

        public static ConfirmationBottomSheetDialog getInstance(@NonNull Context context, ConfirmationBottomSheetDialog.ConfirmationBottomSheetDialogCallback confirmationBottomSheetDialogCallback) {
        return mInstance == null ? new ConfirmationBottomSheetDialog(context, confirmationBottomSheetDialogCallback) : mInstance;
    }

    public ConfirmationBottomSheetDialog(@NonNull Context context, ConfirmationBottomSheetDialog.ConfirmationBottomSheetDialogCallback confirmationBottomSheetDialogCallback) {
        super(context);
        mContext = context;
        callback = confirmationBottomSheetDialogCallback;
        create();
    }

        public void create() {
        View view = getLayoutInflater().inflate(R.layout.dialog_confimation, null);
        ButterKnife.bind(this, view);
        setContentView(view);
    }

        public interface ConfirmationBottomSheetDialogCallback {
            void onClickYes();

            void onClickNo();
        }

    }
