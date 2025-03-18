package fr.imposteur.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import adapters.PlayerAdapter;
import fr.imposteur.R;

public class AddPlayerActivity extends AppCompatActivity {
    private EditText playerNameInput;
    private Button btnAddPlayer, btnPlay, btnBack;
    private RecyclerView listPlayers;
    private ArrayList<String> players;
    private PlayerAdapter adapter;
    private int nbPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        nbPlayers = getIntent().getIntExtra("nbPlayers", 4);

        playerNameInput = findViewById(R.id.editText_playerName);
        btnAddPlayer = findViewById(R.id.btn_addPlayer);
        btnPlay = findViewById(R.id.btn_startRound);
        btnBack = findViewById(R.id.btn_backToNb);
        listPlayers = findViewById(R.id.rview_players);

        players = new ArrayList<>();
        adapter = new PlayerAdapter(players);

        listPlayers.setLayoutManager(new LinearLayoutManager(this));
        listPlayers.setAdapter(adapter);

        btnPlay.setEnabled(false);

        btnAddPlayer.setOnClickListener(view -> {
            String name = playerNameInput.getText().toString().trim().toUpperCase();

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

                if (players.size() == nbPlayers) {
                    btnAddPlayer.setEnabled(false);
                    btnPlay.setEnabled(true);
                }
            } else {
                Toast.makeText(this, "Vous avez déjà ajouté " + nbPlayers + " joueurs", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(view -> {
                Intent intent = new Intent(AddPlayerActivity.this, NbPlayersActivity.class);
                startActivity(intent);
        });

        btnPlay.setOnClickListener(view -> {
            Intent receivedIntent = getIntent();

            Intent intent = new Intent(AddPlayerActivity.this, GameActivity.class);
            int nbAgent = receivedIntent.getIntExtra("nbAgent", 0);
            int nbSpy = receivedIntent.getIntExtra("nbSpy", 0);
            int nbWhitePage = receivedIntent.getIntExtra("nbWhitePage", 0);

            intent.putStringArrayListExtra("players", players);
            intent.putExtra("nbPlayers", nbPlayers);

            intent.putExtra("nbAgent", nbAgent);
            intent.putExtra("nbSpy", nbSpy);
            intent.putExtra("nbWhitePage", nbWhitePage);

            startActivity(intent);
            finish();
        });
    }
}
