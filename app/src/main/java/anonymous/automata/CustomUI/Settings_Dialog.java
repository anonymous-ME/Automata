package anonymous.automata.CustomUI;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Switch;

import anonymous.automata.R;

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
        String value = settings.getString("auto_mode", "");

        final Switch auto_mode = (Switch) findViewById(R.id.auto);

        if(value.equals("1"))
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
                }else{
                    // Writing data to SharedPreferences
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("auto_mode", "0");
                    editor.commit();
                }
            }
        });
    }
}
