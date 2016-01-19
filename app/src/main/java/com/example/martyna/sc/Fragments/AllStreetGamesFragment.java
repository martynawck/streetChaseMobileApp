package com.example.martyna.sc.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martyna.sc.Activities.YourFragmentInterface;
import com.example.martyna.sc.Adapters.AllStreetGamesAdapter;
import com.example.martyna.sc.Adapters.MyStreetGamesAdapter;
import com.example.martyna.sc.Models.StreetGame;
import com.example.martyna.sc.OnEventTaskCompleted;
import com.example.martyna.sc.R;
import com.example.martyna.sc.Tasks.GetAllGamesFromServerTask;
import com.example.martyna.sc.Tasks.GetMyGamesFromServerTask;

import java.util.ArrayList;
import java.util.List;

//import info.androidhive.materialtabs.R;


public class AllStreetGamesFragment extends Fragment implements YourFragmentInterface {

    ListView listView;
    TextView noGames;
    private ArrayList<StreetGame> list = new ArrayList<>();

    public AllStreetGamesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Get ListView object from xml
        View rootView = inflater.inflate(R.layout.fragment_mygames, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        noGames = (TextView) rootView.findViewById(R.id.noGames);
        noGames.setVisibility(View.INVISIBLE);
        updateFragment();


// Adding button to listview at footer

        return rootView;
    }

    public void updateFragment () {
        new GetAllGamesFromServerTask(getActivity().getApplicationContext(), new OnEventTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<StreetGame> strings) {
                list = strings;
                AllStreetGamesAdapter adapter = new AllStreetGamesAdapter(getActivity(), getActivity().getApplicationContext(), R.layout.allgames_row, list);
                listView.setAdapter(adapter);
                if (list.isEmpty())
                    noGames.setVisibility(View.VISIBLE);
                if (!list.isEmpty())
                    noGames.setVisibility(View.INVISIBLE);

            }
        }).execute();
    }

}