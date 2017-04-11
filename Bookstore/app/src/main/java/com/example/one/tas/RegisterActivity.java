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

import static com.example.one.tas.JSONHelper.initiateJSONUserObject;


public class RegisterActivity extends AppCompatActivity {

    Validator validator = new Validator(this);
    private static final String USERS_URL = "http://ksiegarnia-tas.c0.pl/api/users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button passrec = (Button) findViewById(R.id.RegisterButton);

        passrec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView FirstName = (TextView) findViewById(R.id.FirstName);
                TextView SecondName = (TextView) findViewById(R.id.SecondName);
                TextView email = (TextView) findViewById(R.id.email);
                TextView password = (TextView) findViewById(R.id.password);
                TextView adrress = (TextView) findViewById(R.id.adres);
                TextView city = (TextView) findViewById(R.id.city);
                TextView postal_code = (TextView) findViewById(R.id.postal_code);

                User user = new User(FirstName.getText().toString(), SecondName.getText().toString(), email.getText().toString(),  password.getText().toString(), adrress.getText().toString(), city.getText().toString(), postal_code.getText().toString());


                if(validator.userDataIsCorrect(user))
                {
                    JSONObject JSONUser = initiateJSONUserObject(user);

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    String Answer = null;
                    try {

                        Answer = new NetworkRequest(USERS_URL, HttpMethod.POST, JSONUser.toString()).execute();
                        Log.i("server info", Answer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(Answer.contains("User already exists."))
                            Toast.makeText(getApplicationContext() , getApplicationContext().getString(R.string.email_taken), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext() , getApplicationContext().getString(R.string.user_made_succesfuly), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
