package hulatechnologies.notheftautoapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Status extends AppCompatActivity implements AsyncResponse2 {
    PreferenceHandler handler = new PreferenceHandler();
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        Button btnUpdate = (Button)findViewById(R.id.btnUpdate);

        tableLayout = (TableLayout)findViewById(R.id.tableLayout);
        TableRow tr_head = new TableRow(this);
        tr_head.setId(View.generateViewId());
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView label_car = new TextView(this);
        label_car.setId(View.generateViewId());
        label_car.setText("Car");
        label_car.setTextColor(Color.WHITE);
        label_car.setPadding(5, 5, 5, 5);
        tr_head.addView(label_car);// add the column to the table row here

        TextView label_status = new TextView(this);
        label_status.setId(View.generateViewId());
        label_status.setText("Status");
        label_status.setTextColor(Color.WHITE);
        label_status.setPadding(5, 5, 5, 5);
        tr_head.addView(label_status);

        tableLayout.addView(tr_head,new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        String carString = handler.getCarString(getBaseContext());
        Log.d("CarString","Hello" + carString);
        for(int i = 0; i < carString.length();i++){
            String id = carString.substring(i,i+1);
            addRow(handler.getCarAlarmActive(getBaseContext(),id),id);
        }
    }

    public void addRow(boolean alarm,String id){
        String carName = handler.getCarName(getBaseContext(),id+"Name");

        TableRow tR = new TableRow(this);
        tR.setBackgroundColor(Color.GRAY);
        tR.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView label_car = new TextView(this);
        label_car.setId(View.generateViewId());
        label_car.setText(carName);
        label_car.setTextColor(Color.BLUE);
        label_car.setPadding(5, 5, 5, 5);
        tR.addView(label_car);

        TextView label_status = new TextView(this);
        label_status.setId(View.generateViewId());
        if(alarm){
            label_status.setText("Active");
            label_status.setTextColor(Color.RED);
        }
        else{
            label_status.setText("Not active");
            label_status.setTextColor(Color.GREEN);
        }
        label_status.setPadding(5, 5, 5, 5);
        tR.addView(label_status);


        tableLayout.addView(tR, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }
    public void callDataBase(){
        AsyncGetCars cars = new AsyncGetCars();
        cars.delegate = this;
        JSONObject json = new JSONObject();
        try {
            json.put("username",handler.getPrefName(getBaseContext()));
            json.put("password",handler.getPrefPass(getBaseContext()));

            cars.execute(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void Update(View v){
        callDataBase();
    }
    public void cancel(View v){
        startActivity(new Intent(Status.this, MainActivity.class));
        finish();
    }

    @Override
    public void processFinish(String output) {
        String[] list = output.split("/");
        String carString = "";
        for(int i = 0; i < list.length/3;i++){
            if(list[i*3 + 1].equals("1")){
                handler.setCarAlarmActive(true,getBaseContext(),list[i*3]);
                Log.d("Handler", "Alarm sat active");
            }
            else{
                handler.setCarAlarmActive(false,getBaseContext(),list[i*3]);
                Log.d("Handler","Alarm sat not active");
            }
            Log.d("ID",list[i*3]);
            Log.d("Alarm",list[i*3+1]);
            Log.d("Navn",list[i*3+2]);
            handler.setCarName(list[i*3 + 2],getBaseContext(),list[i*3]+"Name");
            carString += list[i*3];
        }
        Log.d("Car String", carString);
        handler.setCarString(carString, getBaseContext());
        startActivity(new Intent(this, Status.class));
        finish();
    }
}
