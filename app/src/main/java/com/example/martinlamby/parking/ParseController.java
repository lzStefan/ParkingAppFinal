package com.example.martinlamby.parking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinlamby on 26.09.15.
 */

//TODO: SECOND INSPECTION DONE HEAD COMMENT IS MISSING

public class ParseController {

    //creates a new Parse User and passes User on to MainActivtiy
    public static void createParseUser(EditText username, EditText password, EditText email, final Context context, final ProgressDialog progressDialog){
        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.setEmail(email.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    StartActivity.sharedPrefs.edit().putBoolean(context.getString(R.string.user_is_signed_up), true).commit();     //recognizes a new User now exists and will not show SignUp View again
                    context.startActivity(new Intent(context, MainActivity.class));     //Proceed to next activity
                    ((Activity) context).finish();
                } else {
                    SignUpActivity.showErrorToast(context, e.getMessage());
                }
                progressDialog.dismiss();
            }
        });
    }

    ////logs in Parse User and passes User on to MainActivtiy
    public static void logInParseUser(String username, String password,final Context context, final ProgressDialog progressDialog) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    System.out.println("Login Sucessfull");
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);     //Proceed to next activity
                    ((Activity) context).finish();
                } else {
                    System.out.println("Login failed | Problem:   " + e.getMessage());
                    SignUpActivity.showErrorToast(context, e.getMessage());
                }
                progressDialog.dismiss();
            }
        });
    }

    //saves location of parked car to Cloud (Parse)
    public static void saveParkedCarPositionToParse(double latitude, double longitude) {
        ParseObject parkedCarPosition = new ParseObject("ParkedCarPosition");
        parkedCarPosition.add("latitude", String.valueOf(latitude));
        parkedCarPosition.add("longitude", String.valueOf(longitude));
        parkedCarPosition.add("username", ParseUser.getCurrentUser().getUsername().toString());
        parkedCarPosition.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    System.out.println("Parked Car Position has been saved successfully");
                } else {
                    System.out.println("Failed to saved Position of parked Car");
                }
            }
        });
    }

    //retrieves only last parked car location from current User
    public static ParkedCarLocation getLastParkedCarLocation(){
        ParkedCarLocation parkedCarLocation = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParkedCarPosition");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername().toString());
        query.orderByDescending("createdAt");
        query.setLimit(1);
        try {
            List<ParseObject> list =  query.find();
            System.out.println("LISTENLÄNGE:  " + list.size());
            for (int i = 0; i < list.size(); i++) {

                double latitude = parseDoubleFromString(list.get(i).get("latitude").toString());
                double longitude = parseDoubleFromString(list.get(i).get("longitude").toString());

                parkedCarLocation = new ParkedCarLocation(latitude, longitude);

            }
        }catch (Exception e){
            System.out.println("Error:  "+e.getMessage());

        }
        System.out.println("LAST PARKED CAR LOCATION:   " + parkedCarLocation);
        return parkedCarLocation;
    }

    //retrieves all parked car locations from current User
    public static ArrayList<ParkedCarLocation> getPrivateParkedCarPositions() {
        final ArrayList<ParkedCarLocation> privateParkedCarLocations = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParkedCarPosition");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername().toString());
        try {
            List<ParseObject> list = query.find();

            for (int i = 0; i < list.size(); i++) {

                double latitude = parseDoubleFromString(list.get(i).get("latitude").toString());
                double longitude = parseDoubleFromString(list.get(i).get("longitude").toString());

                ParkedCarLocation x = new ParkedCarLocation(latitude, longitude);
                privateParkedCarLocations.add(x);

            }
        }catch (Exception e){
            System.out.println("Error:  "+e.getMessage());
        }
        return privateParkedCarLocations;
    }

    //retrieves all parked car locations from all registered Users
    public static ArrayList<ParkedCarLocation> getPublicParkedCarPositions() {
        final ArrayList<ParkedCarLocation> publicParkedCarLocations = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParkedCarPosition");
        try {
            List<ParseObject> list = query.find();
            for (int i = 0; i < list.size(); i++) {

                double latitude = parseDoubleFromString(list.get(i).get("latitude").toString());
                double longitude = parseDoubleFromString(list.get(i).get("longitude").toString());

                ParkedCarLocation x = new ParkedCarLocation(latitude, longitude);
                publicParkedCarLocations.add(x);

            }
        }catch (Exception e) {
            System.out.println("Error:  "+e.getMessage());
        }
        return publicParkedCarLocations;
    }

    //parse String from retrieved Parse value to Double
    public static double parseDoubleFromString(String number) {
        int length = number.length();
        String x = number.substring(1, length - 1);
        return Double.valueOf(x);
    }
}