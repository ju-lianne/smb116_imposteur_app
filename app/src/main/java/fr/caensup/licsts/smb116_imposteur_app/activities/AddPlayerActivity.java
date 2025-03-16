package fr.caensup.licsts.smb116_imposteur_app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AddPlayerActivity extends AppCompatActivity {
    private EditText playerNameInput;
    private Button btnAddPlayer, btnPlay;
    private RecyclerView listPlayers;
    private ArrayList<String> players;
    private PlayerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_player);

        playerNameInput = findViewById(R.id.editText_playerName);
        btnAddPlayer = findViewById(R.id.btn_addPlayer);
        btnPlay = findViewById(R.id.btn_startRound);
        listPlayers = findViewById(R.id.rview_players);

        players = new ArrayList<>();
        adapter = new PlayerAdapter(players);
        listPlayers.setAdapter(adapter);

        btnAddPlayer.setOnClickListener(view -> {
            String name = playerNameInput.getText().toString().trim();
            if (!name.isEmpty() && !players.contains(name)) {
                players.add(name);
                //adapter.notifyDataSetChanged();
                playerNameInput.setText("");
            }
        });

        btnPlay.setOnClickListener(view -> {

        });
    }
}