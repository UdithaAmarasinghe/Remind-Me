package com.remindme.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.remindme.R;

public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadHome();
            }
        }, 1500);
    }

    private void loadHome() {
        if (isLogin()) {
            MainActivity.startActivityView(this);
        } else {
            LoginActivity.startActivity(this);
            goNextAnimation();
        }
        finish();
    }
}