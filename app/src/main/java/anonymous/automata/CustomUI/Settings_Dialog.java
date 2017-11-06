package anonymous.automata.CustomUI;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.valdesekamdem.library.mdtoast.MDToast;

import anonymous.automata.R;
import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by affan on 24/8/17.
 */

public class Settings_Dialog extends Dialog {
    public Settings_Dialog(@NonNull Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_dialog);
        final SharedPreferences settings = getContext().getSharedPreferences("settings", MODE_PRIVATE);

        // Reading from SharedPreferences
        String value_mode = settings.getString("auto_mode", "");
        final String value_ip = settings.getString("server_ip", "");

        final Switch auto_mode = (Switch) findViewById(R.id.auto);
        final EditText server_ip = (EditText) findViewById(R.id.addr);

        if(value_mode.equals("1"))
            auto_mode.setChecked(true);
        else
            auto_mode.setChecked(false);

        auto_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // Writing data to SharedPreferences
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("auto_mode", "1");
                    editor.commit();
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get(value_ip+":3000/automatic",new AsyncHttpResponseHandler(){

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                }else{
                    // Writing data to SharedPreferences
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("auto_mode", "0");
                    editor.commit();
                    AsyncHttpClient client = new AsyncHttpClient();
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
        });

        server_ip.setText(value_ip);

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Writing data to SharedPreferences
                SharedPreferences.Editor editor = settings.edit();
                String value_ip = server_ip.getText().toString();


                if ( URLUtil.isValidUrl(value_ip) & (value_ip.length()>=10) ) {
                    editor.putString("server_ip", value_ip);
                    MDToast.makeText(getContext(),"Server address has been updated!!",MDToast.LENGTH_LONG,MDToast.TYPE_SUCCESS).show();
                    editor.commit();
                } else
                    MDToast.makeText(getContext(),"Please enter a valid server address!!",MDToast.LENGTH_LONG,MDToast.TYPE_WARNING).show();
            }
        });

    }
}
