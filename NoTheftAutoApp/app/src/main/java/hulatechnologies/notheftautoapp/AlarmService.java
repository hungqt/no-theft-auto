package hulatechnologies.notheftautoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;

/**
 * Created by thoma on 3/1/2016.
 */
public class AlarmService extends BroadcastReceiver {

    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.codepath.example.servicesdemo.alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ListenerService.class);
        i.putExtra("foo", "bar");
        context.startService(i);
    }
}
