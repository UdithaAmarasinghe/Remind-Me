package com.remindme.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.remindme.R;
import com.remindme.services.requests.UserRegister;
import com.remindme.services.requests.VerifyEmailRequest;
import com.remindme.services.responses.Response;
import com.remindme.services.responses.UserRegistrationResponse;
import com.remindme.services.sync.UserSync;
import com.remindme.utils.Constant;
import com.remindme.views.dialogs.SuccessBottomSheetDialog;
import com.remindme.views.dialogs.UserTypeBottomSheetDialog;
import com.remindme.views.dialogs.UserVerificationBottomSheetDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class UserRegistrationActivity extends BaseActivity {

    @BindView(R.id.ab_text_title)
    TextView abTextTitle;
    @BindView(R.id.scroll_container)
    ScrollView scrollContainer;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.edt_first_name)
    EditText edtFirstName;
    @BindView(R.id.edt_last_name)
    EditText edtLastName;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.edt_confirm_password)
    EditText edtConfirmPassword;

    private String mSelectedGender = "Male";
    private boolean isSelectedType = false;
    private UserSync mUserSync;
    private UserTypeBottomSheetDialog userTypeBottomSheetDialog;

    @OnClick(R.id.btn_left_back)
    public void onClickBack(View view) {
        onBackPressed();
    }

    @OnClick(R.id.txt_title)
    public void onClickUserType(View view) {
        isSelectedType = true;
        userTypeBottomSheetDialog = new UserTypeBottomSheetDialog(mContext, txtTitle.getText().toString(), new UserTypeBottomSheetDialog.UserTypeBottomSheetDialogCallback() {
            @Override
            public void onClickType(String id, String label) {
                txtTitle.setText(label);
            }
        });
        userTypeBottomSheetDialog.setCancelable(true);
        userTypeBottomSheetDialog.show();
    }

    @OnCheckedChanged({R.id.radio_male, R.id.radio_female})
    void onGenderSelected(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.radio_male:
                    mSelectedGender = "Male";
                    break;
                case R.id.radio_female:
                    mSelectedGender = "Female";
                    break;
            }
        }
    }

    @OnClick(R.id.text_sign_in)
    public void onClickSign(View view) {
        LoginActivity.startActivity(this);
    }

    @OnClick(R.id.btn_create)
    public void onClickOnCreate(View view) {
        hideKeyboard(UserRegistrationActivity.this);
        if (formValidation()) {
            showProgress();

            UserRegister userRegister = new UserRegister();
            userRegister.setEmail(edtEmail.getText().toString());
            userRegister.setFirstName(edtFirstName.getText().toString());
            userRegister.setGender(mSelectedGender);
            userRegister.setLastName(edtLastName.getText().toString());
            userRegister.setPassword(edtPassword.getText().toString());
            userRegister.setTitle(txtTitle.getText().toString());


            showProgress();
            mUserSync.userRegistration(userRegister, new UserSync.UserSyncListeners.UserRegistrationCallback() {
                @Override
                public void onSuccessUserRegistration(UserRegistrationResponse response) {
                    dismissProgress();
                    if(!response.getMessage().equals("User is Already Exist")){
                        otpBottomSheetDialog();
                    }else{
                        showMessageDialog(response.getMessage());
                    }
                }
                @Override
                public void onFailedUserRegistration(String message, int code) {
                    if (message != null) {
                        showMessageDialog(message);
                    }
                    dismissProgress();
                }
            });
        }
    }

    private void otpBottomSheetDialog() {

        final UserVerificationBottomSheetDialog dialog = UserVerificationBottomSheetDialog.getInstance(mContext, edtEmail.getText().toString().trim(), Constant.SIGN_UP_TEXT);
        dialog.setCallback(new UserVerificationBottomSheetDialog.UserVerificationBottomSheetCallback() {
            @Override
            public void VerificationDialog(String code, String email) {
                verifyEmail(code, email);
                dialog.dismiss();

            }

            @Override
            public void ResendCodeDialog(String newEmail) {
                dialog.dismiss();

            }
        });
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.setCancelable(false);
        dialog.show();

    }

    private void verifyEmail(String code, String email) {

        showProgress();
        VerifyEmailRequest verifyEmailRequest = new VerifyEmailRequest();
        verifyEmailRequest.setCode(code);
        verifyEmailRequest.setEmail(email);

        mUserSync.userEmailVerify(verifyEmailRequest, new UserSync.UserSyncListeners.UserEmailVerifyCallback() {
            @Override
            public void onSuccessEmailVerify(Response response) {
                dismissProgress();
                successBottomSheet();
            }

            @Override
            public void onFailedEmailVerify(String message, int code) {
                if (message != null) {
                    showMessageDialog(message);
                }
                dismissProgress();
            }
        });

    }

    private void successBottomSheet() {

        SuccessBottomSheetDialog bottomSheetDialogFragment = new SuccessBottomSheetDialog(mContext, Constant.REGISTRATION);
        bottomSheetDialogFragment.setListener(new SuccessBottomSheetDialog.SuccessDialog() {
            @Override
            public void SuccessBottomSheetDialogPopup() {
                LoginActivity.startActivity(mContext);
                finish();
            }
        });
        bottomSheetDialogFragment.show(this.getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UserRegistrationActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registaration);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        abTextTitle.setText(Constant.SIGN_UP_TEXT);
        mUserSync = new UserSync(this, mRestAPI);
    }


    private boolean formValidation() {

        if (isSelectedType == false) {
            focusAndScrollUp(txtTitle, "Title is required.");
            return false;
        }

        if (edtFirstName.getText().toString().trim().isEmpty()) {
            edtFirstName.requestFocus();
            focusAndScrollUp(null, "First name is required.");
            return false;
        }
        if (edtLastName.getText().toString().trim().isEmpty()) {
            edtLastName.requestFocus();
            focusAndScrollUp(null, "Last name is required.");
            return false;
        }

        if (edtEmail.getText().toString().trim().isEmpty()) {
            focusAndScrollUp(edtEmail, "Email is required.");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString().trim()).matches()) {
            focusAndScrollUp(edtEmail, "Please enter valid email address.");
            return false;
        }

        if (edtPassword.getText().toString().trim().isEmpty()) {
            focusAndScrollUp(edtPassword, "Password is required.");
            return false;
        } else {
            if (edtPassword.getText().toString().trim().length() <= 5) {
                focusAndScrollUp(edtPassword, "Password must be at least 6 characters.");
                return false;
            }
        }

        if (edtConfirmPassword.getText().toString().trim().isEmpty()) {
            focusAndScrollUp(edtConfirmPassword, "Confirm password is required.");
            return false;
        } else {
            if (edtPassword.getText().toString().trim().equals(edtConfirmPassword.getText().toString().trim())) {
                return true;
            } else {
                focusAndScrollUp(edtConfirmPassword, "Password and Confirm Password must be same.");
                edtConfirmPassword.setText("");
                return false;
            }
        }
    }

    private void focusAndScrollUp(final EditText edt, String msg) {
        if (msg != null) {
            showValidationMessageDialog("Validation Message", msg);
        }

        if (edt != null) {
            scrollContainer.post(new Runnable() {
                @Override
                public void run() {
                    edt.requestFocus();
                    scrollContainer.scrollTo(0, edt.getBottom());
                }
            });
        }
    }

    private void focusAndScrollUp(final TextView edt, String msg) {
        if (msg != null) {
            showValidationMessageDialog("Validation Message", msg);
        }
        scrollContainer.post(new Runnable() {
            @Override
            public void run() {
                edt.requestFocus();
                scrollContainer.scrollTo(0, edt.getBottom());
            }
        });
    }


}


