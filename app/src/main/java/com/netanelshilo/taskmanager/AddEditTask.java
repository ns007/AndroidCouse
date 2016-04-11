package com.netanelshilo.taskmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddEditTask extends Activity {

    String objectId;
    private ArrayList<String> TeamUsers = new ArrayList<String>();//string array for TeamUsers
    private Spinner user;
    private Spinner spinner;
    public Object[] objDays;

    private int year, month, day;
    private TextView tvDate;
    private static RadioGroup radio_g;
    private static RadioButton radio_b_priority;
    private static RadioGroup radio_g_DueTime;
    private static RadioButton radio_b_DueTime_today;
    private static RadioButton radio_b_DueTime_tomorrow;
    private Button scanBtn;
    private static EditText etLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        spinner = (Spinner) findViewById(R.id.category_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        tvDate = (TextView)findViewById(R.id.labelDate);
        radio_g = (RadioGroup)findViewById(R.id.rgPriority);
        radio_g_DueTime = (RadioGroup)findViewById(R.id.rgDutTime);
        radio_b_DueTime_today =(RadioButton)findViewById(R.id.radio_today);
        radio_b_DueTime_tomorrow =(RadioButton)findViewById(R.id.radio_tommorw);
        radio_b_priority = (RadioButton)findViewById(R.id.radio_normal);
        etLocation = (EditText)findViewById(R.id.etLocation);
        scanBtn = (Button)findViewById(R.id.btnScan);
        //load the users spinner
        user = (Spinner) findViewById(R.id.users_spinner);
        final List<String> categoris = new ArrayList<String>();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("TeamName", GLB_VERB.teamName);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    for (int i = 0; i < objects.size(); i++) {
                        categoris.add(objects.get(i).getUsername());
                    }

                } else {
                    // Something went wrong.
                }
            }
        });
        categoris.add("Select user");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoris);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        user.setAdapter(dataAdapter);

        /*configUsers(); //get users from db;
        String[] users = TeamUsers.toArray(new String[TeamUsers.size()]);
        //final String[] str= {"Report 1","Report 2","Report 3","Report 4","Report 5"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, TeamUsers);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        user.setAdapter(spinnerArrayAdapter);*/
    }


    public void configUsers () {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("TeamName", GLB_VERB.teamName);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    for (int i = 0; i < objects.size(); i++) {
                        TeamUsers.add(objects.get(i).getUsername());
                    }

                } else {
                    // Something went wrong.
                }
            }
        });
    }
    //========Scanner part====================================//
    public void OpenScanner(View view)
    {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            etLocation.setText(scanContent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    //======================================================//
    public void SelectTime(View view)
    {
        radio_b_DueTime_today.setChecked(false);
        radio_b_DueTime_tomorrow.setChecked(false);

        showDialog(999);
        Toast.makeText(getApplicationContext(), "Please enter Date", Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };
    private void showDate(int year, int month, int day) {
        tvDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    //save task click
    public void AddUpdateTask(View view)
    {
        String Category = spinner.getSelectedItem().toString();
        String Username = user.getSelectedItem().toString();
        int selected_catergory_id = radio_g.getCheckedRadioButtonId();
        radio_b_priority = (RadioButton)(findViewById(selected_catergory_id));
        String Priority = radio_b_priority.getText().toString();
        int selected_DueTIme_id = radio_g_DueTime.getCheckedRadioButtonId();
        radio_b_DueTime_today = (RadioButton)(findViewById(selected_DueTIme_id));
        String DueTIme = radio_b_DueTime_today.getText().toString();

        String location = etLocation.getText().toString();
        String UserName = Username; // for now static, need to take the data from spinner user
        ParseObject task = new ParseObject("Tasks");
        task.put("priority", Priority);
        task.put("teamName", GLB_VERB.teamName);
        task.put("location", location);
        task.put("userName", UserName);
        task.put("taskStatus", "WAITING");
        task.put("taskAcceptStatus", "WAITING");
        task.put("category", Category);
        task.put("DateTIme", DueTIme);
        task.saveInBackground();

        //back to older intent and finsih this one
        //if we here need to open the activiy sign up.
        Intent intent = new Intent(this, TasksviewActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(intent);
        finish();
        return;
    }
}
