package fr.imposteur.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import database.DBHelper;
import database.DataImporter;
import fr.imposteur.R;
import utils.Utils;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCloseApp = findViewById(R.id.btn_closeApp);
        btnCloseApp.setOnClickListener(view -> Utils.showExitConfirmation(this));

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            new DataImporter(this).importData();
            prefs.edit().putBoolean("isFirstRun", false).apply();
            Toast.makeText(this, "Données importées pour la première fois !", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Données déjà importées.", Toast.LENGTH_SHORT).show();
        }

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        if (db != null) {
            Toast.makeText(this, "Base de données chargée avec succès !", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("MainActivity", "Erreur lors du chargement de la base !");
        }

        Button btnStartGame = findViewById(R.id.btn_startGame);
        btnStartGame.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NbPlayersActivity.class);
            startActivity(intent);
            finish();
        });

        Button btnAddWord = findViewById(R.id.btn_addWord);
        btnAddWord.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddWordActivity.class);
            startActivity(intent);
            finish();
        });
    }
}