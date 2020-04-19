package com.remindme.views.activities;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.irozon.sneaker.Sneaker;
import com.remindme.R;
import com.remindme.models.CategoryList;
import com.remindme.models.ItemList;
import com.remindme.models.Reminders;
import com.remindme.services.responses.AddReminderResponse;
import com.remindme.services.responses.ItemListResponse;
import com.remindme.services.sync.UserSync;
import com.remindme.utils.Constant;
import com.remindme.utils.ConstantExtras;
import com.remindme.views.dialogs.CommonBottomSheetDialog;
import com.remindme.views.dialogs.ReminderListBottomSheetDialog;
import com.remindme.views.dialogs.SuccessBottomSheetDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.remindme.utils.ConstantExtras.REQUEST_CODE_SEARCH_ITEM_VIEW;
import static com.remindme.utils.ConstantExtras.REQUEST_CODE_SEARCH_VIEW;

public class AddReminderActivity extends BaseActivity {

    @BindView(R.id.ab_text_title)
    TextView abTextTitle;
    @BindView(R.id.txt_priority)
    TextView txtPriority;
    @BindView(R.id.txt_radius)
    TextView txtRadius;
    @BindView(R.id.txt_category)
    TextView txtCategory;
    @BindView(R.id.txt_to)
    TextView txtTo;
    @BindView(R.id.txt_from)
    TextView txtFrom;
    @BindView(R.id.txt_item)
    TextView txtItem;
    @BindView(R.id.txt_remark)
    TextView txtRemark;
    @BindView(R.id.txt_view_reminder_list)
    TextView txtViewReminderList;
    @BindView(R.id.btn_add_list)
    Button btnAddList;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.txt_date)
    TextView txtDate;

    private UserSync mUserSync;
    private CategoryList mSelectedCategory;
    private ItemList mItemList;
    private boolean mIsMultipleReminders;
    private ArrayList<CategoryList> mCategoryLists;
    private CommonBottomSheetDialog commonBottomSheetDialog;
    private ArrayList<Reminders> mReminders;
    private ArrayList<ItemList> mItemLists;
    private String formattedTime;
    private Calendar now;
    private String mDate = null;

    @OnClick(R.id.btn_left_back)
    public void onClickBack(View view) {
        onBackPressed();
    }

    @OnClick(R.id.btn_save)
    public void onClickSave(View view) {

        showProgress();
        mUserSync.addReminders(mAccessToken, mReminders, new UserSync.UserSyncListeners.AddRemindersCallback() {
            @Override
            public void onSuccessAddReminders(AddReminderResponse response) {
                dismissProgress();
                successBottomSheetDialog();
            }

            @Override
            public void onFailedAddReminders(String message, int code) {
                if (message != null) {
                    showMessageDialog(message);
                }
                dismissProgress();
            }
        });
    }

    @OnClick(R.id.txt_date)
    public void onClickDate(View view) {
        showDatePicker();
    }

    private void showDatePicker() {

        DatePickerDialog datePicker = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                                       @Override
                                                                       public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                                                           mDate = year + "-" + new DecimalFormat("00").format((monthOfYear + 1)) + "-" + new DecimalFormat("00").format((dayOfMonth));
                                                                           txtDate.setText(mDate);
                                                                       }
                                                                   },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        Calendar calendar = Calendar.getInstance();
        datePicker.setMinDate(calendar);
        datePicker.setAccentColor(getResources().getColor(R.color.app_dark_maroon_actionbar));
        datePicker.show(getFragmentManager(), "Datepickerdialog");

    }

    private void successBottomSheetDialog() {
        SuccessBottomSheetDialog bottomSheetDialogFragment = new SuccessBottomSheetDialog(mContext, Constant.REMINDER_ADD);
        bottomSheetDialogFragment.setListener(new SuccessBottomSheetDialog.SuccessDialog() {
            @Override
            public void SuccessBottomSheetDialogPopup() {
                onBackPressed();
            }
        });
        bottomSheetDialogFragment.show(this.getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @OnClick(R.id.btn_add_list)
    public void onClickAddList(View view) {

        if(!txtCategory.getText().toString().isEmpty() && mSelectedCategory != null && !txtItem.getText().toString().isEmpty() && !txtDate.getText().toString().isEmpty() &&
                !txtFrom.getText().toString().isEmpty() && !txtTo.getText().toString().isEmpty()) {


            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
            Date timeFrom = null, timeTo = null;
            try {
                timeFrom = parseFormat.parse(txtFrom.getText().toString());
                timeTo = parseFormat.parse(txtTo.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Reminders reminders = new Reminders();
            reminders.setCategoryId(mSelectedCategory.getId());
            reminders.setCategory(txtCategory.getText().toString());
            reminders.setItemId(String.valueOf(mItemList.getId()));
            reminders.setItem(txtItem.getText().toString());
            reminders.setPriority(txtPriority.getText().toString());
            reminders.setRadius(txtRadius.getText().toString().replace("KM", ""));
            reminders.setRemark(txtRemark.getText().toString());
            reminders.setFrom(txtDate.getText().toString() + " " + displayFormat.format(timeFrom));
            reminders.setTo(txtDate.getText().toString() + " " + displayFormat.format(timeTo));

            mReminders.add(reminders);

            successMessage(Constant.ADD_REMINDERS_LIST);
            txtViewReminderList.setClickable(true);
            txtViewReminderList.setTextColor(getResources().getColor(R.color.app_black));
            btnSave.setVisibility(View.VISIBLE);

            txtCategory.setText("");
            txtItem.setText("");
            txtFrom.setText("");
            txtTo.setText("");
            txtPriority.setText("");
            txtRemark.setText("");
            txtRadius.setText("");

        }else{
            warningMessage("Fill all required fields!");
        }


    }

    @OnClick(R.id.txt_view_reminder_list)
    public void onClickReminderList(View view) {

        if (!mReminders.isEmpty()) {
            ReminderListBottomSheetDialog reminderListBottomSheetDialog = new ReminderListBottomSheetDialog(mContext, mReminders, new ReminderListBottomSheetDialog.ReminderListBottomSheetDialogCallback() {
                @Override
                public void onClickUpdate(ArrayList<Reminders> reminders) {
                    mReminders = reminders;
                    if (mReminders.isEmpty()) {
                        txtViewReminderList.setClickable(false);
                        txtViewReminderList.setTextColor(getResources().getColor(R.color.app_disabled));
                        btnSave.setVisibility(View.GONE);
                    }
                }
            });
            reminderListBottomSheetDialog.setCancelable(false);
            reminderListBottomSheetDialog.show();
        } else {
            warningMessage(Constant.ADD_REMINDERS);
        }
    }

    @OnClick(R.id.txt_category)
    public void onClickCategory(View view) {
        if (mIsMultipleReminders) {
            startSearchActivity();
        }
    }

    @OnClick(R.id.txt_item)
    public void onClickItem(View view) {
        if (mItemLists != null) {
            startSearchItemListActivity();
        } else {
            warningMessage(Constant.SELECT_CATEGORY);
        }
    }

    @OnClick({R.id.txt_from, R.id.txt_to})
    public void onClickTime(View view) {

        TimePickerDialog mTimePicker;
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        mTimePicker = new TimePickerDialog(AddReminderActivity.this,  R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String time = selectedHour + ":" + selectedMinute;
                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                Date date = null;
                try {
                    date = fmt.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");
                formattedTime = fmtOut.format(date);

                if (view.getId() == R.id.txt_from) {
                    txtFrom.setText(formattedTime);
                } else if (view.getId() == R.id.txt_to) {
                    txtTo.setText(formattedTime);
                }

            }
        }, hour, minute, false);//No 24 hour time
        mTimePicker.show();

    }

    @OnClick(R.id.txt_radius)
    public void onClickRadius(View view) {
        commonBottomSheetDialog = new CommonBottomSheetDialog(mContext, Constant.RADIUS, txtRadius.getText().toString(), new CommonBottomSheetDialog.CommonBottomSheetDialogCallback() {
            @Override
            public void onClickType(String id, String label) {
                txtRadius.setText(label);
            }
        });
        commonBottomSheetDialog.setCancelable(true);
        commonBottomSheetDialog.show();
    }

    @OnClick(R.id.txt_priority)
    public void onClickPriority(View view) {
        commonBottomSheetDialog = new CommonBottomSheetDialog(mContext, Constant.PRIORITY, txtPriority.getText().toString(), new CommonBottomSheetDialog.CommonBottomSheetDialogCallback() {
            @Override
            public void onClickType(String id, String label) {
                txtPriority.setText(label);
            }
        });
        commonBottomSheetDialog.setCancelable(true);
        commonBottomSheetDialog.show();
    }

    public static void startActivity(Context context, CategoryList item, ArrayList<CategoryList> mCategoryLists, boolean isMultipleReminders) {
        Intent intent = new Intent(context, AddReminderActivity.class);
        intent.putExtra(ConstantExtras.ARG_CATEGORY, item);
        intent.putExtra(ConstantExtras.ARG_CATEGORY_LIST, mCategoryLists);
        intent.putExtra(ConstantExtras.ARG_MULTIPLE_REMINDERS, isMultipleReminders);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        ButterKnife.bind(this);
        mUserSync = new UserSync(this, mRestAPI);
        now = Calendar.getInstance();
        init();
        loadItems(mSelectedCategory.getId());
        mReminders = new ArrayList<>();
        txtViewReminderList.setClickable(false);
    }

    private void init() {
        mSelectedCategory = (CategoryList) getIntent().getSerializableExtra(ConstantExtras.ARG_CATEGORY);
        mIsMultipleReminders = (boolean) getIntent().getSerializableExtra(ConstantExtras.ARG_MULTIPLE_REMINDERS);
        mCategoryLists = (ArrayList<CategoryList>) getIntent().getSerializableExtra(ConstantExtras.ARG_CATEGORY_LIST);
        txtCategory.setText(mSelectedCategory.getName());
        if (mIsMultipleReminders) {
            abTextTitle.setText("Add Multiple Reminders");
            btnAddList.setVisibility(View.VISIBLE);
        } else {
            abTextTitle.setText(mSelectedCategory.getName());
            btnAddList.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
        }
    }

    private void loadItems(String id) {

        showProgress();
        mUserSync.getItemList(mAccessToken, id, new UserSync.UserSyncListeners.GetItemListCallback() {
            @Override
            public void onSuccessGetItemList(ItemListResponse response) {
                dismissProgress();
                mItemLists = response.getData();
            }

            @Override
            public void onFailedGetItemList(String message, int code) {
                if (message != null) {
                    showMessageDialog(message);
                }
                dismissProgress();
            }
        });
    }

    private void startSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(ConstantExtras.ARG_CATEGORY_LIST, mCategoryLists);
        intent.putExtra(ConstantExtras.ARG_SEARCH_CODE, ConstantExtras.RESULT_CODE_NAME);
        startActivityForResult(intent, REQUEST_CODE_SEARCH_VIEW);
    }

    private void startSearchItemListActivity() {
        Intent intent = new Intent(this, SearchItemActivity.class);
        intent.putExtra(ConstantExtras.ARG_ITEM_LIST, mItemLists);
        intent.putExtra(ConstantExtras.ARG_SEARCH_CODE, ConstantExtras.RESULT_CODE_NAME);
        startActivityForResult(intent, REQUEST_CODE_SEARCH_ITEM_VIEW);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SEARCH_VIEW) {
            if (data == null) return;
            if (resultCode == ConstantExtras.RESULT_CODE_NAME) {
                mSelectedCategory = (CategoryList) data.getSerializableExtra(ConstantExtras.ARG_SEARCH_RESULT);
                txtCategory.setText(mSelectedCategory.getName());
                loadItems(mSelectedCategory.getId());
            }
        } else if (requestCode == REQUEST_CODE_SEARCH_ITEM_VIEW) {
            if (data == null) return;
            mItemList = (ItemList) data.getSerializableExtra(ConstantExtras.ARG_SEARCH_RESULT);
            txtItem.setText(mItemList.getName());
        }
    }
}