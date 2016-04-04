package hulatechnologies.notheftautoapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements AsyncResponse2 {

    private Button btnReg; //Hey
    private Button btnLogin;
    private Button btnLogout;
    private Button btnStatus;
    private static final String PrefName = "User";
    private static final String PrefPass = "Pass";
    private static final String logName = "Log";
    private boolean alarmSet = false;
    private PreferenceHandler handler = new PreferenceHandler();
    private GCMmanager gcmM = new GCMmanager(this,this);
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FragmentTransaction fragmentTransaction;
    private NavigationView navigationView;
    private Button btnMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReg = (Button)findViewById(R.id.btnReg);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogout = (Button)findViewById(R.id.btnLogout);
        btnStatus = (Button)findViewById(R.id.btnStatus);
        btnMap = (Button)findViewById(R.id.btnMap);
        Log.d("Status", handler.getLoggedIn(getBaseContext()) + "");
        Log.d("User", handler.getPrefName(getBaseContext()) + "");
        if(handler.getLoggedIn(getBaseContext())) {
            btnLogin.setVisibility(View.INVISIBLE);
            btnStatus.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
            btnMap.setVisibility(View.VISIBLE);
            if(!handler.getGCMstate(getBaseContext())){
                gcmM.startGCM();
                handler.setGCMactive(true, getBaseContext());
            }
            else{
                updateToken(handler.getToken(getBaseContext()));
            }
        }else{
            btnStatus.setVisibility(View.INVISIBLE);
            btnLogout.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            btnMap.setVisibility(View.INVISIBLE);

            if(alarmSet == true){
                //alarmSet = false;
                //cancelAlarm();
            }
        }
    }

    //Use this method to initiate the navigation drawer in the onCreate() function
    public void initNavigationDrawer(){
        //Instantiate drawerLayout and actionBarDrawerToggle to make a hamburger button.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //FragmentsHandling
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new HomeFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Home Fragment");
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new HomeFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Home Fragment");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.cars_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new CarsFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("My Cars Fragment");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                }

                return true;
            }
        });
    }

    public void goToReg(View v){
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }
    public void goToLogin(View v){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
    public void goToStatus(View v){
        callDataBase();
    }

    public void logOut(View v){
        resetToken();
        handler.resetPrefName(getBaseContext());
        handler.resetPrefPass(getBaseContext());
        handler.resetCarString(getBaseContext());

        btnLogout.setVisibility(View.INVISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        btnStatus.setVisibility(View.INVISIBLE);
        btnMap.setVisibility(View.INVISIBLE);

        handler.setLoggedIn(false, getBaseContext());
    }
    public void goToMap(View v){
        startActivity(new Intent(this,MapActivity.class));
        finish();
    }
    private void resetToken() {
        AsyncUpdateToken updater = new AsyncUpdateToken();
        JSONObject json = new JSONObject();
        try {
            json.put("username", handler.getPrefName(getBaseContext()));
            json.put("token", "");

            updater.execute(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updateToken(String token){
        AsyncUpdateToken updater = new AsyncUpdateToken();
        JSONObject json = new JSONObject();
        try {
            json.put("username", handler.getPrefName(getBaseContext()));
            json.put("token", token);

            updater.execute(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void callDataBase(){
        AsyncGetCars cars = new AsyncGetCars();
        cars.delegate = this;
        JSONObject json = new JSONObject();
        try {
            json.put("username",handler.getPrefName(getBaseContext()));
            json.put("password",handler.getPrefPass(getBaseContext()));

            cars.execute(json);

        } catch (JSONException e) {
        e.printStackTrace();
        }
        }

    @Override
    public void processFinish(String output) {
        String[] list = output.split("/");
        String carString = "";
        for(int i = 0; i < list.length/3;i++){
            if(list[i*3 + 1].equals("1")){
            handler.setCarAlarmActive(true,getBaseContext(),list[i*3]);
            Log.d("Handler", "Alarm sat active");
            }
            else{
            handler.setCarAlarmActive(false,getBaseContext(),list[i*3]);
            Log.d("Handler","Alarm sat not active");
            }
            Log.d("ID",list[i*3]);
            Log.d("Alarm",list[i*3+1]);
            Log.d("Navn",list[i*3+2]);
        handler.setCarName(list[i*3 + 2],getBaseContext(),list[i*3]+"Name");
        carString += list[i*3];
        }
        Log.d("Car String", carString);
        handler.setCarString(carString, getBaseContext());
        startActivity(new Intent(this, Status.class));
        finish();
    }
}
