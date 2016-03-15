package hulatechnologies.notheftautoapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReg = (Button)findViewById(R.id.btnReg);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogout = (Button)findViewById(R.id.btnLogout);
        btnStatus = (Button)findViewById(R.id.btnStatus);
        Log.d("Status", handler.getLoggedIn(getBaseContext()) + "");
        Log.d("User", handler.getPrefName(getBaseContext()) + "");
        if(handler.getLoggedIn(getBaseContext())) {
            scheduleAlarm();
            alarmSet = true;
            btnLogin.setVisibility(View.INVISIBLE);
            btnStatus.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
        }else{
            btnStatus.setVisibility(View.INVISIBLE);
            btnLogout.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            if(alarmSet == true){
                alarmSet = false;
                cancelAlarm();
            }
        }


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
        handler.resetPrefName(getBaseContext());
        handler.resetPrefPass(getBaseContext());
        handler.resetCarString(getBaseContext());

        btnLogout.setVisibility(View.INVISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        btnStatus.setVisibility(View.INVISIBLE);

        cancelAlarm();

        handler.setLoggedIn(false, getBaseContext());
    }

    // Setup a recurring alarm every 15 seconds
    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), AlarmService.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmService.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis, 60000, pIntent);
    }
    //Cancel the set alarm
    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), AlarmService.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmService.REQUEST_CODE,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }
    public void scheduleNotification(){

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