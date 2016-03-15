package hulatechnologies.notheftautoapp;

/**
 * Created by thoma on 3/14/2016.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by thoma on 2/23/2016.
 */
public class SyncGetCars {

    public AsyncResponse delegate = null;

    protected Integer[] doInBackground(JSONObject... params) {
        HttpURLConnection urlConnection= null;
        String answer = null;
        URL url = null;
        try {
            url = new URL("http://folk.ntnu.no/thomborr/NoTheftAuto/getCars.php");
            String message = params[0].toString();

            urlConnection = (HttpURLConnection) url.openConnection();
            //Setter at jeg kan sende data til serveren
            urlConnection.setDoOutput(true);
            //Setter at jeg kan motta data fra serveren
            urlConnection.setDoInput(true);
            //Gj�r noe med byte-streamen s� ikke hele lagres i minnet samtidig (?)
            urlConnection.setChunkedStreamingMode(0);
            //Setter request method til POST
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");


            //Send request
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(message.getBytes());

            //clean up
            out.flush();
            out.close();

            //Receive answer

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            answer = convertStreamToString(in);
            Log.d("Answer",answer);
            Log.d("answers length","" + answer.split("/").length);
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
        }
        if(answer != null && answer.length() > 1) {
            String[] answers = answer.split("/");
            Integer[] carIDs = new Integer[answers.length];
            for (int i = 0; i < carIDs.length; i++) {
                carIDs[i] = Integer.valueOf(answers[i]);
                Log.d(":D", answers[i]);
            }
            Log.d("Length", "" + carIDs.length);
            return carIDs;
        }
        else{
            return null;
        }
    }
    public String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

