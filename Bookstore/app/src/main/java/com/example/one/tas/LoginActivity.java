package com.example.one.tas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.example.one.tas.JSONHelper.initiateJSONLoginObject;
import static com.example.one.tas.JSONHelper.initiateJSONUserObject;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String LOG_URL = "http://ksiegarnia-tas.c0.pl/api/login";

    private Handler handler;

    public static final int LOG_IN=1;
    public static final int REGISTER=1;
    public static final int PASSWORD_RECOVERY=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        handler = new Handler();

        Button logIn = (Button) findViewById(R.id.logInButton);
        logIn.setOnClickListener(new View.OnClickListener() {
            String Answer = " ";
            @Override
            public void onClick(View v) {


                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                TextView email = (TextView) findViewById(R.id.name);
                TextView password = (TextView) findViewById(R.id.password);
                JSONObject JSONLogin = initiateJSONLoginObject(email.getText().toString(), password.getText().toString());
                JSONObject JSONUser = new JSONObject();
                try {
                    Answer = new NetworkRequest(LOG_URL, HttpMethod.POST, JSONLogin.toString()).execute();
                    JSONObject JSONTemp= new JSONObject(Answer);
                    JSONUser = new JSONObject(JSONTemp.getString("user"));
                    Log.v("server info",Answer);

                if (Answer.contains("user"))
                {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    CurrentUser.set(new User(JSONUser.getString("id"),JSONUser.getString("name"),JSONUser.getString("surname"),JSONUser.getString("email"),JSONUser.getString("password"),JSONUser.getString("address"),JSONUser.getString("city"),JSONUser.getString("postal_code")));
                    startActivityForResult(intent, LOG_IN);
                    Toast.makeText(getApplicationContext() , getApplicationContext().getString(R.string.login_succeded), Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext() , getApplicationContext().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button register = (Button) findViewById(R.id.RegisterButton);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REGISTER);
            }
        });

        Button passrec = (Button) findViewById(R.id.SendPasswordButton);
        passrec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PasswordRecoveryActivity.class);
                startActivityForResult(intent, PASSWORD_RECOVERY);
            }
        });


    }

}
