package fr.caensup.licsts.smb116_imposteur_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Importer les données dans la base SQLite
        try {
            SQLiteImportJSON importer = new SQLiteImportJSON(this);
            importer.importData();
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors de l'importation des données : " + e.getMessage(), 
                         Toast.LENGTH_LONG).show();
        }

        Button btnStartGame = findViewById(R.id.btn_startGame);
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPlayerActivity.class);
                startActivity(intent);
            }
        });
    }
}