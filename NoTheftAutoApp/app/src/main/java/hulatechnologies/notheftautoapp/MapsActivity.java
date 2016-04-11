package hulatechnologies.notheftautoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,AsyncResponse3 {

    private GoogleMap mMap;
    PreferenceHandler handler = new PreferenceHandler();
    String cords = "0,0";
    float longitude = 0;
    float latitude = 0;
    final Handler h = new Handler();
    Runnable runner;
    MarkerOptions marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        startUpdater();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(handler.getLatitude(handler.getCurrCar(getBaseContext())+ "latitude",getBaseContext()) != 0.0f) {
            latitude = handler.getLatitude(handler.getCurrCar(getBaseContext())+ "latitude",getBaseContext());
            longitude = handler.getLongitude(handler.getCurrCar(getBaseContext())+ "longitude",getBaseContext());
        }
        LatLng location = new LatLng(latitude,longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17));
    }

    //Start funksjoner for å hente kordinatene
    //startUpdater starter prosessen med å oppdatere longtiude og latitude variabelene

    //Kalles av startUpdater (Dette er funksjonen som henter fra databasen og sn
    public void getCoords(String carname){
        AsyncGetCoords c = new AsyncGetCoords();
        c.delegate = this;
        JSONObject json = new JSONObject();
        try {
            json.put("carname",carname);
            c.execute(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateClick(View v){
        LatLng location = new LatLng(latitude,longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,17));
    }
    public void goBack(View v){
        startActivity(new Intent(this,Status.class));
        finish();
    }

    public void updateMap(float longitude, float latitude){
        LatLng location = new LatLng(latitude, longitude);
        mMap.clear();
        marker = new MarkerOptions().title(handler.getCurrCar(getBaseContext()));
        marker.position(location);
        mMap.addMarker(marker.title(handler.getCurrCar(getBaseContext())));
        handler.setLatitude(handler.getCurrCar(getBaseContext()) + "latitude",latitude,getBaseContext());
        handler.setLongitude(handler.getCurrCar(getBaseContext())+ "longitude",longitude,getBaseContext());
    }
    //launcher status pagen
    public void launchStatus(){
        startActivity(new Intent(this,Status.class));
        finish();
    }
    //Her blir verdiene returnert fra databasen, merk at dette er en asynk operasjon slik at dette gjøres paralelt med andre ting
    @Override
    public void processFinished(String s) {
        cords = s;
        String[] cordinates = s.split(",");
        if(!cordinates[0].equals("") && !cordinates[1].equals("")){
            longitude = Float.valueOf(cordinates[0]);
            latitude = Float.valueOf(cordinates[1]);
        }
        else{
            AlertDialog alertDialog2 = new AlertDialog.Builder(this).create();
            alertDialog2.setTitle("Coordinates not available");
            alertDialog2.setMessage("This car doesn't have any registered coordinates");
            alertDialog2.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    launchStatus();
                    Toast.makeText(getApplicationContext(), "Returned to status page", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog2.show();
            h.removeCallbacks(runner);
        }
        Log.d("Cords", cords);
    }
    //Starter å hente kordinater hvert sekund (kan endres)
    public void startUpdater(){
        final int delay = 1000; //milliseconds
        runner = new Runnable() {
            public void run() {
                getCoords(handler.getCurrCar(getBaseContext()));
                if(longitude != 0 || latitude != 0){
                    updateMap(longitude, latitude);
                }
                h.postDelayed(this, delay);
            }
        };
        h.postDelayed(runner, delay);
    }
    //Stopper å hente når du forlater activitien
    @Override
    public void onPause(){
        super.onPause();
        h.removeCallbacks(runner);
    }
}
