package com.remindme.views.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.FrameStats;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.irozon.sneaker.Sneaker;
import com.remindme.R;
import com.remindme._Application;
import com.remindme.models.User;
import com.remindme.services.RestAPI;
import com.remindme.utils.AppSharedPreference;
import com.remindme.views.dialogs.MessageBottomSheetDialog;
import com.remindme.views.dialogs.MessageValidationBottomSheetDialog;

import javax.inject.Inject;

public abstract class BaseFragment extends Fragment {

    public Context mContext;
    protected User user;
    public AppSharedPreference mPreferences;
    private Dialog mProgress;
    protected String mAccessToken;
    public static String mRefreshToken;
    //private Merlin merlin;
    private FragmentTransaction ft;

    @Inject
    RestAPI mRestAPI;

    @Override
    public void onResume() {
        super.onResume();
        //merlin.bind();
    }

    @Override
    public void onPause() {
        //merlin.bind();
        super.onPause();

    }

    @Override
    public void onDestroy() {
        dismissProgress();
        super.onDestroy();
        System.out.println("---- onDestroy ----");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*merlin = new Merlin.Builder().withConnectableCallbacks().build(mContext);
        merlin.registerConnectable(new Connectable() {
            @Override
            public void onConnect() {
                BaseFragment.this.onConnect();
            }
        });*/
        initial();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((_Application) this.getActivity().getApplication()).getNetComponent().inject(this);
    }

    private void initial() {
        mPreferences = new AppSharedPreference(mContext);
        user = mPreferences.getUser();
        if (user != null) {
            mAccessToken = "Bearer " + mPreferences.getToken();
            mRefreshToken = mPreferences.getRefershToken();
        }
    }

    public void showMessage(String message) {
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (message != null) {
            //Toasty.error(mContext, message, Toast.LENGTH_SHORT, false).show();
        }
    }

    protected boolean isLogin() {
        if (mAccessToken == null || mAccessToken.isEmpty())
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


}
