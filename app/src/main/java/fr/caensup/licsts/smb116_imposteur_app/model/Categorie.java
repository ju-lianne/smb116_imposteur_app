package fr.caensup.licsts.smb116_imposteur_app;

import java.util.ArrayList;
import java.util.List;

public class Categorie {
    private final int id;
    List<Item> words;

    public Categorie(int id) {
        this.id = id;
        this.words = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public List<Item> getWords() {
        return words;
    }

    public void setWords(List<Item> words) {
        this.words = words != null ? words : new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format("Catégorie %d (%d mots)", id, words.size());
    }
}
