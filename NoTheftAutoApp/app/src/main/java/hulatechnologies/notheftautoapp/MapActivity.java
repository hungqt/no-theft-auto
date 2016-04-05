package hulatechnologies.notheftautoapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

public class MapActivity extends AppCompatActivity implements AsyncResponse3 {

    PreferenceHandler handler = new PreferenceHandler();
    String cords = "0,0";
    int longitude = 0;
    int latitude = 0;
    final Handler h = new Handler();
    Runnable runner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("Car", handler.getCurrCar(getBaseContext()));
        startUpdater();

    }
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

    public void updateMap(int longitude, int latitude){
        //Insert code for google maps here
    }

    @Override
    public void processFinished(String s) {
        cords = s;
        String[] cordinates = s.split(",");
        longitude = Integer.valueOf(cordinates[0]);
        latitude = Integer.valueOf(cordinates[1]);
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
        h.postDelayed(runner,delay);
    }
    @Override
    public void onPause(){
        super.onPause();
        h.removeCallbacks(runner);
    }
}
