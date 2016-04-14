package com.netanelsagi.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends Activity {

    private EditText etUser;
    private EditText etPass;
    private EditText etPhone;
    private EditText etEmailAddress;
    private Button btSignUP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUser = (EditText)findViewById(R.id.etSignUser);
        etPass = (EditText)findViewById(R.id.etSignPass);
        etPhone = (EditText)findViewById(R.id.etSignPhone);
        etEmailAddress = (EditText)findViewById(R.id.etEmailAddress);
        btSignUP = (Button)findViewById(R.id.btnSignup);
        //Parse.initialize(this, "E4Mau0ldhUSQ9VusZ4xHUvuKO40ipgRcSIZn0kdd", "3asf9Fo3pgehA7LelWPgQUD35qJ2mRtGzDMK6C02");

    }

    public void SignUpUser(View view)
    {
        //save the data from the activity
        String username = etUser.getText().toString();
        //save the manager user in global filed
        GLB_VERB.managerUser = username;
        String password = etPass.getText().toString();
        String phone = etPhone.getText().toString();
        String email = etEmailAddress.getText().toString();
        String isManager = "1";
        //save the data in parse
        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(username);
        parseUser.setPassword(password);
        parseUser.put("PhoneNum", phone);
        parseUser.put("email", email);
        parseUser.put("Manager",isManager);
        //need to check if the phone exist is not manager else is manager
        parseUser.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Show a simple Toast message upon successful registration
                    Toast.makeText(getApplicationContext(),
                            "Successfully Signed up, please continue",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Sign up Error", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        //need to check that the fileds are ok
        //if we here need to open the activiy sign up.
        Intent intent = new Intent(this, AddMembersActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(intent);
        finish();
        return;
    }
}
