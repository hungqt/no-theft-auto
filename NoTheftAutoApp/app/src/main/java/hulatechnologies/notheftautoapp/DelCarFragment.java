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

import hulatechnologies.notheftautoapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DelCarFragment extends Fragment implements AsyncResponse2 {

    private View v;
    private PreferenceHandler handler = new PreferenceHandler();
    private TextView txtRPI;
    private Button butt;

    public DelCarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_del_car, container, false);
        txtRPI = (TextView)v.findViewById(R.id.txtRPI);
        butt = (Button)v.findViewById(R.id.btnDelete);
        onDelCarClick();
        return v;
    }
    public void delete() {
        AsyncDelCar check = new AsyncDelCar();
        check.delegate = this;
        JSONObject json = new JSONObject();
        try {
            json.put("username", handler.getPrefName(getActivity().getBaseContext()));
            json.put("password", handler.getPrefPass(getActivity().getBaseContext()));
            json.put("rpi_id", txtRPI.getText().toString());
            check.execute(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onDelCarClick(){
        butt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchDialog();
            }
        });
    }

    @Override
    public void processFinish(String s) {
        if(s.equals("Success")){
            Toast.makeText(getActivity().getBaseContext(), "Car was removed", Toast.LENGTH_SHORT).show();
            resetTextViews();
        }
        else{
            Toast.makeText(getActivity().getBaseContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
    public void launchDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete this car?")
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
    private void resetTextViews(){
        txtRPI.setText("");
    }
}
