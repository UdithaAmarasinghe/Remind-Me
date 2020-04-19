package com.remindme;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.remindme.dagger.component.DaggerNetComponent;
import com.remindme.dagger.component.NetComponent;
import com.remindme.dagger.module.AppModule;
import com.remindme.dagger.module.ContextModule;
import com.remindme.dagger.module.NetModule;
import com.remindme.utils.Constant;

public class _Application extends MultiDexApplication {
    private static _Application mInstance;
    private NetComponent mNetComponent;
    private static Context mCtx;

    @Override
    public void onCreate() {
        super.onCreate();
        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .contextModule(new ContextModule(this))
                .netModule(new NetModule(Constant.BASE_URL))
                .build();
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }
}

