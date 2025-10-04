package com.example.coachprueba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachprueba.models.Usuario;
import com.example.coachprueba.utils.SimpleUsuarioManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etNombreCompleto, etContrasena;
    private Button btnIniciarSesion;
    private TextView txtRegistrarse;
    private SimpleUsuarioManager usuarioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        usuarioManager = new SimpleUsuarioManager(this);
        setupClickListeners();

        // CREAR USUARIO DE PRUEBA (funciona siempre)
        usuarioManager.crearUsuarioDePrueba();
        Toast.makeText(this, "Usuario de prueba: Juan Perez / 1234", Toast.LENGTH_LONG).show();

        // Verificar si viene nombre prellenado desde el registro
        String usuarioPrellenado = getIntent().getStringExtra("usuario_prellenado");
        if (usuarioPrellenado != null && !usuarioPrellenado.isEmpty()) {
            etNombreCompleto.setText(usuarioPrellenado);
            etContrasena.requestFocus();
        }
    }

    private void initViews() {
        etNombreCompleto = findViewById(R.id.etNombreCompleto);
        etContrasena = findViewById(R.id.etContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        txtRegistrarse = findViewById(R.id.txtRegistrarse);
    }

    private void setupClickListeners() {
        btnIniciarSesion.setOnClickListener(v -> iniciarSesion());

        txtRegistrarse.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroActivity.class);
            startActivity(intent);
        });
    }

    private void iniciarSesion() {
        String nombre = etNombreCompleto.getText().toString().trim();
        String contrasena = etContrasena.getText().toString().trim();

        android.util.Log.d("LOGIN_SIMPLE", "Intentando login: " + nombre + " / " + contrasena);

        if (nombre.isEmpty()) {
            etNombreCompleto.setError("El nombre es obligatorio");
            etNombreCompleto.requestFocus();
            return;
        }

        if (contrasena.isEmpty()) {
            etContrasena.setError("La contraseña es obligatoria");
            etContrasena.requestFocus();
            return;
        }

        Usuario usuario = usuarioManager.verificarLogin(nombre, contrasena);

        if (usuario != null) {
            Toast.makeText(this, "¡Bienvenido, " + usuario.getNombreCompleto() + "!", Toast.LENGTH_SHORT).show();

            // Guardar sesión
            guardarSesionUsuario(usuario);

            // Ir al menú principal
            Intent intent = new Intent(this, MenuPrincipalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "Credenciales incorrectas. Prueba: Juan Perez / 1234", Toast.LENGTH_LONG).show();
            etContrasena.setText("");
            etContrasena.requestFocus();
        }
    }

    private void guardarSesionUsuario(Usuario usuario) {
        SharedPreferences sharedPreferences = getSharedPreferences("sesion_usuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("usuario_id", usuario.getId());
        editor.putString("usuario_nombre", usuario.getNombreCompleto());
        editor.putString("usuario_objetivo", usuario.getObjetivo());
        editor.putString("deportes_favoritos", usuario.getDeportesFavoritos());
        editor.putBoolean("sesion_activa", true);

        editor.apply();
    }
}
