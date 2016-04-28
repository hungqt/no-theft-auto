package hulatechnologies.notheftautoapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

        v = inflater.inflate(R.layout.fragment_add_car, container, false);
        // Inflate the layout for this fragment

        rpiID = (TextView)v.findViewById(R.id.txtRpiId);
        carName = (TextView)v.findViewById(R.id.txtCarName);
        onAddCarClick();
        return v;
    }

    //Setter opp knappen som legger til bil, denne knappen kaller check-funksjonen
    public void onAddCarClick(){
        butt = (Button)v.findViewById(R.id.btnAddNewCar);
        butt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                check();
            }
        });
    }
    /*Denne funksjonen sjekker om RPIen allerede er i bruk, om den er det får brukeren spørsmål om han vil override
    bilen som RPIen er registrert på fra før, eller så kalles add() med engang*/
    public void check() {
        AsyncSendJSONreturnString check = new AsyncSendJSONreturnString("http://folk.ntnu.no/thomborr/NoTheftAuto/checkIfRegistered.php");
        check.delegate = this;
        JSONObject json = new JSONObject();
        try {
            json.put("rpi_id",rpiID.getText().toString());
            check.execute(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*Denne funksjonen sletter RPIen som er registrert på IDen brukeren har oppgitt
    kaller deretter add()*/
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
    //Denne funksjonen legger til bilen til databasen og oppdaterer navn og bruker-ID
    public void add() {
        AsyncAddCar check = new AsyncAddCar();
        check.delegate = this;
        JSONObject json = new JSONObject();
        try {
            json.put("username", handler.getPrefName(getActivity().getBaseContext()));
            json.put("password", handler.getPrefPass(getActivity().getBaseContext()));
            json.put("carID", rpiID.getText().toString());
            json.put("carName", carName.getText().toString());
            check.execute(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //Output fra delete-kallet
    @Override
    public void processFinish(String output) {
        if (output.equals("Success")) {
            Log.d("Status",output);
            add();
        }
        else {
            Log.d("Status",output);
            Toast.makeText(getActivity().getBaseContext(), "Something went wrong with delete", Toast.LENGTH_SHORT).show();
        }
    }
    //Output fra check-kallet
    @Override
    public void processFinished(String s) {
        if (s.equals("EXIST")) {
            launchDialog();
        }

        else if (s.equals("NOT EXIST")) {
            Log.d("Didn't find it","Strange");
            add();
        }

        else {
            AlertDialog alertDialog2 = new AlertDialog.Builder(getActivity()).create();
            alertDialog2.setTitle("Error");
            alertDialog2.setMessage("Connection error");
            alertDialog2.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog2.show();


        }
    }
    //Output fra add-kallet
    @Override
    public void processFinish2(String output) {
        if (output.equals("Success")) {
            Toast.makeText(getActivity().getBaseContext(), "Car was added", Toast.LENGTH_SHORT).show();
            resetTextViews();
        }
        else if(output.equals("ERROR")){
            AlertDialog alertDialog2 = new AlertDialog.Builder(getActivity()).create();
            alertDialog2.setTitle("Not in system");
            alertDialog2.setMessage("This RPI is not registered in our system");
            alertDialog2.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog2.show();
        }
        else {
            Toast.makeText(getActivity().getBaseContext(), "Something went wrong with add", Toast.LENGTH_SHORT).show();
        }
    }
    //Launcher en ny dialog med ok og cancel knapper
    public void launchDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("This RPI is already in use are you sure you want to override?")
                .setTitle("Warning");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getActivity().getBaseContext(), "Aborted", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //Resetter textviewsene
    private void resetTextViews(){
        rpiID.setText("");
        carName.setText("");
    }
}
