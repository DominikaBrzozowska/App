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

public class categorieProvider {

    private static final String Categories_URL = "http://ksiegarnia-tas.c0.pl/api/categories";
    private final Context context;
    private static final List<Categorie> categories = new ArrayList<>();


    public interface onDownloadedListener {
        void onDownloaded();
    }

    public categorieProvider(Context context) {
        this.context = context;
    }

    public void getCategories(onDownloadedListener listener) throws IOException, JSONException {
        if (isOnline()) {
            categories.clear();
            String s = downloadCategories();
            JSONObject categoriesObject = new JSONObject(s);
            JSONArray categories = categoriesObject.getJSONArray("categories");
            
            for (int i = 0; i < categories.length(); ++i) {
                JSONObject recipeObject = categories.getJSONObject(i);

                Categorie categorie = new Categorie(recipeObject.getInt("id"),
                        recipeObject.getString("title"));

                this.categories.add(categorie);
            }
            
            listener.onDownloaded();
        } else {
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    public static int getCategoriesNumber() {
        return categories.size();
    }

    public List<Categorie> getAllCategories() {
        return categories;
    }


    private String downloadCategories() throws IOException {
        return new NetworkRequest(Categories_URL, HttpMethod.GET).execute();
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static String [] getCategories(){
        String tcat[]=new String[categories.size()+1];
         tcat[0]="Wybierz kategorie";

        for(int i=0; i<categories.size(); i++){
            tcat[i+1]=categories.get(i).gettitle();
        }
        return tcat;
    }

    public static Categorie CategoriesSpiner(String string){
        for(int i=0; i<getCategoriesNumber(); i++)
        {
            if((categories.get(i).gettitle()).equals(string))
                return (categories.get(i));
        }

        return null;
    }
}
