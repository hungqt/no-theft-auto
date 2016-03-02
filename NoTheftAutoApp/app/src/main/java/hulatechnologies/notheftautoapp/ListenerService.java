package hulatechnologies.notheftautoapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.PeriodicSync;
import android.os.Handler;
import android.util.Log;

/**
 * Created by thoma on 3/1/2016.
 */
public class ListenerService extends IntentService {
    public ListenerService() {
        super("ListenerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        //Do stuff
    }
}
