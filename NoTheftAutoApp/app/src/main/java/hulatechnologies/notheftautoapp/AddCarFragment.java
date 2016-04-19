package hulatechnologies.notheftautoapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCarFragment extends Fragment implements AsyncResponse2,AsyncResponse4,AsyncResponse3 {


    private View v;
    private TextView rpiID;
    private TextView carName;
    private PreferenceHandler handler = new PreferenceHandler();
    private Button butt;

    public AddCarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_settings, container, false);
        // Inflate the layout for this fragment

        rpiID = (TextView)v.findViewById(R.id.txtRpiId);
        carName = (TextView)v.findViewById(R.id.txtCarName);
        butt = (Button)v.findViewById(R.id.btnAddingCar);
        onAddCarClick();
        return inflater.inflate(R.layout.fragment_add_car, container, false);
    }

    public void onAddCarClick(){
        butt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                check();
            }
        });
    }

    public void check() {
        AsyncSendJSONreturnString check = new AsyncSendJSONreturnString("http://folk.ntnu.no/thomborr/NoTheftAuto/checkIfRegistered.php");
        check.delegate = this;
        JSONObject json = new JSONObject();
        try {
            json.put("username", handler.getPrefName(getActivity().getBaseContext()));
            json.put("password", handler.getPrefPass(getActivity().getBaseContext()));
            check.execute(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        AsyncDelCar check = new AsyncDelCar();
        check.delegate = this;
        JSONObject json = new JSONObject();
        try {
            json.put("username", handler.getPrefName(getActivity().getBaseContext()));
            json.put("password", handler.getPrefPass(getActivity().getBaseContext()));
            json.put("rpi_id", rpiID.getText().toString());
            check.execute(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void add() {
        AsyncDelCar check = new AsyncDelCar();
        check.delegate = this;
        JSONObject json = new JSONObject();
        try {
            json.put("username", handler.getPrefName(getActivity().getBaseContext()));
            json.put("password", handler.getPrefPass(getActivity().getBaseContext()));
            json.put("rpi_id", rpiID.getText().toString());
            json.put("carName", carName.getText().toString());
            check.execute(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processFinish(String output) {
        if (output.equals("Success")) {
            add();
        }
        else {
            Toast.makeText(getActivity().getBaseContext(), "Something went wrong with delete", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void processFinished(String s) {
        if (s.equals("EXIST")) {
            new AlertDialog.Builder(getActivity().getBaseContext())
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            delete();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        else if (s.equals("NOT EXIST")) {
            add();
        }

        else {
            AlertDialog alertDialog2 = new AlertDialog.Builder(getActivity().getBaseContext()).create();
            alertDialog2.setTitle("Error");
            alertDialog2.setMessage("Connection error");
            alertDialog2.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog2.show();
        }
    }

    @Override
    public void processFinish2(String output) {
        if (output.equals("Success")) {
            Toast.makeText(getActivity().getBaseContext(), "Car was added", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity().getBaseContext(), "Something went wrong with add", Toast.LENGTH_SHORT).show();
        }
    }
}
