package models;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import database.DBManager;
import interfaces.IGame;

public class Game implements IGame {
    private DBManager dbManager;
    int nbPlayers, nbAgent, nbSpy, nbWhitePage;
    private ArrayList<Player> players;
    public List<Integer> remainingCategories;

    public Game(int nbPlayers, int nbAgent, int nbSpy, int nbWhitePage, List<String> playerNames, Context context) {

        this.nbPlayers = nbPlayers;
        this.nbAgent = nbAgent;
        this.nbSpy = nbSpy;
        this.nbWhitePage = nbWhitePage;

        this.dbManager = new DBManager(context);
        remainingCategories = dbManager.getCategoriesIds();

        this.players = new ArrayList<>();

        for (String name : playerNames) {
            Player player = new Player();
            player.setName(name);
            this.players.add(player);
        }
    }

    public void removeCategory(int categoryId) {
        Iterator<Integer> iterator = remainingCategories.iterator();

        while (iterator.hasNext()) {
            Integer currentCategory = iterator.next();
            if (currentCategory.equals(categoryId)) {
                iterator.remove();
                break;
            }
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Integer> getRemainingCategories() {
        return remainingCategories;
    }

    public DBManager getDbManager() {
        return dbManager;
    }
}
