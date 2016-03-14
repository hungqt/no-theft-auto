package hulatechnologies.notheftautoapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Status extends AppCompatActivity {
    static final int Tr_Id = 120001;
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
        tableLayout = (TableLayout)findViewById(R.id.tableLayout);
        TableRow tr_head = new TableRow(this);
        tr_head.setId(View.generateViewId());
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView label_date = new TextView(this);
        label_date.setId(View.generateViewId());
        label_date.setText("Alarms");
        label_date.setTextColor(Color.WHITE);
        label_date.setPadding(5, 5, 5, 5);
        tr_head.addView(label_date);// add the column to the table row here

        tableLayout.addView(tr_head,new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        String carString = handler.getCarString(getBaseContext());
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

        TextView rowText = new TextView(this);
        if(alarm){
            rowText.setText(carName + ": " + "Alarm is active!");
        }
        else{
            rowText.setText(carName + ": " + "Alarm is not active!");
        }

        tR.addView(rowText);
        tableLayout.addView(tR,new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

}
