package com.example.martyna.sc.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.martyna.sc.Models.StreetGame;
import com.example.martyna.sc.Models.Subscription;
import com.example.martyna.sc.Interfaces.OnGetSubscriptionTaskCompleted;
import com.example.martyna.sc.R;
import com.example.martyna.sc.Tasks.GetSubscriptionTask;
import com.example.martyna.sc.Utilities.TimestampManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyCompletedStreetGamesAdapter extends ArrayAdapter<StreetGame> {
    private final int layoutResourceId;
    private final Context context;
    private ArrayList<StreetGame> data = new ArrayList<>();
    EventHolder holder;
    private Activity activity;
    ProgressDialog progressDialog;
    TimestampManager timestampManager;

    public MyCompletedStreetGamesAdapter(Activity activity, ProgressDialog progressDialog, Context context, int layoutResourceId, ArrayList<StreetGame> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.activity = activity;
        this.progressDialog = progressDialog;
        timestampManager = new TimestampManager();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        //      EventHolder holder;
        final StreetGame streetGame = data.get(position);
     //   progressDialog = new ProgressDialog(context);
      //  progressDialog.setMessage("Ładowanie gry");
        if(row == null)

        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new EventHolder(row);
            try {
                holder.result.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new GetSubscriptionTask(context, new OnGetSubscriptionTaskCompleted() {
                            @Override
                            public void onGetSubscription(Subscription subscription) {
                                final ResultGameDialog dialog = new ResultGameDialog(activity);
                                Long difference = subscription.getGame_finished().getTime() - subscription.getGame_started().getTime();
                                dialog.result.setText(timestampManager.toTime(timestampManager.oneHourBack(difference)));
                                dialog.button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                      //  finish();
                                    }
                                });
                                dialog.show();
                            }
                        }).execute(streetGame.getId());	 // task.runVolley();
                       // data.remove(position);
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

        holder.result.setTag(holder);
        holder.time.setTag(holder);
        holder.calendar.setTag(holder);
        holder.name.setText(streetGame.getGameName());
        return row;
    }

    static class EventHolder {
        @Bind(R.id.name) TextView name;
        @Bind(R.id.result) ImageButton result;
        @Bind(R.id.calendar) ImageButton calendar;
        @Bind(R.id.time) ImageButton time;
        @Bind(R.id.maps) ImageButton maps;

        public EventHolder( View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void updateReceiptsList(List<StreetGame> newlist) {
        data.clear();
        data.addAll(newlist);
        this.notifyDataSetChanged();
    }
}