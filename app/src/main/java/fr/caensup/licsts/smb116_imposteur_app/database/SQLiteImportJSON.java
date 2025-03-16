package fr.caensup.licsts.smb116_imposteur_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class SQLiteImportJSON {
    private static final String TAG = "SQLiteImportJSON";
    private final DBHelper dbHelper;
    private final Context context;

    public SQLiteImportJSON(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    public void importData() throws IOException, SQLiteException {
        SQLiteDatabase db = null;
        InputStreamReader wordsReader = null;
        InputStreamReader rolesReader = null;

        try {
            db = dbHelper.getWritableDatabase();
            wordsReader = new InputStreamReader(context.getAssets().open("res/database/nbRoles.json"));
            rolesReader = new InputStreamReader(context.getAssets().open("res/database/nbRoles.json"));

            // Lire le JSON
            Gson gson = new Gson();
            Map<String, List<String>> dataWords;
            Map<String, Map<String, Integer>> dataRoles;

            try {
                dataWords = gson.fromJson(wordsReader, new TypeToken<Map<String, List<String>>>() {}.getType());
                dataRoles = gson.fromJson(rolesReader, new TypeToken<Map<String, Map<String, Integer>>>() {}.getType());

                if (dataWords == null || dataRoles == null) {
                    throw new JsonSyntaxException("Les fichiers JSON sont mal formatés");
                }
            } catch (JsonSyntaxException e) {
                Log.e(TAG, "Erreur de parsing JSON", e);
                throw new IOException("Erreur lors de la lecture des fichiers JSON : " + e.getMessage());
            }

            db.beginTransaction();
            try {
                // Insérer les catégories et les mots
                for (Map.Entry<String, List<String>> entry : dataWords.entrySet()) {
                    String category = entry.getKey();
                    List<String> items = entry.getValue();

                    if (category == null || items == null) {
                        continue;
                    }

                    int categoryId = getCategoryId(db, category);
                    if (categoryId == -1) {
                        categoryId = insertCategory(db, category);
                    }

                    for (String item : items) {
                        if (item != null && !item.trim().isEmpty()) {
                            insertItem(db, categoryId, item.trim());
                        }
                    }
                }

                // Insérer les rôles
                for (Map.Entry<String, Map<String, Integer>> entry : dataRoles.entrySet()) {
                    try {
                        int nbPlayers = Integer.parseInt(entry.getKey());
                        Map<String, Integer> roleData = entry.getValue();

                        if (roleData != null && !roleExists(db, nbPlayers)) {
                            insertRole(db, nbPlayers, roleData);
                        }
                    } catch (NumberFormatException e) {
                        Log.w(TAG, "Nombre de joueurs invalide ignoré : " + entry.getKey());
                    }
                }

                db.setTransactionSuccessful();
                Log.i(TAG, "Données importées avec succès depuis le JSON");
            } finally {
                db.endTransaction();
            }
        } finally {
            if (wordsReader != null) {
                try {
                    wordsReader.close();
                } catch (IOException e) {
                    Log.w(TAG, "Erreur lors de la fermeture du fichier words.json", e);
                }
            }
            if (rolesReader != null) {
                try {
                    rolesReader.close();
                } catch (IOException e) {
                    Log.w(TAG, "Erreur lors de la fermeture du fichier nbRoles.json", e);
                }
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    private int getCategoryId(SQLiteDatabase db, String category) {
        try (Cursor cursor = db.rawQuery("SELECT id FROM " + DBHelper.TABLE_CATEGORIES + 
                                       " WHERE " + DBHelper.COLUMN_NAME + " = ?", 
                                       new String[]{category})) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return -1;
        }
    }

    private int insertCategory(SQLiteDatabase db, String category) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, category);
        return (int) db.insert(DBHelper.TABLE_CATEGORIES, null, values);
    }

    private void insertItem(SQLiteDatabase db, int categoryId, String item) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_CATEGORY_ID, categoryId);
        values.put(DBHelper.COLUMN_NAME, item);
        db.insert(DBHelper.TABLE_ITEMS, null, values);
    }

    private boolean roleExists(SQLiteDatabase db, int nbPlayers) {
        try (Cursor cursor = db.rawQuery("SELECT " + DBHelper.COLUMN_NB_PLAYERS + 
                                       " FROM " + DBHelper.TABLE_ROLES + 
                                       " WHERE " + DBHelper.COLUMN_NB_PLAYERS + " = ?", 
                                       new String[]{String.valueOf(nbPlayers)})) {
            return cursor.moveToFirst();
        }
    }

    private void insertRole(SQLiteDatabase db, int nbPlayers, Map<String, Integer> roleData) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NB_PLAYERS, nbPlayers);
        values.put(DBHelper.COLUMN_NB_CIVILIAN, roleData.getOrDefault("civilian", 0));
        values.put(DBHelper.COLUMN_NB_SPY, roleData.getOrDefault("spy", 0));
        values.put(DBHelper.COLUMN_NB_WHITEPAGE, roleData.getOrDefault("whitepage", 0));
        db.insert(DBHelper.TABLE_ROLES, null, values);
    }
}
