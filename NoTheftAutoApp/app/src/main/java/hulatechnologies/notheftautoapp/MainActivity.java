package hulatechnologies.notheftautoapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnReg; //Hey
    private Button btnLogin;
    private static final String PrefName = "User";
    private static final String PrefPass = "Pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReg = (Button)findViewById(R.id.btnReg);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        scheduleAlarm();

    }

    public void goToReg(View v){
        startActivity(new Intent(this, RegisterActivity.class));
    }
    public void goToLogin(View v){
        startActivity(new Intent(this, LoginActivity.class));
    }


    private String getPrefName(){
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String value=(mSharedPreference.getString(PrefName, ""));
        return value;
    }
    private String getPrefPass(){
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String value=(mSharedPreference.getString(PrefPass, ""));
        return value;
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
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                15000, pIntent);
    }
}
