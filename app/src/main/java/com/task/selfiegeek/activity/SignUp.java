package com.task.selfiegeek.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;
import com.task.selfiegeek.Network.GetClient;
import com.task.selfiegeek.R;

public class SignUp extends AppCompatActivity {

    private EditText username, password;
    private Button signUp;
    private GetClient getClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        intialise();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        getClient = new GetClient(getApplicationContext());
    }

    private void submit() {
        if(check(username,password)) {
            getClient.getClient().user().create(username.getText().toString(), password.getText().toString(), new KinveyUserCallback() {
                public void onFailure(Throwable t) {
                    CharSequence text = "Already Registered.";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    if (getClient.getClient().user().isUserLoggedIn()) {
                        startActivity(new Intent(SignUp.this, MainActivity.class));
                        finishAffinity();
                    }
                }

                public void onSuccess(User u) {
                    if (getApplicationContext() == null) {
                        return;
                    }
                    CharSequence text = "Welcome ";
                    // Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignUp.this, MainActivity.class));
                    finish();
                }

            });
        }
        else
            Toast.makeText(getApplicationContext(),"Enter correct username and password",Toast.LENGTH_SHORT).show();
    }

    private void intialise() {
        username = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        signUp = (Button) findViewById(R.id.sign_up);
    }
    boolean check(EditText username, EditText password) {
        if (username.getText().toString().trim().length() != 0 &&
                password.getText().toString().trim().length() != 0)
            return true;
        else
            return false;
    }
}
