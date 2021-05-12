package com.example.myIMDB;

import android.app.Application;


public final class imovieApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        imovieApplication vInstance = this;
    }
}
