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
        startActivity(new Intent(this,Status.class));
        finish();
    }
    public void logOut(View v){
        resetPrefName();
        resetPrefPass();

        btnLogout.setVisibility(View.INVISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        btnStatus.setVisibility(View.INVISIBLE);

        cancelAlarm();

        handler.setLoggedIn(false,getBaseContext());
    }
    private void resetPrefName(){
        SharedPreferences SPpass = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = SPpass.edit();
        editor.putString(PrefName, "");
        editor.commit();
    }
    private void resetPrefPass(){
        SharedPreferences SPpass = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = SPpass.edit();
        editor.putString(PrefPass, "");
        editor.commit();
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
}
