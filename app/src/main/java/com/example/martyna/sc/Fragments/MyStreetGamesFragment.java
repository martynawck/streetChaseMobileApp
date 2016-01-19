package com.example.martyna.sc.Fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import com.example.martyna.sc.Adapters.MyStreetGamesAdapter;
import com.example.martyna.sc.Models.StreetGame;
import com.example.martyna.sc.OnEventTaskCompleted;
import com.example.martyna.sc.R;
import com.example.martyna.sc.Tasks.GetMyGamesFromServerTask;

import java.util.ArrayList;
import java.util.Date;

//import info.androidhive.materialtabs.R;


public class MyStreetGamesFragment extends Fragment implements YourFragmentInterface {

    ListView listView;
    private ArrayList<StreetGame> list = new ArrayList<>();
    TextView noGames;

    public MyStreetGamesFragment() {
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
        return rootView;
    }

    public void updateFragment() {
         new GetMyGamesFromServerTask(getActivity().getApplicationContext(), new OnEventTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<StreetGame> strings) {
                list = strings;
                MyStreetGamesAdapter adapter = new MyStreetGamesAdapter(getActivity(), getActivity().getApplicationContext(), R.layout.mystreetgame_row, list);
                listView.setAdapter(adapter);
                if (list.isEmpty())
                    noGames.setVisibility(View.VISIBLE);
                if (!list.isEmpty())
                    noGames.setVisibility(View.INVISIBLE);

            }
        }).execute();
    }

}