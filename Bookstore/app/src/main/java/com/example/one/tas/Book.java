package com.example.one.tas;

import java.io.Serializable;

/**
 * Created by Dominika on 15.01.2017.
 */

public class Book implements Serializable {

    private String id;
    private String title;
    private String author;
    private String description;
    private String category;
    private String publisher;
    private String date;
    private String cover;
    private boolean  is_new;
    private boolean  is_bestseller;
    private boolean is_discount;
    private double price;
    private double discount_price;
    private int rating;
    private int votes;
    private float uservote=-1;



    public Book(String id, String title, String author, String description, String category, String publisher, String date, String cover, boolean is_new, boolean is_bestseller, boolean is_discount, double price,
                double discount_price, int rating, int vote) {
        this.id=id;
        this.title=title;
        this.author=author;
        this.description=description;
        this.category=category;
        this.publisher=publisher;
        this.date=date;
        this.cover=cover;
        this.is_new=is_new;
        this.is_bestseller=is_bestseller;
        this.is_discount=is_discount;
        this.price=price;
        this.discount_price=discount_price;
        this.rating=rating;
        this.votes=vote;

    }

    public String getId() {return id;}

    public String getTitle() {
        return title;
    }

    public String getAuthor(){return author;}

    public String getDescription(){return  description;}

    public  String getCategory(){return category;}

    public String getPublisher() {return publisher;}

    public String getDate(){return  date;}

    public String getCover(){return "http://ksiegarnia-tas.c0.pl/uploads/covers/" + cover;}

    public  boolean Is_new(){return is_new;}

    public boolean Is_bestseller(){return  is_bestseller;}

    public boolean Is_discount(){return is_discount;}


    public double getPrice(){return price;}

    public  double getDiscount_price(){return discount_price;}

    public int getRating(){return rating;}

    public int getVotes() {return votes;}

    public String ch_getVotes(){
        String stringVotes = Integer.toString(votes);
        return stringVotes;}


    public String ch_getRating(){
        float average;
        if(votes!=0)
            average=((float)rating/(float)votes);
        else
            average=rating;
        return String.format("%.2f", average);}


    public String ch_getPrice(){
        String stringPrice = Double.toString(price);
        return stringPrice+0;}
    public String ch_getDiscount_Price(){
        String stringDiscPrice = Double.toString(discount_price);
        return stringDiscPrice;}

    public  float userVote(float uservote){
        this.uservote=uservote;
        return this.uservote;
    }

    public float getUserVote(){
        return uservote;
    }

    public Book addvote(int rating, int votes,int uservote){
        this.rating=rating;
        this.votes=votes;
        this.uservote=uservote;
        return this;
    }




}