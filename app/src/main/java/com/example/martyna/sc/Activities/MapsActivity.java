package com.example.martyna.sc.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martyna.sc.Adapters.FinishedGameDialog;
import com.example.martyna.sc.Adapters.QuestionDialog;
import com.example.martyna.sc.Utilities.GPSTracker;
import com.example.martyna.sc.Models.ControlPoint;
import com.example.martyna.sc.Models.Question;
import com.example.martyna.sc.Models.Subscription;
import com.example.martyna.sc.Interfaces.OnAnswerTaskCompleted;
import com.example.martyna.sc.Interfaces.OnControlPointTaskCompleted;
import com.example.martyna.sc.Interfaces.OnGetSubscriptionTaskCompleted;
import com.example.martyna.sc.Interfaces.OnQuestionTaskCompleted;
import com.example.martyna.sc.Interfaces.OnRollbackTaskCompleted;
import com.example.martyna.sc.R;
import com.example.martyna.sc.Tasks.GetAnswerTask;
import com.example.martyna.sc.Tasks.GetControlPointTask;
import com.example.martyna.sc.Tasks.GetQuestionTask;
import com.example.martyna.sc.Tasks.GetSubscriptionTask;
import com.example.martyna.sc.Tasks.RollbackTask;
import com.example.martyna.sc.Tasks.SaveReachedControlPointTask;
import com.example.martyna.sc.Tasks.SendUserLocationTask;
import com.example.martyna.sc.Tasks.SetGameAsPlayedTask;
import com.example.martyna.sc.Tasks.SetGameEndTimeTask;
import com.example.martyna.sc.Tasks.SetGameStartTimeTask;
import com.example.martyna.sc.Utilities.TimestampManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String gameId;
    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private final double METERS = 20;
    private Handler mHandler;
    Button start;
    Button abort;
    TextView hint;
    GPSTracker gps;
    private ControlPoint currentPoint;
    private int initialPointId;
    private Thread checkDistanceThread = null;
    private HashMap<Marker, ArrayList<String>> markersHashMap;
    ProgressDialog progressDialog;
    TimestampManager timestampManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        timestampManager = new TimestampManager();
        initializeMap();



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pobieramy lokalizacje

                if (gps.canGetLocation()) {
                    // jeśli dostatecznie blisko
                    if (gps.checkDistance(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude()),
                            new LatLng(gps.getLatitude(), gps.getLongitude())) < METERS) {
                        // ukrywamy przycisk
                        java.util.Date date= new java.util.Date();
                        Timestamp now = new Timestamp(date.getTime());
                        new SetGameStartTimeTask(getApplicationContext()).execute(gameId, Long.toString(now.getTime()));
                        startSendingCurrentLocationTask();
                        runIteration();

                    // jeśli nie dostatecznie blisko
                    } else
                        Snackbar.make(v, "Nie można rozpocząć gry - znajdujesz się za daleko od miejsca rozpoczęcia gry!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                }
                // startRepeatingTask();
            }
        });
        abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // przestań wysyłać obecne położenie
                stopSendingCurrentLocationTask();
                //ukryj podpowiedź
                hint.setVisibility(View.INVISIBLE);
                progressDialog.setMessage("Wycofywanie zmian w grze");
                progressDialog.show();
                new RollbackTask(getApplicationContext(), new OnRollbackTaskCompleted() {
                    @Override
                    public void onRollback() {
                        finish();
                        progressDialog.dismiss();
                    }
                }).execute(gameId);

            }
        });
    }

    @Override
    public void onBackPressed() {
    }


    public void runIteration() {
        // pobieramy pytanie
        if (currentPoint.getId() == initialPointId){
            progressDialog.setMessage("Pobieranie pytania");
            progressDialog.show();
        }
        new GetQuestionTask(getApplicationContext(), new OnQuestionTaskCompleted() {
            @Override
            public void onQuestionReturned(Question question) {
                //pokazujemy dialog z pytaniem
                if (currentPoint.getId() == initialPointId) {
                    progressDialog.dismiss();
                }
                showQuestionDialog(question);
            }
        }).execute(Integer.toString(currentPoint.getId()));
    }

    public void showQuestionDialog(Question q) {
        final Question qu = q;
        // inicjalizacje itd
        final QuestionDialog dialog = new QuestionDialog(MapsActivity.this);

        dialog.question.setText(qu.getQuestion());
        dialog.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sprawdzamy odpowiedz
                new GetAnswerTask(getApplicationContext(), new OnAnswerTaskCompleted() {
                    @Override
                    public void onAnswerReturned(String result) {
                        // jesli odpowiedz dobra
                        if (result == "0") {
                            abort.setVisibility(View.VISIBLE);
                            start.setVisibility(View.INVISIBLE);
                            dialog.dismiss();
                            java.util.Date date = new java.util.Date();
                            Timestamp now = new Timestamp(date.getTime());
                            new SaveReachedControlPointTask(getApplicationContext()).execute(Integer.toString(currentPoint.getId()), Long.toString(now.getTime()));
                            // pobieray nowy punkt
                            progressDialog.setMessage("Pobieranie podpowiedzi");
                            progressDialog.show();
                            if (currentPoint.getNext_point_id() != -1) {
                                new GetControlPointTask(getApplicationContext(), new OnControlPointTaskCompleted() {
                                    @Override
                                    public void onControlPointReturned(ControlPoint controlPoint) {
                                        afterPointReturned(controlPoint, qu, dialog.answer.getText().toString());
                                    }
                                }).execute(Integer.toString(currentPoint.getNext_point_id()));
                            } else {
                                // koniec gry
                                progressDialog.dismiss();
                                stopSendingCurrentLocationTask();
                                java.util.Date date2 = new java.util.Date();
                                Timestamp now2 = new Timestamp(date2.getTime());
                                new SetGameEndTimeTask(getApplicationContext()).execute(gameId, Long.toString(now2.getTime()));
                                new SetGameAsPlayedTask(getApplicationContext()).execute(gameId);
                                showFinishedGameDialog();
                            }
                            // w przeciwnym razie nic nie rob
                        } else {
                            Toast.makeText(MapsActivity.this, "Niepoprawna odpowiedź! Spróbuj jeszcze raz!",
                                    Toast.LENGTH_LONG).show();
                            dialog.answer.setText("");
                        }

                    }
                }).execute(Integer.toString(qu.getId()), dialog.answer.getText().toString());
            }
        });
        dialog.show();
    }


    public void afterPointReturned (ControlPoint controlPoint, Question question, String answer) {
        if (currentPoint.getId() == initialPointId) {
            mMap.clear();
            addMarkerToMap(currentPoint,question, answer);
        } else {
            addMarkerToMap(currentPoint, question, answer);
        }
        progressDialog.dismiss();
        currentPoint = controlPoint;
        hint.setText("Podpowiedź: " + currentPoint.getHint());
        hint.setVisibility(View.VISIBLE);

            checkDistanceToNextControlPoint();
    }


    public void showFinishedGameDialog() {

        progressDialog.setMessage("Pobieranie wyniku");
        new GetSubscriptionTask(getApplicationContext(), new OnGetSubscriptionTaskCompleted() {
            @Override
            public void onGetSubscription(Subscription subscription) {
                progressDialog.dismiss();
                Long difference = subscription.getGame_finished().getTime() - subscription.getGame_started().getTime();
                final FinishedGameDialog dialog = new FinishedGameDialog(MapsActivity.this);
                dialog.result.setText(timestampManager.toTime(timestampManager.oneHourBack(difference)));
                dialog.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.show();
            }
        }).execute(gameId);

    }

    public void initializeMap() {
        Intent intent = getIntent(); // gets the previously created intent
        gameId = intent.getStringExtra("game_id");
        currentPoint = new ControlPoint();
        currentPoint.setInitialControlPoint(intent);
        initialPointId = currentPoint.getId();
        markersHashMap = new HashMap<>();
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMyLocationEnabled(true);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude()), 10);
        addInitialMarkerToMap(currentPoint);
        mMap.moveCamera(yourLocation);
        gps = new GPSTracker(MapsActivity.this);
        mHandler = new Handler();
        start = (Button) findViewById(R.id.start);
        abort = (Button) findViewById(R.id.abort);
        hint = (TextView) findViewById(R.id.hint);
        abort.setVisibility(View.INVISIBLE);
        hint.setVisibility(View.INVISIBLE);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker arg0) {
                View v = getLayoutInflater().inflate(R.layout.control_point_window, null);
                ArrayList<String> myMarker = markersHashMap.get(arg0);
                TextView markerLabel = (TextView) v.findViewById(R.id.marker_label);
                TextView markerQuestion = (TextView) v.findViewById(R.id.marker_question);
                TextView markerAnswer = (TextView) v.findViewById(R.id.marker_answer);
                markerLabel.setText(myMarker.get(0));
                markerQuestion.setText(myMarker.get(1));
                markerAnswer.setText(myMarker.get(2));
                return v;
            }
        });
        progressDialog = new ProgressDialog(MapsActivity.this);
      //  questionProgress.setMessage("Wczytywanie pytania");
       // hintProgress = new ProgressDialog(MapsActivity.this);
   //     hintProgress.setMessage("Wczytywanie podpowiedzi");
    }

    public void addInitialMarkerToMap(ControlPoint controlPoint) {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(controlPoint.getLatitude(), controlPoint.getLongitude()))
                .title(controlPoint.getName())
                .snippet("Punkt początkowy"));
    }

    public void addMarkerToMap(ControlPoint controlPoint, Question question, String answer) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(controlPoint.getLatitude(), controlPoint.getLongitude()))
                .title(controlPoint.getName())
                .snippet("Pytanie: " + question.getQuestion() + "\nOdpowiedź: " + answer));
        ArrayList list = new ArrayList(3);
        Log.d("X",controlPoint.getName());

        list.add(controlPoint.getName());
        list.add(question.getQuestion());
        list.add(answer);
        markersHashMap.put(marker, list);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }



    /***** THREADS */

    public void checkDistanceToNextControlPoint() {
        checkDistanceThread = new Thread(new Runnable() {

            @Override
            public void run() {
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    java.util.Date date= new java.util.Date();

                    while (gps.checkDistance(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude()),
                            new LatLng(gps.getLatitude(), gps.getLongitude())) > METERS) {
                        try {
                            Thread.sleep(5000);
                        }catch (Exception e) {
                            Log.d("Exception", e.getMessage());
                        }
                    }
                    /// gps.showSettingsAlert();
                    runIteration();
                }
            }
        });
        checkDistanceThread.start();
    }


    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                java.util.Date date= new java.util.Date();
                Timestamp now = new Timestamp(date.getTime());
                new SendUserLocationTask(getApplicationContext()).execute(Long.toString(now.getTime()), gameId, Double.toString(latitude), Double.toString(longitude));


            } else {
                gps.showSettingsAlert();
            }
            //  updateStatus(); //this function can change value of mInterval.
            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };

    void startSendingCurrentLocationTask() {
        mStatusChecker.run();
    }

    void stopSendingCurrentLocationTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
}
