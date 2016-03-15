package hulatechnologies.notheftautoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements AsyncResponse3{

    private Button btnRegister;
    private EditText userText;
    private EditText passText1;
    private EditText passText2;
    private EditText emailText;
    private boolean userExist = true;
    private PreferenceHandler handler = new PreferenceHandler();
    private AlertDialog alertDialog2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnRegister = (Button)findViewById(R.id.btnRegister);
        userText = (EditText)findViewById(R.id.userText);
        passText1 = (EditText)findViewById(R.id.passText1);
        passText2 = (EditText)findViewById(R.id.passText2);
        emailText = (EditText)findViewById(R.id.emailText);

    }
    public void onRegClick(View v){
        AsyncTaskRegister data = new AsyncTaskRegister();
        data.delegate = this;
        JSONObject userInfo = new JSONObject();
        try {
            userInfo.put("username",userText.getText().toString());
            String pass1 = passText1.getText().toString();
            String pass2 = passText2.getText().toString();
            String email = emailText.getText().toString();
            if(!pass1.equals(pass2)){
                throw new IllegalStateException();
            }
            userInfo.put("email",email);
            userInfo.put("password", pass1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
          catch(IllegalStateException e){
              e.printStackTrace();
          }
        if(userInfo != null) {
            data.execute(userInfo);
            Log.d("Noe skjedde","Lmfao");
        }
        else{
            Log.d("Error:", "Noe gikk galt");
        }
    }
    public void onCancel(View v){
        userText.setText("");
        passText1.setText("");
        passText2.setText("");
        emailText.setText("");
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void processFinished(String output) {
        Log.d("Output",output + "What");
        if(output.equals("User exists")){
            alertDialog2 = new AlertDialog.Builder(this).create();
            alertDialog2.setTitle("Failure");
            alertDialog2.setMessage("Username is taken");
            alertDialog2.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog2.show();
        }
        else{
            Log.d("Success!","It worked");
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
