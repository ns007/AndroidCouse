package com.netanelsagi.taskmanager;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class AllTaskActivity extends ListActivity {

    protected List<ParseObject> mTasks;
    private FloatingActionButton addFAB;
    protected ParseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_task);

        if(GLB_VERB.is_manager == false){
            addFAB = (FloatingActionButton)findViewById(R.id.fabPlus);
            addFAB.hide();
        }
        currentUser = ParseUser.getCurrentUser();
        if(currentUser != null){
            getTasks();
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

    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first

        getTasks();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //getTasks();
    }
    @Override
    protected void onResume() {
        super.onResume();
        getTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.hamLogout:
                ParseUser.logOut();
                Intent login = new Intent(this,LoginActivity.class);
                startActivity(login);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //add
    public void AddTask(View view)
    {
        Intent intent = new Intent(this, AddEditTask.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(intent);
    }

    public void RefrashTasks(View view)
    {
        getTasks();
    }

    private void getTasks() {
        //show user the all tasks from database
        if(GLB_VERB.is_manager == false)
        {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tasks");
            query.whereEqualTo("userName",currentUser.getUsername());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> tasks, ParseException e) {
                    if(e==null){
                        //success
                        mTasks = tasks;
                        TaskAdapter adapter = new TaskAdapter(getListView().getContext(),mTasks);
                        setListAdapter(adapter);
                    }
                    else{
                        //there was a problem Alert user
                    }
                }
            });
        }
        else{
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tasks");
            query.whereEqualTo("teamName",GLB_VERB.teamName);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> tasks, ParseException e) {
                    if(e==null){
                        //success
                        mTasks = tasks;
                        TaskAdapter adapter = new TaskAdapter(getListView().getContext(),mTasks);
                        setListAdapter(adapter);
                    }
                    else{
                        //there was a problem Alert user
                    }
                }
            });
        }
    }

}
