package models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Round {
    private final Game game;
    private Categorie categorie;
    public Item spyWord;
    public Item agentWord;
    public final List<Player> remainingPlayers;
    private int agentCount;
    private int villainCount;

    public Round(Game game) {
        this.game = game;
        remainingPlayers = new ArrayList<>(game.getPlayers());


        setCategorie();
        setRolesWords();
        setPlayersRoles();
        countRoles();
        setPlayersWords();
    }

    private void setCategorie() {
        int randomIndex = new Random().nextInt(game.getRemainingCategories().size());
        int categoryId = game.getRemainingCategories().get(randomIndex);
        this.categorie = new Categorie(categoryId, game.getDbManager());
    }

    private void setRolesWords() {
        int nbWords = this.categorie.getWords().size();
        int indexAgent = new Random().nextInt(nbWords);

        this.agentWord = this.categorie.getWords().get(indexAgent);

        ArrayList<Item> remainingWords = new ArrayList<>(this.categorie.getWords());
        remainingWords.remove(indexAgent);
        int indexSpy = new Random().nextInt(remainingWords.size());

        this.spyWord = remainingWords.get(indexSpy);
    }

    private void setPlayersRoles() {
        List<Role> roles = new ArrayList<>();

        Map<Role, Integer> roleCounts = Map.of(
                Role.AGENT, game.nbAgent,
                Role.SPY, game.nbSpy,
                Role.WHITEPAGE, game.nbWhitePage
        );

        roleCounts.forEach((role, count) -> {
            for (int i = 0; i < count; i++) {
                roles.add(role);
            }
        });

        for (int i = 0; i < game.nbPlayers; i++) {
            int index = new Random().nextInt(roles.size());
            game.getPlayers().get(i).setRole(roles.get(index));
            roles.remove(index);
        }
    }

    private void countRoles() {
        agentCount = 0;
        villainCount = 0;
        for (Player player : remainingPlayers) {
            if (player.getRole().equals(Role.AGENT)) {
                agentCount++;
            } else if (player.getRole().equals(Role.SPY)) {
                villainCount++;
            }
        }
    }

    private void setPlayersWords() {
        for (Player player : game.getPlayers()) {
            switch (player.role) {
                case AGENT:
                    player.word = agentWord.getName();
                    break;
                case SPY:
                    player.word = spyWord.getName();
                    break;
                case WHITEPAGE:
                    player.word = player.getWord();
                    break;
                default:
                   break;
            }
        }
    }

}
