package anonymous.automata.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.List;

import anonymous.automata.API.Automata_API;
import anonymous.automata.Adapters.MonAdapter;
import anonymous.automata.Models.Room;
import anonymous.automata.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

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
                MDToast.makeText(getView().getContext(),"Connecting...",MDToast.LENGTH_LONG,MDToast.TYPE_INFO).show();
                update();
            }
        });
        update();

        // Inflate the layout for this fragment
        return view;
    }

    private void update(){
        mAdapter.clear();

        final SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
        final String value_ip = settings.getString("server_ip", "");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(value_ip+":3000/")
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
