package fr.caensup.licsts.smb116_imposteur_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DBManager {
    private static final String TAG = "DBManager";
    private final DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    private SQLiteDatabase getReadableDB() throws SQLiteException {
        if (db == null || !db.isOpen()) {
            db = dbHelper.getReadableDatabase();
        }
        return db;
    }

    public void closeDB() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public List<Item> getItemsByCategory(int categoryId) {
        List<Item> items = new ArrayList<>();
        String query = "SELECT " + DBHelper.COLUMN_NAME + 
                      " FROM " + DBHelper.TABLE_ITEMS + 
                      " WHERE " + DBHelper.COLUMN_CATEGORY_ID + " = ?";
        
        try (Cursor cursor = getReadableDB().rawQuery(query, new String[]{String.valueOf(categoryId)})) {
            if (cursor.moveToFirst()) {
                do {
                    items.add(new Item(cursor.getString(0)));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error getting items for category " + categoryId, e);
        }
        return items;
    }

    public int getCategoriesLength() {
        String query = "SELECT COUNT(" + DBHelper.COLUMN_ID + ") FROM " + DBHelper.TABLE_CATEGORIES;
        try (Cursor cursor = getReadableDB().rawQuery(query, null)) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error getting categories count", e);
        }
        return 0;
    }

    public List<Integer> getCategoriesIds() {
        List<Integer> categoryIds = new ArrayList<>();
        String query = "SELECT " + DBHelper.COLUMN_ID + " FROM " + DBHelper.TABLE_CATEGORIES;
        
        try (Cursor cursor = getReadableDB().rawQuery(query, null)) {
            if (cursor.moveToFirst()) {
                do {
                    categoryIds.add(cursor.getInt(0));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error getting category IDs", e);
        }
        return categoryIds;
    }

    public int getCategoryId() {
        List<Integer> categoryIds = getCategoriesIds();
        if (categoryIds.isEmpty()) {
            throw new IllegalStateException("Aucune catégorie trouvée dans la base de données !");
        }
        return categoryIds.get(new Random().nextInt(categoryIds.size()));
    }

    public NbRoles getRolesForPlayers(int nbPlayers) {
        String query = "SELECT " + 
                      DBHelper.COLUMN_NB_CIVILIAN + ", " +
                      DBHelper.COLUMN_NB_SPY + ", " +
                      DBHelper.COLUMN_NB_WHITEPAGE +
                      " FROM " + DBHelper.TABLE_ROLES +
                      " WHERE " + DBHelper.COLUMN_NB_PLAYERS + " = ?";

        try (Cursor cursor = getReadableDB().rawQuery(query, new String[]{String.valueOf(nbPlayers)})) {
            if (cursor.moveToFirst()) {
                return new NbRoles(
                    nbPlayers,
                    cursor.getInt(0), // nbCivilian
                    cursor.getInt(1), // nbSpy
                    cursor.getInt(2)  // nbWhitePage
                );
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error getting roles for " + nbPlayers + " players", e);
        }
        throw new IllegalStateException("Aucune configuration de rôles trouvée pour " + nbPlayers + " joueurs");
    }

    @Override
    protected void finalize() throws Throwable {
        closeDB();
        super.finalize();
    }
}
