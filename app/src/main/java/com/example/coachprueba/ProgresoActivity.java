package com.example.coachprueba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coachprueba.models.Rutina;
import com.example.coachprueba.utils.RutinaManager;
import java.util.List;

public class ProgresoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRutinas;
    private ImageView btnVolver;
    private LinearLayout navRutinas, navCoach, navDeportes;
    private RutinaAdapter rutinaAdapter;
    private List<Rutina> rutinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progreso);

        initViews();
        setupRecyclerView();
        setupBottomNavigation();
        loadRutinas();
    }

    private void initViews() {
        recyclerViewRutinas = findViewById(R.id.recyclerViewRutinas);
        btnVolver = findViewById(R.id.btnVolver);
        navRutinas = findViewById(R.id.nav_rutinas);
        navCoach = findViewById(R.id.nav_coach);
        navDeportes = findViewById(R.id.nav_deportes);
    }

    private void setupRecyclerView() {
        recyclerViewRutinas.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupBottomNavigation() {
        btnVolver.setOnClickListener(v -> finish());

        navRutinas.setOnClickListener(v -> {
            startActivity(new Intent(this, MenuPrincipalActivity.class));
            finish();
        });

        navCoach.setOnClickListener(v -> {
            startActivity(new Intent(this, CoachOnlineActivity.class));
        });

        navDeportes.setOnClickListener(v -> {
            startActivity(new Intent(this, MisDeportesActivity.class));
        });
    }

    private void loadRutinas() {
        // Cargar rutinas de todos los deportes
        rutinas = RutinaManager.getAllRutinas();
        rutinaAdapter = new RutinaAdapter(rutinas, this::onRutinaClick);
        recyclerViewRutinas.setAdapter(rutinaAdapter);
    }

    private void onRutinaClick(Rutina rutina) {
        Intent intent = new Intent(this, DetalleRutinaActivity.class);
        intent.putExtra("rutina_id", rutina.getId());
        intent.putExtra("rutina_nombre", rutina.getNombre());
        intent.putExtra("rutina_deporte", rutina.getDeporte());
        startActivity(intent);
    }
}
