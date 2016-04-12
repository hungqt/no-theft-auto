package hulatechnologies.notheftautoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class Status extends Fragment implements AsyncResponse2 {
    PreferenceHandler handler = new PreferenceHandler();
    TableLayout tableLayout;
    AlertDialog alertDialog2;
    View v;
    FragmentTransaction fragmentTransaction;
    DrawerLayout drawerLayout;

    public Status (){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(!handler.getLoggedIn(getActivity().getBaseContext())){
            alertDialog2 = new AlertDialog.Builder(getActivity()).create();
            alertDialog2.setTitle("Not logged in");
            alertDialog2.setMessage("Please login to check your car status");
            alertDialog2.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    launchLogin();
                    Toast.makeText(getActivity().getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog2.show();
        }
        v = inflater.inflate(R.layout.fragment_cars, container, false);

        Button btnUpdate = (Button)v.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Update();
            }
        });


        tableLayout = (TableLayout)v.findViewById(R.id.tableLayout);
        TableRow tr_head = new TableRow(getActivity());
        View table_row_view = v.inflate(getActivity(),R.layout.fragment_cars,tr_head); //inflate from parent view
        tr_head.setId(View.generateViewId());
        tr_head.setBackgroundColor(Color.TRANSPARENT);
        tr_head.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView label_car = new TextView(getActivity());
        label_car.setId(View.generateViewId());
        label_car.setText("Car");
        label_car.setTextColor(Color.WHITE);
        label_car.setPadding(5, 5, 5, 5);
        tr_head.addView(label_car);// add the column to the table row here

        TextView label_status = new TextView(getActivity());
        label_status.setId(View.generateViewId());// define id that must be unique
        label_status.setText("Alarm status"); // set the text for the header
        label_status.setTextColor(Color.WHITE); // set the color
        label_status.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_status); // add the column to the table row here

        TextView label_map = new TextView(getActivity());
        label_map.setId(View.generateViewId());// define id that must be unique
        label_map.setText("Map tracking"); // set the text for the header
        label_map.setTextColor(Color.WHITE); // set the color
        label_map.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_map); // add the column to the table row here

        tableLayout.addView(tr_head, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        String carString = handler.getCarString(getActivity().getBaseContext());
        Log.d("CarString", "Hello" + carString);
        for(int i = 0; i < carString.length();i++){
            String id = carString.substring(i,i+1);
            addRow(handler.getCarAlarmActive(getActivity().getBaseContext(),id),id);
        }

        // Inflate the layout for this fragment
        return v;
    }


    private void launchLogin(){
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
    public void addRow(boolean alarm,String id){
        final String carName = handler.getCarName(getActivity().getBaseContext(),id+"Name");
        TableRow tR = new TableRow(getActivity());
        tR.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView label_car = new TextView(getActivity());
        label_car.setId(View.generateViewId());
        label_car.setText(carName);
        label_car.setTextColor(Color.WHITE);
        label_car.setPadding(5, 5, 5, 5);
        tR.addView(label_car);

        TextView label_status = new TextView(getActivity());
        label_status.setId(View.generateViewId());
        if(alarm){
            label_status.setText("Active");
            label_status.setTextColor(Color.RED);
        }
        else {
            label_status.setText("Not Active");
            label_status.setTextColor(Color.GREEN);
        }
        tR.addView(label_status);

        Button carBtn = new Button(getActivity());
        carBtn.setId(View.generateViewId());
        carBtn.setText("Map");
        carBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                handler.setCurrCar(carName, getActivity().getBaseContext());
                goToMap();
            }
        });

        tR.addView(carBtn);

        tableLayout.addView(tR, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }
    public void goToMap(){
        startActivity(new Intent(getActivity(), MapsActivity.class));
    }
    public void callDataBase(){
        AsyncGetCars cars = new AsyncGetCars();
        cars.delegate = this;
        JSONObject json = new JSONObject();
        try {
            json.put("username",handler.getPrefName(getActivity().getBaseContext()));
            json.put("password",handler.getPrefPass(getActivity().getBaseContext()));

            cars.execute(json);

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Update(){
        callDataBase();
    }

    @Override
    public void processFinish(String output) {
        String[] list = output.split("/");
        String carString = "";
        for(int i = 0; i < list.length/3;i++){
            if(list[i*3 + 1].equals("1")){
                handler.setCarAlarmActive(true,getActivity().getBaseContext(),list[i*3]);
                Log.d("Handler", "Alarm sat active");
            }
            else{
                handler.setCarAlarmActive(false,getActivity().getBaseContext(),list[i*3]);
                Log.d("Handler","Alarm sat not active");
            }
            Log.d("ID",list[i*3]);
            Log.d("Alarm",list[i*3+1]);
            Log.d("Navn",list[i*3+2]);
            handler.setCarName(list[i*3 + 2],getActivity().getBaseContext(),list[i*3]+"Name");
            carString += list[i*3];
        }
        Log.d("Car String", carString);
        handler.setCarString(carString, getActivity().getBaseContext());
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, new Status());
        fragmentTransaction.commit();
    }
}
