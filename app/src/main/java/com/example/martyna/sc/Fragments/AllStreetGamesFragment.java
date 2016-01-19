package com.example.martyna.sc.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.example.martyna.sc.Interfaces.UpdateFragmentInterface;
import com.example.martyna.sc.Adapters.AllStreetGamesAdapter;
import com.example.martyna.sc.Models.StreetGame;
import com.example.martyna.sc.Interfaces.OnEventTaskCompleted;
import com.example.martyna.sc.R;
import com.example.martyna.sc.Tasks.GetAllGamesFromServerTask;
import java.util.ArrayList;

public class AllStreetGamesFragment extends Fragment implements UpdateFragmentInterface {

    ListView listView;
    TextView noGames;
    private ArrayList<StreetGame> list = new ArrayList<>();

    public AllStreetGamesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mygames, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        noGames = (TextView) rootView.findViewById(R.id.noGames);
        noGames.setVisibility(View.INVISIBLE);
        updateFragment();

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