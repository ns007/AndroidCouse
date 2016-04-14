package com.netanelsagi.taskmanager;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by netanels on 06/03/2016.
 */
public class TaskManager extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this);
    }
}
