package anonymous.automata;

import android.app.Application;
import android.content.SharedPreferences;

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

        //Set Mode On Server Side
        AsyncHttpClient client = new AsyncHttpClient();
        final SharedPreferences settings = this.getSharedPreferences("settings", MODE_PRIVATE);

        // Reading from SharedPreferences
        String value = settings.getString("auto_mode", "");

        if(value.equals("1"))
            client.get("http://172.26.46.80:3000/automatic",new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        else
            client.get("http://172.26.46.80:3000/manual",new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });


    }
}
