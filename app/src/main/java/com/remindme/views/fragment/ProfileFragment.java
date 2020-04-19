package com.remindme.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.remindme.R;
import com.remindme.services.requests.UserUpdateRequest;
import com.remindme.services.responses.UserUpdateResponse;
import com.remindme.services.sync.UserSync;
import com.remindme.utils.Constant;
import com.remindme.views.activities.BaseActivity;
import com.remindme.views.activities.LoginActivity;
import com.remindme.views.dialogs.SuccessBottomSheetDialog;
import com.remindme.views.dialogs.UserTypeBottomSheetDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.edt_first_name)
    EditText edtFirstName;
    @BindView(R.id.edt_last_name)
    EditText edtLastName;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.radio_male)
    RadioButton radio_male;
    @BindView(R.id.radio_female)
    RadioButton radio_female;


    private View view;
    private UserSync mUserSync;
    private Context mContext;
    private String mSelectedGender = "";

    @OnClick(R.id.btn_update)
    public void onClickUpdate(View view) {

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setFirstName(edtFirstName.getText().toString());
        updateRequest.setLastName(edtLastName.getText().toString());
        updateRequest.setGender(mSelectedGender);
        updateRequest.setTitle(txtTitle.getText().toString());

        showProgress();
        mUserSync.userUpdate(mAccessToken, updateRequest, new UserSync.UserSyncListeners.UserUpdateCallback() {
            @Override
            public void onSuccessUserUpdate(UserUpdateResponse response) {
                dismissProgress();
                successBottomSheet();
            }

            @Override
            public void onFailedUserUpdate(String message, int code) {
                if (message != null) {
                    showMessageDialog(message);
                }
                dismissProgress();
            }
        });

    }

    private void successBottomSheet() {

        SuccessBottomSheetDialog bottomSheetDialogFragment = new SuccessBottomSheetDialog(mContext, Constant.USER_UPDATE);
        bottomSheetDialogFragment.setListener(new SuccessBottomSheetDialog.SuccessDialog() {
            @Override
            public void SuccessBottomSheetDialogPopup() {

            }
        });
        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @OnClick(R.id.txt_title)
    public void onClickUserType(View view) {
        UserTypeBottomSheetDialog userTypeBottomSheetDialog = new UserTypeBottomSheetDialog(mContext, txtTitle.getText().toString(), new UserTypeBottomSheetDialog.UserTypeBottomSheetDialogCallback() {
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

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void init() {
        mUserSync = new UserSync(mContext, mRestAPI);
        mSelectedGender = user.getGender();

        if("Male".equals(mSelectedGender)){
            radio_male.setChecked(true);
        }else if("Female".equals(mSelectedGender)){
            radio_female.setChecked(true);
        }

        txtTitle.setText(user.getTitle());
        edtFirstName.setText(user.getFirstName());
        edtLastName.setText(user.getLastName());
        edtEmail.setText(user.getEmail());
    }

}
