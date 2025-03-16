package fr.imposteur.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import adapters.PlayerAdapter;
import fr.imposteur.R;

public class AddPlayerActivity extends AppCompatActivity {
    private EditText playerNameInput;
    private Button btnAddPlayer, btnPlay;
    private RecyclerView listPlayers;
    private ArrayList<String> players;
    private PlayerAdapter adapter;
    private int nbPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        nbPlayers = getIntent().getIntExtra("nbPlayers", 4);

        playerNameInput = findViewById(R.id.editText_playerName);
        btnAddPlayer = findViewById(R.id.btn_addPlayer);
        btnPlay = findViewById(R.id.btn_startRound);
        listPlayers = findViewById(R.id.rview_players);

        players = new ArrayList<>();
        adapter = new PlayerAdapter(players);

        listPlayers.setLayoutManager(new LinearLayoutManager(this));
        listPlayers.setAdapter(adapter);

        btnPlay.setEnabled(false);

        btnAddPlayer.setOnClickListener(view -> {
            String name = playerNameInput.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer un nom", Toast.LENGTH_SHORT).show();
                return;
            }

            if (players.contains(name)) {
                Toast.makeText(this, "Ce nom est déjà utilisé", Toast.LENGTH_SHORT).show();
                return;
            }

            if (players.size() < nbPlayers) {
                players.add(name);
                adapter.notifyDataSetChanged();
                playerNameInput.setText("");

                // Activer le bouton "Jouer" si tous les joueurs sont ajoutés
                if (players.size() == nbPlayers) {
                    btnPlay.setEnabled(true);
                }
            } else {
                Toast.makeText(this, "Vous avez déjà ajouté " + nbPlayers + " joueurs", Toast.LENGTH_SHORT).show();
            }
        });

        btnPlay.setOnClickListener(view -> {

        });
    }
}
