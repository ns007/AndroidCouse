package com.netanelsagi.taskmanager;

import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;

import com.parse.ParseObject;

import java.util.List;

public class TasksviewActivity extends TabActivity {

    private static final String WAITINGTASKS_SPEC = "Waitng Tasks";
    private static final String ALLTASKS_SPEC = "All Tasks";

    ActionBar ab;
    ViewPager vp;
    protected List<ParseObject> mTasks;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tasksview);

            TabHost tabHost = getTabHost();
            // waiting Tab
            TabHost.TabSpec inboxSpec = tabHost.newTabSpec(WAITINGTASKS_SPEC);
            //Tab Icon
            inboxSpec.setIndicator(WAITINGTASKS_SPEC);
            Intent waitningtaskIntent = new Intent(this, WaitingTaskActivity.class);
            //Tab Content
            inboxSpec.setContent(waitningtaskIntent);
            //alltaksks Tab
            TabHost.TabSpec outboxSpec = tabHost.newTabSpec(ALLTASKS_SPEC);
            outboxSpec.setIndicator(ALLTASKS_SPEC);
            Intent alltasksIntent = new Intent(this, AllTaskActivity.class);
            outboxSpec.setContent(alltasksIntent);

            // Adding all TabSpec to TabHost
            tabHost.addTab(inboxSpec);
            tabHost.addTab(outboxSpec);
        }

}
