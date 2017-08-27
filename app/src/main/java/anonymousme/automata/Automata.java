package anonymousme.automata;

import android.app.Application;

import com.sylversky.fontreplacer.FontReplacer;
import com.sylversky.fontreplacer.Replacer;

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

    }
}
