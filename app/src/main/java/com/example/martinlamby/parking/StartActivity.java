package com.example.martinlamby.parking;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.parse.Parse;
import com.parse.ParseUser;

//TODO: SECOND INSPECTION DONE HEAD COMMENT IS MISSING

public class StartActivity extends Application {

    public static SharedPreferences sharedPrefs;

    @Override
    public void onCreate() {
        super.onCreate();

        //initialize Parse Database
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "yrtjBvsxg5XuPeOED8H12dm5X7gdN1v3P7bajf9L","ZeSXy39DIWy5Br6ncKKTYIYf0EMCIM6SIahBSrbV");

        //initialize sharedPreferences
        sharedPrefs = getSharedPreferences(getString(R.string.shared_preferences_key), MODE_PRIVATE);

        logOutCurrentUser();
    }

    //make sure Current User is logged Out
    public void logOutCurrentUser(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();
    }

    //shows Pop up Progress Dialog after Sign Up (Activity) and after Login (Activity)
    public static ProgressDialog showProgressDialog(String titleMessage, Context context){
        ProgressDialog progressDialog = ProgressDialog.show(context, titleMessage, context.getString(R.string.please_wait), true);
        progressDialog.show();
        return progressDialog;
    }

}
