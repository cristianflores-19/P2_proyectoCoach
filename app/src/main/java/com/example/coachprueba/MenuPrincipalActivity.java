package com.example.coachprueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MenuPrincipalActivity extends AppCompatActivity {

    private TextView txtSaludo, txtRutinaActiva, txtTiempo, txtCalorias, txtEjercicios;
    private LinearLayout navCoach, navProgreso, navDeportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        initViews();
        setupBottomNavigation();
        loadUserData();
    }

    private void initViews() {
        txtSaludo = findViewById(R.id.txtSaludo);
        txtRutinaActiva = findViewById(R.id.txtRutinaActiva);
        txtTiempo = findViewById(R.id.txtTiempo);
        txtCalorias = findViewById(R.id.txtCalorias);
        txtEjercicios = findViewById(R.id.txtEjercicios);

        navCoach = findViewById(R.id.nav_coach);
        navProgreso = findViewById(R.id.nav_progreso);
        navDeportes = findViewById(R.id.nav_deportes);
    }

    private void setupBottomNavigation() {
        navCoach.setOnClickListener(v -> {
            // Abrir el chat con IA integrada con Ollama
            startActivity(new Intent(this, ChatActivity.class));
        });

        navProgreso.setOnClickListener(v -> {
            startActivity(new Intent(this, ProgresoActivity.class));
        });

        navDeportes.setOnClickListener(v -> {
            startActivity(new Intent(this, MisDeportesActivity.class));
        });
    }

    private void loadUserData() {
        // TODO: Cargar datos del usuario desde la base de datos
        txtSaludo.setText("¡Hola, Usuario!");
        txtRutinaActiva.setText("Rutina Activa - GYM");
        txtTiempo.setText("50 min");
        txtCalorias.setText("300 kcal");
        txtEjercicios.setText("2/3 ejercicios");
    }
}
