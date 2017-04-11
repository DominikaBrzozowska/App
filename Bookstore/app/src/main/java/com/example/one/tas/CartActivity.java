package com.example.one.tas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.one.tas.CartList.addToCart;
import static com.example.one.tas.CartList.getToBuy;
import static com.example.one.tas.JSONHelper.initiateJSONOrderObject;


public class CartActivity extends AppCompatActivity{

    private Spinner menu;
    public static final int MAIN=1;
    private static final String ORD_URL = "http://ksiegarnia-tas.c0.pl/api/orders";
    public static boolean isEmpty=true;
    public static boolean getReady=true;
    public static final String NEW_BOOK_ID="New";
    BookAdapter cart_adapter;
    private ListView cart_content;
    List<Book> books = new ArrayList<Book>();

    ArrayAdapter<String> mainadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initializeSpiner();
        Intent intent = getIntent();
        double cost = 0;
        Button buy = (Button) findViewById(R.id.Buy);
        if( getReady==true) {
            Book temp = (Book) intent.getExtras().getSerializable(NEW_BOOK_ID);
            addToCart(temp);
        }
            isEmpty = false;
            cart_content = (ListView) findViewById(R.id.cart_content);
            cart_adapter = new BookAdapter(this);
            cart_content.setAdapter(cart_adapter);

            books = getToBuy();
            for (int i = 0; i < books.size(); i++) {
                cost = cost + books.get(i).getPrice();
            }

            buy.setText(getApplicationContext().getString(R.string.buy) + ": " + cost + "0 zł");
            cart_adapter.setBooks(books);
            cart_adapter.notifyDataSetChanged();



        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (CartActivity.this, MainActivity.class);
                if(CurrentUser.isLogedIn()) {
                    List<String> booksIds = new ArrayList<String>();
                    for (int i = 0; i < books.size(); i++) {
                        booksIds.add(books.get(i).getId());
                    }
                    String Answer;
                    Log.v("ID", CurrentUser.get().getId());
                    JSONObject JSONOrder = initiateJSONOrderObject(CurrentUser.get().getId(), booksIds);
                    Log.v("JSON", JSONOrder.toString());
                    try {
                        Answer = new NetworkRequest(ORD_URL, HttpMethod.POST, JSONOrder.toString()).execute();
                        if(Answer.contains("order")) {
                            Log.v("server info", Answer);
                            Toast.makeText(getApplicationContext() , getApplicationContext().getString(R.string.order_made), Toast.LENGTH_LONG).show();
                            books.clear();
                            CartList.clear();
                            isEmpty = true;
                            startActivityForResult(intent, MAIN);
                        }
                        else
                            Toast.makeText(getApplicationContext() , getApplicationContext().getString(R.string.server_exception), Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    Toast.makeText(getApplicationContext() , getApplicationContext().getString(R.string.not_loged_in), Toast.LENGTH_LONG).show();
            }
        });

        cart_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = cart_adapter.getItem(position);
                CartList.toBuy.remove(book);
                if(CartList.toBuy.isEmpty())
                    isEmpty=true;
                if(isEmpty==false) {
                    cart_adapter.setBooks(CartList.toBuy);
                    cart_adapter.notifyDataSetChanged();
                }
                else{
                    Intent intent= new Intent (CartActivity.this, MainActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });


    }
    public static void getReady(boolean ready){
        getReady=ready;
    }
    private void initializeSpiner(){

        menu=(Spinner)findViewById(R.id.menu);
        menu.setAdapter(mainadapter);

        menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String pos=mainadapter.getItem(position);

                if(pos.equals("Zaloguj")){
                    if(CurrentUser.get().getEmail().equals("guest")) {
                        Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                        startActivityForResult(intent, 1);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.loged_in), Toast.LENGTH_LONG).show();
                    }

                }
                if(pos.equals("Rejestracja")){
                    Intent intent= new Intent (CartActivity.this, RegisterActivity.class);
                    startActivityForResult(intent, 1);

                }
                if(pos.equals("Moje konto")){

                }
                if(pos.equals("Wyloguj")){
                    if(CurrentUser.get().getEmail().equals("guest"))
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.not_loged_in), Toast.LENGTH_LONG).show();
                    else
                    {
                        CurrentUser.set(new User());
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.loged_out), Toast.LENGTH_LONG).show();
                    }
                }
                if(pos.equals("Koszyk")){

                }



            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

    }
}
