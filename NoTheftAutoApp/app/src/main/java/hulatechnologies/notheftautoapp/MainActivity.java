package hulatechnologies.notheftautoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnReg; //Hey
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReg = (Button)findViewById(R.id.btnReg);
        btnLogin = (Button)findViewById(R.id.btnLogin);

    }

    public void goToReg(View v){
        startActivity(new Intent(this, RegisterActivity.class));
    }
    public void goToLogin(View v){
        startActivity(new Intent(this, LoginActivity.class));
    }
}
