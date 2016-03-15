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

        TextView label_car = new TextView(this);
        label_car.setId(View.generateViewId());
        label_car.setText("Car");
        label_car.setTextColor(Color.WHITE);
        label_car.setPadding(5, 5, 5, 5);
        tr_head.addView(label_car);// add the column to the table row here

        TextView label_status = new TextView(this);
        label_status.setId(View.generateViewId());// define id that must be unique
        label_status.setText("Status"); // set the text for the header
        label_status.setTextColor(Color.WHITE); // set the color
        label_status.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_status); // add the column to the table row here

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
        tR.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView label_car = new TextView(this);
        label_car.setId(View.generateViewId());
        label_car.setText(carName);
        label_car.setTextColor(Color.BLUE);
        label_car.setPadding(5, 5, 5, 5);
        tR.addView(label_car);

        TextView label_status = new TextView(this);
        label_status.setId(View.generateViewId());
        if(alarm){
            label_status.setText("Active");
            label_status.setTextColor(Color.RED);
        }
        else{
            label_status.setText("Not Active");
            label_status.setTextColor(Color.GREEN);
        }
        label_status.setPadding(5, 5, 5, 5);
        tR.addView(label_status);

        tableLayout.addView(tR, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

}
