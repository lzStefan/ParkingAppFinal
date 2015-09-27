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

        PublicHeatMapTask publicHeatMapTask = new PublicHeatMapTask();
        PrivateHeatMapTask privateHeatMapTask = new PrivateHeatMapTask();
        publicHeatMapTask.execute();
        privateHeatMapTask.execute();

    }

    private class PublicHeatMapTask extends AsyncTask<Void,Void,ArrayList<ParkedCarLocation>>{

        private Button publicHeatmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            publicHeatmap = (Button) findViewById(R.id.publicHeatMapButton);
        }

        @Override
        protected ArrayList<ParkedCarLocation> doInBackground(Void... params) {
            publicParkedCarLocations = ParseController.getPublicParkedCarPositions();

            return publicParkedCarLocations;
        }

        @Override
        protected void onPostExecute(ArrayList<ParkedCarLocation> parkedCarLocations) {
            super.onPostExecute(parkedCarLocations);
            System.out.println("PUBLIC   " + parkedCarLocations.size());
            publicHeatmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent heatmapIntent = (new Intent(getApplicationContext(), DisplayHeatMapActivity.class));
                    heatmapIntent.putExtra(getString(R.string.publicCL), publicParkedCarLocations);
                    startActivity(heatmapIntent);
                }
            });
        }
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
            System.out.println("PRIVATE   "+parkedCarLocations.size());
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
