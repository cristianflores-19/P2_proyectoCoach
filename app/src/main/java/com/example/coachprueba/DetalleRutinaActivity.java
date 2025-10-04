package com.example.coachprueba;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coachprueba.models.Rutina;
import com.example.coachprueba.models.Ejercicio;
import com.example.coachprueba.utils.RutinaManager;
import java.util.List;

public class DetalleRutinaActivity extends AppCompatActivity {

    private TextView txtNombreRutina, txtDeporteRutina, txtDescripcionRutina;
    private RecyclerView recyclerViewEjercicios;
    private Button btnIniciarRutina, btnMarcarCompletada;
    private ImageView btnVolver;

    private String rutinaId;
    private String rutinaNombre;
    private String rutinaDeporte;
    private Rutina rutina;
    private EjercicioAdapter ejercicioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_rutina);

        initViews();
        getIntentData();
        setupRecyclerView();
        setupClickListeners();
        loadRutinaDetails();
    }

    private void initViews() {
        txtNombreRutina = findViewById(R.id.txtNombreRutina);
        txtDeporteRutina = findViewById(R.id.txtDeporteRutina);
        txtDescripcionRutina = findViewById(R.id.txtDescripcionRutina);
        recyclerViewEjercicios = findViewById(R.id.recyclerViewEjercicios);
        btnIniciarRutina = findViewById(R.id.btnIniciarRutina);
        btnMarcarCompletada = findViewById(R.id.btnMarcarCompletada);
        btnVolver = findViewById(R.id.btnVolver);
    }

    private void getIntentData() {
        rutinaId = getIntent().getStringExtra("rutina_id");
        rutinaNombre = getIntent().getStringExtra("rutina_nombre");
        rutinaDeporte = getIntent().getStringExtra("rutina_deporte");
    }

    private void setupRecyclerView() {
        recyclerViewEjercicios.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        btnVolver.setOnClickListener(v -> finish());

        btnIniciarRutina.setOnClickListener(v -> {
            Toast.makeText(this, "Iniciando rutina: " + rutinaNombre, Toast.LENGTH_SHORT).show();
            // Aquí se puede implementar la lógica para iniciar la rutina
        });

        btnMarcarCompletada.setOnClickListener(v -> {
            if (rutina != null) {
                rutina.setCompletada(true);
                RutinaManager.actualizarRutina(rutina);
                Toast.makeText(this, "Rutina marcada como completada", Toast.LENGTH_SHORT).show();
                updateUI();
            }
        });
    }

    private void loadRutinaDetails() {
        if (rutinaId != null) {
            rutina = RutinaManager.getRutinaPorId(rutinaId);
            if (rutina != null) {
                displayRutinaInfo();
                loadEjercicios();
            }
        } else {
            // Si no hay ID, usar los datos básicos del intent
            txtNombreRutina.setText(rutinaNombre != null ? rutinaNombre : "Rutina");
            txtDeporteRutina.setText(rutinaDeporte != null ? rutinaDeporte : "Deporte");
            txtDescripcionRutina.setText("Descripción no disponible");
        }
    }

    private void displayRutinaInfo() {
        txtNombreRutina.setText(rutina.getNombre());
        txtDeporteRutina.setText(rutina.getDeporte());
        txtDescripcionRutina.setText(rutina.getDescripcion());
    }

    private void loadEjercicios() {
        if (rutina != null && rutina.getEjercicios() != null) {
            ejercicioAdapter = new EjercicioAdapter(rutina.getEjercicios());
            recyclerViewEjercicios.setAdapter(ejercicioAdapter);
        }
    }

    private void updateUI() {
        if (rutina != null && rutina.isCompletada()) {
            btnMarcarCompletada.setText("Completada ✓");
            btnMarcarCompletada.setEnabled(false);
        }
    }
}
