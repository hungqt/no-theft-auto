package hulatechnologies.notheftautoapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;


public class SettingsFragment extends Fragment {

    private View v;

    public SettingsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_settings, container, false);
        onAddClick();
        onDelClick();
        return v;
    }

    public void onDelClick(){

        Button butt = (Button)v.findViewById(R.id.btnAddCar);

        butt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.main_container, new AddCarFragment());
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

    }

    public void onAddClick() {

        Button butt = (Button)v.findViewById(R.id.btnAddCar);

        butt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.main_container, new AddCarFragment());
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });


    }
}