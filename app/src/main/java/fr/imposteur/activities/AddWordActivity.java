package fr.imposteur.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import fr.imposteur.R;

public class AddWordActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        Button btnBack = findViewById(R.id.btn_backToMain);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(AddWordActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
