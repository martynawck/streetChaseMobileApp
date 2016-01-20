package com.example.martyna.sc.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.example.martyna.sc.Interfaces.UpdateFragmentInterface;
import com.example.martyna.sc.Adapters.MyPlayedStreetGamesAdapter;
import com.example.martyna.sc.Models.StreetGame;
import com.example.martyna.sc.Interfaces.OnEventTaskCompleted;
import com.example.martyna.sc.R;
import com.example.martyna.sc.Tasks.GetMyPlayedStreetGamesServerTask;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyPlayedStreetGamesFragment extends Fragment implements UpdateFragmentInterface {

    @Bind(R.id.listView)ListView listView;
    @Bind(R.id.noGames) TextView noGames;
    private ArrayList<StreetGame> list = new ArrayList<>();
    ProgressDialog progressDialog;


    public MyPlayedStreetGamesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mygames, container, false);
        ButterKnife.bind(this, rootView);
        noGames.setVisibility(View.INVISIBLE);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Ładowanie gry");
        updateFragment();

        return rootView;
    }

    public void updateFragment() {
        new GetMyPlayedStreetGamesServerTask(getActivity().getApplicationContext(), new OnEventTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<StreetGame> strings) {
                list = strings;
                MyPlayedStreetGamesAdapter adapter = new MyPlayedStreetGamesAdapter(getActivity(), progressDialog, getActivity().getApplicationContext(), R.layout.signedupgames_row, list);

                listView.setAdapter(adapter);
                if (list.isEmpty())
                    noGames.setVisibility(View.VISIBLE);
                if (!list.isEmpty())
                    noGames.setVisibility(View.INVISIBLE);

            }
        }).execute();
    }

}