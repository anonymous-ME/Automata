package anonymousme.automata.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by affan on 21/8/17.
 */

public class MonAdapter extends RecyclerView.Adapter<MonAdapter.MonViewHolder> {

    @Override
    public MonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MonViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MonViewHolder extends RecyclerView.ViewHolder {
        public MonViewHolder(View itemView) {
            super(itemView);
        }
    }
}