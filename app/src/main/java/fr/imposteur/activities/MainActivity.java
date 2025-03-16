package fr.imposteur.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import fr.imposteur.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStartGame = findViewById(R.id.btn_startGame);
        btnStartGame.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NbPlayersActivity.class);
            startActivity(intent);
            finish();
        });
    }
}