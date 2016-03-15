package hulatechnologies.notheftautoapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thoma on 3/1/2016.
 */
public class ListenerService extends IntentService {
    public ListenerService() {
        super("ListenerService");
    }
    private PreferenceHandler handler = new PreferenceHandler();
    private NotificationCompat.Builder m notifier = new NotificationActivity();


    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("MyTestService", "Service running");
        callDataBase();
    }
    public void callDataBase(){
        //SyncGetCars henter alle ID-ene til bilene basert p� brukernavn og passord
        //SyncAlarm sjekker om alarmen er g�tt p� den spesifikke IDen og returnerer dette sammen med navnet p� bilen
        SyncGetCars cars = new SyncGetCars();
        SyncAlarm check = new SyncAlarm();
        JSONObject json = new JSONObject();
        String carIDsString = "";
        try {
            json.put("username", handler.getPrefName(getBaseContext()));
            json.put("password", handler.getPrefPass(getBaseContext()));
            Log.d("username",handler.getPrefName(getBaseContext()));
            Log.d("password", handler.getPrefPass(getBaseContext()));
            Integer[] carIDs = cars.doInBackground(json);
            if(carIDs != null){
            for(int i = 0; i < carIDs.length; i++){
                JSONObject j = new JSONObject();
                j.put("carID",carIDs[i]+"");
                String c = check.doInBackground(j);
                int alarm = Integer.valueOf(c.substring(0,1));

                handler.setCarName(c.substring(1,c.length()),getBaseContext(),carIDs[i]+"Name");

                if (alarm == 1){
                    Log.d("Alarm ACTIVE", c.substring(1, c.length()));
                    handler.setCarAlarmActive(true,getBaseContext(),carIDs[i]+"");
                    notifier.notification();
                }
                else{
                    Log.d("Alarm NOT ACTIVE",c.substring(1,c.length()));
                    handler.setCarAlarmActive(false,getBaseContext(),carIDs[i]+"");
                }
                carIDsString += carIDs[i];
                }
            }
            handler.setCarString(carIDsString,getBaseContext());
            Log.d("CarIDs",handler.getCarString(getBaseContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
