package com.remindme.views.activities;

import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.remindme.R;
import com.remindme.services.requests.PasswordChangeRequest;
import com.remindme.services.requests.PasswordResetOTPRequest;
import com.remindme.services.responses.ForgetPasswordResponse;
import com.remindme.services.responses.Response;
import com.remindme.services.sync.UserSync;
import com.remindme.utils.Constant;
import com.remindme.views.dialogs.ChangePasswordBottomSheetDialog;
import com.remindme.views.dialogs.SuccessBottomSheetDialog;
import com.remindme.views.dialogs.UserVerificationBottomSheetDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ForgetPasswordActivity extends BaseActivity {

    @BindView(R.id.edt_email)
    EditText edEmail;
    @BindView(R.id.btn_forget)
    Button btnForget;
    @BindView(R.id.ab_text_title)
    TextView abTextTitle;

    private UserSync mUserSync;
    private PasswordResetOTPRequest passwordResetOTPRequest;

    @OnClick(R.id.btn_left_back)
    public void onClickBack(View view) {
        onBackPressed();
    }

    @OnClick(R.id.btn_forget)
    public void onClickForget(View view) {
        hideKeyboard(ForgetPasswordActivity.this);
        if (isValidEmailId(edEmail.getText().toString().trim())) {
            passwordResetOTPRequest = new PasswordResetOTPRequest();
            passwordResetOTPRequest.setEmail(edEmail.getText().toString());

            forgetPassword(passwordResetOTPRequest);
        } else {
            warningMessage(Constant.EMAIL_VALID);
        }
    }

    private void forgetPassword(PasswordResetOTPRequest passwordResetOTPRequest) {

        showProgress();
        mUserSync.forgetPasswordOTP(passwordResetOTPRequest, new UserSync.UserSyncListeners.UserForgetPasswordOTPCallback() {
            @Override
            public void onSuccessForgetPasswordOTP(ForgetPasswordResponse response) {
                dismissProgress();
                Toast.makeText(mContext, "Code " + response.getCode(), Toast.LENGTH_SHORT).show();
                otpBottomSheetDialog();
            }

            @Override
            public void onFailedForgetPasswordOTP(String message, int code) {
                if (message != null) {
                    showMessageDialog(message);
                }
                dismissProgress();
            }
        });

    }

    private void otpBottomSheetDialog() {
        final UserVerificationBottomSheetDialog dialog = UserVerificationBottomSheetDialog.getInstance(mContext, edEmail.getText().toString().trim(), Constant.FORGET_PASSWORD);
        dialog.setCallback(new UserVerificationBottomSheetDialog.UserVerificationBottomSheetCallback() {
            @Override
            public void VerificationDialog(String code, String email) {
                changePasswordBottomSheetDialog(code, email);
                dialog.dismiss();

            }

            @Override
            public void ResendCodeDialog(String newEmail) {
                dialog.dismiss();
                forgetPassword(passwordResetOTPRequest);
            }
        });
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void changePasswordBottomSheetDialog(String code, String email) {

        ChangePasswordBottomSheetDialog changePasswordBottomSheetDialog = new ChangePasswordBottomSheetDialog(mContext, new ChangePasswordBottomSheetDialog.ChangePasswordBottomSheetDialogCallback() {
            @Override
            public void onClick(String password, String passwordConfirmation) {


                PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
                passwordChangeRequest.setEmail(email);
                passwordChangeRequest.setPassword(password);
                passwordChangeRequest.setPasswordConfirmation(passwordConfirmation);
                passwordChangeRequest.setToken(code);

                changePassword(passwordChangeRequest);

            }
        });
        changePasswordBottomSheetDialog.setCancelable(false);
        changePasswordBottomSheetDialog.show();
    }

    private void changePassword(PasswordChangeRequest passwordChangeRequest) {

        hideKeyboard(ForgetPasswordActivity.this);
        showProgress();
        mUserSync.passwordChange(passwordChangeRequest, new UserSync.UserSyncListeners.ChangePasswordCallback() {
            @Override
            public void onSuccessChangePassword(Response response) {
                dismissProgress();
                successBottomSheet();
            }

            @Override
            public void onFailedChangePassword(String message, int code) {
                if (message != null) {
                    showMessageDialog(message);
                }
                dismissProgress();
            }
        });


    }

    private void successBottomSheet() {

        SuccessBottomSheetDialog bottomSheetDialogFragment = new SuccessBottomSheetDialog(mContext, Constant.PASSWORD_CHANGE);
        bottomSheetDialogFragment.setListener(new SuccessBottomSheetDialog.SuccessDialog() {
            @Override
            public void SuccessBottomSheetDialogPopup() {
                LoginActivity.startActivity(mContext);
                finish();
            }
        });
        bottomSheetDialogFragment.show(this.getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @OnTextChanged(R.id.edt_email)
    protected void onTextChangedOne(CharSequence text) {

        if (isValidEmailId(edEmail.getText().toString().trim())) {
            btnForget.setBackgroundResource(R.drawable.selector_btn_pink_bg);

        } else {
            btnForget.setBackgroundColor(ActivityCompat.getColor(this, R.color.disable_bg_proceed_btn));
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ForgetPasswordActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        mUserSync = new UserSync(this, mRestAPI);
        abTextTitle.setText(Constant.FORGET_PASSWORD);
    }
}
