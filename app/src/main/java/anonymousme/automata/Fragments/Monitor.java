package anonymousme.automata.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import anonymousme.automata.API.Automata_API;
import anonymousme.automata.Adapters.MonAdapter;
import anonymousme.automata.Models.Room;
import anonymousme.automata.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by affan on 20/8/17.
 */

public class Monitor extends Fragment {

    private RecyclerView recyclerView;
    private MonAdapter mAdapter;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayout no_conn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rec_mon);

        mAdapter = new MonAdapter(view.getContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light,
                android.R.color.holo_blue_bright);
        no_conn = (LinearLayout) view.findViewById(R.id.no_conn_view);
        no_conn.findViewById(R.id.retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = getView();
                if ( v != null )
                    v.findViewById(R.id.main_progress).setVisibility(View.VISIBLE);
                Toast.makeText(getView().getContext(),"Connecting...",Toast.LENGTH_SHORT).show();
                update();
            }
        });
        update();

        // Inflate the layout for this fragment
        return view;
    }

    private void update(){
        mAdapter.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.26.46.10:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Automata_API api = retrofit.create(Automata_API.class);

        Call<List<Room>> roomListCall = api.getRoomList();

        roomListCall.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                try {

                    for (Room room : response.body())
                        mAdapter.add(room);
                }
                catch (Exception e) {
                    no_conn.setVisibility(View.VISIBLE);
                    ((TextView) no_conn.findViewById(R.id.no_conn_txt)).setText("Error. "+e.getMessage());
                }
                mAdapter.notifyDataSetChanged();

                if ( swipeContainer.isRefreshing() )
                    swipeContainer.setRefreshing(false);
                no_conn.setVisibility(View.GONE);
                View v = getView();
                if ( v != null )
                    v.findViewById(R.id.main_progress).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                View v = getView();
                if ( v != null )
                    v.findViewById(R.id.main_progress).setVisibility(View.GONE);
                no_conn.setVisibility(View.VISIBLE);
                ((TextView) no_conn.findViewById(R.id.no_conn_txt)).setText("Error. "+t.getMessage());
                if ( swipeContainer.isRefreshing() )
                    swipeContainer.setRefreshing(false);
            }
        });
        mAdapter.notifyDataSetChanged();
    }

}
