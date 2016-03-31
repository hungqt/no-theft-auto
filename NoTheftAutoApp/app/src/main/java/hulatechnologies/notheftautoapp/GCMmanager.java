package hulatechnologies.notheftautoapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by thoma on 3/30/2016.
 */
public class GCMmanager extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private Context context;
    private Activity activity;

    public GCMmanager(Context context,Activity activity){
        this.context = context;
        this.activity = activity;
    }

    //Opens GCM connection
    public void startGCM(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(QuickStartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.d("Message", "Sent");
                } else {
                    Log.d("Message","Error");
                }
            }
        };

        // Registering BroadcastReceiver
        registerReceiver();
        if(checkPlayServices()) {
            Intent intent = new Intent(context, RegistrationIntentService.class);
            context.startService(intent);
            Log.d("Started","Registration intent");
        }
        //END of GCM
    }
    //Closes the connection
    public void closeGCM(){

    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickStartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
            Log.d("Registered","Was here");
        }
    }
}
