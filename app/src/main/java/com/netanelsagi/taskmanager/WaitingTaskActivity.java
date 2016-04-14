package com.netanelsagi.taskmanager;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class WaitingTaskActivity extends ListActivity {

    protected List<ParseObject> mTasks;
    private TextView tvMassage;
    private FloatingActionButton addFAB;
    protected ParseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_task);

        if(GLB_VERB.is_manager == false){
            addFAB = (FloatingActionButton)findViewById(R.id.fabPlus);
            addFAB.hide();
        }
        tvMassage = (TextView)findViewById(R.id.tvMessge);
        currentUser = ParseUser.getCurrentUser();
        if(currentUser != null){
            RefrashTask();
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ParseObject taskObject = mTasks.get(position);
        String objectId = taskObject.getObjectId();

        GLB_VERB.is_new = false;
        Intent goToDetailView = new Intent(this,TaskReportActivity.class);
        goToDetailView.putExtra("objectID", objectId);
        startActivity(goToDetailView);
    }

    //add
    public void AddTask(View view)
    {
        Intent intent = new Intent(this, AddEditTask.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first
        RefrashTask();
    }

    public void RefrashTasks(View view)
    {
        RefrashTask();
    }

    public void RefrashTask(){
        if(GLB_VERB.is_manager == true)
        {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if(currentUser != null){
                //show user the all tasks from database
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tasks");
                query.whereEqualTo("taskStatus","WAITING");
                query.whereEqualTo("teamName",GLB_VERB.teamName);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> tasks, ParseException e) {
                        if(e==null){
                            //success
                            mTasks = tasks;
                            if(mTasks.size() == 0)
                            {
                                tvMassage.setText("No current tasks");
                                return;
                            }
                            else
                            {
                                TaskAdapter adapter = new TaskAdapter(getListView().getContext(),mTasks);
                                setListAdapter(adapter);
                                tvMassage.setText("Total waiting tasks  : " + String.valueOf(mTasks.size()));
                                tvMassage.setTextColor(Color.BLACK);
                            }

                        }
                        else{
                            //there was a problem Alert user try to bring local database
                        }
                    }
                });
            }
        }
        else {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if(currentUser != null){
                //show user the all tasks from database
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tasks");
                query.whereEqualTo("taskStatus","WAITING");
                query.whereEqualTo("userName",GLB_VERB.Glb_user);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> tasks, ParseException e) {
                        if(e==null){
                            //success
                            mTasks = tasks;
                            if(mTasks.size() == 0)
                            {
                                tvMassage.setText("No current tasks");
                                return;
                            }
                            else
                            {
                                TaskAdapter adapter = new TaskAdapter(getListView().getContext(),mTasks);
                                setListAdapter(adapter);
                                tvMassage.setText("Total waiting tasks  : " + String.valueOf(mTasks.size()));
                                tvMassage.setTextColor(Color.BLACK);
                            }
                        }
                        else{
                            //there was a problem Alert user
                        }
                    }
                });
            }
        }
    }
}
