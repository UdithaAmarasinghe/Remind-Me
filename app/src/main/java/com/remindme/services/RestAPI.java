package com.remindme.services;

import com.remindme.models.Reminders;
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
import com.remindme.services.responses.Response;
import com.remindme.services.responses.TokenResponse;
import com.remindme.services.responses.UpdateReminderResponse;
import com.remindme.services.responses.UserDetails;
import com.remindme.services.responses.UserLocationResponse;
import com.remindme.services.responses.UserRegistrationResponse;
import com.remindme.services.responses.UserUpdateResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestAPI {

    @Headers({"Content-Type: application/json"})
    @POST("auth/refresh_token")
    Call<TokenResponse> authentication(@Query("client_id") String clientId, @Query("client_secret") String clientSecret, @Query("grant_type") String grantType, @Query("refresh_token") String refreshToken);

    @Headers({"Content-Type: application/json"})
    @POST("auth")
    Call<TokenResponse> userLogin(@Body LoginRequest password);

    @Headers({"Content-Type: application/json"})
    @POST("register")
    Call<UserRegistrationResponse> userRegistration(@Body UserRegister userUpdateRequest);

    @Headers({"Content-Type: application/json"})
    @POST("verify")
    Call<Response> verifyEmail(@Body VerifyEmailRequest emailRequest);

    @Headers({"Content-Type: application/json"})
    @GET("user")
    Call<UserDetails> getUserData(@Header("Authorization") String token);

    @Headers({"Content-Type: application/json"})
    @GET("product/list/{id}")
    Call<ItemListResponse> getItemList(@Header("Authorization") String token, @Path("id") String id);

    @Headers({"Content-Type: application/json"})
    @POST("reminder/add")
    Call<AddReminderResponse> addReminders(@Header("Authorization") String token, @Body ArrayList<Reminders> reminders);

    @Headers({"Content-Type: application/json"})
    @POST("user/location")
    Call<UserLocationResponse> userLocation(@Header("Authorization") String token, @Body UserLocationRequest locationRequest);

    @Headers({"Content-Type: application/json"})
    @POST("password/create")
    Call<ForgetPasswordResponse> forgetPasswordOTP(@Body PasswordResetOTPRequest otpRequest);

    @Headers({"Content-Type: application/json"})
    @POST("password/reset")
    Call<Response> passwordChange(@Body PasswordChangeRequest changeRequest);

    @Headers({"Content-Type: application/json"})
    @POST("user/update")
    Call<UserUpdateResponse> userUpdate(@Header("Authorization") String token, @Body UserUpdateRequest updateRequest);

    @Headers({"Content-Type: application/json"})
    @GET("reminder")
    Call<PendingRemindersResponse> pendingList(@Header("Authorization") String token);

    @Headers({"Content-Type: application/json"})
    @POST("reminder/check/{id}")
    Call<Response> completeReminder(@Header("Authorization") String token, @Path("id") String id);

    @Headers({"Content-Type: application/json"})
    @POST("reminder/update/{id}")
    Call<UpdateReminderResponse> updateReminder(@Header("Authorization") String token, @Path("id") String id, @Body UpdateReminderRequest updateReminderRequest);

    @Headers({"Content-Type: application/json"})
    @GET("reminder/available")
    Call<PushRemindersResponse> pushReminderList(@Header("Authorization") String token);
}
