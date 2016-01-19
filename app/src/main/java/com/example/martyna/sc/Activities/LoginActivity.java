package com.example.martyna.sc.Activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;

import com.example.martyna.sc.Utilities.SessionManager;
import com.example.martyna.sc.R;
import com.example.martyna.sc.Tasks.AuthenticationTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.login)
    EditText et;
    @Bind(R.id.password)
    EditText pass;

    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        session = new SessionManager(getApplicationContext());
    }

    @OnClick(R.id.email_sign_in_button)
    public void OnClick() {
        ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.validating_in_progress), true);
        AuthenticationTask task = new AuthenticationTask(getApplicationContext(), LoginActivity.this, dialog, et.getText().toString().trim(), pass.getText().toString().trim());
        task.runVolley();
    }

    protected void onResume() {
        if (session.resumeSession()){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        super.onResume();
    }

    public void showAlert(){
        LoginActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle(getString(R.string.login_error_title));
                builder.setMessage(getString(R.string.user_not_found))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void StartAsyncTaskInParallel(AsyncTask task) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }


}

