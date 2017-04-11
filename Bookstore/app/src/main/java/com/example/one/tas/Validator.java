package com.example.one.tas;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {


    Context context;
    public Validator(Context context) {
        this.context = context;
    }


    boolean userDataIsCorrect(User user){

             if (!userNameIsCorrect(user.getName()))
                 Toast.makeText(context, context.getString(R.string.invalid_name), Toast.LENGTH_LONG).show();
        else if (!userSurnameIsCorrect(user.getSurname()))
            Toast.makeText(context , context.getString(R.string.invalid_surname), Toast.LENGTH_LONG).show();
        else if (!emailIsCorrect(user.getEmail()))
            Toast.makeText(context , context.getString(R.string.invalid_email), Toast.LENGTH_LONG).show();
        else if (!passwordIsCorrect(user.getPassword()))
            Toast.makeText(context , context.getString(R.string.invalid_password), Toast.LENGTH_LONG).show();
        else if (!addressIsCorrect(user.getAddress()))
            Toast.makeText(context , context.getString(R.string.invalid_address), Toast.LENGTH_LONG).show();
        else if (!cityIsCorrect(user.getCity()))
            Toast.makeText(context , context.getString(R.string.invalid_city), Toast.LENGTH_LONG).show();
        else if (!postalCodeIsCorrect(user.getPostal_code()))
            Toast.makeText(context , context.getString(R.string.invalid_postal_code), Toast.LENGTH_LONG).show();
        else
        return true; // Jeśli nie znajdzie błędu dojdzie tutaj i zwróci 'true'

        //Jeśli znajdzie błąd to wyjdzie ze sprawdzania i zwróci 'false'
        return false;
    }
    private boolean userNameIsCorrect(String name){
        Pattern pattern = Pattern.compile("\\A[a-zA-z]{3,}\\z");
        Matcher matcher =  pattern.matcher(name);
        return matcher.matches();
    }
    private boolean userSurnameIsCorrect(String surname){
        Pattern pattern = Pattern.compile("\\A[a-zA-z]{2,}\\z");
        Matcher matcher =  pattern.matcher(surname);
        return matcher.matches();
    }
    private boolean emailIsCorrect(String email){
        Pattern pattern = Pattern.compile("\\A[a-zA-z0-9]{3,}@([a-zA-z]+\\.)+[a-zA-z]{1,}\\z");
        Matcher matcher =  pattern.matcher(email);
        return matcher.matches();
    }
    private boolean passwordIsCorrect(String email){
        Pattern pattern = Pattern.compile(".*[0-9].*");
        Matcher matcher =  pattern.matcher(email);
        return matcher.matches();
    }
    private boolean cityIsCorrect(String city){
        Pattern pattern = Pattern.compile("\\A[a-zA-z]{2,}\\z");
        Matcher matcher =  pattern.matcher(city);
        return matcher.matches();
    }
    private boolean addressIsCorrect(String address){
        Pattern pattern = Pattern.compile("\\A[a-zA-z\\. ]{4,} [a-z]*[0-9]*");
        Matcher matcher =  pattern.matcher(address);
        return matcher.matches();
    }
    private boolean postalCodeIsCorrect(String postalCode){
        Pattern pattern = Pattern.compile("\\A[0-9]{2}-[0-9]{3}\\z");
        Matcher matcher =  pattern.matcher(postalCode);
        return matcher.matches();
    }
}
;