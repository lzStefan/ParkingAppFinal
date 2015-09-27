package com.example.martinlamby.parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

//TODO: COMMENTS ARE MISSING !!!

    //Provides the menu for private and public heat map and calls the Datasets (from Parse.com) for both.

public class HeatMapActivity extends AppCompatActivity {

    // parking locations of all users and the current user

    private ArrayList<ParkedCarLocation> publicParkedCarLocations = new ArrayList<>();
    private ArrayList<ParkedCarLocation> privateParkedCarLocations = new ArrayList<>();


    //setting up the layout and onClickListeners for the menu, and adding values to the ArrayLists


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heat_map);
        privateParkedCarLocations = ParseController.getPrivateParkedCarPositions();
        publicParkedCarLocations = ParseController.getPublicParkedCarPositions();


        Button privateHeatmap = (Button) findViewById(R.id.privateHeatMapButton);
        Button publicHeatmap = (Button) findViewById(R.id.publicHeatMapButton);


        //starts the displayHeatmapActivity and passes the dataset for the public heatmap (all users)

        publicHeatmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent heatmapIntent = (new Intent(getApplicationContext(), DisplayHeatMapActivity.class));
                heatmapIntent.putExtra(getString(R.string.publicCL), publicParkedCarLocations);
                startActivity(heatmapIntent);
            }
        });

        //starts the displayHeatmapActivity and passes the dataset for the private heatmap (current user)

        privateHeatmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent heatmapIntent = (new Intent(getApplicationContext(), DisplayHeatMapActivity.class));
                heatmapIntent.putExtra(getString(R.string.privateCL), privateParkedCarLocations);
                startActivity(heatmapIntent);
            }
        });

    }


}
