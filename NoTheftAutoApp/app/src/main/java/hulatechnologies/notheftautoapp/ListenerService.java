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
        SyncAlarm data = new SyncAlarm();
        JSONObject json = new JSONObject();
        try {
            json.put("username", handler.getPrefName(getBaseContext()));
            json.put("password", handler.getPrefPass(getBaseContext()));
            Log.d("username",handler.getPrefName(getBaseContext()));
            Log.d("password",handler.getPrefPass(getBaseContext()));
            int answer = data.doInBackground(json);

            if(answer == 1){
                Log.d("Alarm","Active");
            }
            else if(answer == 0){
                Log.d("Alarm", "Not active");
            }
            else{
                Log.d("Connection error","Something went wrong");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
