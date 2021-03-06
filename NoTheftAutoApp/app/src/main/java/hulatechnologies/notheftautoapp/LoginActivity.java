package hulatechnologies.notheftautoapp;

import android.app.Activity;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class LoginActivity extends AppCompatActivity implements AsyncResponse{

    private Button btnLogin;
    private EditText userText;
    private EditText passText;
    private AlertDialog alertDialog;
    private AlertDialog alertDialog2;
    private PreferenceHandler handler = new PreferenceHandler();
    private GCMmanager gcm = new GCMmanager(this,this);
    private String verificationString;
    private SecureRandom random = new SecureRandom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI(findViewById(R.id.parent));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Kobler alle knapper og tekstvinduer med xml-layouten

        btnLogin = (Button) findViewById(R.id.btnLogin);
        userText = (EditText) findViewById(R.id.userText);
        passText = (EditText) findViewById(R.id.passText);

        //Initialize a alertDialog for later use

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

    //Metoden som blir kalt når du trykker på login knappen i loginscreenen

    public void onLClick(View v){

        //Launcher en Asynkron tråd som kjører i paralell med resten av koden
        //delegate er til for å hente ut svaret fra Async-klassen og sende det til processFinished-metoden lenger ned

        AsyncTaskCheckUser data = new AsyncTaskCheckUser();
        data.delegate = this;

        //Oppretter et json-objekt som skal sendes til webserveren med et backend script som skal snakke med databasen
        //verificationString er en random-generert streng som skulle vært brukt til loginstate-validering om vi hadde rukket det
        //navn og pass hentes ut fra tekstfeltene i loginscreenen
        JSONObject json = new JSONObject();
        verificationString = nextSessionId();
        try {
            json.put("username", userText.getText().toString());
            json.put("password", passText.getText().toString());
            json.put("verification",verificationString);
            data.execute(json);

            handler.setPrefName(userText.getText().toString(), getBaseContext());
            handler.setPrefPass(passText.getText().toString(), getBaseContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Genererer en ny random streng for state validering (Rakk ikke bli ferdig)
    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
    //Her kommer svaret fra webserveren om hva som skjedde i databasen
    @Override
    public void processFinish(Integer output) {
        // 1 betyr at login var vellykket og statusen blir oppdatert til "logged in"
        //Går tilbake til main
        if(output == 1){
            handler.setLoggedIn(true, getBaseContext());
            handler.setVerificationString(verificationString,getBaseContext());
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        //Dette betyr at login var mislykket, feil navn eller passord
        else if(output == 2){
            alertDialog.show();
        }
        //Dette betyr at noe gikk galt, enten på server siden eller her
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
    //Metoden som gjemmer keyboardet når du trykker utenfor keyboardet
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    //Copy paste kode jeg ikke vet helt hvordan funker, men den gjemmer keyboardet om du trykker en plass utenfor det
    public void setupUI(View view) {

        //Setter opp en lytter som sjekker om du trykker utenfor tekstfeltet og keyboardet
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(LoginActivity.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion (Copy-paste kommentar)
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

}

