package hulatechnologies.notheftautoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thoma on 4/14/2016.
 */
public class userVerification implements AsyncResponse3 {
    private Context context;
    private Activity activity;
    private PreferenceHandler handler = new PreferenceHandler();

    public userVerification(Context context,Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public void isValid(){
        AsyncValidateUser data = new AsyncValidateUser();
        data.delegate = this;
        JSONObject json = new JSONObject();
        try {
            json.put("username", handler.getPrefName(context));
            json.put("verification", handler.getVerificationString(context));
            data.execute(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processFinished(String s) {
        if(s == null){
            logOutFromMain();
        }
        else if(s.equals("Success")){
            //
        }
        else{
            logOutFromMain();
        }
    }
    public void logOutFromMain(){
        resetToken();
        handler.clear(context);
        handler.setLoggedIn(false, context);
    }
    private void resetToken() {
        AsyncUpdateToken updater = new AsyncUpdateToken();
        JSONObject json = new JSONObject();
        try {
            json.put("username", handler.getPrefName(context));
            json.put("token", "");

            updater.execute(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
