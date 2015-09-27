package com.example.martinlamby.parking;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

//TODO: SECOND INSPECTION DONE HEAD COMMENT IS MISSING

public class MainActivity extends AppCompatActivity {


    private ImageView navigateToCar;
    private ImageView shareCarPosition;
    private ImageView heatMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start Location and Shake Service
        startService(new Intent(getBaseContext(), GeoLocationService.class));
        startService(new Intent(getBaseContext(), ShakeDetectorService.class));

        //init UI components
        navigateToCar = (ImageView) findViewById(R.id.NavigateToCarButton);
        shareCarPosition = (ImageView) findViewById(R.id.shareButton);
        heatMap = (ImageView) findViewById(R.id.heatMapButton);
        
        //setup Click Function for every Button
        navigateToCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send Location to Google Maps and start navigation
                ParseNavigationTask parseLocationTask = new ParseNavigationTask();
                parseLocationTask.execute();
            }
        });
        shareCarPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //refer User To ContactList (only email) in ShareCarPositionActivity
                startActivity(new Intent(getApplicationContext(), ShareCarPositionActivity.class));
            }
        });
        heatMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //refer User to Select View (private or public) in HeatMapActivity
                startActivity(new Intent(getApplicationContext(), HeatMapActivity.class));
            }
        });

    }

    //terminates all Services when called
    @Override
    protected void onDestroy() {
        stopService(new Intent(getBaseContext(), GeoLocationService.class));
        stopService(new Intent(getBaseContext(), ShakeDetectorService.class));
        super.onDestroy();
    }

    //AsyncTask that starts navigation after last parked car location has been successfully retrieved
    private class ParseNavigationTask extends AsyncTask<Void,Void,ParkedCarLocation>{

        @Override
        protected ParkedCarLocation doInBackground(Void... params) {
            System.out.println("ParseLocationTask do in Background");
            return ParseController.getLastParkedCarLocation();
        }

        @Override
        protected void onPostExecute(ParkedCarLocation parkedCarLocation) {
            super.onPostExecute(parkedCarLocation);
            System.out.println("ParseLocationTask on Post Execute");
            if(parkedCarLocation == null){
                SignUpActivity.showErrorToast(getApplicationContext(), getString(R.string.car_not_parked));
            }else {
                startNavigation(parkedCarLocation);
            }
        }
    }

    //starts pedestrian navigation to parked car position if Google Maps App is installed
    public void startNavigation(ParkedCarLocation parkedCarLocation) {
        boolean isInstalled = isGoogleMapsInstalled();
        if (isInstalled == true) {
            //TODO: Nochmal überprüfen; die Locations können irgendwie nicht stimmen
            Uri gMapsIntentUri = Uri.parse("google.navigation:q=" + parkedCarLocation.getLatitude() + "," + parkedCarLocation.getLongitude() +"&mode=w");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gMapsIntentUri);
            mapIntent.setPackage(getString(R.string.google_maps_app));
            startActivity(mapIntent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please install Google Maps");
            builder.show();
        }
    }

    //checks if Google Maps is installed and returns corresponding boolean value
    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
