package com.netanelsagi.taskmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class AddMembersActivity extends Activity {

    private EditText etTeamName;
    private boolean mailSent = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        etTeamName = (EditText)findViewById(R.id.etTaemName);
        if(GLB_VERB.is_firstSignUP == false){
            etTeamName.setText(GLB_VERB.teamName);
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
                if(GLB_VERB.is_manager == false){
                    break;
                }
                Intent intent = new Intent(this,AddMembersActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //Sign up click
    public void openDialog(View view)
    {
        final Dialog dialog = new Dialog(AddMembersActivity.this);
        dialog.setTitle("ADD MEMBERS");
        dialog.setContentView(R.layout.dialog_addmember);
        dialog.show();

        final EditText etPhonenum = (EditText)dialog.findViewById(R.id.phone);
        final EditText etEmail = (EditText)dialog.findViewById(R.id.email);
        Button Addmember = (Button)dialog.findViewById(R.id.btnAdddia);
        Button finsih = (Button)dialog.findViewById(R.id.btnBackdia);

        Addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MEMBER member = new MEMBER();
                String email = etEmail.getText().toString();
                //check if the mail entered is valid email
                if(isEmailValid(email) == false){
                    Toast.makeText(getApplicationContext(), "Please insert a valid email address",
                            Toast.LENGTH_LONG).show();
                    etEmail.setText("");
                    return;
                }
                member.setEmailAddress(etEmail.getText().toString());
                member.setPhoneNum(etPhonenum.getText().toString());
                GLB_VERB.MembersList.add(member);
                etPhonenum.setText("");
                etEmail.setText("");
            }
        });
        finsih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cretaelistviwe();
                dialog.cancel();
            }
        });
    }

    //saveandsend click
    public void SaveAndSend(View view){

        String mystrig = new String();
        for (int i = 0 ;i<GLB_VERB.MembersList.size() ;i++){
            MEMBER temp = GLB_VERB.MembersList.get(i);
            mystrig += temp.getEmailAddress() + ";";
        }

        /* Create the Intent */
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        /* Fill it with Data */
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mystrig});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invitation to Join OTS team");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hi\n" +
                "\tYou have been invited to be a team member in an OTS Team created by me.\n" +
                "\tUse this link to download and install the App from Google Play.\n" +
                "\t<LINK to Google Play download>");

        /* Send it off to the Activity-Chooser */
        mailSent = true;
        this.startActivity(Intent.createChooser(emailIntent, "Send invitation mail..."));
    }

    //saveandsend click
    public void FinishClick(View view){
        if(mailSent == false){
            return;
        }
        if(GLB_VERB.is_firstSignUP != false){
            //update the team name for the manager
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", GLB_VERB.managerUser);
            query.getFirstInBackground(new GetCallback<ParseUser>() {
                public void done(ParseUser object, ParseException e) {
                    if (object == null) {
                        Log.d("error", "connection error");
                    } else {
                        Log.d("success", "Retrieved the object.");
                        object.put("TeamName", etTeamName.getText().toString());
                        object.saveInBackground();
                    }
                }
            });
        }

        //save all my users in parse database
        for(MEMBER member : GLB_VERB.MembersList)
        {
            //save the data from the activity
            String username = member.getEmailAddress();
            String password = member.getPhoneNum();
            String phone = member.getPhoneNum();
            String isManager = "0";
            //save the data in parse
            ParseUser parseUser = new ParseUser();
            parseUser.setUsername(username);
            parseUser.setPassword(password);
            parseUser.put("PhoneNum", phone);
            parseUser.put("Manager",isManager);
            //need to check if the phone exist is not manager else is manager
            parseUser.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        //user have been save
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Sign up Error", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
        }
        GLB_VERB.is_firstSignUP = false;
        Intent intent = new Intent(this, TasksviewActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(intent);
    }


    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void cretaelistviwe() {
        int x = GLB_VERB.MembersList.size();
        MEMBER[] listofmembers;
        listofmembers = GLB_VERB.MembersList.toArray(new MEMBER[GLB_VERB.MembersList.size()]);
        ListAdapter memberadapter = new MembersAdapter(this,listofmembers);
        ListView mylistview = (ListView)findViewById(R.id.membersListview);
        mylistview.setAdapter(memberadapter);
    }
}
