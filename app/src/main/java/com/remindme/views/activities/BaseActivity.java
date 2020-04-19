package com.remindme.views.activities;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.irozon.sneaker.Sneaker;
import com.remindme.R;
import com.remindme._Application;
import com.remindme.models.Token;
import com.remindme.models.User;
import com.remindme.services.RestAPI;
import com.remindme.utils.AppSharedPreference;
import com.remindme.views.dialogs.MessageBottomSheetDialog;
import com.remindme.views.dialogs.MessageValidationBottomSheetDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class BaseActivity extends AppCompatActivity {

    private Dialog mProgress;

    public Context mContext;
    @Inject
    public
    RestAPI mRestAPI;

    public static AppSharedPreference mPreferences;
    public static String mAccessToken;
    public static String mRefreshToken;
    public static User user;

    public BaseActivity() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((_Application) getApplication()).getNetComponent().inject(this);
        init();
    }

    private void init() {
        mContext = this;
        mPreferences = new AppSharedPreference(mContext);
        user = mPreferences.getUser();
        if (user != null) {
            mAccessToken = "Bearer " + mPreferences.getToken();
            mRefreshToken = mPreferences.getRefershToken();
        }
    }

    public void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void goNextAnimation() {
        overridePendingTransition(R.anim.slide_to_left, R.anim.keep_active);
    }

    public void showProgress() {
        try {
            if (mProgress == null) {

                mProgress = new ProgressDialog(mContext, R.style.Progressbar);
                mProgress.getWindow().setBackgroundDrawable(new
                        ColorDrawable(android.graphics.Color.TRANSPARENT));
                //mProgress.(true);
                mProgress.setCancelable(false);
                mProgress.show();
                mProgress.setContentView(R.layout.dialog_progress_spinner);

            }

            if (mProgress != null) {
                if (mProgress.isShowing() == false) {
                    mProgress.show();
                }
            }
        } catch (Exception e) {

        }
    }

    public void dismissProgress() {
        try {
            if (mProgress != null) {
                if (mProgress.isShowing()) {
                    mProgress.dismiss();
                    mProgress = null;
                }
            }
        } catch (Exception _e) {

        }
    }

    protected boolean isLogin() {
        if (mPreferences.getUser() == null)
            return false;

        return true;
    }

    public void showValidationMessageDialog(String title, String message) {
        MessageValidationBottomSheetDialog dialog = MessageValidationBottomSheetDialog.getInstance(mContext);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showMessageDialog(String message) {
        MessageBottomSheetDialog dialog = MessageBottomSheetDialog.getInstance(mContext);
        dialog.setMessage(message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void successMessage(String message){
        Sneaker.with(this)
                .setTitle("Success!!")
                .setMessage(message)
                .sneakSuccess();
    }

    public void warningMessage(String message){
        Sneaker.with(this)
                .setTitle("Warning!!")
                .setMessage(message)
                .sneakWarning();
    }

    public boolean isValidEmailId(String email) {
        Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher emailMatcher = emailPattern.matcher(email);
        return emailMatcher.matches();
    }

    @Override
    protected void onDestroy() {
        dismissProgress();
        super.onDestroy();
        System.out.println("---- onDestroy ----");
    }
}
