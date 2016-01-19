package com.example.martyna.sc.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.martyna.sc.Utilities.SessionManager;
import com.example.martyna.sc.Models.StreetGame;
import com.example.martyna.sc.R;
import com.example.martyna.sc.Tasks.SubscribeToGameTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.Bind;

public class AllStreetGamesAdapter extends ArrayAdapter<StreetGame> {
    private final int layoutResourceId;
    private final Context context;
    private ArrayList<StreetGame> data = new ArrayList<>();
    EventHolder holder;
    private Activity activity;
    SessionManager sessionManager;

    public AllStreetGamesAdapter(Activity activity, Context context, int layoutResourceId, ArrayList<StreetGame> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.activity = activity;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final StreetGame streetGame = data.get(position);
        sessionManager = new SessionManager(context);
        if(row == null)

        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new EventHolder(row);
            try {
                holder.join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SubscribeToGameTask(context).execute(streetGame.getId());	 // task.runVolley();
                        data.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }catch (Exception e) {
                System.out.print(e.toString());
            }
            holder.calendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date date = streetGame.getStartTime();

                    try {
                        Snackbar.make(v, "Dzień rozpoczęcia gry to: "+ toDate(date.getTime()), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    catch (Exception e) {
                    }
                }
            });
            holder.time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date date = streetGame.getStartTime();

                    try {
                        Snackbar.make(v, "Czas rozpoczęcia gry to: "+ toTime(date.getTime()), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    catch (Exception e) {
                    }
                }
            });

            holder.maps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date date = streetGame.getStartTime();

                    try {
                        Snackbar.make(v, "Miejsce rozpoczęcia gry to: "+streetGame.getStartPointDescription(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    catch (Exception e) {
                    }
                }
            });
            row.setTag(holder);
        } else {
            holder = (EventHolder) row.getTag();
        }

        holder.join.setTag(holder);
        holder.time.setTag(holder);
        holder.calendar.setTag(holder);
        holder.name.setText(streetGame.getGameName());
        return row;
    }

    private String toDate(long timestamp) {
        Date date = new Date (timestamp);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private String toTime(long timestamp) {
        Date date = new Date (timestamp);
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

    static class EventHolder {
        @Bind(R.id.name) TextView name;
        @Bind(R.id.join) ImageButton join;
        @Bind(R.id.calendar) ImageButton calendar;
        @Bind(R.id.time) ImageButton time;
        @Bind(R.id.maps) ImageButton maps;

        public EventHolder( View view) {
            ButterKnife.bind(this, view);
        }
    }
}