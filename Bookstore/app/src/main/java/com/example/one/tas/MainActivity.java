package com.example.one.tas;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int LOG=1;

    private Spinner listCat;
    private Spinner menu;
    private ListView listBest;
    private ListView listNew;
    private Handler handler;

    private CategorieAdapter categorieAdapter;
    private BookAdapter bookAdapterNew;
    private BookAdapter bookAdapterBest;
    ArrayAdapter<String> adapter;
    AutoCompleteTextView autocomplete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getCategoriesFromNetwork();
        getBooksFromNetwork();

        initialize();
        search();

        Button returntoMain = (Button) findViewById(R.id.booksStore);
        returntoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (MainActivity.this, MainActivity.class);
                startActivityForResult(intent, LOG);

            }
        });

    }

    private void getCategoriesFromNetwork() {
        try {
            fetchCategories();
        } catch (IOException e) {
            Log.e(TAG, "IOException while fetching", e);
            showToast(R.string.io_exception);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException while fetching", e);
            showToast(R.string.server_exception);
        }
    }

    private void getBooksFromNetwork() {
        try {
            fetchBooks();
        } catch (IOException e) {
            Log.e(TAG, "IOException while fetching", e);
            showToast(R.string.io_exception);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException while fetching", e);
            showToast(R.string.server_exception);
        }
    }



    private void fetchCategories() throws IOException, JSONException {
        final categorieProvider categorieProvider = new categorieProvider(this);

        categorieProvider.getCategories(new com.example.one.tas.categorieProvider.onDownloadedListener() {

            @Override
            public void onDownloaded() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Log.d(TAG, "Fetched " + categorieProvider.getCategoriesNumber() + " categories");
                        categorieAdapter.setCategories(categorieProvider.getAllCategories());
                        categorieAdapter.notifyDataSetChanged();
                    }
                });
            }
        });


    }



    private void fetchBooks() throws IOException, JSONException {
        final BookProvider bookProvider = new BookProvider(this);
        bookProvider.getBooks(new BookProvider.onDownloadedListener() {

            @Override
            public void onDownloaded() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Log.d(TAG, "Fetched " + bookProvider.getBooksNumber() + " books");
                        bookAdapterBest.setBooks(bookProvider.getBestBooks());
                        bookAdapterNew.setBooks(bookProvider.getNewBooks());
                        bookAdapterNew.notifyDataSetChanged();
                        bookAdapterBest.notifyDataSetChanged();
                    }
                });
            }
        });

    }

    private void showToast(final int resId) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, resId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        categorieAdapter.notifyDataSetChanged();
        bookAdapterNew.notifyDataSetChanged();
        bookAdapterBest.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
        autocomplete.setText("");
        listCat.setSelection(0);
        menu.setSelection(0);
    }

    private void initialize() {
        listCat = (Spinner) findViewById(R.id.catList);
        listBest=(ListView)findViewById(R.id.bestList);
        listNew=(ListView)findViewById(R.id.newList);
        menu=(Spinner)findViewById(R.id.menu);

        categorieAdapter = new CategorieAdapter(this);
        bookAdapterNew = new BookAdapter(this);
        bookAdapterBest = new BookAdapter(this);


        listBest.setAdapter(bookAdapterBest);
        listNew.setAdapter(bookAdapterNew);

        String[] items = new String[]{"Wybierz akcje", "Zaloguj","Rejestracja", "Moje konto", "Wyloguj", "Koszyk"};

        final ArrayAdapter<String> adaptercat = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categorieProvider.getCategories());
        listCat.setAdapter(adaptercat);

        final ArrayAdapter<String> mainadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        menu.setAdapter(mainadapter);

        menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String pos=mainadapter.getItem(position);

                if(pos.equals("Zaloguj")){
                    if(CurrentUser.get().getEmail().equals("guest")) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivityForResult(intent, LOG);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.loged_in), Toast.LENGTH_LONG).show();
                    }

                }
                if(pos.equals("Rejestracja")){
                    Intent intent= new Intent (MainActivity.this, RegisterActivity.class);
                    startActivityForResult(intent, LOG);

                }
                if(pos.equals("Moje konto")){
                    if(CurrentUser.get().getEmail().equals("guest"))
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.not_loged_in), Toast.LENGTH_LONG).show();
                    else
                    {
                        Log.v("userdata",CurrentUser.get().getAddress());
                        Intent intent= new Intent (MainActivity.this, AccountActivity.class);
                        startActivityForResult(intent, LOG);
                    }
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
                    Intent intent= new Intent (MainActivity.this, CartActivity.class);
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

        listCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tittle=adaptercat.getItem(position);
                Categorie categorie= categorieProvider.CategoriesSpiner(tittle);
                if(categorie!=null)
                showCategories(categorie);


            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        listBest.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book bookB = bookAdapterBest.getItem(position);
                showBooks(bookB);

            }
        });

        listNew.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = bookAdapterNew.getItem(position);
                showBooks(book);

            }
        });


    }

    private void showCategories(Categorie categorie) {
        Intent i = new Intent(this, CategorieDetails.class);

        i.putExtra(CategorieDetails.Categories_EXTRA_KEY, categorie);

        startActivity(i);
    }

    private void showBooks(Book book) {
        Intent i = new Intent(this, BookDetails.class);

        i.putExtra(BookDetails.Book_EXTRA_KEY, book);

        startActivity(i);
    }

    private  void search(){

        autocomplete = (AutoCompleteTextView) findViewById(R.id.search);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, BookProvider.getMatchTitle());
        autocomplete.setThreshold(1);
        adapter.notifyDataSetChanged();
        autocomplete.setAdapter(adapter);

        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tittle=adapter.getItem(position);
                Book book= BookProvider.bookAutocomplete(tittle);
                showBooks(book);
            }
        });

        autocomplete.setOnKeyListener(new AutoCompleteTextView.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String tittle=autocomplete.getText().toString();
                    Book book= BookProvider.bookAutocomplete(tittle);
                    if(book==null) {
                        Toast.makeText(getApplicationContext(), "Brak pasującej książki",
                                Toast.LENGTH_LONG).show();
                    }
                    else
                        showBooks(book);

                    return true;
                }
                return false;
            }
        });
    }

}
