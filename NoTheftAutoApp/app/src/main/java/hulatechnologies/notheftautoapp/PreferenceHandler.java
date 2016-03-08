package hulatechnologies.notheftautoapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by thoma on 3/7/2016.
 */
public class PreferenceHandler {

    private static final String PrefName = "User";
    private static final String PrefPass = "Pass";
    private static final String logName = "Log";
    private static final String remName = "False";

    public void setPrefName(String prefName, Context context){
        SharedPreferences SPname = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SPname.edit();
        editor.putString(PrefName, prefName);
        editor.commit();
    }
    public void setPrefPass(String prefPass,Context context){
        SharedPreferences SPpass = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SPpass.edit();
        editor.putString(PrefPass, prefPass);
        editor.commit();
    }
    public void setPrefRem(boolean pref,Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(remName, pref).commit();
    }
    public void setLoggedIn(boolean pref,Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(logName, pref).commit();
    }
    public String getPrefName(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PrefName,"");
    }
    public String getPrefPass(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PrefPass, "");
    }
    public boolean getPrefRem(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(remName, false);
    }
    public boolean getLoggedIn(Context context){
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(context);
        boolean value=(mSharedPreference.getBoolean(logName, false));
        return value;
    }

}
