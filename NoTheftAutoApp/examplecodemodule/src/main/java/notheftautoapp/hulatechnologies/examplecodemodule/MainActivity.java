package notheftautoapp.hulatechnologies.examplecodemodule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
//Must import this manually to use ActionBar
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView generatedTxt;
    Button whatcd;
    String cd = "I just got this new CD";
    String gotem = "See Deez Nuts... HAH GOTEEEEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generatedTxt = (TextView)findViewById(R.id.generatedTxt);

        //Hiding the action bar on create
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    public void generateText(View view){
        whatcd = (Button)findViewById(R.id.whatcd);

        if(generatedTxt.getText().toString().equals(cd)) {
            generatedTxt.setText(gotem);
            whatcd.setText("ayy lmao");
        }

        else{
            generatedTxt.setText(cd);
            whatcd.setText("What CD?");
        }

    }

    public void changeToNext(View view){
        Intent intent = new Intent(this, NavDrawerActivity.class);
        startActivity(intent);
    }

}