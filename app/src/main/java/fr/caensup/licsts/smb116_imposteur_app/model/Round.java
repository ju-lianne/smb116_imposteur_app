package fr.caensup.licsts.smb116_imposteur_app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Round {
    private final Game game;
    private Categorie categorie;
    private Item spyWord;
    private Item civilWord;
    private final List<Player> remainingPlayers;
    private int civilianCount;
    private int baddieCount;

    protected Round(Game game) {
        this.game = game;
        remainingPlayers = new ArrayList<>(game.getPlayers());

        setCategorie();
        setWords();
    }

    protected void processRound(Scanner scanner) {
        givePlayersRoles();
        showPlayersWords(game, scanner);
        processEliminations(scanner);
        game.removeCategory(categorie.getId());
        if (game.remainingCategories.isEmpty()) {
            System.out.println("La liste est vide !");
            game.remainingCategories = game.getDbManager().getCategoriesIds();
        }
    }

    private void givePlayersRoles() {
        setPlayersRoles(game.nbRoles);
        countRoles();
        setPlayersWords();
    }

    private void setCategorie() {
        try {
            if (game.remainingCategories.isEmpty()) {
                System.out.println("Recharge de toutes les catégories...");
                game.remainingCategories = game.getDbManager().getCategoriesIds();
                if (game.remainingCategories.isEmpty()) {
                    throw new IllegalStateException("Aucune catégorie disponible dans la base de données.");
                }
            }
            
            int randomIndex = new Random().nextInt(game.remainingCategories.size());
            int categoryId = game.remainingCategories.get(randomIndex);
            
            List<Item> words = game.getDbManager().getItemsByCategory(categoryId);
            if (words.isEmpty()) {
                throw new IllegalStateException("La catégorie " + categoryId + " ne contient aucun mot.");
            }
            
            this.categorie = new Categorie(categoryId);
            this.categorie.words = words;
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation de la catégorie : " + e.getMessage());
            throw new RuntimeException("Impossible de continuer le jeu.", e);
        }
    }

    private void setWords() {
        int nbWords = this.categorie.words.size();
        int index = new Random().nextInt(nbWords);

        this.civilWord = this.categorie.words.get(index);

        ArrayList<Item> remainingWords = new ArrayList<>(this.categorie.words);
        remainingWords.remove(index);
        index = new Random().nextInt(remainingWords.size());

        this.spyWord = remainingWords.get(index);
    }

    private void setPlayersRoles(NbRoles nbRoles) {
        List<Role> roles = new ArrayList<>();

        Map<Role, Integer> roleCounts = Map.of(
                Role.CIVILIAN, nbRoles.nbCivilian,
                Role.SPY, nbRoles.nbSpy,
                Role.WHITEPAGE, nbRoles.nbWhitePage
        );

        roleCounts.forEach((role, count) -> {
            for (int i = 0; i < count; i++) {
                roles.add(role);
            }
        });

        for (int i = 0; i < nbRoles.nbPlayers; i++) {
            int index = new Random().nextInt(roles.size());
            game.getPlayers().get(i).setRole(roles.get(index));
            roles.remove(index);
        }
    }

    private void setPlayersWords() {
        for (Player player : game.getPlayers()) {
            switch(player.role) {
                case CIVILIAN:
                    player.word = civilWord.getName();
                    break;
                case SPY:
                    player.word = spyWord.getName();
                    break;
                case WHITEPAGE:
                    player.word = player.getWord();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + player.role);
            }

        }
    }

    private void showPlayersWords(Game game, Scanner scanner) {
        for (Player player : game.getPlayers()) {
            clearScreen();
            System.out.println("Joueur : " + player.getName() + "\n\nAppuyez sur Entrée pour voir votre mot");
            scanner.nextLine();
            System.out.println("\nVotre mot : " + player.getWord() + "\nVotre rôle : " + player.getRole() + "\n\nAppuyez sur Entrée pour continuer");
            scanner.nextLine();
            clearScreen();
        }
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void processEliminations(Scanner scanner) {
        boolean endGame = false;
        while (!endGame) {
            displayGameStatus();
            Player playerToKill = askPlayerElimination(scanner);
            eliminatePlayer(playerToKill);
            endGame = endGameCondition();
        }
    }

    private void displayGameStatus() {
        System.out.println("\n=== État du jeu ===");
        System.out.println("Civils restants : " + civilianCount);
        System.out.println("Espions/Pages blanches restants : " + baddieCount);
        System.out.println("==================\n");
    }

    private Player askPlayerElimination(Scanner scanner) {
        Player eliminatedPlayer = null;
        Player currentPlayer = null;

        while (eliminatedPlayer == null) {
            System.out.println("\nQui souhaitez-vous éliminer ?");
            for (Player player : remainingPlayers) {
                System.out.println("- " + player.getName());
            }

            String playerName = scanner.nextLine().trim();
            eliminatedPlayer = getPlayerByName(playerName);

            if (eliminatedPlayer == null) {
                System.out.println("Ce joueur n'existe pas. Veuillez entrer un nom valide.");
            } else if (eliminatedPlayer.equals(currentPlayer)) {
                System.out.println("Vous ne pouvez pas vous éliminer vous-même !");
                eliminatedPlayer = null;
            }
        }

        return eliminatedPlayer;
    }

    void eliminatePlayer(Player player) {
        System.out.print("\n" + player.getName() + " était " + player.getRole() + "\n");
        remainingPlayers.remove(player);
        countRoles();
    }

    boolean endGameCondition() {
        if (civilianCount <= baddieCount) {
            System.out.println("Les espions et pages blanches gagnent !");
            return true;
        }
        if (remainingPlayers.stream().noneMatch(player -> 
                player.getRole().equals(Role.SPY) || player.getRole().equals(Role.WHITEPAGE))) {
            System.out.println("Les civils gagnent !");
            return true;
        }
        return false;
    }

    private void countRoles() {
        civilianCount = (int) remainingPlayers.stream()
                .filter(player -> player.getRole().equals(Role.CIVILIAN))
                .count();
        baddieCount = (int) remainingPlayers.stream()
                .filter(player -> player.getRole().equals(Role.SPY) || player.getRole().equals(Role.WHITEPAGE))
                .count();
    }

    private Player getPlayerByName(String name) {
        return remainingPlayers.stream()
                .filter(player -> player.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
