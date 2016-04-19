package hulatechnologies.notheftautoapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Image;
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
import android.widget.ImageView;
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
    private Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Setter layout-xml, finner toolbaren i xmlen og knappene/tekstfelt


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReg = (Button)findViewById(R.id.btnReg);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("No Theft Auto");
        mActionBarToolbar.setTitle("No Theft Auto");

        //Sjekker om brukeren er logget inn
        if(handler.getLoggedIn(getBaseContext())) {
            //Hvis bruker er logged in, gå til status siden
            setContentView(R.layout.activity_nav_drawer);
            initNavigationDrawer();
            //Sjekker om en gcm connection er opprettet, hvis ikke oppretter den en connection
            if(!handler.getGCMstate(getBaseContext())){
                gcmM.startGCM();
                handler.setGCMactive(true, getBaseContext());
            }
            //Ellers bare oppdaterer man sin personlige token i databasen slik at den samsvarer med den rette
            else{
                updateToken(handler.getToken(getBaseContext()));
            }
        }else{
            //Hvis ikke hvis setter den login-knappen til true
            btnLogin.setVisibility(View.VISIBLE);

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
                        mActionBarToolbar.setTitle("No Theft Auto");
                        btnReg = (Button) findViewById(R.id.btnReg);
                        btnLogin = (Button) findViewById(R.id.btnLogin);
                        break;
                }
                return true;
            }
        });
    }

    //Overrider back-knappen hvis man er i navigation-baren sånn at den lukkes og ikke hele programmet
    @Override
    public void onBackPressed() {
        if(drawerLayout.isEnabled()){
            drawerLayout.closeDrawers();
        }
        else{
            super.onBackPressed();
        }
    }

    //Gå til registreringsscreenen
    public void goToReg(View v){
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }
    //Gå til loginscreenen
    public void goToLogin(View v) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
    //Sletter lokal data om man logger ut fra main og setter status til logged out
    public void logOutFromMain(){
        resetToken();

        handler.resetPrefName(getBaseContext());
        handler.resetPrefPass(getBaseContext());

        handler.resetCars(getBaseContext(),handler.getCarString(getBaseContext()));
        handler.resetCarString(getBaseContext());
        
        btnLogin.setVisibility(View.VISIBLE);

        handler.setLoggedIn(false, getBaseContext());
    }
    //Resetter telefonenstoken i databasen
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
    //Setter telefonens token i databasen
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
