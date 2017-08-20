package anonymousme.automata.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by affan on 21/8/17.
 */

public class CCAdapter extends RecyclerView.Adapter<CCAdapter.CCViewHolder> {

    @Override
    public CCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CCViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CCViewHolder extends RecyclerView.ViewHolder {
        public CCViewHolder(View itemView) {
            super(itemView);
        }
    }
}
