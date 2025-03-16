package fr.imposteur.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import fr.imposteur.R;

public class NbPlayersActivity extends AppCompatActivity {
    private NumberPicker nbPlayers;
    private Button btnNext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nb_players);

        nbPlayers = findViewById(R.id.nbPicker_nbPlayers);
        btnNext = findViewById(R.id.btn_next);

        // Configurer le NumberPicker
        nbPlayers.setMinValue(4);
        nbPlayers.setMaxValue(12);
        nbPlayers.setValue(4);

        btnNext.setOnClickListener(view -> {
            int selectedPlayers = nbPlayers.getValue();

            // Envoyer le nombre de joueurs à AddPlayerActivity
            Intent intent = new Intent(NbPlayersActivity.this, AddPlayerActivity.class);
            intent.putExtra("nbPlayers", selectedPlayers);
            startActivity(intent);
        });
    }
}
