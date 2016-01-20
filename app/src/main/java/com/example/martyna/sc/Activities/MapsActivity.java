package com.example.martyna.sc.Activities;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.martyna.sc.Interfaces.OnEndTimeSet;
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
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String gameId;
    private int mInterval = 5000;
    private final double METERS = 20;
    private Handler mHandler;

    @Bind(R.id.start) Button start;
    @Bind(R.id.abort) Button abort;
    @Bind(R.id.exit) Button exit;
    @Bind(R.id.hint) TextView hint;

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
        ButterKnife.bind(this);
        timestampManager = new TimestampManager();
        initializeMap();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* pobieranie lokalizacji */

                if (gps.canGetLocation()) {
                    /* jeśli jesteśmy dostatecznie blisko */
                    if (gps.checkDistance(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude()),
                            new LatLng(gps.getLatitude(), gps.getLongitude())) < METERS) {
                        /* ukrywamy grę i zaznaczamy początkowy czas */
                        java.util.Date date = new java.util.Date();
                        Timestamp now = new Timestamp(date.getTime());
                        SetGameStartTimeTask task = new SetGameStartTimeTask(getApplicationContext(), new String[]{gameId,
                                Long.toString(now.getTime())});
                        task.runVolley();
                        /* zaczynamy wysyłanie współrzędnych na serwer i zaczynamy iterację */
                        startSendingCurrentLocationTask();
                        runIteration();

                        /*jeśli nie jesteśmy dostatecznie blisko dostajemy taką informację */
                    } else
                        Snackbar.make(v, "Nie można rozpocząć gry - znajdujesz się za daleko od miejsca rozpoczęcia gry!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                }
            }
        });
        abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* przestajemy wysyłać położenie */
                stopSendingCurrentLocationTask();
                /* ukrywamy podpowiedź */
                hint.setVisibility(View.INVISIBLE);
                progressDialog.setMessage("Wycofywanie zmian w grze");
                progressDialog.show();
                /* wycofujemy zmiany */
               RollbackTask task =  new RollbackTask(getApplicationContext(), new OnRollbackTaskCompleted() {
                    @Override
                    public void onRollback() {
                        finish();
                        progressDialog.dismiss();
                    }
                },gameId);
                task.runVolley();

            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
    }


    public void runIteration() {
        /*pobieramy pytanie */
        if (currentPoint.getId() == initialPointId){
            progressDialog.setMessage("Pobieranie pytania");
            progressDialog.show();
        }
        GetQuestionTask gqt = new GetQuestionTask(getApplicationContext(), new OnQuestionTaskCompleted() {
            @Override
            public void onQuestionReturned(Question question) {
                /* pokazujemy dialog z pytaniem */
                if (currentPoint.getId() == initialPointId) {
                    progressDialog.dismiss();
                }
                showQuestionDialog(question);
            }
        },Integer.toString(currentPoint.getId()));
        gqt.runVolley();
    }

    public void showQuestionDialog(Question q) {
        final Question qu = q;
        /* inicjalizujemy dialog z pytaniem */
        final QuestionDialog dialog = new QuestionDialog(MapsActivity.this);

        dialog.question.setText(qu.getQuestion());
        dialog.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* sprawdzamy czy oodpowiedź się zgadza */
                new GetAnswerTask(getApplicationContext(), new OnAnswerTaskCompleted() {
                    @Override
                    public void onAnswerReturned(String result) {
                        /* jeśli tak to */
                        if (result == "0") {
                            abort.setVisibility(View.VISIBLE);
                            start.setVisibility(View.INVISIBLE);
                            exit.setVisibility(View.INVISIBLE);
                            dialog.dismiss();
                            java.util.Date date = new java.util.Date();
                            Timestamp now = new Timestamp(date.getTime());
                            /* zapisujemy odwiedzenie punktu kontrolnego */
                            SaveReachedControlPointTask task = new SaveReachedControlPointTask(getApplicationContext(),
                                    new String[]{Integer.toString(currentPoint.getId()), Long.toString(now.getTime())});
                            task.runVolley();
                            progressDialog.setMessage("Pobieranie podpowiedzi");
                            progressDialog.show();
                            /* jeśli to nie jest ostatni punkt w kolejce */
                            if (currentPoint.getNext_point_id() != -1) {
                                /* pobieramy nowy punkt */
                                GetControlPointTask gcpt = new GetControlPointTask(getApplicationContext(), new OnControlPointTaskCompleted() {
                                    @Override
                                    public void onControlPointReturned(ControlPoint controlPoint) {
                                        afterPointReturned(controlPoint, qu, dialog.answer.getText().toString());
                                    }
                                },Integer.toString(currentPoint.getNext_point_id()));//
                                gcpt.runVolley();
                            } else {
                                /* jeśli ostatni to koniec gry */
                                progressDialog.dismiss();
                                /* przestajemy zapisywać położenie na serwer */
                                stopSendingCurrentLocationTask();
                                java.util.Date date2 = new java.util.Date();
                                Timestamp now2 = new Timestamp(date2.getTime());
                                /* zapisujemy fakt zakończenia gry i to, że użytkownik już zagrał w grę */
                                SetGameEndTimeTask task3 = new SetGameEndTimeTask(getApplicationContext(), new String[]{gameId, Long.toString(now2.getTime())}, new OnEndTimeSet() {
                                    @Override
                                    public void onEndTime() {
                                        SetGameAsPlayedTask task2 = new SetGameAsPlayedTask(getApplicationContext(), gameId);
                                        task2.runVolley();

                                        showFinishedGameDialog();
                                    }
                                });
                                task3.runVolley();
                            }
                            /* jeśli odpowiedzieliśmy źle to nic się nie dzieje */
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
        /* dodaliśmy ostatni punt na mapie i wyświetlamy związaną z następnym punktem podpowiedź */
        progressDialog.dismiss();
        currentPoint = controlPoint;
        hint.setText("Podpowiedź: " + currentPoint.getHint());
        hint.setVisibility(View.VISIBLE);

            /* uruchamiamy wątek sprawdzający, czy jesteśmy dostatecznie blisko nowego punktu */
            checkDistanceToNextControlPoint();
    }


    public void showFinishedGameDialog() {

        progressDialog.setMessage("Pobieranie wyniku");
        /* pobieramy dane o zapisie użytkownika do gry */
        GetSubscriptionTask task = new GetSubscriptionTask(getApplicationContext(), new OnGetSubscriptionTaskCompleted() {
            @Override
            public void onGetSubscription(Subscription subscription) {
                progressDialog.dismiss();
                /* wyświetlamy dialog z wynikiem*/
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
        }, gameId);
        task.runVolley();

    }

    /* inicjalizacja mapy*/
    public void initializeMap() {
        Intent intent = getIntent();
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
        abort.setVisibility(View.INVISIBLE);
        hint.setVisibility(View.INVISIBLE);
        /* jak zachowują się markery */
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

    /* sprawdzanie odległości gps do punktu */
    public void checkDistanceToNextControlPoint() {
        checkDistanceThread = new Thread(new Runnable() {

            @Override
            public void run() {
                if (gps.canGetLocation()) {

                    while (gps.checkDistance(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude()),
                            new LatLng(gps.getLatitude(), gps.getLongitude())) > METERS) {
                        try {
                            Thread.sleep(5000);
                        }catch (Exception e) {
                            Log.d("Exception", e.getMessage());
                        }
                    }
                    runIteration();
                }
            }
        });
        checkDistanceThread.start();
    }

    /* wysyłanie położenie z gpsa na serwer */
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
