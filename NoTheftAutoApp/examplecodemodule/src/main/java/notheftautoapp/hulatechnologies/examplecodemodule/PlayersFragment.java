package notheftautoapp.hulatechnologies.examplecodemodule;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.Locale;

/**
 * Fragment that appears in the "content_frame", shows a player
 */
public class PlayersFragment extends Fragment{
    public static final String ARG_PLAYER_NUMBER = "player_number";

    public PlayersFragment(){
        //Empty constructor required for Fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        int i = getArguments().getInt(ARG_PLAYER_NUMBER);
        String player = getResources().getStringArray(R.array.nba_players_array)[i];

        int imageId = getResources().getIdentifier(player.toLowerCase(Locale.getDefault()), "drawable", getActivity().getPackageName());
        ((ImageView))rootView.findViewById(R.id.image)).setImageResource(imageId);
        getActivity().setTitle(player);
        return rootView;
    }
}
