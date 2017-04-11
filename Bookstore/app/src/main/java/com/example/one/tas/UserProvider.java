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

public class UserProvider {

    private static final String USERS_URL = "http://ksiegarnia-tas.c0.pl/api/users";

    private final Context context;

    private final List<User> users = new ArrayList<>();

    public interface OnUsersDownloadedListener {
        void onUsersDownloaded();
    }

    public UserProvider(Context context) {
        this.context = context;
    }

    public void getUsers(OnUsersDownloadedListener listener) throws IOException, JSONException {
        if (isOnline()) {
            String s = downloadUsers();
            JSONObject usersObject = new JSONObject(s);
            JSONArray usersArray = usersObject.getJSONArray("users");

            for (int i = 0; i < usersArray.length(); ++i) {
                JSONObject userObject = usersArray.getJSONObject(i);

                User user = new User(
                        userObject.getString("email"),
                        userObject.getString("password"),
                        userObject.getString("name"),
                        userObject.getString("surname"),
                        userObject.getString("address"),
                        userObject.getString("city"),
                        userObject.getString("postal_code"));

                users.add(user);
            }

            listener.onUsersDownloaded();
        } else {
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    public int getUsersNumber() {
        return users.size();
    }


    public List<User> getAllRecipes() {
        return users;
    }


    private String downloadUsers() throws IOException {
        return new NetworkRequest(USERS_URL, HttpMethod.GET).execute();
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
