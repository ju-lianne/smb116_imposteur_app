package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class DataImporter {
    private Context context;

    public DataImporter(Context context) {
        this.context = context;
    }

    public void importData() {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            // Lire le fichier JSON
            InputStream wordsIs = context.getAssets().open("words.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(wordsIs));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();

            // Convertir en objet JSON
            JSONObject jsonData = new JSONObject(jsonBuilder.toString());
            JSONArray categories = jsonData.names();

            for (int i = 0; i < Objects.requireNonNull(categories).length(); i++) {
                String category = categories.getString(i);
                JSONArray items = jsonData.getJSONArray(category);

                ContentValues categoryValues = new ContentValues();
                categoryValues.put("name", category);
                long categoryId = db.insert("categories", null, categoryValues);

                for (int j = 0; j < items.length(); j++) {
                    ContentValues itemValues = new ContentValues();
                    itemValues.put("category_id", categoryId);
                    itemValues.put("name", items.getString(j));
                    db.insert("items", null, itemValues);
                }
            }

            InputStream nbRolesIs = context.getAssets().open("nbRoles.json");
            reader = new BufferedReader(new InputStreamReader(nbRolesIs));
            jsonBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();

            JSONObject rolesData = new JSONObject(jsonBuilder.toString());
            JSONArray rolesKeys = rolesData.names();

            for (int i = 0; i < Objects.requireNonNull(rolesKeys).length(); i++) {
                String nbPlayersStr = rolesKeys.getString(i);
                int nbPlayers = Integer.parseInt(nbPlayersStr);

                JSONObject roleInfo = rolesData.getJSONObject(nbPlayersStr);
                int nbAgent = roleInfo.getInt("agent");
                int nbSpy = roleInfo.getInt("spy");
                int nbWhitePage = roleInfo.getInt("whitepage");

                ContentValues roleValues = new ContentValues();
                roleValues.put("nbPlayers", nbPlayers);
                roleValues.put("nbAgent", nbAgent);
                roleValues.put("nbSpy", nbSpy);
                roleValues.put("nbWhitePage", nbWhitePage);

                db.insert("nbRoles", null, roleValues);
            }
        } catch (Exception e) {
            Log.e("DataImporter", "Erreur d'importation JSON : " + e.getMessage());
        }
    }
}
