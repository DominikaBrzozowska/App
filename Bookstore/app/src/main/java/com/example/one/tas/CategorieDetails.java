package com.example.one.tas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

public class CategorieDetails extends AppCompatActivity {

    public static final String Categories_EXTRA_KEY = "categorie";
    private Categorie categorie;
    public static final int LOG=1;
    private Spinner listCat;
    private ListView listB;
    private Handler handler;
    private static final String TAG = CategorieDetails.class.getSimpleName();
    private Spinner menu;
    private CategorieAdapter categorieAdapter;
    private BookAdapter bookAdapterB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_details);


        Intent i = getIntent();
        categorie = (Categorie) i.getExtras().getSerializable(Categories_EXTRA_KEY);


        handler = new Handler();
        initialize();

        new Thread(new Runnable() {

            @Override
            public void run() {
                getCategoriesFromNetwork();
            }
        }).start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                getBooksFromNetwork();
            }
        }).start();

        showCategories(categorie);

        Button returntoMain = (Button) findViewById(R.id.booksStore);
        returntoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (CategorieDetails.this, MainActivity.class);
                startActivityForResult(intent, LOG);

            }
        });

    }


    private void showCategories(Categorie categorie) {
        TextView title = (TextView) findViewById(R.id.catti);
        title.setText(categorie.gettitle());

    }


    @Override
    protected void onResume() {
        super.onResume();

        categorieAdapter.notifyDataSetChanged();
        bookAdapterB.notifyDataSetChanged();
        listCat.setSelection(0);
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



    private void showToast(final int resId) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CategorieDetails.this, resId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {
        listCat = (Spinner) findViewById(R.id.catList);
        listB =(ListView)findViewById(R.id.catbook);
        menu=(Spinner)findViewById(R.id.menu);
        bookAdapterB = new BookAdapter(this);
        categorieAdapter = new CategorieAdapter(this);

        String[] items = new String[]{"Wybierz akcje", "Zaloguj","Rejestracja", "Moje konto", "Wyloguj", "Koszyk"};

        listB.setAdapter(bookAdapterB);

        listB.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book bookB = bookAdapterB.getItem(position);
                showBooks(bookB);

            }
        });

        final ArrayAdapter<String> adaptercat = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categorieProvider.getCategories());
        listCat.setAdapter(adaptercat);
        listCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tittle=adaptercat.getItem(position);
                Categorie categorie= categorieProvider.CategoriesSpiner(tittle);
                if(categorie!=null)
                    showCategoriesList(categorie);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

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
                        Intent intent = new Intent(CategorieDetails.this, LoginActivity.class);
                        startActivityForResult(intent, LOG);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.loged_in), Toast.LENGTH_LONG).show();
                    }

                }
                if(pos.equals("Rejestracja")){
                    Intent intent= new Intent (CategorieDetails.this, RegisterActivity.class);
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
                        Intent intent= new Intent (CategorieDetails.this, CartActivity.class);
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


    private void showCategoriesList(Categorie categorie) {
        Intent i = new Intent(this, CategorieDetails.class);

        i.putExtra(CategorieDetails.Categories_EXTRA_KEY, categorie);

        startActivity(i);
    }

    private void showBooks(Book book) {
        Intent i = new Intent(this, BookDetails.class);

        i.putExtra(BookDetails.Book_EXTRA_KEY, book);

        startActivity(i);
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
                        bookAdapterB.setBooks(bookProvider.getBookCategorie(categorie));
                        bookAdapterB.notifyDataSetChanged();

                    }
                });
            }
        });
    }



}
