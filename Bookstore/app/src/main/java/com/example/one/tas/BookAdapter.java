package com.example.one.tas;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Dominika on 15.01.2017.
 */

public class BookAdapter extends BaseAdapter {

    private List<Book> bookList = new ArrayList<>();
    private Context context;
    public BookAdapter(Context context) {
        this.context = context;
    }


    public void setBooks(Collection<Book> books) {
        bookList.clear();
        bookList.addAll(books);
    }



    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Book getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View bookView;

        if (convertView == null) {
            bookView = LayoutInflater.from(context).inflate(R.layout.best_det, parent, false);
        } else {
            bookView = convertView;
        }

        Book book = getItem(position);

        bindBookToView(book, bookView, position);
        loadImage(book, bookView);
        return bookView;
    }

    private void bindBookToView(Book book, View bookView, int position) {
        TextView bookLabel = (TextView) bookView.findViewById(R.id.bestdet);
        TextView author=(TextView) bookView.findViewById(R.id.besta) ;
        TextView price=(TextView) bookView.findViewById(R.id.bestc);
        bookLabel.setText(book.getTitle());
        author.setText(book.getAuthor());
        price.setText(book.ch_getPrice());


    }

    private void loadImage(Book book, View bookView) {
        final ImageView imageBook = (ImageView) bookView.findViewById(R.id.bestimage);
        imageBook.setImageBitmap(null);

        loadImageWithPicasso(book, imageBook);
    }

    private void loadImageWithPicasso(Book book, ImageView imageBook) {
        Picasso.with(context).load(book.getCover()).into(imageBook);
    }
}