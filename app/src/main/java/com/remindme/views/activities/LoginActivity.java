package com.remindme.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.remindme.R;
import com.remindme.services.requests.LoginRequest;
import com.remindme.services.responses.TokenResponse;
import com.remindme.services.responses.UserDetails;
import com.remindme.services.sync.UserSync;
import com.remindme.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.scroll_container)
    ScrollView scroll_container;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText mEdtPassword;

    private String firebaseToken;
    private UserSync mUserSync;

    @OnClick(R.id.btn_login)
    public void onClickLogin(View view) {
        checkValidation();
    }

    private void checkValidation() {

        if (edtEmail.getText().toString().trim().isEmpty()) {
            focusAndScrollUp(edtEmail, "Email is required.");
            return;
        }

        if (mEdtPassword.getText().toString().trim().isEmpty()) {
            focusAndScrollUp(mEdtPassword, "Password couldn't be empty.");
            return;
        }

        if (!isValidEmailId(edtEmail.getText().toString().trim())) {
            focusAndScrollUp(edtEmail, "Please enter valid email address.");
            return;
        }
        loginCall();
    }

    private void loginCall() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName(edtEmail.getText().toString());
        loginRequest.setPassword(mEdtPassword.getText().toString());
        loginRequest.setClientId(Constant.CLIENT_ID);
        loginRequest.setClientSecret(Constant.CLIENT_SECRET);
        loginRequest.setGrantType(Constant.GANT_TYPE);
        loginRequest.setDeviceToken(firebaseToken);

        showProgress();
        mUserSync.userLogin(loginRequest, new UserSync.UserSyncListeners.UserLoginCallback() {
            @Override
            public void onSuccessUserLogin(TokenResponse response) {
                dismissProgress();
                getUserData();
            }

            @Override
            public void onFailedUserLogin(String message, int code) {
                if (message != null) {
                    showMessageDialog(message);
                }
                dismissProgress();
            }
        });
    }

    private void getUserData() {

        showProgress();
        mUserSync.getUserData(Constant.BEARER + mPreferences.getToken(), new UserSync.UserSyncListeners.UserDetailsCallback() {
            @Override
            public void onSuccessUserDetails(UserDetails response) {
                MainActivity.startActivityView(mContext);
                finish();
            }

            @Override
            public void onFailedUserDetails(String message, int code) {
                if (message != null) {
                    showMessageDialog(message);
                }
                dismissProgress();
            }
        });
    }

    @OnClick(R.id.layout_sign_up)
    public void onClickSign_up(View view) {
        UserRegistrationActivity.startActivity(mContext);
    }

    @OnClick(R.id.txt_forget_password)
    public void onClickSForgetPassword(View view) {
        ForgetPasswordActivity.startActivity(mContext);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        firebaseToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("Firebase Token", "loadDefault: "+firebaseToken);
        mUserSync = new UserSync(this, mRestAPI);
    }

    private void focusAndScrollUp(final EditText edt, String msg) {
        if (msg != null) {
            showValidationMessageDialog("Validation Message", msg);
        }

        if (edt != null) {
            scroll_container.post(new Runnable() {
                @Override
                public void run() {
                    edt.requestFocus();
                    scroll_container.scrollTo(0, edt.getBottom());
                }
            });
        }
    }
}
