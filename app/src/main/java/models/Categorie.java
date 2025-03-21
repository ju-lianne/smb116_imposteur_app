package models;

import android.content.Context;

import java.util.List;

import database.DBManager;

public class Categorie {
    private final int id;
    private List<Item> words;

    public Categorie(int id, DBManager dbManager) {
        this.id = id;
        this.words = dbManager.getItemsByCategory(id);
    }

    protected int getId() {
        return this.id;
    }

    protected List<Item> getWords() {
        return this.words;
    }
}
