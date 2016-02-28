package notheftautoapp.hulatechnologies.examplecodemodule;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


private class DrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        selectItem(position);
    }
}

/** Swaps fragments in the main content view */
private void selectItem(int position) {
    // Create a new fragment and specify the player to show based on position
    Fragment fragment = new PlayersFragment();
    Bundle args = new Bundle();
    args.putInt(PlayersFragment.ARG_PLAYER_NUMBER, position);
    fragment.setArguments(args);

    //Insert the fragment by replacing any existing fragment
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

    //Highlight the selected item, update the title, close the drawer
    drawerList.setItemChecked(position, true);
    setTitle(nbaPlayerNames[position]);
    drawerLayout.closeDrawer(DrawerList);
}

@Override
public void setTitle(CharSequence title){
    mTitle = title;
    getActionBar().setTitle(mTitle);
}





