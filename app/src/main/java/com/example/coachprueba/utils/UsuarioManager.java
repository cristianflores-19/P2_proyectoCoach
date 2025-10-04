package com.example.coachprueba.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.coachprueba.database.DatabaseHelper;
import com.example.coachprueba.models.Usuario;

public class UsuarioManager {
    private DatabaseHelper dbHelper;
    private Context context;

    public UsuarioManager(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    // Registrar un nuevo usuario
    public boolean registrarUsuario(Usuario usuario) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        android.util.Log.d("DB_REGISTRO", "Intentando registrar usuario: " + usuario.getNombreCompleto());

        try {
            // Verificar si el usuario ya existe
            android.util.Log.d("DB_REGISTRO", "Verificando si el usuario ya existe...");
            if (existeUsuario(usuario.getNombreCompleto())) {
                android.util.Log.d("DB_REGISTRO", "Usuario ya existe: " + usuario.getNombreCompleto());
                return false; // Usuario ya existe
            }

            android.util.Log.d("DB_REGISTRO", "Usuario no existe, procediendo a insertar...");

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_NOMBRE_COMPLETO, usuario.getNombreCompleto());
            values.put(DatabaseHelper.COLUMN_CONTRASENA, usuario.getContrasena());
            values.put(DatabaseHelper.COLUMN_EDAD, usuario.getEdad());
            values.put(DatabaseHelper.COLUMN_PESO, usuario.getPeso());
            values.put(DatabaseHelper.COLUMN_ALTURA, usuario.getAltura());
            values.put(DatabaseHelper.COLUMN_SEXO, usuario.getSexo());
            values.put(DatabaseHelper.COLUMN_OBJETIVO, usuario.getObjetivo());
            values.put(DatabaseHelper.COLUMN_DEPORTES_FAVORITOS, usuario.getDeportesFavoritos());

            android.util.Log.d("DB_REGISTRO", "Datos a insertar:");
            android.util.Log.d("DB_REGISTRO", "- Nombre: " + usuario.getNombreCompleto());
            android.util.Log.d("DB_REGISTRO", "- Contraseña: " + usuario.getContrasena());
            android.util.Log.d("DB_REGISTRO", "- Edad: " + usuario.getEdad());
            android.util.Log.d("DB_REGISTRO", "- Peso: " + usuario.getPeso());
            android.util.Log.d("DB_REGISTRO", "- Altura: " + usuario.getAltura());
            android.util.Log.d("DB_REGISTRO", "- Sexo: " + usuario.getSexo());
            android.util.Log.d("DB_REGISTRO", "- Objetivo: " + usuario.getObjetivo());
            android.util.Log.d("DB_REGISTRO", "- Deportes: " + usuario.getDeportesFavoritos());

            long result = db.insert(DatabaseHelper.TABLE_USUARIOS, null, values);
            android.util.Log.d("DB_REGISTRO", "Resultado de inserción: " + result);

            boolean exito = result != -1;
            android.util.Log.d("DB_REGISTRO", "Registro " + (exito ? "exitoso" : "falló"));
            return exito; // Retorna true si se insertó correctamente

        } catch (Exception e) {
            android.util.Log.e("DB_REGISTRO", "Error en registrarUsuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    // Verificar credenciales de login
    public Usuario verificarLogin(String nombreCompleto, String contrasena) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        android.util.Log.d("DB", "Verificando login para: '" + nombreCompleto + "' con contraseña: '" + contrasena + "'");

        try {
            String selection = DatabaseHelper.COLUMN_NOMBRE_COMPLETO + " = ? AND " +
                             DatabaseHelper.COLUMN_CONTRASENA + " = ?";
            String[] selectionArgs = {nombreCompleto, contrasena};

            android.util.Log.d("DB", "Query: " + selection);
            android.util.Log.d("DB", "Args: [" + nombreCompleto + ", " + contrasena + "]");

            Cursor cursor = db.query(
                DatabaseHelper.TABLE_USUARIOS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
            );

            android.util.Log.d("DB", "Resultados encontrados: " + (cursor != null ? cursor.getCount() : 0));

            if (cursor != null && cursor.moveToFirst()) {
                Usuario usuario = new Usuario(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMBRE_COMPLETO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTRASENA)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EDAD)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PESO)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ALTURA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SEXO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_OBJETIVO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPORTES_FAVORITOS))
                );
                cursor.close();
                android.util.Log.d("DB", "Usuario encontrado: " + usuario.getNombreCompleto());
                return usuario;
            }

            if (cursor != null) {
                cursor.close();
            }
            android.util.Log.d("DB", "No se encontró usuario con esas credenciales");
            return null; // Credenciales incorrectas

        } catch (Exception e) {
            android.util.Log.e("DB", "Error en verificarLogin: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
    }

    // Verificar si existe un usuario
    public boolean existeUsuario(String nombreCompleto) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            String selection = DatabaseHelper.COLUMN_NOMBRE_COMPLETO + " = ?";
            String[] selectionArgs = {nombreCompleto};

            Cursor cursor = db.query(
                DatabaseHelper.TABLE_USUARIOS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
            );

            boolean existe = cursor != null && cursor.getCount() > 0;

            if (cursor != null) {
                cursor.close();
            }

            return existe;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    // Obtener usuario por nombre
    public Usuario obtenerUsuario(String nombreCompleto) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            String selection = DatabaseHelper.COLUMN_NOMBRE_COMPLETO + " = ?";
            String[] selectionArgs = {nombreCompleto};

            Cursor cursor = db.query(
                DatabaseHelper.TABLE_USUARIOS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
            );

            if (cursor != null && cursor.moveToFirst()) {
                Usuario usuario = new Usuario(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMBRE_COMPLETO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTRASENA)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EDAD)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PESO)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ALTURA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SEXO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_OBJETIVO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPORTES_FAVORITOS))
                );
                cursor.close();
                return usuario;
            }

            if (cursor != null) {
                cursor.close();
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
    }
}
