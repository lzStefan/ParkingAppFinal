package com.example.martinlamby.parking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//TODO: SECOND INSPECTION DONE HEAD COMMENT IS MISSING

public class SignUpActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText email;
    private Button signUp;
    private ProgressDialog signUpProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        passUserToLogin();

        setContentView(R.layout.activity_sign_up);

        //init UI components
        username = (EditText) findViewById(R.id.usernameSignUpEditText);
        password = (EditText) findViewById(R.id.passwordSignUpEditText);
        email = (EditText) findViewById(R.id.emailSignUpEditText);
        signUp = (Button) findViewById(R.id.signUpButton);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }


    //checks if User has already created Account and if so refers him to the Login Screen (LoginActivity)
    public void passUserToLogin(){

        boolean userExists = StartActivity.sharedPrefs.getBoolean(getString(R.string.user_is_signed_up), false);   //gets boolean if User already exists

        if(userExists == true){
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    //checks for empty Fields after the User has clicked signUp (Button) and creates a new User
    public void signUpUser(){
        boolean usernameEmptyField;
        boolean passwordEmptyField;
        boolean emailEmptyField;
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();
        String emailString = email.getText().toString();

        //notifies User if field is Empty with unique Toast
        usernameEmptyField = isEmptyString(this, usernameString, getString(R.string.missing_username));
        passwordEmptyField = isEmptyString(this, passwordString, getString(R.string.missing_password));
        emailEmptyField = isEmptyString(this, emailString, getString(R.string.missing_email));

        //initiates actual SignUp
        if(usernameEmptyField == false && passwordEmptyField == false && emailEmptyField == false) {
            signUpProgressDialog = StartActivity.showProgressDialog(getString(R.string.sign_up_in_progress),this);
            ParseController.createParseUser(username, password, email, this, signUpProgressDialog);
        }
    }

    //used also in LoginActivity
    //universal Method that checks if a given String is empty and return the corresponding boolean value
    public static boolean isEmptyString(Context context, String message, String errorMessage){
        if(message.isEmpty()){
            showErrorToast(context, errorMessage);
            return true;
        }else{
            return false;
        }
    }

    //used also in multiple Activites
    //universal Error Toast that displays given message for a short time
    public static void showErrorToast(Context context, String message){
        Toast errorMessage = Toast.makeText(context, message,Toast.LENGTH_SHORT);
        errorMessage.show();
    }
}
