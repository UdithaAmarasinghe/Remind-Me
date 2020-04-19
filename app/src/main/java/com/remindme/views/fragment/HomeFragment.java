package com.remindme.views.fragment;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.snackbar.Snackbar;
import com.remindme.R;
import com.remindme.models.CategoryList;
import com.remindme.utils.Constant;
import com.remindme.views.activities.AddReminderActivity;
import com.remindme.views.adapters.CategoryListAdapter;
import com.remindme.views.dialogs.ConfirmationBottomSheetDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.rv_category_list)
    RecyclerView mRvCategoryList;
    Unbinder unbinder;
    private ArrayList<CategoryList> mCategoryLists;
    private CategoryListAdapter mCategoryListAdapter;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;

    public static boolean PERMISSION_GRANTED_REQUEST = false;
    public static boolean LOCATION_REQUEST_GRANTED = false;
    private String pushType, pushTypeId;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void init() {
        initCategoryListRecyclerView();
    }

    private void initCategoryListRecyclerView() {

        mCategoryLists = new ArrayList<>();

        mCategoryLists.add(new CategoryList("1", "Banks", "#FF671A5A", R.mipmap.ic_bank_white));
        mCategoryLists.add(new CategoryList("2", "Bars", "#FF8F1D64", R.mipmap.ic_bar_white));
        mCategoryLists.add(new CategoryList("3", "Cafe", "#FFAE287C", R.mipmap.ic_coffe_white));
        mCategoryLists.add(new CategoryList("4", "Cinema", "#FFC0033F", R.mipmap.ic_cinema_white));
        mCategoryLists.add(new CategoryList("5", "Corporate", "#FFD61661", R.mipmap.ic_corporate_white));
        mCategoryLists.add(new CategoryList("6", "Education", "#FFEE2D76", R.mipmap.ic_education_white));
        mCategoryLists.add(new CategoryList("7", "Electrical", "#FFD1410A", R.mipmap.ic_electrical_white));
        mCategoryLists.add(new CategoryList("8", "Electronics", "#FFEF5217", R.mipmap.ic_electronics_white));
        mCategoryLists.add(new CategoryList("9", "Embassy", "#FFFA652E", R.mipmap.ic_emmbassy_white));
        mCategoryLists.add(new CategoryList("10", "Fashion", "#FF671A5A", R.mipmap.ic_fashion_white));
        mCategoryLists.add(new CategoryList("11", "Fast Food", "#FF8F1D64", R.mipmap.ic_fastfood_white));
        mCategoryLists.add(new CategoryList("12", "Florist", "#FFAE287C", R.mipmap.ic_florist_white));
        mCategoryLists.add(new CategoryList("13", "Fuel", "#FFC0033F", R.mipmap.ic_fuel_white));
        mCategoryLists.add(new CategoryList("14", "Government", "#FFD61661", R.mipmap.ic_government_white));
        mCategoryLists.add(new CategoryList("15", "Grocery", "#FFEE2D76", R.mipmap.ic_grocery_white));
        mCategoryLists.add(new CategoryList("16", "Hardware", "#FFD1410A", R.mipmap.ic_hardware_white));
        mCategoryLists.add(new CategoryList("17", "Health & Fitness", "#FFEF5217", R.mipmap.ic_fitness_white));
        mCategoryLists.add(new CategoryList("18", "Hospital", "#FFFA652E", R.mipmap.ic_hospital_white));
        mCategoryLists.add(new CategoryList("19", "Hotels", "#FF671A5A", R.mipmap.ic_hotel_white));
        mCategoryLists.add(new CategoryList("20", "Household Services", "#FF8F1D64", R.mipmap.ic_householdservices_white));
        mCategoryLists.add(new CategoryList("21", "Industry", "#FFAE287C", R.mipmap.ic_industry_white));
        mCategoryLists.add(new CategoryList("22", "Interior", "#FFC0033F", R.mipmap.ic_interior_white));
        mCategoryLists.add(new CategoryList("23", "Jewellery", "#FFD61661", R.mipmap.ic_jewellery_white));
        mCategoryLists.add(new CategoryList("24", "Motors", "#FFEE2D76", R.mipmap.ic_motor_white));
        mCategoryLists.add(new CategoryList("25", "Pets", "#FFD1410A", R.mipmap.ic_pet_white));
        mCategoryLists.add(new CategoryList("26", "Pharmacy", "#FFEF5217", R.mipmap.ic_pharmacy_white));
        mCategoryLists.add(new CategoryList("27", "Post Office", "#FFFA652E", R.mipmap.ic_postoffice_white));
        mCategoryLists.add(new CategoryList("28", "Police", "#FF671A5A", R.mipmap.ic_police_white));
        mCategoryLists.add(new CategoryList("29", "Professional Services", "#FF8F1D64", R.mipmap.ic_professional_white));
        mCategoryLists.add(new CategoryList("30", "Religion", "#FFAE287C", R.mipmap.ic_religion_white));
        mCategoryLists.add(new CategoryList("31", "Restaurants", "#FFC0033F", R.mipmap.ic_resturent_white));
        mCategoryLists.add(new CategoryList("32", "Retail", "#FFD61661", R.mipmap.ic_retails_white));
        mCategoryLists.add(new CategoryList("33", "Salon & Spas", "#FFEE2D76", R.mipmap.ic_salon_white));
        mCategoryLists.add(new CategoryList("34", "Supermarket", "#FFD1410A", R.mipmap.ic_supermarket_white));
        mCategoryLists.add(new CategoryList("35", "Taxi", "#FFEF5217", R.mipmap.ic_taxi_white));
        mCategoryLists.add(new CategoryList("36", "Travel", "#FFFA652E", R.mipmap.ic_travels_white));


        int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        mCategoryListAdapter = new CategoryListAdapter(mContext, mCategoryLists, width / 3, new CategoryListAdapter.CategoryListAdapterCallback() {
            @Override
            public void onClickItem(CategoryList item, int position) {
                if (checkPermissions()) {
                    showSettingDialog(item, mCategoryLists);
                   /* if(LOCATION_REQUEST_GRANTED == true){
                        getConfirmation(item, mCategoryLists);
                    }else{
                        showSettingDialog();
                    }*/
                } else if (!checkPermissions()) {
                    requestPermissions();
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        mRvCategoryList.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRvCategoryList.setHasFixedSize(true);
        mRvCategoryList.setLayoutManager(layoutManager);
        mRvCategoryList.setAdapter(mCategoryListAdapter);
        animateShadMainContainer(mRvCategoryList);
    }

    private void getConfirmation(CategoryList item, ArrayList<CategoryList> mCategoryLists) {

        ConfirmationBottomSheetDialog confirmationBottomSheetDialog = new ConfirmationBottomSheetDialog(mContext, new ConfirmationBottomSheetDialog.ConfirmationBottomSheetDialogCallback() {
            @Override
            public void onClickYes() {
                AddReminderActivity.startActivity(mContext, item, mCategoryLists, true);
            }

            @Override
            public void onClickNo() {
                AddReminderActivity.startActivity(mContext, item, null, false);
            }
        });
        confirmationBottomSheetDialog.setCancelable(true);
        confirmationBottomSheetDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void animateShadMainContainer(final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        view.setAlpha((Float) animation.getAnimatedValue());
                    }
                });
                animator.setDuration(Constant.HOME_ANIM_DELAY);
                animator.setRepeatMode(ValueAnimator.INFINITE);
                animator.setRepeatCount(0);
                animator.start();
            }
        }, 1000);

    }


    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionState2 = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * Start permissions requests.
     */
    private void requestPermissions() {

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION);


        // Provide an additional rationale to the img_user. This would happen if the img_user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale || shouldProvideRationale2) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                getActivity().findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If img_user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                PERMISSION_GRANTED_REQUEST = true;
                Log.i(TAG, "Permission granted, updates requested, starting location updates");

                showSettingDialog(null, null);


              //  startStep3();

            } else {
                PERMISSION_GRANTED_REQUEST = false;
                // Permission denied.

                // Notify the img_user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the img_user for permission (device policy or "Never ask
                // again" prompts). Therefore, a img_user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                /*showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });*/
            }
        }
    }

    private void showSettingDialog(CategoryList item, ArrayList<CategoryList> mCategoryLists) {

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();


        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        LOCATION_REQUEST_GRANTED = true;

                        if(item != null && mCategoryLists != null){
                            getConfirmation(item, mCategoryLists);
                        }
                        //Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        // updateGPSStatus("GPS is Enabled in your device");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        LOCATION_REQUEST_GRANTED = false;
                      //  Toast.makeText(getActivity(), "RESOLUTION_REQUIRED", Toast.LENGTH_SHORT).show();
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        LOCATION_REQUEST_GRANTED = false;
                      //  Toast.makeText(getActivity(), "SETTINGS_CHANGE_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


}
