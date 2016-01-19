package com.example.martyna.sc.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.martyna.sc.Activities.MapsActivity;
import com.example.martyna.sc.Models.ControlPoint;
import com.example.martyna.sc.Models.StreetGame;
import com.example.martyna.sc.Interfaces.OnControlPointTaskCompleted;
import com.example.martyna.sc.R;
import com.example.martyna.sc.Tasks.GetInitialControlPoint;
import com.example.martyna.sc.Tasks.UnsubscribeFromGameTask;
import com.example.martyna.sc.Utilities.TimestampManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

public class MyPlayedStreetGamesAdapter extends ArrayAdapter<StreetGame> {
    private final int layoutResourceId;
    private final Context context;
    private ArrayList<StreetGame> data = new ArrayList<>();
    EventHolder holder;
    private Activity activity;
    ProgressDialog progressDialog;
    TimestampManager timestampManager;

    public MyPlayedStreetGamesAdapter(Activity activity, ProgressDialog progressDialog, Context context, int layoutResourceId, ArrayList<StreetGame> data) {
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
                holder.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new UnsubscribeFromGameTask(context).execute(streetGame.getId());	 // task.runVolley();
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
            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date startdate = streetGame.getStartTime();
                    Date enddate = streetGame.getEndTime();
                    long time= System.currentTimeMillis();

                    if (startdate.getTime() <= time && enddate.getTime() >= time) {
                    progressDialog.show();
                            /*Snackbar.make(v, "PLAY!!!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
*/
                      //  progressDialog.show();
                        new GetInitialControlPoint(context, new OnControlPointTaskCompleted() {
                            @Override
                            public void onControlPointReturned(ControlPoint controlPoint) {
                        //        progressDialog.dismiss();
                                Intent i = new Intent(activity, MapsActivity.class);
                                i.putExtra("game_id", streetGame.getId());
                                i.putExtra("id", controlPoint.getId());
                                i.putExtra("control_point_latitude", controlPoint.getLatitude());
                                i.putExtra("control_point_longitude", controlPoint.getLongitude());
                                i.putExtra("control_point_name", controlPoint.getName());
                                i.putExtra("control_point_next", controlPoint.getNext_point_id());
                                activity.startActivity(i);
                                progressDialog.dismiss();
                            }
                        }).execute(streetGame.getId());


                    } else {

                        Snackbar.make(v, "Nie można grać, gra się jeszcze nie rozpoczęła!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
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

        holder.cancel.setTag(holder);
        holder.time.setTag(holder);
        holder.calendar.setTag(holder);
        holder.name.setText(streetGame.getGameName());
        return row;
    }

    static class EventHolder {
        @Bind(R.id.name) TextView name;
        @Bind(R.id.cancel) ImageButton cancel;
        @Bind(R.id.calendar) ImageButton calendar;
        @Bind(R.id.time) ImageButton time;
        @Bind(R.id.play) ImageButton play;
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