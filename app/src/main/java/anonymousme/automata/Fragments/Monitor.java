package anonymousme.automata.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rec_mon);

        mAdapter = new MonAdapter(view.getContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        update();

        // Inflate the layout for this fragment
        return view;
    }

    private void update(){
        mAdapter.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.2:5000/")
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
                    Toast.makeText(getView().getContext(),"Error. "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Toast.makeText(getView().getContext(),"Error. "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.notifyDataSetChanged();
    }

}
