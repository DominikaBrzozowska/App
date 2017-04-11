package com.example.one.tas;

import java.io.Serializable;

public class Categorie implements Serializable {

    private int id;
    private String title;
    public static final int LOG=1;

    public Categorie(int id, String title) {
        this.id = id;
        this.title = title;

    }

    public int getid() {
        return id;
    }

    public String gettitle() {
        return title;
    }

}
