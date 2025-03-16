package fr.caensup.licsts.smb116_imposteur_app;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Game implements IGame {
    private final DBManager dbManager;
    private final Context context;
    public final int nbPlayers;
    public ArrayList<Player> players;
    List<Integer> remainingCategories;
    NbRoles nbRoles;
    private Scanner gameScanner;

    public Game(Context context, final int nbPlayers) throws IllegalNbPlayersException {
        this.context = context;
        this.dbManager = new DBManager(context);
        this.nbPlayers = nbPlayers;
        this.players = new ArrayList<>();
        
        try {
            validatePlayerCount(nbPlayers);
            this.remainingCategories = dbManager.getCategoriesIds();
            this.nbRoles = dbManager.getRolesForPlayers(nbPlayers);
            this.gameScanner = new Scanner(System.in);
            
            setPlayersNames(nbPlayers);
        } catch (Exception e) {
            cleanup();
            throw e;
        }
    }

    public void startRound() {
        try {
            Round round = new Round(this);
            round.processRound(gameScanner);
        } catch (Exception e) {
            System.err.println("Erreur pendant le round : " + e.getMessage());
        }
    }

    public static void startGame(Context context, int nbPlayers) throws IllegalNbPlayersException {
        Game game = null;
        try {
            game = new Game(context, nbPlayers);
            boolean quit;
            
            do {
                System.out.println("\n=== Nouveau Round ===");
                System.out.println("Taper \"oui\" pour lancer un round de jeu ?");
                quit = !game.gameScanner.nextLine().trim().equals("oui");

                if (!quit) {
                    game.startRound();
                }
            } while (!quit);
            
        } finally {
            if (game != null) {
                game.cleanup();
            }
        }
    }

    private void cleanup() {
        if (gameScanner != null) {
            gameScanner.close();
            gameScanner = null;
        }
        if (dbManager != null) {
            dbManager.closeDB();
        }
    }

    private void setPlayersNames(int nbPlayers) {
        Set<String> namesSet = new HashSet<>();
        
        for (int i = 0; i < nbPlayers; i++) {
            Player player = new Player();
            System.out.println("\nJoueur " + (i + 1) + "/" + nbPlayers);
            
            String name;
            boolean isNameValid;
            do {
                System.out.println("Entrer le nom du joueur : ");
                name = gameScanner.nextLine().trim();
                isNameValid = isNameValid(name);
                
                if (namesSet.contains(name.toUpperCase())) {
                    System.out.println("Ce nom est déjà pris, veuillez entrer un autre nom.");
                    isNameValid = false;
                }
            } while (!isNameValid);

            namesSet.add(name.toUpperCase());
            player.setName(name);
            players.add(player);
        }
    }

    public void addPlayer(Player... newPlayers) throws IllegalNbPlayersException {
        validatePlayerCount(newPlayers.length + this.players.size());
        for (Player player : newPlayers) {
            if (isPlayerNameUnique(player.getName())) {
                this.players.add(player);
            } else {
                throw new IllegalArgumentException("Le nom " + player.getName() + " est déjà utilisé.");
            }
        }
    }

    private boolean isPlayerNameUnique(String name) {
        return players.stream()
                .noneMatch(p -> p.getName().equalsIgnoreCase(name));
    }

    private void validatePlayerCount(int nbPlayers) throws IllegalNbPlayersException {
        if (nbPlayers < MIN_PLAYERS || nbPlayers > MAX_PLAYERS) {
            throw new IllegalNbPlayersException("Le nombre de joueurs doit être entre " + MIN_PLAYERS + " et " + MAX_PLAYERS + ".");
        }
    }

    private boolean isNameValid(String name) {
        if (name.isEmpty()) {
            System.out.println("Le nom ne peut pas être vide.");
            return false;
        }
        if (name.length() > MAX_PLAYER_NAME_LENGTH) {
            System.out.println("Le nom est trop long (max " + MAX_PLAYER_NAME_LENGTH + " caractères).");
            return false;
        }
        return true;
    }

    protected ArrayList<Player> getPlayers() {
        return players;
    }

    public void removeCategory(int categoryId) {
        remainingCategories.removeIf(id -> id.equals(categoryId));
        if (remainingCategories.isEmpty()) {
            System.out.println("Recharge de toutes les catégories...");
            remainingCategories = dbManager.getCategoriesIds();
        }
    }

    protected DBManager getDbManager() {
        return dbManager;
    }
}
