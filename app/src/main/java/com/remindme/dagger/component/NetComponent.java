package com.remindme.dagger.component;

import com.remindme.dagger.module.AppModule;
import com.remindme.dagger.module.ContextModule;
import com.remindme.dagger.module.NetModule;
import com.remindme.views.activities.BaseActivity;
import com.remindme.views.fragment.BaseFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ContextModule.class, NetModule.class})
public interface NetComponent {
    void inject(BaseFragment activity);
    void inject(BaseActivity activity);
}
