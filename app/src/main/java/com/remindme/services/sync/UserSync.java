package com.remindme.services.sync;

import android.content.Context;

import com.remindme.models.Reminders;
import com.remindme.models.Token;
import com.remindme.models.User;
import com.remindme.services.RestAPI;
import com.remindme.services.requests.LoginRequest;
import com.remindme.services.requests.PasswordChangeRequest;
import com.remindme.services.requests.PasswordResetOTPRequest;
import com.remindme.services.requests.UpdateReminderRequest;
import com.remindme.services.requests.UserLocationRequest;
import com.remindme.services.requests.UserRegister;
import com.remindme.services.requests.UserUpdateRequest;
import com.remindme.services.requests.VerifyEmailRequest;
import com.remindme.services.responses.AddReminderResponse;
import com.remindme.services.responses.ForgetPasswordResponse;
import com.remindme.services.responses.ItemListResponse;
import com.remindme.services.responses.PendingRemindersResponse;
import com.remindme.services.responses.PushRemindersResponse;
import com.remindme.services.responses.TokenResponse;
import com.remindme.services.responses.UpdateReminderResponse;
import com.remindme.services.responses.UserDetails;
import com.remindme.services.responses.UserLocationResponse;
import com.remindme.services.responses.UserRegistrationResponse;
import com.remindme.services.responses.UserUpdateResponse;
import com.remindme.utils.AppSharedPreference;
import com.remindme.utils.Constant;
import com.remindme.utils.NetworkAccess;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.remindme.views.activities.BaseActivity.mPreferences;

public class UserSync extends AuthenticationSync {

    private static final String TAG = "UserSync";
    private final AppSharedPreference mPreferences;

    public UserSync(Context mContext, RestAPI restAPI) {
        super(mContext, restAPI);

        mPreferences = new AppSharedPreference(mContext);
    }


