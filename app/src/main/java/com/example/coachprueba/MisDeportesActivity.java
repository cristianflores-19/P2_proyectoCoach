package com.example.coachprueba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MisDeportesActivity extends AppCompatActivity {

    private Button btnGym, btnCiclismo, btnFutbol, btnTenis, btnBasquetbol,
                   btnNatacion, btnCorrer, btnBoxeo, btnVoleibol;
    private ImageView btnVolver;
    private LinearLayout navRutinas, navCoach, navProgreso;
    private TextView txtDeportesSeleccionados;

    private final List<String> deportesSeleccionados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_deportes);

        initViews();
        setupListeners();
        setupBottomNavigation();
        loadCurrentSports();
        updateSelectedSports();
    }

    private void initViews() {
        btnGym = findViewById(R.id.btnGym);
        btnCiclismo = findViewById(R.id.btnCiclismo);
        btnFutbol = findViewById(R.id.btnFutbol);
        btnTenis = findViewById(R.id.btnTenis);
        btnBasquetbol = findViewById(R.id.btnBasquetbol);
        btnNatacion = findViewById(R.id.btnNatacion);
        btnCorrer = findViewById(R.id.btnCorrer);
        btnBoxeo = findViewById(R.id.btnBoxeo);
        btnVoleibol = findViewById(R.id.btnVoleibol);

        btnVolver = findViewById(R.id.btnVolver);
        txtDeportesSeleccionados = findViewById(R.id.txtDeportesSeleccionados);

        navRutinas = findViewById(R.id.nav_rutinas);
        navCoach = findViewById(R.id.nav_coach);
        navProgreso = findViewById(R.id.nav_progreso);
    }

    private void setupListeners() {
        btnVolver.setOnClickListener(v -> finish());

        // Listeners para deportes
        btnGym.setOnClickListener(v -> toggleDeporte("GYM", btnGym));
        btnCiclismo.setOnClickListener(v -> toggleDeporte("Ciclismo", btnCiclismo));
        btnFutbol.setOnClickListener(v -> toggleDeporte("Futbol", btnFutbol));
        btnTenis.setOnClickListener(v -> toggleDeporte("Tenis", btnTenis));
        btnBasquetbol.setOnClickListener(v -> toggleDeporte("Basquetbol", btnBasquetbol));
        btnNatacion.setOnClickListener(v -> toggleDeporte("Natacion", btnNatacion));
        btnCorrer.setOnClickListener(v -> toggleDeporte("Correr", btnCorrer));
        btnBoxeo.setOnClickListener(v -> toggleDeporte("Boxeo", btnBoxeo));
        btnVoleibol.setOnClickListener(v -> toggleDeporte("Voleibol", btnVoleibol));
    }

    private void setupBottomNavigation() {
        navRutinas.setOnClickListener(v -> {
            startActivity(new Intent(this, MenuPrincipalActivity.class));
            finish();
        });

        navCoach.setOnClickListener(v -> {
            startActivity(new Intent(this, CoachOnlineActivity.class));
        });

        navProgreso.setOnClickListener(v -> {
            startActivity(new Intent(this, ProgresoActivity.class));
        });
    }

    private void loadCurrentSports() {
        // TODO: Cargar deportes actuales del usuario desde la base de datos
        // Por ahora simulamos que tiene Boxeo seleccionado
        deportesSeleccionados.add("Boxeo");
        updateButtonStates();
    }

    private void updateButtonStates() {
        // Resetear todos los botones
        resetAllButtons();

        // Actualizar botones seleccionados
        for (String deporte : deportesSeleccionados) {
            Button button = getButtonForSport(deporte);
            if (button != null) {
                button.setBackgroundResource(R.drawable.sport_button_selected);
                button.setTextColor(getResources().getColor(android.R.color.white));
            }
        }
    }

    private void resetAllButtons() {
        Button[] buttons = {btnGym, btnCiclismo, btnFutbol, btnTenis, btnBasquetbol,
                           btnNatacion, btnCorrer, btnBoxeo, btnVoleibol};

        for (Button button : buttons) {
            button.setBackgroundResource(R.drawable.sport_button_unselected);
            button.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

    private Button getButtonForSport(String deporte) {
        switch (deporte) {
            case "GYM": return btnGym;
            case "Ciclismo": return btnCiclismo;
            case "Futbol": return btnFutbol;
            case "Tenis": return btnTenis;
            case "Basquetbol": return btnBasquetbol;
            case "Natacion": return btnNatacion;
            case "Correr": return btnCorrer;
            case "Boxeo": return btnBoxeo;
            case "Voleibol": return btnVoleibol;
            default: return null;
        }
    }

    private void toggleDeporte(String deporte, Button button) {
        if (deportesSeleccionados.contains(deporte)) {
            deportesSeleccionados.remove(deporte);
            button.setBackgroundResource(R.drawable.sport_button_unselected);
            button.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            deportesSeleccionados.add(deporte);
            button.setBackgroundResource(R.drawable.sport_button_selected);
            button.setTextColor(getResources().getColor(android.R.color.white));
        }
        updateSelectedSports();
        // TODO: Guardar cambios en la base de datos
    }

    private void updateSelectedSports() {
        if (deportesSeleccionados.isEmpty()) {
            txtDeportesSeleccionados.setText("Ninguno");
        } else {
            txtDeportesSeleccionados.setText(String.join(", ", deportesSeleccionados));
        }
    }
}
