package hulatechnologies.notheftautoapp;

import android.annotation.SuppressLint;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ObserverActivity extends ContentObserver {


    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public ObserverActivity(Handler handler) {
        super(handler);
    }
    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {

    }
}
