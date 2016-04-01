package hulatechnologies.notheftautoapp;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements AsyncResponse1And2{

    private Button btnLogin;
    private EditText userText;
    private EditText passText;
    private CheckBox remMe;
    private AlertDialog alertDialog;
    private AlertDialog alertDialog2;
    private PreferenceHandler handler = new PreferenceHandler();
    private GCMmanager gcm = new GCMmanager(this,this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        remMe = (CheckBox)findViewById(R.id.remMe);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        userText = (EditText) findViewById(R.id.userText);
        passText = (EditText) findViewById(R.id.passText);
        if(handler.getPrefRem(getBaseContext()) == true){
            userText.setText(handler.getPrefName(getBaseContext()));
            passText.setText(handler.getPrefPass(getBaseContext()));
        }
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Wrong username or password");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onLClick(View v){
        AsyncTaskCheckUser data = new AsyncTaskCheckUser();
        data.delegate = this;
        JSONObject json = new JSONObject();
        try {
            json.put("username", userText.getText().toString());
            json.put("password", passText.getText().toString());
            data.execute(json);

            if(remMe.isChecked()){
                handler.setPrefRem(true,getBaseContext());
            }
            handler.setPrefName(userText.getText().toString(), getBaseContext());
            handler.setPrefPass(passText.getText().toString(), getBaseContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void callDataBase(){
        AsyncGetCars cars = new AsyncGetCars();
        cars.delegate = this;
        JSONObject json = new JSONObject();
        try {
            json.put("username", handler.getPrefName(getBaseContext()));
            json.put("password", handler.getPrefPass(getBaseContext()));

            cars.execute(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //Handles the login request
    @Override
    public void processFinish(Integer output) {
        if(output == 1){
            handler.setLoggedIn(true, getBaseContext());
            //callDataBase();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else if(output == 2){
            alertDialog.show();
        }
        else{
            alertDialog2 = new AlertDialog.Builder(this).create();
            alertDialog2.setTitle("Error");
            alertDialog2.setMessage("Connection error");
            alertDialog2.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog2.show();
        }
    }

    //Updates the cars
    @Override
    public void processFinish(String output) {
        String[] list = output.split("/");
        String carString = "";
        Boolean startPush = false;
        for(int i = 0; i < list.length/3;i++){
            if(list[i*3 + 1].equals("1")){
                handler.setCarAlarmActive(true,getBaseContext(),list[i*3]);
                startPush = false;
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
        if(startPush){
            int requestID = (int) System.currentTimeMillis();
            Intent notificationIntent;
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context
                    .NOTIFICATION_SERVICE);
            notificationIntent = new Intent(this, Status.class);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setVibrate(new long[]{1, 1, 1})
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setContentTitle("Attention").setColor(Color.RED)
                    .setContentText("Your car might have been stolen!")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setContentIntent(contentIntent);
            notificationManager.notify(0, notificationBuilder.build());

            startPush = false;
        }
        Log.d("Car String", carString);
        handler.setCarString(carString, getBaseContext());
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

