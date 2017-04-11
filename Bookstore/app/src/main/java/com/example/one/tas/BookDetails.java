package com.example.one.tas;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by Dominika on 15.01.2017.
 */

public class BookDetails extends AppCompatActivity {

    public static final String Book_EXTRA_KEY = "booksB";
    public static final int LOG=1;
    private Book book;

    private Dialog dialog;
    private RatingBar ratingBar;
    private Spinner menu;
    private static final String URL = "http://ksiegarnia-tas.c0.pl/api/books/";
    private float checkUserVote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_details);

        Intent i = getIntent();
        book = (Book) i.getExtras().getSerializable(Book_EXTRA_KEY);
        showBooks(book);

        if(book.getUserVote()!=-1) {
            checkUserVote = book.getUserVote();
        }

        String[] items = new String[]{"Wybierz akcje", "Zaloguj","Rejestracja", "Moje konto", "Wyloguj", "Koszyk"};
        menu=(Spinner)findViewById(R.id.menu);

        Button returntoMain = (Button) findViewById(R.id.booksStore);
        returntoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (BookDetails.this, MainActivity.class);
                startActivityForResult(intent, LOG);

            }
        });

        Button cart = (Button) findViewById(R.id.bookcart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity.getReady(true);
                Intent intent= new Intent (BookDetails.this, CartActivity.class);
                intent.putExtra(CartActivity.NEW_BOOK_ID, book);
                startActivityForResult(intent, LOG);

            }
        });


        final Button vote = (Button) findViewById(R.id.bookVote);
        vote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog = new Dialog(BookDetails.this, R.style.RatingStyle);
                dialog.setContentView(R.layout.rating);
                ratingBar = (RatingBar)dialog.findViewById(R.id.ratingbar);
                ratingBar.setRating(book.getUserVote());

                Button accept = (Button) dialog.findViewById(R.id.accept);
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int vs=book.getVotes()+1;
                        int rg=book.getRating();
                        float rating = (ratingBar.getRating()+rg);
                        String r = Float.toString(rating);
                        String s = Integer.toString(vs);
                        JSONObject rate = JSONHelper.JSONvotes(s,r);

                        try {
                            Log.v("odp", rate.toString());
                           BookProvider.rate(book.getId(),vs,(int)rating,((int)rating)-rg);
                            String Answer = new NetworkRequest(URL+book.getId(), HttpMethod.PUT, rate.toString()).execute();
                            if(checkUserVote ==0){
                                Toast.makeText(getApplicationContext(), "Twoja ocena została dodana",
                                        Toast.LENGTH_LONG).show();}
                            else{
                                Toast.makeText(getApplicationContext(), "Twoja ocena została zmieniona",
                                        Toast.LENGTH_LONG).show();}

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();

                    }
                });
                dialog.show();
            }

        });

        final ArrayAdapter<String> mainadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        menu.setAdapter(mainadapter);

        menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String pos=mainadapter.getItem(position);

                if(pos.equals("Zaloguj")){
                    if(CurrentUser.get().getEmail().equals("guest")) {
                        Intent intent = new Intent(BookDetails.this, LoginActivity.class);
                        startActivityForResult(intent, LOG);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.loged_in), Toast.LENGTH_LONG).show();
                    }

                }
                if(pos.equals("Rejestracja")){
                    Intent intent= new Intent (BookDetails.this, RegisterActivity.class);
                    startActivityForResult(intent, LOG);

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
                    if(CartActivity.isEmpty==false){
                        CartActivity.getReady(false);
                        Intent intent= new Intent (BookDetails.this, CartActivity.class);
                        startActivityForResult(intent, LOG);
                    }
                    else
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.basket_is_empty), Toast.LENGTH_LONG).show();

                }

            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });


    }


    private void showBooks(Book book) {

        boolean is_bestseller=book.Is_bestseller();
        boolean is_new=book.Is_new();
        boolean is_disc=book.Is_discount();
        ImageView image=(ImageView) findViewById(R.id.bookCover);
        TextView title = (TextView) findViewById(R.id.bookTitle);
        TextView author = (TextView) findViewById(R.id.bookAuthor);
        TextView category= (TextView) findViewById(R.id.bookCategory);
        TextView description= (TextView) findViewById(R.id.bookDescription);
        TextView publisher= (TextView) findViewById(R.id.bookPublisher);
        TextView date = (TextView) findViewById(R.id.bookDate);
        TextView vote= (TextView) findViewById(R.id.bookVotes);
        TextView rating=(TextView) findViewById(R.id.bookRating) ;
        TextView price=(TextView) findViewById(R.id.bookPrice) ;


        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        category.setText(book.getCategory());
        description.setText(book.getDescription());
        publisher.setText(book.getPublisher());
        date.setText(book.getDate());
        vote.setText(book.ch_getVotes());
        rating.setText(book.ch_getRating());
        loadImageWithPicasso(book,image);


        if(is_bestseller==true) {
        TextView best=(TextView) findViewById(R.id.bookif);
            best.setText("Ta książka jest bestsellerem!");
        }
        if(is_new==true){
            TextView best=(TextView) findViewById(R.id.bookif);
            best.setText("Ta książka to nowość!");

        }
        if(is_disc==true){
            price.setText(book.ch_getDiscount_Price());
        }else
            price.setText(book.ch_getPrice());


    }



    private void loadImageWithPicasso(Book book, ImageView imageBook) {
        Picasso.with(this).load(book.getCover()).into(imageBook);
    }


}