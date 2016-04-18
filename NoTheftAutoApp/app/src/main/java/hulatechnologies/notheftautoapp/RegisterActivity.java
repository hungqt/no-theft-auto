package hulatechnologies.notheftautoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
    Context c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUI(findViewById(R.id.parent));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnRegister = (Button)findViewById(R.id.btnRegister);
        userText = (EditText)findViewById(R.id.userText);
        passText1 = (EditText)findViewById(R.id.passText1);
        passText2 = (EditText)findViewById(R.id.passText2);
        emailText = (EditText)findViewById(R.id.emailText);
    }
    public void onRegClick(View v){
        AsyncSendJSONreturnString data = new AsyncSendJSONreturnString("http://folk.ntnu.no/thomborr/NoTheftAuto/Register.php");
        data.delegate = this;
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

            if(userInfo != null) {
                data.execute(userInfo);
                Log.d("Noe skjedde","Lmfao");
            }
            else{
                Log.d("Error:", "Noe gikk galt");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
          catch(IllegalStateException e){
              e.printStackTrace();
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
        else if(output.equals("Success!")){
            alertDialog2 = new AlertDialog.Builder(this).create();
            alertDialog2.setTitle("Success!");
            alertDialog2.setMessage("Account created!");
            alertDialog2.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startMain();
                    finish();
                    Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog2.show();

        }
        else{
            alertDialog2 = new AlertDialog.Builder(this).create();
            alertDialog2.setTitle("Connection error");
            alertDialog2.setMessage("There is a error in the connection or the server");
            alertDialog2.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog2.show();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
    public void startMain(){
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
    //Metoden som gjemmer keyboardet
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    //Copy paste kode jeg ikke vet helt hvordan funker, men den gjemmer keyboardet om du trykker en plass utenfor det
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(RegisterActivity.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }
}
