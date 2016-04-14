package hulatechnologies.notheftautoapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by thoma on 3/7/2016.
 */
public class PreferenceHandler {

    //Klasse for � lagre alle verdiene som m� huskes over tid

    private static final String PrefName = "User";
    private static final String PrefPass = "Pass";
    private static final String logName = "Log";
    private static final String remName = "False";
    private static final String carIDName = "carID";
    private static final String notificationID = "notificationID";
    private static final String GCMactive = "GCMstate";
    private static final String GCMToken = "GCMToken";
    private static final String GCMcords = "GCMcords";
    private static final String mapLatitude = "mapLatitude";
    private static final String mapLongitude = "mapLongitude";
    private static final String verificationString = "verificationString";

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
    public void setVerificationString(String randomString,Context context){
        SharedPreferences SPpass = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SPpass.edit();
        editor.putString(verificationString, randomString);
        editor.commit();
    }
    public void setCarName(String car,Context context, String saveSpot){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(saveSpot, car).commit();
    }
    public void setPrefRem(boolean pref,Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(remName, pref).commit();
    }
    public void setLoggedIn(boolean pref,Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(logName, pref).commit();
    }
    public void setCarAlarmActive(boolean pref,Context context, String carName){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(carName, pref).commit();
    }
    public void setCarString(String s, Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(carIDName, s).commit();
    }
    public void setNotificationActive(boolean pref, Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(notificationID, pref).commit();
    }
    public void setGCMactive(boolean pref, Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(GCMactive, pref).commit();
    }
    public void setToken(String token, Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(GCMToken, token).commit();
    }
    public void setLatitude(String savespot,Float latitude, Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putFloat(savespot, latitude).commit();
    }
    public void setLongitude(String savespot,Float longitude, Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putFloat(savespot, longitude).commit();
    }
    public void setCurrCar(String cords,Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(GCMcords, cords).commit();
    }
    public String getVerificationString(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(verificationString,"");
    }
    public float getLatitude(String savespot,Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(savespot,0);
    }
    public float getLongitude(String savespot,Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(savespot, 0);
    }
    public String getCurrCar(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(GCMcords, "");
    }
    public String getCarName(Context context, String saveSpot){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(saveSpot, "");
    }
    public String getToken(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(GCMToken,"");
    }
    public String getCarString(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(carIDName, "");
    }
    public boolean getCarAlarmActive(Context context, String carName){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(carName, false);
    }
    public String getPrefName(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PrefName, "");
    }
    public String getPrefPass(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PrefPass, "");
    }
    public boolean getPrefRem(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(remName, false);
    }
    public boolean getNotificationManager(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(notificationID, false);
    }
    public boolean getLoggedIn(Context context){
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(context);
        boolean value=(mSharedPreference.getBoolean(logName, false));
        return value;
    }
    public boolean getGCMstate(Context context){
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(context);
        boolean value=(mSharedPreference.getBoolean(GCMactive, false));
        return value;
    }
    public void resetPrefName(Context context){
        SharedPreferences SPpass = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SPpass.edit();
        editor.putString(PrefName, "");
        editor.commit();
    }
    public void resetPrefPass(Context context){
        SharedPreferences SPpass = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SPpass.edit();
        editor.putString(PrefPass, "");
        editor.commit();
    }
    public void resetCarString(Context context){
        setCarString("", context);
    }

    public void clear(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context); // here you get your prefrences by either of two methods
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }


}
