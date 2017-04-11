package com.example.one.tas;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominika on 15.01.2017.
 */

public class BookProvider {

    private static final String Books_URL = "http://ksiegarnia-tas.c0.pl/api/books";
    private final Context context;
    private static final List<Book> books = new ArrayList<>();
    private final List<Book> bookBest =new ArrayList<>();
    private final List<Book> bookNew =new ArrayList<>();

    public interface onDownloadedListener {
        void onDownloaded();
    }

    public BookProvider(Context context) {
        this.context = context;
    }


    public void getBooks(onDownloadedListener listener) throws IOException, JSONException {
        if (isOnline()) {
            books.clear();
            String s = downloadRecipes();
            JSONObject booksObject = new JSONObject(s);
            JSONArray books = booksObject.getJSONArray("books");

            for (int i = 0; i < books.length(); ++i) {
                JSONObject bookObject = books.getJSONObject(i);

                Book book = new Book(bookObject.getString("id"),
                        bookObject.getString("title"),
                        bookObject.getString("author"),
                        bookObject.getString("description"),
                        bookObject.getString("category"),
                        bookObject.getString("publisher"),
                        bookObject.getString("date"),
                        bookObject.getString("cover"),
                        bookObject.getBoolean("is_new"),
                        bookObject.getBoolean("is_bestseller"),
                        bookObject.getBoolean("is_discount"),
                        bookObject.getDouble("price"),
                        bookObject.getDouble("discount_price"),
                        bookObject.getInt("rating"),
                        bookObject.getInt("votes"));

                this.books.add(book);
                if(book.Is_bestseller()==true){
                    this.bookBest.add(book);
                }
                if(book.Is_new()==true){
                    this.bookNew.add(book);
                }
            }
            listener.onDownloaded();
        } else {
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }


    }

    public static int getBooksNumber() {
        return books.size();
    }

    public  List<Book> getAllBooks() {
        return books;
    }
    public List<Book> getBestBooks() {
        return bookBest;
    }
    public List<Book> getNewBooks() {
        return bookNew;
    }


    public List<Book> getBookCategorie(Categorie categorie){

        List<Book> bookCategorie=new ArrayList<>();
        List<Book> bookc=getAllBooks();

        for (int i = 0; i < bookc.size(); ++i) {

            if (bookc.get(i).getCategory().contains( categorie.gettitle()))
               bookCategorie.add(bookc.get(i));
        }

        return  bookCategorie;

    }

    public static String [] getMatchTitle(){
        String tbooks[]=new String[books.size()];

        for(int i=0; i<books.size(); i++){
            tbooks[i]=books.get(i).getTitle();
        }
        return tbooks;
    }


    private String downloadRecipes() throws IOException {
        return new NetworkRequest(Books_URL, HttpMethod.GET).execute();
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static Book bookAutocomplete(String string){
        for(int i=0; i<getBooksNumber(); i++)
        {
            if((books.get(i).getTitle().toLowerCase()).equals(string.toLowerCase()) )
                return (books.get(i));
        }

            return null;
    }
    public static void rate(String id, int votes, int rating, int currentRating){
        for(int i=0; i<books.size();i++)
        {
            if(books.get(i).getId()==id){
                books.set(i,books.get(i).addvote(rating,votes, currentRating));
            }
        }
    }

}
