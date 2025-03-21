package fr.imposteur.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import database.DBHelper;
import fr.imposteur.R;
import utils.Utils;

public class NbPlayersActivity extends AppCompatActivity {
    // XML elements
    private TextView txtNbSpy, txtNbAgent, txtNbWhitePage;
    private NumberPicker pickerNbPlayers;
    private Button btnNext;

    // game logic variables
    private int nbPlayers, nbAgent, nbSpy, nbWhitePage;

    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nb_players);

        Button btnCloseApp = findViewById(R.id.btn_closeApp);
        btnCloseApp.setOnClickListener(view -> Utils.showExitConfirmation(this));

        txtNbSpy = findViewById(R.id.txt_nbSpy);
        txtNbAgent = findViewById(R.id.txt_nbAgent);
        txtNbWhitePage = findViewById(R.id.txt_nbWhitePage);

        pickerNbPlayers = findViewById(R.id.nbPicker_nbPlayers);
        btnNext = findViewById(R.id.btn_next);

        pickerNbPlayers.setMinValue(4);
        pickerNbPlayers.setMaxValue(12);
        pickerNbPlayers.setValue(4);

        nbPlayers = pickerNbPlayers.getValue();

        updateRoles(nbPlayers);

        pickerNbPlayers.setOnValueChangedListener((picker, oldVal, newVal) -> {
            nbPlayers = newVal;
            updateRoles(nbPlayers);
        });

        btnNext.setOnClickListener(view -> {
            Intent intent = new Intent(NbPlayersActivity.this, AddPlayerActivity.class);
            intent.putExtra("nbPlayers", nbPlayers);
            intent.putExtra("nbAgent", nbAgent);
            intent.putExtra("nbSpy", nbSpy);
            intent.putExtra("nbWhitePage", nbWhitePage);
            startActivity(intent);
            finish();
        });
    }

    private void updateRoles(int nbPlayers) {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT nbSpy, nbAgent, nbWhitePage FROM nbRoles WHERE nbPlayers = ?",
                new String[]{Integer.toString(nbPlayers)});
        if (cursor.moveToFirst()) {
            nbSpy = cursor.getInt(0);
            nbAgent = cursor.getInt(1);
            nbWhitePage = cursor.getInt(2);

            txtNbSpy.setText("Imposteurs : " + nbSpy);
            txtNbAgent.setText("Agents : " + nbAgent);
            txtNbWhitePage.setText("Amnésiques : " + nbWhitePage);
        } else {
            txtNbSpy.setText("Imposteurs : -");
            txtNbAgent.setText("Agents : -");
            txtNbWhitePage.setText("Amnésiques : -");
        }
        cursor.close();
        db.close();
    }
}
