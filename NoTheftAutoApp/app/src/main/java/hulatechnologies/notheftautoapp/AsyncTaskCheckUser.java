package hulatechnologies.notheftautoapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by thoma on 2/23/2016.
 */
public class AsyncTaskCheckUser extends AsyncTask<JSONObject, Void, Integer> {

    public AsyncResponse delegate = null;

    @Override
    protected Integer doInBackground(JSONObject... params) {
        HttpURLConnection urlConnection= null;
        String answer = null;
        URL url = null;
        try {
            url = new URL("http://folk.ntnu.no/thomborr/PT/checkDataSecure.php");
            String message = md5(params[0].toString());

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
        if(answer.equals("Success")){
            return 1;
        }
        else if(answer.equals("Failure")){
            return 2;
        }
        else{
            return 3;
        }
    }
    public String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    public static String md5(String s)
    {
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")),0,s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
            return hash;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }
    @Override
    protected void onPostExecute(Integer result) {
        delegate.processFinish(result);
    }
}
