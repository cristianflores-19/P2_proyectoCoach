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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
                Toast.makeText(this, "¡Rutina completada! 🎉", Toast.LENGTH_SHORT).show();
                updateUI();

                // Enviar logro a Telegram vía n8n
                enviarLogroTelegram(rutina.getNombre());
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

    private void enviarLogroTelegram(String nombreRutina) {
        // Ejecutar en un hilo en background
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                // URL del webhook de n8n para logros
                URL url = new URL("https://cristianproyectop2.app.n8n.cloud/webhook-test/logro-fitness");
                connection = (HttpURLConnection) url.openConnection();

                // Configurar la conexión
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setConnectTimeout(15000); // 15 segundos timeout
                connection.setReadTimeout(15000);

                // Crear el JSON body con el nombre de la rutina completada
                String jsonBody = String.format(
                    "{\"app\":\"ADAPTIA\",\"accion\":\"rutina_completada\",\"usuario\":\"desde_android\",\"rutina\":\"%s\"}",
                    nombreRutina
                );

                // Enviar el body
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                    os.flush();
                }

                // Obtener el código de respuesta
                int responseCode = connection.getResponseCode();
                String responseMessage = connection.getResponseMessage();

                // Log para depuración
                android.util.Log.d("N8N_LOGRO", "Response Code: " + responseCode);
                android.util.Log.d("N8N_LOGRO", "Response Message: " + responseMessage);
                android.util.Log.d("N8N_LOGRO", "Rutina enviada: " + nombreRutina);

                // Mostrar resultado en el UI thread
                final int finalResponseCode = responseCode;
                runOnUiThread(() -> {
                    if (finalResponseCode >= 200 && finalResponseCode < 300) {
                        Toast.makeText(DetalleRutinaActivity.this,
                            "🏆 ¡Logro enviado a Telegram!",
                            Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DetalleRutinaActivity.this,
                            "⚠️ Rutina completada, pero no se pudo notificar a Telegram",
                            Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                // Log del error
                android.util.Log.e("N8N_LOGRO", "Error al enviar logro: " + e.getMessage(), e);

                // Manejar errores en el UI thread
                runOnUiThread(() -> {
                    Toast.makeText(DetalleRutinaActivity.this,
                        "⚠️ Rutina completada localmente",
                        Toast.LENGTH_SHORT).show();
                });
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }
}
