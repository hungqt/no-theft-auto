package hulatechnologies.notheftautoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText userText;
    private EditText passText1;
    private EditText passText2;
    private EditText emailText;


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
        startActivity(new Intent(this, StartActivity.class));
    }
    public void onCancel(View v){
        userText.setText("");
        passText1.setText("");
        passText2.setText("");
        emailText.setText("");
        startActivity(new Intent(this, StartActivity.class));
    }

}
