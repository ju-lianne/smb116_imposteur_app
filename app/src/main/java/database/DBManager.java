package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.Item;

public class DBManager {
    private SQLiteDatabase db;

    public DBManager(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public List<Item> getItemsByCategory(int categoryId) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE category_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(categoryId)});

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                items.add(new Item(name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public int getCategoriesLength() {
        String sql = "SELECT count(id) FROM categories";
        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public List<Integer> getCategoriesIds() {
        List<Integer> categoryIds = new ArrayList<>();
        String sql = "SELECT id FROM categories";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                categoryIds.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categoryIds;
    }

    public int getCategoryId() {
        List<Integer> categoryIds = getCategoriesIds();
        if (categoryIds.isEmpty()) {
            throw new IllegalStateException("Aucune catégorie trouvée !");
        }
        return categoryIds.get(new Random().nextInt(categoryIds.size()));
    }
}
