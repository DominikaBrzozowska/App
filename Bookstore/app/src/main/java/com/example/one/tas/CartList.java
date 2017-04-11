package com.example.one.tas;


import java.util.ArrayList;
import java.util.List;

public class CartList {

    public static List<Book> toBuy=new ArrayList<Book>();

    public static void addToCart(Book book){
        toBuy.add(book);
    }
    public static List<Book> getToBuy() {
        return toBuy;
    }
        public static void clear() {
            toBuy.clear();
        }
}
