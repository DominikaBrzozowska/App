package com.example.one.tas;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import static com.example.one.tas.JSONHelper.initiateJSONEmailObject;



public class PasswordRecoveryActivity extends AppCompatActivity {

    private static final String REC_URL = "http://ksiegarnia-tas.c0.pl/api/restore";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_rec);

        Button passrec = (Button) findViewById(R.id.RecoverPasswordButton);

        passrec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView Email = (TextView) findViewById(R.id.EmailToRecover);
                String Answer = "0";
                JSONObject JSONEmail = initiateJSONEmailObject(Email.getText().toString());
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                try {
                    Answer = new NetworkRequest(REC_URL, HttpMethod.POST, JSONEmail.toString()).execute();
                    Log.v("server info", Answer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(Answer.contains("204"))
                    Toast.makeText(getApplicationContext() , getApplicationContext().getString(R.string.password_recovered), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext() , getApplicationContext().getString(R.string.wrong_email), Toast.LENGTH_LONG).show();

            }


        });
    }
}
