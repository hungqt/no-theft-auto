package hulatechnologies.notheftautoapp;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by thoma on 4/4/2016.
 */
public class AsyncConnection implements Runnable,AsyncResponse2 {

    String ip;
    String username;
    String password;

    public AsyncConnection(String ip,String username, String password){
        this.ip = ip;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        uploadIP(ip,username);
        startConnection(username,password);
    }

    public void uploadIP(String ip,String username){
        AsyncUpdateIP updater = new AsyncUpdateIP();
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("IP", ip);

            updater.execute(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(String message,String IP){

        int port = 9009;

        try {
            Socket socket = new Socket(IP,port);
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            dOut.writeByte(1);
            dOut.writeUTF(message);
            dOut.flush();
            dOut.close();
            openConnection(IP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startConnection(String username,String password){
        AsyncGetCarIPs IPs = new AsyncGetCarIPs();
        IPs.delegate = this;
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);

            IPs.execute(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void openConnection(String ip){

        int port = 9010;

        Socket socket = null; // Set up receive socket
        try {
            socket = new Socket(ip,port);
            DataInputStream dIn = new DataInputStream(socket.getInputStream());

            boolean done = false;
            while(!done) {
                byte messageType = dIn.readByte();

                switch(messageType)
                {
                    case 1: // Type A
                        System.out.println("Message A: " + dIn.readUTF());
                        break;
                    case 2: // Type B
                        System.out.println("Message B: " + dIn.readUTF());
                        break;
                    case 3: // Type C
                        System.out.println("Message C [1]: " + dIn.readUTF());
                        System.out.println("Message C [2]: " + dIn.readUTF());
                        break;
                    default:
                        done = true;
                }
            }

            dIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processFinish(String output) {
        String[] IPs = output.split("/");
        for(int i = 0; i < IPs.length; i++){
            sendMessage("Test",IPs[i]);
        }
    }
}
