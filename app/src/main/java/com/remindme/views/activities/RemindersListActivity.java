package com.remindme.views.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.remindme.R;
import com.remindme.models.PendingList;
import com.remindme.models.PushReminderList;
import com.remindme.services.requests.UpdateReminderRequest;
import com.remindme.services.responses.PendingRemindersResponse;
import com.remindme.services.responses.PushRemindersResponse;
import com.remindme.services.responses.Response;
import com.remindme.services.responses.UpdateReminderResponse;
import com.remindme.services.sync.UserSync;
import com.remindme.utils.Constant;
import com.remindme.views.adapters.PendingListAdapter;
import com.remindme.views.adapters.PushReminderAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RemindersListActivity extends BaseActivity {

    @BindView(R.id.ab_text_title)
    TextView abTextTitle;
    @BindView(R.id.rv_reminder_list)
    RecyclerView mRvReminderList;

    private UserSync mUserSync;
    private String mDate = null;
    private String mFromTime, mToTime;
    private Calendar now;
    private PushReminderAdapter pushReminderAdapter;

    @OnClick(R.id.btn_left_back)
    public void onClickBack(View view) {
        onBackPressed();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RemindersListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders_list);
        ButterKnife.bind(this);
        init();
        initPendingListRecyclerView();
        getReminderList();
    }

    public void init() {
        mUserSync = new UserSync(this, mRestAPI);
        now = Calendar.getInstance();
        abTextTitle.setText(Constant.REMINDER_LIST);
    }

    private void initPendingListRecyclerView() {
        pushReminderAdapter = new PushReminderAdapter(mContext, new ArrayList<PushReminderList>(), Constant.PUSH_NOTIFICATION_LIST, new PushReminderAdapter.PushReminderAdapterCallback() {
            @Override
            public void onClickSnooze(PushReminderList pendingList, int adapterPosition) {
                showDatePicker(pendingList, adapterPosition);
            }

            @Override
            public void onClickDone(PushReminderList pendingList, int adapterPosition) {
                completeReminderItem(pendingList, adapterPosition);
            }

            @Override
            public void onClickNavigation(PushReminderList pendingList, int adapterPosition) {
                openMapDirection(pendingList, adapterPosition);
            }
        });
        mRvReminderList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRvReminderList.setAdapter(pushReminderAdapter);
    }

    private void openMapDirection(PushReminderList pushReminderList, int adapterPosition) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?   saddr="
                        + pushReminderList.getShop().getLat() + "," + pushReminderList.getShop().getLng() + "&daddr="
                        + pushReminderList.getShop().getLat() + "," + pushReminderList.getShop().getLng()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    private void showDatePicker(PushReminderList pushReminderList, int adapterPosition) {

        DatePickerDialog datePicker = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                                       @Override
                                                                       public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                                                           mDate = year + "-" + new DecimalFormat("00").format((monthOfYear + 1)) + "-" + new DecimalFormat("00").format((dayOfMonth));
                                                                           showTimePickerFrom(mDate, pushReminderList, adapterPosition);
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

    private void showTimePickerFrom(String selectedDate, PushReminderList pushReminderList, int adapterPosition) {

        TimePickerDialog mTimePicker;
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        mTimePicker = new TimePickerDialog(RemindersListActivity.this, R.style.Dialog, new TimePickerDialog.OnTimeSetListener() {
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
                mFromTime = fmtOut.format(date);
                showTimePickerTo(selectedDate, mFromTime, pushReminderList, adapterPosition);

            }
        }, hour, minute, false);//No 24 hour time


        // Create a TextView programmatically.
        TextView tv = new TextView(this);

        // Create a TextView programmatically
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setPadding(10, 10, 10, 10);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        tv.setText("Select Time From");
        tv.setTextColor(Color.parseColor("#ffffff"));
        tv.setBackgroundColor(Color.parseColor("#832a5f"));
        mTimePicker.setCustomTitle(tv);

        mTimePicker.setTitle("Select Time From");

        mTimePicker.show();

    }

    private void showTimePickerTo(String selectedDate, String fromTime, PushReminderList pushReminderList, int adapterPosition) {


        TimePickerDialog mTimePicker;
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        mTimePicker = new TimePickerDialog(RemindersListActivity.this, R.style.Dialog, new TimePickerDialog.OnTimeSetListener() {
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
                mToTime = fmtOut.format(date);
                updateReminder(selectedDate, fromTime, mToTime, pushReminderList, adapterPosition);

            }
        }, hour, minute, false);//No 24 hour time

        // Create a TextView programmatically.
        TextView tv = new TextView(this);

        // Create a TextView programmatically
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setPadding(10, 10, 10, 10);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        tv.setText("Select Time To");
        tv.setTextColor(Color.parseColor("#ffffff"));
        tv.setBackgroundColor(Color.parseColor("#832a5f"));
        mTimePicker.setCustomTitle(tv);

        mTimePicker.setTitle("Select Time To");
        mTimePicker.show();

    }

    private void updateReminder(String date, String fromTime, String toTime, PushReminderList pushReminderList, int adapterPosition) {

        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        Date timeFrom = null, timeTo = null;
        try {
            timeFrom = parseFormat.parse(fromTime);
            timeTo = parseFormat.parse(toTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        showProgress();
        UpdateReminderRequest updateReminderRequest = new UpdateReminderRequest();
        updateReminderRequest.setFrom(date + " " + displayFormat.format(timeFrom));
        updateReminderRequest.setTo(date + " " + displayFormat.format(timeTo));

        mUserSync.updateReminder(mAccessToken, String.valueOf(pushReminderList.getId()), updateReminderRequest, new UserSync.UserSyncListeners.UpdateReminderCallback() {
            @Override
            public void onSuccessUpdateReminder(UpdateReminderResponse response) {
                dismissProgress();
                successMessage(Constant.COMPLETE_UPDATE);
                pushReminderAdapter.updateItem(adapterPosition, response.getData());
            }

            @Override
            public void onFailedUpdateReminder(String message, int code) {
                if (message != null) {
                    showMessageDialog(message);
                }
                dismissProgress();
            }
        });


    }


    private void getReminderList() {

        showProgress();
        mUserSync.pushReminderList(mAccessToken, new UserSync.UserSyncListeners.PushReminderCallback() {
            @Override
            public void onSuccessPushReminder(PushRemindersResponse response) {
                dismissProgress();
                pushReminderAdapter.addAll(response.getData());
            }

            @Override
            public void onFailedPushReminder(String message, int code) {
                if (message != null) {
                    showMessageDialog(message);
                }
                dismissProgress();
            }
        });
    }

    private void completeReminderItem(PushReminderList pushReminderList, int adapterPosition) {

        showProgress();
        mUserSync.completeReminder(mAccessToken, String.valueOf(pushReminderList.getId()), new UserSync.UserSyncListeners.CompleteReminderCallback() {
            @Override
            public void onSuccessCompleteReminder(Response response) {
                dismissProgress();
                successMessage(Constant.COMPLETE_REMINDER);
                pushReminderAdapter.remove(adapterPosition);
            }

            @Override
            public void onFailedCompleteReminder(String message, int code) {
                if (message != null) {
                    showMessageDialog(message);
                }
                dismissProgress();
            }
        });

    }
}
