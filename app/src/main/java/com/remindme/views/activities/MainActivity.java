package com.remindme.views.activities;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.navdrawer.SimpleSideDrawer;
import com.remindme.BuildConfig;
import com.remindme.R;
import com.remindme.models.User;
import com.remindme.services.requests.Location;
import com.remindme.services.requests.UserLocationRequest;
import com.remindme.services.responses.UserLocationResponse;
import com.remindme.services.sync.UserSync;
import com.remindme.utils.AppSharedPreference;
import com.remindme.utils.Constant;
import com.remindme.utils.ConstantExtras;
import com.remindme.utils.LocationMonitoringService;
import com.remindme.views.dialogs.LogoutConfirmationBottomSheetDialog;
import com.remindme.views.fragment.HomeFragment;
import com.remindme.views.fragment.PendingListFragment;
import com.remindme.views.fragment.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private boolean mAlreadyStartedService = false;

    @BindView(R.id.txt_title)
    TextView mTxtTitle;

    private SimpleSideDrawer mNav;
    private int currentPageId = -1;
    private Button navBtnHome, navBtnProfile, navBtnPending, navBtnLogout;
    private TextView userName;
    private UserSync mUserSync;
    private User mUser;
    public static boolean PERMISSION_GRANTED_REQUEST = false;
    public static boolean LOCATION_REQUEST_GRANTED = false;
    private String pushType, pushTypeId;
    private boolean peremissionRejected = false;

    @OnClick(R.id.btn_action_left)
    public void onClickMenu(View v) {
        mNav.toggleLeftDrawer();
    }

    public static void startActivityView(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);
        init();
    }

    private void init() {

        Intent intent = getIntent();
        pushType = intent.getStringExtra(ConstantExtras.ARG_PUSH_TYPE);
        pushTypeId = intent.getStringExtra(ConstantExtras.ARG_PUSH_ID);

        mNav = new SimpleSideDrawer(this);
        mUserSync = new UserSync(this, mRestAPI);
        mNav.setLeftBehindContentView(R.layout.activity_behind_left_simple);
        userName = (TextView) findViewById(R.id.name);
        userName.setText(user.getName());
        initialMenuButtons();
        setBodyContent(navBtnHome);

        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);

                        if (latitude != null && longitude != null && !latitude.equals("") && !longitude.equals("") ) {
                            userLocation(latitude, longitude);
                           // Toast.makeText(context, getString(R.string.msg_location_service_started) + "\n Latitude : " + latitude + "\n Longitude: " + longitude, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );


        /*handle push notification*/
        if(pushType != null){
            RemindersListActivity.startActivity(mContext);
        }

    }

    private void userLocation(String latitude, String longitude) {

        UserLocationRequest locationRequest = new UserLocationRequest();

        Location location = new Location();
        location.setLat(Double.parseDouble(latitude));
        location.setLng(Double.parseDouble(longitude));

        locationRequest.setLocation(location);

        mUserSync.userLocation(mAccessToken, locationRequest, new UserSync.UserSyncListeners.UserLocationCallback() {
            @Override
            public void onSuccessUserLocation(UserLocationResponse response) {
              //  Toast.makeText(mContext, "Message " + response.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailedUserLocation(String message, int code) {
                if (message != null) {
                  //  showMessageDialog(message);
                }
            }
        });

    }

    private void initialMenuButtons() {
        navBtnHome = (Button) findViewById(R.id.nav_btn_home);
        navBtnProfile = (Button) findViewById(R.id.nav_btn_profile);
        navBtnPending = (Button) findViewById(R.id.nav_btn_pending);
        navBtnLogout = (Button) findViewById(R.id.nav_btn_logout);

        navBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                mNav.closeLeftSide();
                setBodyContent(navBtnHome);
            }
        });

        navBtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                mNav.closeLeftSide();
                setBodyContent(navBtnProfile);
            }
        });

        navBtnPending.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                mNav.closeLeftSide();
                setBodyContent(navBtnPending);
            }
        });

        navBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                mNav.closeLeftSide();
                logoutBottomSheetDialog();
            }
        });
    }

    private void setBodyContent(final Button btn) {

        String title = btn.getText().toString();
        mTxtTitle.setText(title);// set menu title to actionbar title
        currentPageId = btn.getId();

        resetAllMenuItemIconAndTextColor();
        changeButtonIconAndTextColor(btn, R.color.purple_menu_item_drawable_selected);

        final Class finalFragmentClass = getFragmentClassByNavItem(btn);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                replaceFragment(finalFragmentClass, btn);
            }
        }, 0);
    }

    private Class getFragmentClassByNavItem(View view) {

        Class fragmentClass = null;
        switch (view.getId()) {
            case R.id.nav_btn_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_btn_profile:
                fragmentClass = ProfileFragment.class;
                break;
            case R.id.nav_btn_pending:
                  fragmentClass = PendingListFragment.class;
                break;
            case R.id.nav_btn_logout:
                break;

        }
        return fragmentClass;
    }

    private void changeButtonIconAndTextColor(Button btn, @ColorRes int textColor) {
        btn.setTextColor(ActivityCompat.getColor(mContext, textColor));
    }

    private void replaceFragment(Class fragmentClass, Button btn) {
        Fragment fragment = null;
        try {

            fragment = (Fragment) fragmentClass.newInstance();
            Bundle args = new Bundle();
            // args.putSerializable(HOME_INIT_DATA, homeData);
            fragment.setArguments(args);

            FragmentManager fm = getSupportFragmentManager();

            fm.beginTransaction()
                    .replace(R.id.container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    //.addToBackStack(null)
                    .commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void logoutBottomSheetDialog() {

        LogoutConfirmationBottomSheetDialog logoutConfirmationBottomSheetDialog = new LogoutConfirmationBottomSheetDialog(mContext, new LogoutConfirmationBottomSheetDialog.LogoutConfirmationBottomSheetDialogCallback() {
            @Override
            public void onClickLogOut() {
                AppSharedPreference mSharedPreferences = new AppSharedPreference(mContext);
                mSharedPreferences.clearSharefPreference();
                startActivity(new Intent(mContext, SplashActivity.class));
                finish();
            }
        });
        logoutConfirmationBottomSheetDialog.setCancelable(true);
        logoutConfirmationBottomSheetDialog.show();
    }

    private void resetAllMenuItemIconAndTextColor() {
        changeButtonIconAndTextColor(navBtnHome, R.color.app_white);
        changeButtonIconAndTextColor(navBtnProfile, R.color.app_white);
        changeButtonIconAndTextColor(navBtnPending, R.color.app_white);
        changeButtonIconAndTextColor(navBtnLogout, R.color.app_white);
    }

    @Override
    public void onResume() {
        super.onResume();

        startStep1();
    }


    private void startStep1() {

        //Check whether this user has installed Google play service which is being used by Location updates.
        if (isGooglePlayServicesAvailable()) {

            //Passing null to indicate that it is executing for the first time.
            startStep2(null);

        } else {
            Toast.makeText(getApplicationContext(), "no_google_playservice_available", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Step 2: Check & Prompt Internet connection
     */
    private Boolean startStep2(DialogInterface dialog) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            promptInternetConnect();
            return false;
        }


        if (dialog != null) {
            dialog.dismiss();
        }

        //Yes there is active internet connection. Next check Location is granted by user or not.

        if (checkPermissions()) { //Yes permissions are granted by the user. Go to the next step.
            startStep3();
        } else {  //No user has not granted the permissions yet. Request now.
            requestPermissions();
        }
        return true;
    }

    /**
     * Show A Dialog with button to refresh the internet state.
     */
    private void promptInternetConnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Title");
        builder.setMessage("Alert");

        String positiveText = getString(R.string.app_name);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //Block the Application Execution until user grants the permissions
                        if (startStep2(dialog)) {

                            //Now make sure about location permission.
                            if (checkPermissions()) {

                                //Step 2: Start the Location Monitor Service
                                //Everything is there to start the service.
                                startStep3();
                            } else if (!checkPermissions()) {
                                requestPermissions();
                            }

                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Step 3: Start the Location Monitor Service
     */
    private void startStep3() {

        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.

        if (!mAlreadyStartedService) {

            //  mMsgView.setText(R.string.msg_location_service_started);

            //Start location sharing service to app server.........
            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);

            mAlreadyStartedService = true;
            //Ends................................................
        }
    }

    /**
     * Return the availability of GooglePlayServices
     */
    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionState2 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * Start permissions requests.
     */
    private void requestPermissions() {

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
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
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                        REQUEST_PERMISSIONS_REQUEST_CODE);
                            }
                        });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
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

        if (peremissionRejected) {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(mainTextStringId),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(actionStringId), listener).show();

        }
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

                showSettingDialog();

                startStep3();

            } else {
                PERMISSION_GRANTED_REQUEST = false;
                peremissionRejected = true;
                // Permission denied.

                showSnackbar(R.string.permission_denied_explanation,
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
                        });

            }
        }
    }

    private void showSettingDialog() {


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
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        // updateGPSStatus("GPS is Enabled in your device");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        LOCATION_REQUEST_GRANTED = false;
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        LOCATION_REQUEST_GRANTED = false;
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    @Override
    public void onDestroy() {


        //Stop location sharing service to app server.........

        stopService(new Intent(this, LocationMonitoringService.class));
        mAlreadyStartedService = false;
        //Ends................................................


        super.onDestroy();
    }
}
