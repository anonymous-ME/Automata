package anonymous.automata;

import android.app.Application;
import android.content.SharedPreferences;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sylversky.fontreplacer.FontReplacer;
import com.sylversky.fontreplacer.Replacer;

import cz.msebera.android.httpclient.Header;

/**
 * Created by affan on 26/8/17.
 */

public class Automata extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Replacer replacer = FontReplacer.Build(getApplicationContext());
        replacer.setDefaultFont("Comfortaa-Regular.ttf");
        replacer.setBoldFont("Comfortaa-Bold.ttf");
        replacer.setLightFont("Comfortaa-Light.ttf");
        replacer.setItalicFont("italics.otf");
        replacer.applyFont();

        //FirebaseMessaging.getInstance().subscribeToTopic("everything");


        final SharedPreferences settings = this.getSharedPreferences("settings", MODE_PRIVATE);



        //Set Mode On Server Side
        AsyncHttpClient client = new AsyncHttpClient();

        // Reading from SharedPreferences
        String value_mode = settings.getString("auto_mode", "");
        String value_ip = settings.getString("server_ip", "");

        //Set Default Settings Values
        if (!(value_mode.equals("0")|value_mode.equals("1"))) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("auto_mode", "0");
            editor.commit();
        }

        if ( !(URLUtil.isValidUrl(value_ip) & (value_ip.length()>=10 )) ) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("server_ip", "http://192.168.1.2");
            value_ip = "http://192.168.1.2";
            editor.commit();
        }

        if(value_mode.equals("1"))
            client.get(value_ip+":3000/automatic",new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        else
            client.get(value_ip+":3000/manual",new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });


    }
}
