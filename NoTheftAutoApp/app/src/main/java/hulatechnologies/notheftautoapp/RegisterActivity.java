package hulatechnologies.notheftautoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText userText;
    private EditText passText1;
    private EditText passText2;
    private EditText emailText;
    Context c;

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
        JSONObject userInfo = new JSONObject();
        c = this;
        try {
            userInfo.put("username",userText.getText().toString());
            String pass1 = passText1.getText().toString();
            String pass2 = passText2.getText().toString();
            String email = emailText.getText().toString();
            Validation val = new Validation();
            if(!val.checkEmail(email) || !val.checkUsername(userText.getText().toString())){
                Toast.makeText(c,"Email or username was invalid", Toast.LENGTH_SHORT).show();
                return;
            }
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
        startActivity(new Intent(this, MainActivity.class));
    }
    public void onCancel(View v){
        userText.setText("");
        passText1.setText("");
        passText2.setText("");
        emailText.setText("");
        startActivity(new Intent(this, MainActivity.class));
    }

    public void showAlert(View view) {
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage("Invalid username or email,username can only contain letters")
                .setNeutralButton("GG...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setTitle("")
                .create();
        myAlert.show();
    }
    }
