package com.remindme.services.sync;

import android.content.Context;

import com.remindme.models.Token;
import com.remindme.services.RestAPI;
import com.remindme.services.responses.TokenResponse;
import com.remindme.utils.AppSharedPreference;
import com.remindme.utils.Constant;
import com.remindme.utils.NetworkAccess;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.remindme.views.activities.BaseActivity.mPreferences;

public class AuthenticationSync {

    protected Context mContext;
    protected RestAPI mRestAPI;

    public AuthenticationSync(Context mContext, RestAPI restAPI) {
        this.mContext = mContext;
        this.mRestAPI = restAPI;
    }

    public interface AuthenticationSyncListeners {

        void onSuccessAuthentication(String token);

        void onFailedAuthentication(String message, int code);

    }

    public void getAuthentication(String clientId, String clientSecret, String grantType, String refreshToken, final AuthenticationSyncListeners callback) {

        if (!NetworkAccess.isNetworkAvailable(mContext)) {
            callback.onFailedAuthentication(null, Constant.NO_INTERNET_CODE);
            return;
        }

        mRestAPI.authentication(clientId, clientSecret, grantType, refreshToken)
                .enqueue(new Callback<TokenResponse>() {
                    @Override
                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                        if (response.code() == Constant.NO_TECHNICAL_CODE) {

                            return;
                        }
                        if (response.isSuccessful()) {
                           // Token tokenResponse = new Token(response.body().getData().getAccessToken(), response.body().getData().getRefreshToken());

                            //mPreferences.setToken(tokenResponse);
                            callback.onSuccessAuthentication(Constant.BEARER + response.body().getData().getAccessToken());
                        } else {
                            callback.onFailedAuthentication(Constant.ERROR_OOPS, Constant.NO_RESPONSE_ERROR_CODE);
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {
                        callback.onFailedAuthentication(Constant.ERROR_OOPS, Constant.NO_TECHNICAL_CODE);
                    }
                });
    }

}
