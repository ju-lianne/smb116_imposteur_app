package fr.imposteur.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import database.DBHelper;
import fr.imposteur.R;

public class NbPlayersActivity extends AppCompatActivity {
    private TextView txtNbSpy, txtNbAgent, txtNbWhitePage;
    private NumberPicker nbPlayersPicker;
    private Button btnNext;
    private int nbPlayers;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nb_players);

        txtNbSpy = findViewById(R.id.txt_nbSpy);
        txtNbAgent = findViewById(R.id.txt_nbAgent);
        txtNbWhitePage = findViewById(R.id.txt_nbWhitePage);

        nbPlayersPicker = findViewById(R.id.nbPicker_nbPlayers);
        btnNext = findViewById(R.id.btn_next);

        nbPlayersPicker.setMinValue(4);
        nbPlayersPicker.setMaxValue(12);
        nbPlayersPicker.setValue(4);

        nbPlayers = nbPlayersPicker.getValue();

        updateRoles(nbPlayers);

        nbPlayersPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            nbPlayers = newVal;
            updateRoles(nbPlayers);
            Toast.makeText(this, "Nombre de joueurs : " + nbPlayers, Toast.LENGTH_SHORT).show();
        });

        btnNext.setOnClickListener(view -> {
            Intent intent = new Intent(NbPlayersActivity.this, AddPlayerActivity.class);
            intent.putExtra("nbPlayers", nbPlayers);
            startActivity(intent);
        });
    }

    private void updateRoles(int nbPlayers) {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT nbSpy, nbAgent, nbWhitePage FROM nbRoles WHERE nbPlayers = ?",
                new String[]{Integer.toString(nbPlayers)});
        if (cursor.moveToFirst()) {
            int nbSpy = cursor.getInt(0);
            int nbAgent = cursor.getInt(1);
            int nbWhitePage = cursor.getInt(2);

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
