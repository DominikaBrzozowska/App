package com.example.one.tas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CategorieAdapter extends BaseAdapter {

    private List<Categorie> categorieList = new ArrayList<>();
    private Context context;

    public CategorieAdapter(Context context) {
        this.context = context;
    }

    public void setCategories(Collection<Categorie> categories) {
        categorieList.clear();
        categorieList.addAll(categories);
    }

    @Override
    public int getCount() {
        return categorieList.size();
    }

    @Override
    public Categorie getItem(int position) {
        return categorieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View categorieView;

        if (convertView == null) {
            categorieView = LayoutInflater.from(context).inflate(R.layout.cat_det, parent, false);
        } else {
            categorieView = convertView;
        }

        Categorie categorie = getItem(position);

        bindRecipeToView(categorie, categorieView, position);

        return categorieView;
    }

    private void bindRecipeToView(Categorie categorie, View categorieView, int position) {
        TextView categoriesLabel = (TextView) categorieView.findViewById(R.id.catdet);
        categoriesLabel.setText(categorie.gettitle());

    }

}
