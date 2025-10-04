package com.example.coachprueba.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "coachprueba.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla usuarios
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE_COMPLETO = "nombre_completo";
    public static final String COLUMN_CONTRASENA = "contrasena";
    public static final String COLUMN_EDAD = "edad";
    public static final String COLUMN_PESO = "peso";
    public static final String COLUMN_ALTURA = "altura";
    public static final String COLUMN_SEXO = "sexo";
    public static final String COLUMN_OBJETIVO = "objetivo";
    public static final String COLUMN_DEPORTES_FAVORITOS = "deportes_favoritos";

    private static final String CREATE_TABLE_USUARIOS =
        "CREATE TABLE " + TABLE_USUARIOS + " (" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_NOMBRE_COMPLETO + " TEXT NOT NULL, " +
        COLUMN_CONTRASENA + " TEXT NOT NULL, " +
        COLUMN_EDAD + " INTEGER, " +
        COLUMN_PESO + " REAL, " +
        COLUMN_ALTURA + " REAL, " +
        COLUMN_SEXO + " TEXT, " +
        COLUMN_OBJETIVO + " TEXT, " +
        COLUMN_DEPORTES_FAVORITOS + " TEXT" +
        ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USUARIOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }
}
