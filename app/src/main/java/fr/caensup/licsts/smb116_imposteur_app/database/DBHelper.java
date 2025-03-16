package fr.caensup.licsts.smb116_imposteur_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "words.db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_ITEMS = "items";
    public static final String TABLE_ROLES = "nbRoles";

    // Colonnes communes
    public static final String COLUMN_ID = "id";
    
    // Colonnes categories
    public static final String COLUMN_NAME = "name";
    
    // Colonnes items
    public static final String COLUMN_CATEGORY_ID = "category_id";
    
    // Colonnes roles
    public static final String COLUMN_NB_PLAYERS = "nbPlayers";
    public static final String COLUMN_NB_CIVILIAN = "nbCivilian";
    public static final String COLUMN_NB_SPY = "nbSpy";
    public static final String COLUMN_NB_WHITEPAGE = "nbWhitepage";

    private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT NOT NULL"
            + ");";

    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEGORY_ID + " INTEGER, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + "FOREIGN KEY (" + COLUMN_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_ID + ")"
            + ");";

    private static final String CREATE_ROLES_TABLE = "CREATE TABLE " + TABLE_ROLES + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NB_PLAYERS + " INTEGER, "
            + COLUMN_NB_CIVILIAN + " INTEGER, "
            + COLUMN_NB_SPY + " INTEGER, "
            + COLUMN_NB_WHITEPAGE + " INTEGER"
            + ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(CREATE_CATEGORIES_TABLE);
            db.execSQL(CREATE_ITEMS_TABLE);
            db.execSQL(CREATE_ROLES_TABLE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.beginTransaction();
        try {
            // Sauvegarde des données si nécessaire pour les futures versions
            dropAllTables(db);
            onCreate(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void dropAllTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
    }

    public void clearAllTables(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL("DELETE FROM " + TABLE_ITEMS);
            db.execSQL("DELETE FROM " + TABLE_ROLES);
            db.execSQL("DELETE FROM " + TABLE_CATEGORIES);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
