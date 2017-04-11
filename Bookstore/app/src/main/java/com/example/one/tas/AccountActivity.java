package com.example.one.tas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;



public class AccountActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        TextView firstName = (TextView) findViewById(R.id.FirstName);
        TextView secondName = (TextView) findViewById(R.id.SecondName);
        TextView email = (TextView) findViewById(R.id.email);
        TextView address = (TextView) findViewById(R.id.address);
        TextView city = (TextView) findViewById(R.id.city);
        TextView postal_code = (TextView) findViewById(R.id.postal_code);


        User currentUser = CurrentUser.get();

        firstName.setText(getApplicationContext().getString(R.string.FirstName)+": "+currentUser.getName());

        secondName.setText(getApplicationContext().getString(R.string.SecondName)+": "+currentUser.getSurname());

        email.setText(getApplicationContext().getString(R.string.name)+": "+currentUser.getEmail());

        address.setText(getApplicationContext().getString(R.string.address_less)+": "+currentUser.getAddress());

        city.setText(getApplicationContext().getString(R.string.city)+": "+currentUser.getCity());

        postal_code.setText(getApplicationContext().getString(R.string.postal_code)+": "+currentUser.getPostal_code());


    }
}
