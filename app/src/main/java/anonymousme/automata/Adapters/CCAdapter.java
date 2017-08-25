package anonymousme.automata.Adapters;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anonymousme.automata.Models.Room;
import anonymousme.automata.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by affan on 21/8/17.
 */

public class CCAdapter extends RecyclerView.Adapter<CCAdapter.CCViewHolder> {

    private List<Room> mDataset;
    private Context context;

    public CCAdapter(Context context) {
        this.context = context;
        mDataset = new ArrayList<>();
    }

    public void add(Room item) {
        mDataset.add(item);
        notifyItemInserted(mDataset.indexOf(item));
    }

    public void remove(Room item) {
        mDataset.remove(item);
        notifyItemInserted(mDataset.indexOf(item));
    }

    public void clear(){
        mDataset.clear();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public CCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cc_card, parent, false);
        CCViewHolder vh = new CCViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CCViewHolder holder, int position) {
        Room room_data = mDataset.get(position);

        holder.title.setText(room_data.getLocation_id());
        holder.fan_switch.setChecked(room_data.isFansOn());
        holder.light_switch.setChecked(room_data.isLightsOn());

        SharedPreferences settings = context.getSharedPreferences("settings", MODE_PRIVATE);
        // Reading from SharedPreferences
        String value = settings.getString("auto_mode", "");

        if(value.equals("1")) {
            holder.fan_switch.setEnabled(false);
            holder.light_switch.setEnabled(false);
        }else {
            holder.fan_switch.setEnabled(true);
            holder.light_switch.setEnabled(true);
        }
    }



    public class CCViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public Switch fan_switch;
        public Switch light_switch;


        public CCViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            fan_switch = (Switch) v.findViewById(R.id.fan);
            light_switch = (Switch) v.findViewById(R.id.light);
        }
    }
}
