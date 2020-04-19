package com.remindme.views.dialogs;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.remindme.R;
import com.remindme.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class UserVerificationBottomSheetDialog extends BottomSheetDialog {

    @BindView(R.id.edt_pin_one)
    EditText edtPinOne;
    @BindView(R.id.edt_pin_two)
    EditText edtPinTwo;
    @BindView(R.id.edt_pin_three)
    EditText edtPinThree;
    @BindView(R.id.edt_pin_four)
    EditText edtPinFour;
    @BindView(R.id.edt_pin_five)
    EditText edtPinFive;
    @BindView(R.id.edt_pin_six)
    EditText edtPinSix;
    @BindView(R.id.text_data)
    TextView text_data;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.txt_resend_code)
    TextView txt_resend_code;
    private Context mContext;
    private static UserVerificationBottomSheetDialog mInstance;
    private String mEmail, mType;
    private boolean resend = false;
    private  UserVerificationBottomSheetCallback mCallback;

    public void setCallback(UserVerificationBottomSheetCallback callback) {
        mCallback = callback;
    }

    @OnClick(R.id.txt_resend_code)
    public void onClickResend(View v) {
        resend = true;
        mCallback.ResendCodeDialog(mEmail);
        dismiss();
    }

    @OnClick(R.id.btn_cancel)
    public void onClickCancel(View v) {
        dismiss();
    }

    @OnClick(R.id.btn_submit)
    public void onClickSubmit(View v) {
        dismiss();
        submit();
    }

    private void submit() {
        String code = edtPinOne.getText().toString() + edtPinTwo.getText().toString() + edtPinThree.getText().toString() + edtPinFour.getText().toString()+ edtPinFive.getText().toString()+ edtPinSix.getText().toString();
        mCallback.VerificationDialog(code, mEmail);
    }

    @OnTextChanged(value = {R.id.edt_pin_one, R.id.edt_pin_two, R.id.edt_pin_three, R.id.edt_pin_four, R.id.edt_pin_five, R.id.edt_pin_six})
    protected void onTextChangedOne(CharSequence text) {

        if (edtPinOne.getText().toString().trim().length() > 0)
            edtPinTwo.requestFocus();

        if (edtPinTwo.getText().toString().trim().length() > 0)
            edtPinThree.requestFocus();

        if (edtPinThree.getText().toString().trim().length() > 0)
            edtPinFour.requestFocus();

        if (edtPinFour.getText().toString().trim().length() > 0)
            edtPinFive.requestFocus();

        if (edtPinFive.getText().toString().trim().length() > 0)
            edtPinSix.requestFocus();

        if (edtPinSix.getText().toString().trim().length() > 0)
            btnSubmit.setBackgroundResource(R.drawable.selector_btn_pink_bg);

    }


    public static UserVerificationBottomSheetDialog getInstance(@NonNull Context context, String email, String type) {
        return mInstance == null ? new UserVerificationBottomSheetDialog(context, email, type) : mInstance;
    }

    public UserVerificationBottomSheetDialog(@NonNull Context context, String email, String type) {
        super(context);
        mContext = context;
        mEmail = email;
        mType = type;
        create();
    }

    public void create() {
        View view = getLayoutInflater().inflate(R.layout.dialog_user_email_verification_pin, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        init();
        text_data.setText("Enter the 6 - digit code sent to you at "+mEmail);

        if(mType.equals(Constant.SIGN_UP_TEXT)){
            txt_resend_code.setVisibility(View.GONE);
        }else if(mType.equals(Constant.FORGET_PASSWORD)){
            txt_resend_code.setVisibility(View.VISIBLE);
        }

        edtPinFour.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (edtPinFour.getText().toString().trim().length() <= 0){
                    return false;
                }

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submit();
                    InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                return false;
            }


        });
    }

    private void init() {
        edtPinOne.setOnKeyListener(ForgetPasswordKeyDownListener);
        edtPinTwo.setOnKeyListener(ForgetPasswordKeyDownListener);
        edtPinThree.setOnKeyListener(ForgetPasswordKeyDownListener);
        edtPinFour.setOnKeyListener(ForgetPasswordKeyDownListener);

    }

    View.OnKeyListener ForgetPasswordKeyDownListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((keyCode == KeyEvent.KEYCODE_DEL)) {
                backPress(v);
                return true;
            } else
                return false;
        }
    };

    private void backPress(View v) {


        if (v.getId() == R.id.edt_pin_six) {
            edtPinSix.setText("");
            edtPinFive.requestFocus();
        }

        if (v.getId() == R.id.edt_pin_five) {
            edtPinFive.setText("");
            edtPinFour.requestFocus();
        }

        if (v.getId() == R.id.edt_pin_four) {
            edtPinFour.setText("");
            edtPinThree.requestFocus();
        }


        if (v.getId() == R.id.edt_pin_three) {
            edtPinThree.setText("");
            edtPinTwo.requestFocus();
        }

        if (v.getId() == R.id.edt_pin_two) {
            edtPinTwo.setText("");
            edtPinOne.requestFocus();
        }

        if (v.getId() == R.id.edt_pin_one) {
            edtPinOne.setText("");

        }

    }

    public interface UserVerificationBottomSheetCallback {
        void VerificationDialog(String code, String newEmail);

        void ResendCodeDialog(String mEmail);
    }
}
