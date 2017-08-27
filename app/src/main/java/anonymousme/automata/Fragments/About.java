package anonymousme.automata.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sylversky.fontreplacer.FontReplacer;

import anonymousme.automata.R;

/**
 * Created by affan on 20/8/17.
 */

public class About extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView about = (TextView) view.findViewById(R.id.about);
        about.setTypeface(FontReplacer.getItalicFont());

        return view;
    }
}
