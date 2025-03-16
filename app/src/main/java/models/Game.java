package models;

import java.util.ArrayList;
import java.util.List;

import exceptions.IllegalNbPlayersException;
import interfaces.IGame;

public class Game implements IGame {
    public ArrayList<Player> players;

    public Game(List<String> playerNames) throws IllegalNbPlayersException {
        validatePlayerCount(playerNames.size());

        this.players = new ArrayList<>();
    }

    private void validatePlayerCount(int nbPlayers) throws IllegalNbPlayersException {
        if (nbPlayers < MIN_PLAYERS || nbPlayers > MAX_PLAYERS) {
            throw new IllegalNbPlayersException("Le nombre de joueurs doit être entre " + MIN_PLAYERS + " et " + MAX_PLAYERS + ".");
        }
    }
}
