package hulatechnologies.notheftautoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NavDrawer extends AppCompatActivity {

    PreferenceHandler handler = new PreferenceHandler();
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
    }
}
