package hulatechnologies.notheftautoapp;

import android.app.AlertDialog;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.PeriodicSync;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thoma on 3/1/2016.
 */
public class ListenerService extends IntentService implements AsyncResponse {
    public ListenerService() {
        super("ListenerService");
    }

    private static final String PrefName = "User";
    private static final String PrefPass = "Pass";

    @Override
    protected void onHandleIntent(Intent intent) {
        callDataBase();
    }
    public void callDataBase(){
        AsyncAlarm data = new AsyncAlarm();
        data.delegate = this;
        JSONObject json = new JSONObject();
        try {
            json.put("username", getPrefName());
            json.put("password", getPrefPass());
            data.execute(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void processFinish(Integer output) {
        if(output == 1){
            Log.d("Alarm","Active");
        }
        else{
            Log.d("Alarm", "Not active");
        }
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
}
