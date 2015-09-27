package com.example.martinlamby.parking;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

//TODO: COMMENTS ARE MISSING !!!

public class HeatMapActivity extends AppCompatActivity {

    private ArrayList<ParkedCarLocation> publicParkedCarLocations = new ArrayList<>();
    private ArrayList<ParkedCarLocation> privateParkedCarLocations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heat_map);
        //privateParkedCarLocations = ParseController.getPrivateParkedCarPositions();

        PrivateHeatMapTask x = new PrivateHeatMapTask();
        x.execute();


        /*publicParkedCarLocations = ParseController.getPublicParkedCarPositions();



        Button publicHeatmap = (Button) findViewById(R.id.publicHeatMapButton);



        publicHeatmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent heatmapIntent = (new Intent(getApplicationContext(), DisplayHeatMapActivity.class));
                heatmapIntent.putExtra(getString(R.string.publicCL), publicParkedCarLocations);
                startActivity(heatmapIntent);
            }
        });

*/


    }

    private class PrivateHeatMapTask extends AsyncTask<Void,Void,ArrayList<ParkedCarLocation>>{

        private Button privateHeatmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            privateHeatmap = (Button) findViewById(R.id.privateHeatMapButton);
        }

        @Override
        protected ArrayList<ParkedCarLocation> doInBackground(Void... params) {
            privateParkedCarLocations = ParseController.getPrivateParkedCarPositions();
            return privateParkedCarLocations;
        }

        @Override
        protected void onPostExecute(ArrayList<ParkedCarLocation> parkedCarLocations) {
            super.onPostExecute(parkedCarLocations);
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


}
