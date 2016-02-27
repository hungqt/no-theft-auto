package notheftautoapp.hulatechnologies.examplecodemodule;

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
    
}



