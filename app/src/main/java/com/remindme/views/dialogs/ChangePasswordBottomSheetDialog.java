package com.remindme.views.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.remindme.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

@SuppressLint("ValidFragment")
public class ChangePasswordBottomSheetDialog extends BottomSheetDialog {

    @BindView(R.id.new_password)
    EditText new_password;
    @BindView(R.id.edt_repeart_password)
    EditText edt_repeart_password;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    String mCode, mMobileNumber;

    private Context mContext;
    private static ChangePasswordBottomSheetDialog mInstance;
    private ChangePasswordBottomSheetDialog.ChangePasswordBottomSheetDialogCallback callback;

    @OnClick(R.id.btn_cancel)
    public void onClickCancel(View v) {
        dismiss();
    }

    @OnTextChanged(R.id.edt_repeart_password)
    protected void onTextChangedOne(CharSequence text) {

        if (edt_repeart_password.getText().toString().trim().length() > 0)
            btn_submit.setBackgroundResource(R.drawable.selector_btn_pink_bg);
    }

    @OnClick(R.id.btn_submit)
    public void onClickApply(View v) {

        if (edt_repeart_password.getText().toString().equals(new_password.getText().toString())) {
            if (new_password.getText().toString() != null && edt_repeart_password.getText().toString() != null) {
                dismiss();
                callback.onClick(new_password.getText().toString().trim(), edt_repeart_password.getText().toString());
            }
        } else {
            Toast.makeText(mContext, "Password not same", Toast.LENGTH_SHORT).show();
        }
    }

    public static ChangePasswordBottomSheetDialog getInstance(@NonNull Context context, ChangePasswordBottomSheetDialog.ChangePasswordBottomSheetDialogCallback changePasswordBottomSheetDialogCallback) {
        return mInstance == null ? new ChangePasswordBottomSheetDialog(context, changePasswordBottomSheetDialogCallback) : mInstance;
    }

    public ChangePasswordBottomSheetDialog(@NonNull Context context, ChangePasswordBottomSheetDialog.ChangePasswordBottomSheetDialogCallback changePasswordBottomSheetDialogCallback) {
        super(context);
        mContext = context;
        callback = changePasswordBottomSheetDialogCallback;
        create();
    }

    public void create() {
        View view = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        ButterKnife.bind(this, view);
        setContentView(view);
    }


    public interface ChangePasswordBottomSheetDialogCallback {
        void onClick(String password, String passwordConfirmation);
    }

}
