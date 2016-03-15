package hulatechnologies.notheftautoapp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Odd on 14.03.2016.
 */
public class Validation {

    /*public static void main(String[] args) {
        Validation val = new Validation();
        String meh = "odd@";
        String meh2 = "odd";
        String pass = "123456";
        System.out.println(val.checkUsername(meh));
        System.out.println(val.checkUsername(meh2));
        System.out.println(val.checkEmail(meh2));
        System.out.println(val.checkEmail(meh));
        System.out.println(val.checkPass(pass, 7));
        System.out.println(val.checkPass(pass, 5));
    }*/

    public boolean checkUsername(String username) {
        char[] chars = username.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkEmail(String email){
        char[] chars = email.toCharArray();

        for (char c : chars) {
            if (c == '@') {
                return true;
            }
        }
        return false;
    }

    // Checks if password is longer than <number>
    public boolean checkPass(String password, int n){
        if(password.length()< n){
            return false;
        }
        return true;
    }

    public static void toast(final String message, final Context context){
        //_(*****TOAST: " + message);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
