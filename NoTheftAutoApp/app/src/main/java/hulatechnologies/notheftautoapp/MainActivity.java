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

public class MainActivity extends AppCompatActivity {

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
    private TextView name;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReg = (Button)findViewById(R.id.btnReg);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogout = (Button)findViewById(R.id.btnLogout);
        Log.d("Status", handler.getLoggedIn(getBaseContext()) + "");
        Log.d("User", handler.getPrefName(getBaseContext()) + "");
        Log.d("Verification", handler.getVerificationString(getBaseContext()) + "Hello");
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("No Theft Auto");
        if(handler.getLoggedIn(getBaseContext())) {
            setContentView(R.layout.activity_nav_drawer);
            initNavigationDrawer();
            if(!handler.getGCMstate(getBaseContext())){
                gcmM.startGCM();
                handler.setGCMactive(true, getBaseContext());
            }
            else{
                updateToken(handler.getToken(getBaseContext()));
            }
        }else{
            btnLogout.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.VISIBLE);

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
        fragmentTransaction.add(R.id.main_container, new Status());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Status");
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new Status());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Status");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.log_out_id:
                        logOutFromMain();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        setContentView(R.layout.activity_main);
                        btnReg = (Button) findViewById(R.id.btnReg);
                        btnLogin = (Button) findViewById(R.id.btnLogin);
                        btnLogout = (Button) findViewById(R.id.btnLogout);
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isEnabled()){
            drawerLayout.closeDrawers();
        }
    }

    public void goToReg(View v){
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }
    public void goToLogin(View v) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
    public void logOut(View v){
        logOutFromMain();
    }

    public void logOutFromMain(){
        resetToken();
        handler.clear(getBaseContext());

        btnLogout.setVisibility(View.INVISIBLE);
        btnLogin.setVisibility(View.VISIBLE);

        handler.setLoggedIn(false, getBaseContext());
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
}
