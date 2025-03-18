package fr.imposteur.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import exceptions.IllegalNbPlayersException;
import fr.imposteur.R;
import models.Game;
import models.Player;
import models.Round;

public class GameActivity extends AppCompatActivity {
    private TextView txtDisplay;
    private Button btnNewRound;

    private Game game;
    private List<Player> players;
    private int currentIndex = 0;
    private boolean showingWord = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btnNewRound = findViewById(R.id.btn_startRound);
        txtDisplay = findViewById(R.id.txt_display);

        List<String> playerNames = getIntent().getStringArrayListExtra("players");
        int nbPlayers = getIntent().getIntExtra("nbPlayers", 4);
        int nbAgent = getIntent().getIntExtra("nbAgent", 0);
        int nbSpy = getIntent().getIntExtra("nbSpy", 0);
        int nbWhitePage = getIntent().getIntExtra("nbWhitePage", 0);

        try {
            assert playerNames != null;
            game = new Game(nbPlayers, nbAgent, nbSpy, nbWhitePage, playerNames, this);
            Round round = new Round(game);
            players = round.remainingPlayers;
            showNext(); // Afficher le premier joueur

            txtDisplay.setOnClickListener(view -> showNext());

        } catch (IllegalNbPlayersException e) {
            e.printStackTrace();
            Toast.makeText(this, "Nombre de joueurs invalide", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showNext() {
        if (currentIndex >= players.size()) {
            txtDisplay.setText("Tous les joueurs ont vu leur mot !");
            return;
        }

        Player currentPlayer = players.get(currentIndex);

        if (!showingWord) {
            // Afficher le nom du joueur
            txtDisplay.setText("Joueur : " + currentPlayer.getName() + "\n\nTouchez pour voir le mot");
            showingWord = true;
        } else {
            // Afficher son mot
            txtDisplay.setText("Mot : " + currentPlayer.getWord() + "\n\nTouchez pour continuer");
            showingWord = false;
            currentIndex++; // Passer au joueur suivant après affichage du mot
        }
    }
}
