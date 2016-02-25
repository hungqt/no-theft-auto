package notheftautoapp.hulatechnologies.examplecodemodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NavDrawerActivity extends AppCompatActivity {

    private String[] nbaPlayerNames;
    private DrawerLayout DrawerLayout;
    private ListView DrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        //Hiding the action bar on create
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initDrawerList();
    }

    public void initDrawerList(){
        //Getting planet-titles from /res/values/strings.xml (Manually write it in, in this example; the list of NBA players)
        nbaPlayerNames = getResources().getStringArray(R.array.planets_array);
        DrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        DrawerList = (ListView) findViewById(R.id.left_drawer);

        //Set the adapter for the list view
        DrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, nbaPlayerNames));

        //Set the list's click listener
        //DrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

}