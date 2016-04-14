package com.netanelsagi.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

    private EditText username;
    private EditText password;
    private boolean tempStatus = false;
    String Manage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.etUser);
        password = (EditText)findViewById(R.id.etPassword);

    }

    //login button click
    public void Login(View view)
    {
        String user = username.getText().toString();
        String pass = password.getText().toString();

        if(user.matches(""))
        {
            username.setHint("Username : Must requeird field");
            username.setHintTextColor(Color.RED);
            return;
        }
        if(pass.matches(""))
        {
            password.setHint("Password : Must requeird field");
            password.setHintTextColor(Color.RED);
            return;
        }
        //try to login the user
        ParseUser.logInInBackground(user, pass, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    //open the activity and remmber who is the user
                    GLB_VERB.Glb_user = user.getUsername();
                    Manage = user.get("Manager").toString();
                    GLB_VERB.teamName = user.get("TeamName").toString();
                    if(Manage.equals("1"))
                        GLB_VERB.is_manager = true;
                    else
                        GLB_VERB.is_manager = false;
                    Toast.makeText(LoginActivity.this, "Welcome back to TaskManager", Toast.LENGTH_LONG).show();
                    GLB_VERB.is_firstSignUP =false;
                    Intent mainview = new Intent(LoginActivity.this, TasksviewActivity.class);
                    startActivity(mainview);
                } else {
                    //cant sign
                    Toast.makeText(LoginActivity.this, "Username or Password incorrect \n please retype your credentials", Toast.LENGTH_LONG).show();
                    username.setText("");
                    password.setText("");
                    return;
                }

            }
        });
    }
    //==============================================================
    //Sign up click
    public void SignUp(View view)
    {
        //if we here need to open the activiy sign up.
        Intent intent = new Intent(this, SignupActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(intent);
        //finish();
        //return;
    }
}
