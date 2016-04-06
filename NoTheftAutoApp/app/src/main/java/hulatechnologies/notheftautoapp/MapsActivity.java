package hulatechnologies.notheftautoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    //Start funksjoner for å hente kordinatene
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

    public void updateMap(float longitude, float latitude){
        //Insert code for google maps here
    }

    public void launchStatus(){
        startActivity(new Intent(this,Status.class));
        finish();
    }

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
    public void startUpdater(){
        final int delay = 1000; //milliseconds
        runner = new Runnable() {
            public void run() {
                getCoords(handler.getCurrCar(getBaseContext()));
                updateMap(longitude, latitude);
                h.postDelayed(this, delay);
            }
        };
        h.postDelayed(runner, delay);
    }
    @Override
    public void onPause(){
        super.onPause();
        h.removeCallbacks(runner);
    }
}
