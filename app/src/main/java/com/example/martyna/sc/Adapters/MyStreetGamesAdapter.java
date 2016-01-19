package com.example.martyna.sc.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.martyna.sc.Models.StreetGame;
import com.example.martyna.sc.R;
import com.example.martyna.sc.Tasks.DeleteGameTask;
import com.example.martyna.sc.Tasks.UnsubscribeFromGameTask;
import com.example.martyna.sc.Utilities.TimestampManager;
import com.squareup.picasso.Picasso;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class MyStreetGamesAdapter extends ArrayAdapter<StreetGame> {
    private final int layoutResourceId;
    private final Context context;
    private ArrayList<StreetGame> data = new ArrayList<>();
    EventHolder holder;
    private Activity activity;
    TimestampManager timestampManager;

    public MyStreetGamesAdapter(Activity activity, Context context, int layoutResourceId, ArrayList<StreetGame> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.activity = activity;
        timestampManager = new TimestampManager();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final StreetGame streetGame = data.get(position);
        if(row == null)

        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new EventHolder(row);
            try {
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DeleteGameTask(context).execute(streetGame.getId());	 // task.runVolley();
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
                        Snackbar.make(v, "Dzień rozpoczęcia gry to: "+ timestampManager.toDate(date.getTime()), Snackbar.LENGTH_LONG)
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
                        Snackbar.make(v, "Czas rozpoczęcia gry to: "+ timestampManager.toTime(date.getTime()), Snackbar.LENGTH_LONG)
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

        holder.delete.setTag(holder);
        holder.time.setTag(holder);
        holder.calendar.setTag(holder);
        holder.name.setText(streetGame.getGameName());
        return row;
    }

    static class EventHolder {

        @Bind(R.id.name) TextView name;
        @Bind(R.id.delete2) ImageButton delete;
        @Bind(R.id.calendar) ImageButton calendar;
        @Bind(R.id.time) ImageButton time;
        @Bind(R.id.maps) ImageButton maps;

        public EventHolder( View view) {
            ButterKnife.bind(this, view);
        }
    }
}