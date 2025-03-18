package fr.imposteur.activities;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.List;

import fr.imposteur.R;
import models.Game;
import models.Player;
import models.Round;

public class GameActivity extends AppCompatActivity {
    // Sensor
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener accelerometerListener;

    // XML elements
    private TextView txtDisplay;
    private LinearLayout layoutPlayerList;
    private Button btnNewRound;

    // game logic variables
    private boolean isEliminationPhase;
    private Game game;
    private List<Player> players;
    private int currentIndex;
    private boolean showingWord;
    private Round round;

    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // XML elements
        txtDisplay = findViewById(R.id.txt_display);
        layoutPlayerList = findViewById(R.id.layout_players_list);
        btnNewRound = findViewById(R.id.btn_newRound);

        // get extras
        List<String> playerNames = getIntent().getStringArrayListExtra("players");
        int nbPlayers = getIntent().getIntExtra("nbPlayers", 4);
        int nbAgent = getIntent().getIntExtra("nbAgent", 3);
        int nbSpy = getIntent().getIntExtra("nbSpy", 1);
        int nbWhitePage = getIntent().getIntExtra("nbWhitePage", 0);

        assert playerNames != null;
        txtDisplay.setVisibility(View.GONE);

        game = new Game(nbPlayers, nbAgent, nbSpy, nbWhitePage, playerNames, this);

        btnNewRound.setOnClickListener(view -> startNewRound(game));

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null) {
            accelerometerListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float z = event.values[2]; // Axe Z de l'accéléromètre (vertical)

                    if (z < -9) { // Seuil pour détecter le téléphone face contre table
                        if (!isEliminationPhase) {
                            isEliminationPhase = true;
                            showEliminationPhase(round);
                        }

                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            };
            sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void startNewRound(Game game) {
        round = new Round(game);
        isEliminationPhase = false;
        currentIndex = 0;
        showingWord = false;

        btnNewRound.setVisibility(View.GONE);
        layoutPlayerList.removeAllViews();

        players = game.getPlayers();

        for (Player player : round.remainingPlayers) {
            Log.d("PlayersDebug", "remaning player : " + player.getName());
        }

        layoutPlayerList.setVisibility(View.GONE);
        txtDisplay.setVisibility(View.VISIBLE);

        showNext();

        txtDisplay.setOnClickListener(view -> showNext());
    }


    private void showNext() {
        if (currentIndex >= players.size()) {
            txtDisplay.setText("Retournez le téléphone contre la table et jouez 2 rounds. Puis reprenez le téléphone.");
            return;
        }

        Player currentPlayer = players.get(currentIndex);

        if (!showingWord) {
            txtDisplay.setText("Joueur : " + currentPlayer.getName() + "\n\nTouchez pour voir le mot");
            showingWord = true;
        } else {
            txtDisplay.setText("Mot : " + currentPlayer.getWord() + "\n\nTouchez pour continuer");
            showingWord = false;
            currentIndex++;
        }
    }

    private void showEliminationPhase(Round round) {
        txtDisplay.setText("Qui éliminer ?");
        layoutPlayerList.setVisibility(View.VISIBLE);

        for (Player player : round.remainingPlayers) {
            Button playerButton = new Button(this);
            playerButton.setText(player.getName());
            playerButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            playerButton.setOnClickListener(view -> {
                round.eliminatePlayer(player);
                layoutPlayerList.removeView(playerButton);
                txtDisplay.setText("Il reste : " + round.getAgentCount() + " agents et " + round.getVillainCount() + " vilains !" );

                if (round.endRoundCondition() == 1) {
                    txtDisplay.setText("Les vilains ont gagné" );
                    layoutPlayerList.setVisibility(View.GONE);
                    btnNewRound.setVisibility(View.VISIBLE);
                } else if (round.endRoundCondition() == 2) {
                    txtDisplay.setText("Les agents ont gagné" );
                    layoutPlayerList.setVisibility(View.GONE);
                    btnNewRound.setVisibility(View.VISIBLE);
                }
            });
            layoutPlayerList.addView(playerButton);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null && accelerometerListener != null) {
            sensorManager.unregisterListener(accelerometerListener);
        }
    }
}
