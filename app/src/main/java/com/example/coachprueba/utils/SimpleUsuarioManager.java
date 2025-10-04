package com.example.coachprueba.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.coachprueba.models.Usuario;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SimpleUsuarioManager {
    private static final String PREFS_NAME = "usuarios_prefs";
    private static final String USUARIOS_KEY = "usuarios_list";
    private SharedPreferences prefs;
    private Gson gson;

    public SimpleUsuarioManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public boolean registrarUsuario(Usuario usuario) {
        android.util.Log.d("SIMPLE_AUTH", "Registrando usuario: " + usuario.getNombreCompleto());

        try {
            List<Usuario> usuarios = obtenerTodosLosUsuarios();

            // Verificar si ya existe
            for (Usuario u : usuarios) {
                if (u.getNombreCompleto().equals(usuario.getNombreCompleto())) {
                    android.util.Log.d("SIMPLE_AUTH", "Usuario ya existe");
                    return false;
                }
            }

            // Agregar nuevo usuario
            usuario.setId(usuarios.size() + 1);
            usuarios.add(usuario);

            // Guardar lista actualizada
            String json = gson.toJson(usuarios);
            prefs.edit().putString(USUARIOS_KEY, json).apply();

            android.util.Log.d("SIMPLE_AUTH", "Usuario registrado exitosamente");
            return true;

        } catch (Exception e) {
            android.util.Log.e("SIMPLE_AUTH", "Error al registrar: " + e.getMessage());
            return false;
        }
    }

    public Usuario verificarLogin(String nombre, String contrasena) {
        android.util.Log.d("SIMPLE_AUTH", "Verificando login: " + nombre + " / " + contrasena);

        try {
            List<Usuario> usuarios = obtenerTodosLosUsuarios();

            for (Usuario usuario : usuarios) {
                if (usuario.getNombreCompleto().equals(nombre) &&
                    usuario.getContrasena().equals(contrasena)) {
                    android.util.Log.d("SIMPLE_AUTH", "Login exitoso");
                    return usuario;
                }
            }

            android.util.Log.d("SIMPLE_AUTH", "Credenciales incorrectas");
            return null;

        } catch (Exception e) {
            android.util.Log.e("SIMPLE_AUTH", "Error en login: " + e.getMessage());
            return null;
        }
    }

    private List<Usuario> obtenerTodosLosUsuarios() {
        try {
            String json = prefs.getString(USUARIOS_KEY, "[]");
            Type listType = new TypeToken<List<Usuario>>(){}.getType();
            return gson.fromJson(json, listType);
        } catch (Exception e) {
            android.util.Log.e("SIMPLE_AUTH", "Error al obtener usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void crearUsuarioDePrueba() {
        Usuario usuarioPrueba = new Usuario("Juan Perez", "1234", 25, 70.0, 175.0, "Masculino", "Ganar músculo", "GYM,Futbol");
        boolean creado = registrarUsuario(usuarioPrueba);
        android.util.Log.d("SIMPLE_AUTH", "Usuario de prueba " + (creado ? "creado" : "ya existe"));
    }
}
