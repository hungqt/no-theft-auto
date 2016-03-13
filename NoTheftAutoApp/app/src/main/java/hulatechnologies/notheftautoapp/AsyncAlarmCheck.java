package hulatechnologies.notheftautoapp;

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
 * Created by Thomas on 3/4/2016.
 */
public class AsyncAlarmCheck extends AsyncTask<JSONObject, Void, Integer> {

    public AsyncResponse delegate = null;

    @Override
    protected Integer doInBackground(JSONObject... params) {
        HttpURLConnection urlConnection= null;
        String answer = null;
        URL url = null;
        try {
            url = new URL("http://folk.ntnu.no/thomborr/NoTheftAuto/AlarmCheck.php");
            String message = params[0].toString();

            urlConnection = (HttpURLConnection) url.openConnection();
            //Setter at jeg kan sende data til serveren
            urlConnection.setDoOutput(true);
            //Setter at jeg kan motta data fra serveren
            urlConnection.setDoInput(true);
            //Gjør noe med byte-streamen så ikke hele lagres i minnet samtidig (?)
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
            Log.d("Response", answer);
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
        }
        if(answer.equals("1")){
            return 1;
        }
        else{
            return 0;
        }
    }
    public String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    @Override
    protected void onPostExecute(Integer result) {
        delegate.processFinish(result);
    }
}
