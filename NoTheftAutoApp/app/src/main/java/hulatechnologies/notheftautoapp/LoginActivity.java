package hulatechnologies.notheftautoapp;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements AsyncResponse{

    private Button btnLogin;
    private EditText userText;
    private EditText passText;
    private CheckBox remMe;
    private AlertDialog alertDialog;
    private AlertDialog alertDialog2;
    private AlertDialog acceptDialog;
    private static final String PrefName = "User";
    private static final String PrefPass = "Pass";
    private NotificationManager NM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NM=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        remMe = (CheckBox)findViewById(R.id.remMe);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        userText = (EditText) findViewById(R.id.userText);
        passText = (EditText) findViewById(R.id.passText);
        if(!getPrefName().isEmpty()){
            userText.setText(getPrefName());
            passText.setText(getPrefPass());
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



            if(remMe.isChecked()) {
                setPrefName(userText.getText().toString());
                setPrefPass(passText.getText().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void processFinish(Integer output) {
        if(output == 1){
            startActivity(new Intent(LoginActivity.this, StartActivity.class));
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
    private void setPrefName(String prefName){
        PreferenceManager.getDefaultSharedPreferences(LoginActivity.this.getApplicationContext()).edit().putString(PrefName, prefName).commit();
    }
    private void setPrefPass(String prefPass){
        PreferenceManager.getDefaultSharedPreferences(LoginActivity.this.getApplicationContext()).edit().putString(PrefPass, prefPass).commit();
    }
    private String getPrefName(){
        return PreferenceManager.getDefaultSharedPreferences(LoginActivity.this.getApplicationContext()).getString(PrefName,"");
    }
    private String getPrefPass(){
        return PreferenceManager.getDefaultSharedPreferences(LoginActivity.this.getApplicationContext()).getString(PrefPass,"");
    }
}

