package com.example.martinlamby.parking;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by martinlamby on 16.09.15.
 */

//TODO: COMMENTS ARE MISSING

    //custom data type for storing locations specific to each user

public class ParkedCarLocation implements Parcelable{

    //location (divided in latitude and longitude) and username

    private double latitude;
    private double longitude;
    private String username;

    //constructor for ParkedCarLocation

    public ParkedCarLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //getter Methods for Location

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    //Interface Methods for Parcelable. Required, so we can pass the custom data type via Intent in HeatMapActivity

    protected ParkedCarLocation(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        username = in.readString();
    }


    public static final Creator<ParkedCarLocation> CREATOR = new Creator<ParkedCarLocation>() {
        @Override
        public ParkedCarLocation createFromParcel(Parcel in) {
            return new ParkedCarLocation(in);
        }

        @Override
        public ParkedCarLocation[] newArray(int size) {
            return new ParkedCarLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(username);
    }
}
