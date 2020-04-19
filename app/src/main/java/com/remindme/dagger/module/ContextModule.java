package com.remindme.dagger.module;

import android.content.Context;

import com.remindme.dagger.qualifier.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    Context mContext;

    public ContextModule(Context context) {
        mContext = context.getApplicationContext();
    }

    @Provides
    @Singleton
    @ApplicationContext
    Context providesContext() {
        return mContext;
    }
}
