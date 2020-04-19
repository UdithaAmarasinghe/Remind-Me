package com.remindme.views.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.remindme.R;
import com.remindme.models.CategoryList;
import com.remindme.models.Reminders;
import com.remindme.views.adapters.RemindersListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("ValidFragment")
public class ReminderListBottomSheetDialog extends BottomSheetDialog {

    @BindView(R.id.rv_reminder_list)
    RecyclerView mRvReminderList;

    private Context mContext;
    private static ReminderListBottomSheetDialog mInstance;
    ArrayList<Reminders> mReminders;
    private ArrayList<CategoryList> mCategoryLists;
    private RemindersListAdapter remindersListAdapter;
    private ReminderListBottomSheetDialog.ReminderListBottomSheetDialogCallback callback;

    @OnClick(R.id.btn_cancel)
    public void onClickCancel(View v) {
        dismiss();
        callback.onClickUpdate(remindersListAdapter.getDataSet());
    }

    public static ReminderListBottomSheetDialog getInstance(@NonNull Context context, ArrayList<Reminders> reminders, ReminderListBottomSheetDialog.ReminderListBottomSheetDialogCallback reminderListBottomSheetDialogCallback) {
        return mInstance == null ? new ReminderListBottomSheetDialog(context, reminders, reminderListBottomSheetDialogCallback) : mInstance;
    }

    public ReminderListBottomSheetDialog(@NonNull Context context, ArrayList<Reminders> reminders, ReminderListBottomSheetDialogCallback reminderListBottomSheetDialogCallback) {
        super(context);
        mContext = context;
        mReminders = reminders;
        callback = reminderListBottomSheetDialogCallback;
        create();
    }

    public void create() {
        View view = getLayoutInflater().inflate(R.layout.dialog_reminder_list, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        initRemindersListRecyclerView();
    }


    private void initRemindersListRecyclerView() {

        remindersListAdapter = new RemindersListAdapter(mContext, new ArrayList<Reminders>(), new RemindersListAdapter.RemindersListAdapterCallback() {
            @Override
            public void onClickRemove(int position, Reminders product) {
                remindersListAdapter.remove(position);
                if(remindersListAdapter.getDataSet().size() == 0){
                    dismiss();
                    callback.onClickUpdate(remindersListAdapter.getDataSet());
                }
            }
        });
        mRvReminderList.setHasFixedSize(true);
        mRvReminderList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRvReminderList.setAdapter(remindersListAdapter);
        remindersListAdapter.addAll(mReminders);
    }

    public interface ReminderListBottomSheetDialogCallback {
        void onClickUpdate(ArrayList<Reminders> mReminders);
    }

}
