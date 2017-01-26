package com.task.selfiegeek.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;
import com.task.selfiegeek.Network.GetClient;
import com.task.selfiegeek.R;

public class LogIn extends AppCompatActivity {

    private EditText userName, password;
    private TextView noAccount;
    private Button logIn;
    private GetClient getClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);
        initialise();
        if (getClient.getClient().user().isUserLoggedIn()) {
            loggedIn();
        }
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getClient.getClient().user().login(userName.getText().toString(), password.getText().toString(), new KinveyUserCallback() {
                    @Override
                    public void onSuccess(User result) {
                        CharSequence text = "Logged in " + result.get("username") + ".";
                        // Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                        Log.e("yo", "" + text);
                        loggedIn();
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        CharSequence text = "Something went wrong -> " + error;
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                });
            }
        });
        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });
    }

    private void initialise() {
        userName = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        logIn = (Button) findViewById(R.id.log_in);
        getClient = new GetClient(getApplicationContext());
        noAccount = (TextView) findViewById(R.id.no_account);
    }

    private void loggedIn() {

        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        startActivity(new Intent(LogIn.this, MainActivity.class));
        finish();
    }

}
