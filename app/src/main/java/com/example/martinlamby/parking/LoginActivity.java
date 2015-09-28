package com.example.martinlamby.parking;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//Handles login and starts Mainactivity on successful login

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button logIn;
    private ProgressDialog loginProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.usernameLoginEditText);
        password = (EditText) findViewById(R.id.passwordLoginEditText);
        logIn = (Button) findViewById(R.id.loginButton);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInUser();
            }
        });
    }
    //checks for empty Fields after the User has clicked login (Button) and logs in User
    public void logInUser(){
        boolean usernameEmptyField = false;
        boolean passwordEmptyField = false;
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();

        usernameEmptyField = SignUpActivity.isEmptyString(this, usernameString, getString(R.string.missing_username));
        passwordEmptyField = SignUpActivity.isEmptyString(this, passwordString, getString(R.string.missing_password));

        if(usernameEmptyField == false && passwordEmptyField == false) {
            loginProgressDialog = StartActivity.showProgressDialog(getString(R.string.login_in_progress),this);
            ParseController.logInParseUser(usernameString, passwordString, this, loginProgressDialog);

        }

    }

}
