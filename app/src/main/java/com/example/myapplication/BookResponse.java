package com.example.myapplication;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class BookResponse {
    private String name;
    private String author;
    private String price;
    private String user;

    public BookResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public BookResponse(String name, String author, String price, String user) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.user = user;
    }
}
