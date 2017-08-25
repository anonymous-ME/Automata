package anonymousme.automata.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anonymousme.automata.Models.Room;
import anonymousme.automata.R;

/**
 * Created by affan on 21/8/17.
 */

public class MonAdapter extends RecyclerView.Adapter<MonAdapter.MonViewHolder> {

    private List<Room> mDataset;
    private Context context;

    public MonAdapter(Context context) {
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
    public MonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mon_card, parent, false);
        MonViewHolder vh = new MonViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MonViewHolder holder, int position) {
        Room room_data = mDataset.get(position);

        holder.title.setText(room_data.getLocation_id());
        holder.temp.setText(room_data.getTemperature()+" °C");
        if (room_data.isOccupied()) {
            holder.stat.setText("Occupied");
            holder.stat_img.setImageResource(R.drawable.closed);
        }else{
            holder.stat.setText("Vacant");
            holder.stat_img.setImageResource(R.drawable.open);
        }
    }



    public class MonViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView temp;
        public TextView stat;
        public ImageView stat_img;


        public MonViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            temp = (TextView) v.findViewById(R.id.temp);
            stat = (TextView) v.findViewById(R.id.stat);
            stat_img = (ImageView) v.findViewById(R.id.stat_img);
        }
    }
}