package fr.imposteur.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        playerNameInput = findViewById(R.id.editText_playerName);
        btnAddPlayer = findViewById(R.id.btn_addPlayer);
        btnPlay = findViewById(R.id.btn_startRound);
        listPlayers = findViewById(R.id.rview_players);

        players = new ArrayList<>();
        adapter = new PlayerAdapter(players);

        listPlayers.setLayoutManager(new LinearLayoutManager(this));
        listPlayers.setAdapter(adapter);

        btnAddPlayer.setOnClickListener(view -> {
            String name = playerNameInput.getText().toString().trim();
            if (!name.isEmpty() && !players.contains(name)) {
                players.add(name);
                adapter.notifyDataSetChanged();
                playerNameInput.setText("");
            }
        });

        btnPlay.setOnClickListener(view -> {

        });
    }
}
