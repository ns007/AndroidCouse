package com.netanelshilo.taskmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TaskReportActivity extends FragmentActivity {

    String objectId;
    ArrayList<String> TeamUsers = new ArrayList<String>();//string array for TeamUsers
    RadioButton radio_priority_save;
    RadioButton radio_DueTime_save;
    RadioButton radio_accept_save;
    RadioButton radio_status_save;
    String Category,Priority,DueTIme,Accept,status,location,UserName;
    ParseFile file;
    private Spinner spCategory;
    private RadioGroup rgPriority;
    private RadioButton rbNormal;
    private RadioButton rbUrgent;
    private RadioButton rbLow;
    private EditText etLocation;
    private Spinner spUsers;
    private RadioGroup rgDueTime;
    private RadioButton rbToday;
    private RadioButton rbTommorw;
    private RadioGroup rgAccept;
    private RadioButton rbWaiting;
    private RadioButton rbAccept;
    private RadioButton rbReject;
    private RadioGroup rgStatus;
    private RadioButton rbWaitingSts;
    private RadioButton rbInProgress;
    private RadioButton rbDoneStatus;

    private ImageView iv1;
    private Button btCamera;
    Bitmap photo;
    private static final int CAMERA_REQUEST = 1888;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_report);

        btCamera = (Button)findViewById(R.id.btnCamera);
        btCamera.setEnabled(false);
        iv1 = (ImageView)findViewById(R.id.imageView1);

        spCategory = (Spinner)findViewById(R.id.category_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spCategory.setAdapter(adapter);

        rgPriority = (RadioGroup)findViewById(R.id.rgPriority);
        rbNormal = (RadioButton)findViewById(R.id.radio_normal);
        rbUrgent = (RadioButton)findViewById(R.id.radio_urgent);
        rbLow = (RadioButton)findViewById(R.id.radio_low);
        etLocation = (EditText)findViewById(R.id.etLocation);

        spUsers = (Spinner)findViewById(R.id.users_spinner);
        final List<String> categoris = new ArrayList<String>();
        ParseQuery<ParseUser> query1 = ParseUser.getQuery();
        query1.whereEqualTo("TeamName", GLB_VERB.teamName);
        query1.findInBackground(new FindCallback<ParseUser>() {
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
        spUsers.setAdapter(dataAdapter);


        rgDueTime = (RadioGroup)findViewById(R.id.rgDutTime);
        rbToday = (RadioButton)findViewById(R.id.radio_today);
        rbTommorw = (RadioButton)findViewById(R.id.radio_tommorw);
        rgAccept = (RadioGroup)findViewById(R.id.rgAccept);
        rbWaiting = (RadioButton)findViewById(R.id.radio_waiting);
        rbAccept = (RadioButton)findViewById(R.id.radio_accept);
        rbReject =(RadioButton)findViewById(R.id.radio_reject);
        rgStatus =(RadioGroup)findViewById(R.id.rgSts);
        rbWaitingSts = (RadioButton)findViewById(R.id.radio_waiting_sts);
        rbInProgress = (RadioButton)findViewById(R.id.radio_progress);
        rbDoneStatus =(RadioButton)findViewById(R.id.radio_done);


        //load the data from parse to screen
        Intent intent = getIntent();
        objectId = intent.getStringExtra("objectID");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e==null){
                    //success we have a task
                    String category = object.get("category").toString();
                    for(int i= 0; i < spCategory.getAdapter().getCount(); i++)
                    {
                        if(spCategory.getAdapter().getItem(i).toString().contains(category))
                        {
                            spCategory.setSelection(i);
                        }
                    }
                    String priority = object.get("priority").toString();
                    if(priority.equals("Normal") == true)
                        rbNormal.setChecked(true);
                    else if(priority.equals("High") == true )
                        rbUrgent.setChecked(true);
                    else if(priority.equals("Low") == true)
                        rbLow.setChecked(true);

                    String location = object.get("location").toString();
                    etLocation.setText(location);

                    //need to load the user spinner

                    String user = object.get("userName").toString();
                    for(int i= 0; i < spUsers.getAdapter().getCount(); i++)
                    {
                        if(spUsers.getAdapter().getItem(i).toString().contains(user))
                        {
                            spUsers.setSelection(i);
                        }
                    }

                    String duetime = object.get("DateTIme").toString();
                    if(duetime.equals("Today") == true)
                        rbToday.setChecked(true);
                    else if(duetime.equals("Tomorrow") == true)
                        rbTommorw.setChecked(true);

                    String acceptStatus = object.get("taskAcceptStatus").toString();
                    if(acceptStatus.equals("WAITING") == true)
                        rbWaiting.setChecked(true);
                    else if(acceptStatus.equals("ACCEPT") == true )
                        rbAccept.setChecked(true);
                    else if(acceptStatus.equals("REJECT") == true)
                        rbReject.setChecked(true);

                    String Status = object.get("taskStatus").toString();
                    if(Status.equals("WAITING") == true)
                        rbWaitingSts.setChecked(true);
                    else if(Status.equals("IN PROGRESS") == true )
                        rbInProgress.setChecked(true);
                    else if(Status.equals("DONE") == true)
                    {
                        rbDoneStatus.setChecked(true);
                        ParseFile fileObject = (ParseFile) object.get("ImageFile");
                        if(fileObject != null)
                        {
                            fileObject.getDataInBackground(new GetDataCallback() {
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        // Decode the Byte[] into Bitmap
                                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        // Set the Bitmap into theImageView
                                        iv1.setImageBitmap(bmp);

                                    } else {
                                        Log.d("imageLoad", "Problem load image the data.");
                                    }
                                }
                            });
                        }
                    }
                }
                else{
                    // there is a error show user massege
                }
            }
        });
        if(GLB_VERB.is_manager == false){

            spCategory.setEnabled(false);
            spUsers.setEnabled(false);
            rbNormal.setEnabled(false);
            rbUrgent.setEnabled(false);
            rbLow.setEnabled(false);
            rbToday.setEnabled(false);
            rbTommorw.setEnabled(false);
            rgPriority.setEnabled(false);
            etLocation.setEnabled(false);
            rgDueTime.setEnabled(false);
        }
        //if the user is manager all data aviable to him.

        //else if user is not manager only the down two are open to change

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            iv1.setImageBitmap(photo);
        }
    }

    public void takePic(View view)
    {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void CheckDone(View view)
    {
        if(rbDoneStatus.isChecked()){
            btCamera.setEnabled(true);
        }
        else{
            btCamera.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homepage,menu);
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
            case R.id.hamAbout:
                //open dialog with sagi and me detalis
                DialogFragment newFragment = new AboutDialogFragment();
                newFragment.show(getFragmentManager(), "About");
                break;
            case R.id.hamSetting:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                final EditText edittext = new EditText(this);
                alert.setMessage("Check for new update");
                alert.setTitle("Enter check for update time");

                alert.setView(edittext);

                alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        GLB_VERB.UpdateTime = Integer.parseInt(edittext.getText().toString());
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });
                alert.show();
                break;
            case  R.id.hamManage:
                Intent intent = new Intent(this,AddMembersActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //save task click
    public void UpdateTask(View view)
    {
        Category  = spCategory.getSelectedItem().toString();
        //String Username = user.getSelectedItem().toString();
        int selected_priority_id = rgPriority.getCheckedRadioButtonId();
        radio_priority_save = (RadioButton) (findViewById(selected_priority_id));
        Priority = radio_priority_save.getText().toString();

        int selected_DueTIme_id = rgDueTime.getCheckedRadioButtonId();
        radio_DueTime_save = (RadioButton)(findViewById(selected_DueTIme_id));
        DueTIme = radio_DueTime_save.getText().toString();

        int selected_accept_id = rgAccept.getCheckedRadioButtonId();
        radio_accept_save = (RadioButton)(findViewById(selected_accept_id));
        Accept = radio_accept_save.getText().toString();

        int selected_status_id = rgStatus.getCheckedRadioButtonId();
        radio_status_save = (RadioButton)(findViewById(selected_status_id));
        status = radio_status_save.getText().toString();

        location = etLocation.getText().toString();
        UserName = ParseUser.getCurrentUser().getUsername();
        //String UserName = "nshilo7@gmail.com"; // for now static, need to take the data from spinner user
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    //success we have a task
                    object.put("priority", Priority);
                    object.put("teamName", GLB_VERB.teamName);
                    object.put("location", location);
                    object.put("userName", UserName);
                    object.put("taskStatus", status);
                    object.put("taskAcceptStatus", Accept);
                    object.put("category", Category);
                    object.put("DateTIme", DueTIme);


                    if(iv1.getDrawable() != null)
                    {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] data = stream.toByteArray();

                        ParseFile imageFile = new ParseFile(Category + ".png", data);
                        object.put("ImageFile", imageFile);
                    }
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException ex) {
                            if (ex == null) {
                                if(Accept.equals("ACCEPT")){
                                    Toast.makeText(TaskReportActivity.this, "Tasks" + Accept + "\n Task status is " + status, Toast.LENGTH_LONG).show();
                                }
                                if(Accept.equals("REJECT")){
                                    Toast.makeText(TaskReportActivity.this, "Tasks" + Accept, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                // Failed
                                Toast.makeText(TaskReportActivity.this, "Error saving task status \n please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    //object.saveInBackground();
                    /*if(Accept.equals("ACCEPT")){
                        Toast.makeText(TaskReportActivity.this, "Tasks" + Accept + "\n Task status is " + status, Toast.LENGTH_LONG).show();
                    }
                    if(Accept.equals("REJECT")){
                        Toast.makeText(TaskReportActivity.this, "Tasks" + Accept, Toast.LENGTH_LONG).show();
                    }*/
                } else {
                    // there is a error show user massege
                    Toast.makeText(TaskReportActivity.this, "Error saving task status \n please try again", Toast.LENGTH_LONG).show();

                }
                finish();
            }
        });

        /*ParseObject task = new ParseObject("Tasks");


        //back to older intent and finsih this one
        //if we here need to open the activiy sign up.
        Intent intent = new Intent(this, TasksviewActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(intent);
        finish();
        return;*/

    }
}