    public void userLogin(LoginRequest request, final UserSyncListeners.UserLoginCallback callback) {

        if (!NetworkAccess.isNetworkAvailable(mContext)) {
            callback.onFailedUserLogin(null, Constant.NO_INTERNET_CODE);
            return;
        }

        mRestAPI.userLogin(request).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, final Response<TokenResponse> response) {
                try {

                    if (response.code() == Constant.CODE_SUCCESS) {
                        if (response.body() != null) {
                            mPreferences.setToken(response.body().getData().getAccessToken());
                            mPreferences.setRefershToken(response.body().getData().getAccessToken());
                            callback.onSuccessUserLogin(response.body());

                        } else {
                            callback.onFailedUserLogin(response.body().getMessage(), Constant.NO_RESPONSE_ERROR_CODE);
                        }

                    } else if (response.code() == Constant.BAD_REQUEST_CODE) {
                        callback.onFailedUserLogin(Constant.INVALID_LOGIN, Constant.NO_TECHNICAL_CODE);
                    }


                } catch (Exception e) {
                    System.out.println(" Error " + e.getMessage());
                    callback.onFailedUserLogin(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
                }

            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                callback.onFailedUserLogin(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
            }
        });
    }


    public void userRegistration(UserRegister request, final UserSyncListeners.UserRegistrationCallback callback) {

        if (!NetworkAccess.isNetworkAvailable(mContext)) {
            callback.onFailedUserRegistration(null, Constant.NO_INTERNET_CODE);
            return;
        }

        mRestAPI.userRegistration(request).enqueue(new Callback<UserRegistrationResponse>() {
            @Override
            public void onResponse(Call<UserRegistrationResponse> call, final Response<UserRegistrationResponse> response) {
                try {

                    if (response.code() == Constant.CODE_SUCCESS) {
                        if (response.body() != null) {
                            callback.onSuccessUserRegistration(response.body());

                        } else {
                            callback.onFailedUserRegistration(response.body().getMessage(), Constant.NO_RESPONSE_ERROR_CODE);
                        }

                    } else {
                        callback.onFailedUserRegistration(response.body().getMessage(), Constant.NO_TECHNICAL_CODE);
                    }


                } catch (Exception e) {
                    System.out.println(" Error " + e.getMessage());
                    callback.onFailedUserRegistration(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
                }

            }

            @Override
            public void onFailure(Call<UserRegistrationResponse> call, Throwable t) {
                callback.onFailedUserRegistration(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
            }
        });
    }


    public void userEmailVerify(VerifyEmailRequest emailRequest, final UserSyncListeners.UserEmailVerifyCallback callback) {

        if (!NetworkAccess.isNetworkAvailable(mContext)) {
            callback.onFailedEmailVerify(null, Constant.NO_INTERNET_CODE);
            return;
        }

        mRestAPI.verifyEmail(emailRequest).enqueue(new Callback<com.remindme.services.responses.Response>() {
            @Override
            public void onResponse(Call<com.remindme.services.responses.Response> call, final Response<com.remindme.services.responses.Response> response) {
                try {

                    if (response.code() == Constant.CODE_SUCCESS) {
                        if (response.body() != null) {
                            callback.onSuccessEmailVerify(response.body());

                        } else {
                            callback.onFailedEmailVerify(response.body().getMessage(), Constant.NO_RESPONSE_ERROR_CODE);
                        }

                    } else {
                        callback.onFailedEmailVerify(response.body().getMessage(), Constant.NO_TECHNICAL_CODE);
                    }


                } catch (Exception e) {
                    System.out.println(" Error " + e.getMessage());
                    callback.onFailedEmailVerify(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
                }

            }

            @Override
            public void onFailure(Call<com.remindme.services.responses.Response> call, Throwable t) {
                callback.onFailedEmailVerify(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
            }
        });
    }


    public void getUserData(String token, final UserSyncListeners.UserDetailsCallback callback) {

        if (!NetworkAccess.isNetworkAvailable(mContext)) {
            callback.onFailedUserDetails(null, Constant.NO_INTERNET_CODE);
            return;
        }

        mRestAPI.getUserData(token).enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, final Response<UserDetails> response) {
                try {

                    if (response.code() == Constant.CODE_SUCCESS) {
                        if (response.body() != null) {
                            User user = new User(response.body().getData().getId(), response.body().getData().getName(), response.body().getData().getEmail(), response.body().getData().getTitle(), response.body().getData().getFirstName(), response.body().getData().getLastName(), response.body().getData().getGender());
                            mPreferences.setUser(user);
                            callback.onSuccessUserDetails(response.body());

                        } else {
                            callback.onFailedUserDetails(response.body().getMessage(), Constant.NO_RESPONSE_ERROR_CODE);
                        }
                    } else {
                        callback.onFailedUserDetails(response.body().getMessage(), Constant.NO_TECHNICAL_CODE);
                    }
                } catch (Exception e) {
                    System.out.println(" Error " + e.getMessage());
                    callback.onFailedUserDetails(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                callback.onFailedUserDetails(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
            }
        });
    }

    public void getItemList(String token, String id, final UserSyncListeners.GetItemListCallback callback) {

        if (!NetworkAccess.isNetworkAvailable(mContext)) {
            callback.onFailedGetItemList(null, Constant.NO_INTERNET_CODE);
            return;
        }

        mRestAPI.getItemList(token, id).enqueue(new Callback<ItemListResponse>() {
            @Override
            public void onResponse(Call<ItemListResponse> call, final Response<ItemListResponse> response) {
                try {

                    if (response.code() == Constant.CODE_SUCCESS) {
                        if (response.body() != null) {
                            callback.onSuccessGetItemList(response.body());

                        } else {
                            callback.onFailedGetItemList(response.body().getMessage(), Constant.NO_RESPONSE_ERROR_CODE);
                        }
                    } else {
                        callback.onFailedGetItemList(response.body().getMessage(), Constant.NO_TECHNICAL_CODE);
                    }
                } catch (Exception e) {
                    System.out.println(" Error " + e.getMessage());
                    callback.onFailedGetItemList(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
                }
            }

            @Override
            public void onFailure(Call<ItemListResponse> call, Throwable t) {
                callback.onFailedGetItemList(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
            }
        });
    }


    public void addReminders(String token, ArrayList<Reminders> reminders, final UserSyncListeners.AddRemindersCallback callback) {

        if (!NetworkAccess.isNetworkAvailable(mContext)) {
            callback.onFailedAddReminders(null, Constant.NO_INTERNET_CODE);
            return;
        }

        mRestAPI.addReminders(token, reminders).enqueue(new Callback<AddReminderResponse>() {
            @Override
            public void onResponse(Call<AddReminderResponse> call, final Response<AddReminderResponse> response) {
                try {

                    if (response.code() == Constant.CODE_SUCCESS) {
                        if (response.body() != null) {
                            callback.onSuccessAddReminders(response.body());

                        } else {
                            callback.onFailedAddReminders(response.body().getMessage(), Constant.NO_RESPONSE_ERROR_CODE);
                        }
                    } else {
                        callback.onFailedAddReminders(response.body().getMessage(), Constant.NO_TECHNICAL_CODE);
                    }
                } catch (Exception e) {
                    System.out.println(" Error " + e.getMessage());
                    callback.onFailedAddReminders(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
                }
            }

            @Override
            public void onFailure(Call<AddReminderResponse> call, Throwable t) {
                callback.onFailedAddReminders(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
            }
        });
    }


    public void userLocation(String token, UserLocationRequest locationRequest, final UserSyncListeners.UserLocationCallback callback) {

        if (!NetworkAccess.isNetworkAvailable(mContext)) {
            callback.onFailedUserLocation(null, Constant.NO_INTERNET_CODE);
            return;
        }

        mRestAPI.userLocation(token, locationRequest).enqueue(new Callback<UserLocationResponse>() {
            @Override
            public void onResponse(Call<UserLocationResponse> call, final Response<UserLocationResponse> response) {
                try {

                    if (response.code() == Constant.CODE_SUCCESS) {
                        if (response.body() != null) {
                            callback.onSuccessUserLocation(response.body());

                        } else {
                            callback.onFailedUserLocation(response.body().getMessage(), Constant.NO_RESPONSE_ERROR_CODE);
                        }
                    } else {
                        callback.onFailedUserLocation(response.body().getMessage(), Constant.NO_TECHNICAL_CODE);
                    }
                } catch (Exception e) {
                    System.out.println(" Error " + e.getMessage());
                    callback.onFailedUserLocation(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
                }
            }

            @Override
            public void onFailure(Call<UserLocationResponse> call, Throwable t) {
                callback.onFailedUserLocation(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
            }
        });
    }


    public void forgetPasswordOTP(PasswordResetOTPRequest otpRequest, final UserSyncListeners.UserForgetPasswordOTPCallback callback) {

        if (!NetworkAccess.isNetworkAvailable(mContext)) {
            callback.onFailedForgetPasswordOTP(null, Constant.NO_INTERNET_CODE);
            return;
        }

        mRestAPI.forgetPasswordOTP(otpRequest).enqueue(new Callback<ForgetPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgetPasswordResponse> call, final Response<ForgetPasswordResponse> response) {
                try {

                    if (response.code() == Constant.CODE_SUCCESS) {
                        if (response.body() != null) {
                            callback.onSuccessForgetPasswordOTP(response.body());

                        } else {
                            callback.onFailedForgetPasswordOTP(response.body().getMessage(), Constant.NO_RESPONSE_ERROR_CODE);
                        }
                    } else {
                        callback.onFailedForgetPasswordOTP(response.body().getMessage(), Constant.NO_TECHNICAL_CODE);
                    }
                } catch (Exception e) {
                    System.out.println(" Error " + e.getMessage());
                    callback.onFailedForgetPasswordOTP(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
                }
            }

            @Override
            public void onFailure(Call<ForgetPasswordResponse> call, Throwable t) {
                callback.onFailedForgetPasswordOTP(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
            }
        });
    }

    public void passwordChange(PasswordChangeRequest passwordChangeRequest, final UserSyncListeners.ChangePasswordCallback callback) {

        if (!NetworkAccess.isNetworkAvailable(mContext)) {
            callback.onFailedChangePassword(null, Constant.NO_INTERNET_CODE);
            return;
        }

        mRestAPI.passwordChange(passwordChangeRequest).enqueue(new Callback<com.remindme.services.responses.Response>() {
            @Override
            public void onResponse(Call<com.remindme.services.responses.Response> call, final Response<com.remindme.services.responses.Response> response) {
                try {

                    if (response.code() == Constant.CODE_SUCCESS) {
                        if (response.body() != null) {
                            callback.onSuccessChangePassword(response.body());

                        } else {
                            callback.onFailedChangePassword(response.body().getMessage(), Constant.NO_RESPONSE_ERROR_CODE);
                        }
                    } else {
                        callback.onFailedChangePassword(response.body().getMessage(), Constant.NO_TECHNICAL_CODE);
                    }
                } catch (Exception e) {
                    System.out.println(" Error " + e.getMessage());
                    callback.onFailedChangePassword(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
                }
            }

            @Override
            public void onFailure(Call<com.remindme.services.responses.Response> call, Throwable t) {
                callback.onFailedChangePassword(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
            }
        });
    }


    public void userUpdate(String token, UserUpdateRequest userUpdateRequest, final UserSyncListeners.UserUpdateCallback callback) {

        if (!NetworkAccess.isNetworkAvailable(mContext)) {
            callback.onFailedUserUpdate(null, Constant.NO_INTERNET_CODE);
            return;
        }

        mRestAPI.userUpdate(token, userUpdateRequest).enqueue(new Callback<UserUpdateResponse>() {
            @Override
            public void onResponse(Call<UserUpdateResponse> call, final Response<UserUpdateResponse> response) {
                try {

                    if (response.code() == Constant.CODE_SUCCESS) {
                        if (response.body() != null) {
                            User user = new User(response.body().getData().getId(), response.body().getData().getName(), response.body().getData().getEmail(), response.body().getData().getTitle(), response.body().getData().getFirstName(), response.body().getData().getLastName(), response.body().getData().getGender());
                            mPreferences.setUser(user);
                            callback.onSuccessUserUpdate(response.body());

                        } else {
                            callback.onFailedUserUpdate(response.body().getMessage(), Constant.NO_RESPONSE_ERROR_CODE);
                        }
                    } else {
                        callback.onFailedUserUpdate(response.body().getMessage(), Constant.NO_TECHNICAL_CODE);
                    }
                } catch (Exception e) {
                    System.out.println(" Error " + e.getMessage());
                    callback.onFailedUserUpdate(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
                }
            }

            @Override
            public void onFailure(Call<UserUpdateResponse> call, Throwable t) {
                callback.onFailedUserUpdate(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
            }
        });
    }


    public void pendingList(String token, final UserSyncListeners.UserPendingListCallback callback) {

        if (!NetworkAccess.isNetworkAvailable(mContext)) {
            callback.onFailedPendingList(null, Constant.NO_INTERNET_CODE);
            return;
        }

        mRestAPI.pendingList(token).enqueue(new Callback<PendingRemindersResponse>() {
            @Override
            public void onResponse(Call<PendingRemindersResponse> call, final Response<PendingRemindersResponse> response) {
                try {

                    if (response.code() == Constant.CODE_SUCCESS) {
                        if (response.body() != null) {
                            callback.onSuccessPendingList(response.body());

                        } else {
                            callback.onFailedPendingList(response.body().getMessage(), Constant.NO_RESPONSE_ERROR_CODE);
                        }
                    } else {
                        callback.onFailedPendingList(response.body().getMessage(), Constant.NO_TECHNICAL_CODE);
                    }
                } catch (Exception e) {
                    System.out.println(" Error " + e.getMessage());
                    callback.onFailedPendingList(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
                }
            }

            @Override
            public void onFailure(Call<PendingRemindersResponse> call, Throwable t) {
                callback.onFailedPendingList(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
            }
        });
    }


    public void completeReminder(String token, String id, final UserSyncListeners.CompleteReminderCallback callback) {

        if (!NetworkAccess.isNetworkAvailable(mContext)) {
            callback.onFailedCompleteReminder(null, Constant.NO_INTERNET_CODE);
            return;
        }

        mRestAPI.completeReminder(token, id).enqueue(new Callback<com.remindme.services.responses.Response>() {
            @Override
            public void onResponse(Call<com.remindme.services.responses.Response> call, final Response<com.remindme.services.responses.Response> response) {
                try {

                    if (response.code() == Constant.CODE_SUCCESS) {
                        if (response.body() != null) {
                            callback.onSuccessCompleteReminder(response.body());

                        } else {
                            callback.onFailedCompleteReminder(response.body().getMessage(), Constant.NO_RESPONSE_ERROR_CODE);
                        }
                    } else {
                        callback.onFailedCompleteReminder(response.body().getMessage(), Constant.NO_TECHNICAL_CODE);
                    }
                } catch (Exception e) {
                    System.out.println(" Error " + e.getMessage());
                    callback.onFailedCompleteReminder(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
                }
            }

            @Override
            public void onFailure(Call<com.remindme.services.responses.Response> call, Throwable t) {
                callback.onFailedCompleteReminder(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
            }
        });
    }

    public void updateReminder(String token, String id, UpdateReminderRequest updateReminderRequest, final UserSyncListeners.UpdateReminderCallback callback) {

        if (!NetworkAccess.isNetworkAvailable(mContext)) {
            callback.onFailedUpdateReminder(null, Constant.NO_INTERNET_CODE);
            return;
        }

        mRestAPI.updateReminder(token, id, updateReminderRequest).enqueue(new Callback<UpdateReminderResponse>() {
            @Override
            public void onResponse(Call<UpdateReminderResponse> call, final Response<UpdateReminderResponse> response) {
                try {

                    if (response.code() == Constant.CODE_SUCCESS) {
                        if (response.body() != null) {
                            callback.onSuccessUpdateReminder(response.body());

                        } else {
                            callback.onFailedUpdateReminder(response.body().getMessage(), Constant.NO_RESPONSE_ERROR_CODE);
                        }
                    } else {
                        callback.onFailedUpdateReminder(response.body().getMessage(), Constant.NO_TECHNICAL_CODE);
                    }
                } catch (Exception e) {
                    System.out.println(" Error " + e.getMessage());
                    callback.onFailedUpdateReminder(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
                }
            }

            @Override
            public void onFailure(Call<UpdateReminderResponse> call, Throwable t) {
                callback.onFailedUpdateReminder(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
            }
        });
    }


    public void pushReminderList(String token, final UserSyncListeners.PushReminderCallback callback) {

        if (!NetworkAccess.isNetworkAvailable(mContext)) {
            callback.onFailedPushReminder(null, Constant.NO_INTERNET_CODE);
            return;
        }

        mRestAPI.pushReminderList(token).enqueue(new Callback<PushRemindersResponse>() {
            @Override
            public void onResponse(Call<PushRemindersResponse> call, final Response<PushRemindersResponse> response) {
                try {

                    if (response.code() == Constant.CODE_SUCCESS) {
                        if (response.body() != null) {
                            callback.onSuccessPushReminder(response.body());

                        } else {
                            callback.onFailedPushReminder(response.body().getMessage(), Constant.NO_RESPONSE_ERROR_CODE);
                        }
                    } else {
                        callback.onFailedPushReminder(response.body().getMessage(), Constant.NO_TECHNICAL_CODE);
                    }
                } catch (Exception e) {
                    System.out.println(" Error " + e.getMessage());
                    callback.onFailedPushReminder(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
                }
            }

            @Override
            public void onFailure(Call<PushRemindersResponse> call, Throwable t) {
                callback.onFailedPushReminder(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
            }
        });
    }

    public interface UserSyncListeners {

        interface UserLoginCallback {
            void onSuccessUserLogin(TokenResponse response);

            void onFailedUserLogin(String message, int code);
        }

        interface UserRegistrationCallback {
            void onSuccessUserRegistration(UserRegistrationResponse response);

            void onFailedUserRegistration(String message, int code);
        }

        interface UserEmailVerifyCallback {
            void onSuccessEmailVerify(com.remindme.services.responses.Response response);

            void onFailedEmailVerify(String message, int code);
        }

        interface UserDetailsCallback {
            void onSuccessUserDetails(UserDetails response);

            void onFailedUserDetails(String message, int code);
        }

        interface GetItemListCallback {
            void onSuccessGetItemList(ItemListResponse response);

            void onFailedGetItemList(String message, int code);
        }

        interface AddRemindersCallback {
            void onSuccessAddReminders(AddReminderResponse response);

            void onFailedAddReminders(String message, int code);
        }

        interface UserLocationCallback {
            void onSuccessUserLocation(UserLocationResponse response);

            void onFailedUserLocation(String message, int code);
        }

        interface UserForgetPasswordOTPCallback {
            void onSuccessForgetPasswordOTP(ForgetPasswordResponse response);

            void onFailedForgetPasswordOTP(String message, int code);
        }

        interface ChangePasswordCallback {
            void onSuccessChangePassword(com.remindme.services.responses.Response response);

            void onFailedChangePassword(String message, int code);
        }

        interface UserUpdateCallback {
            void onSuccessUserUpdate(UserUpdateResponse response);

            void onFailedUserUpdate(String message, int code);
        }

        interface UserPendingListCallback {
            void onSuccessPendingList(PendingRemindersResponse response);

            void onFailedPendingList(String message, int code);
        }

        interface CompleteReminderCallback {
            void onSuccessCompleteReminder(com.remindme.services.responses.Response response);

            void onFailedCompleteReminder(String message, int code);
        }

        interface UpdateReminderCallback {
            void onSuccessUpdateReminder(UpdateReminderResponse response);

            void onFailedUpdateReminder(String message, int code);
        }

        interface PushReminderCallback {
            void onSuccessPushReminder(PushRemindersResponse response);

            void onFailedPushReminder(String message, int code);
        }

    }

}