package com.atc.navigator.application;

import android.app.Application;

import com.atc.navigator.BuildConfig;

import timber.log.Timber;

public class NavigatorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
