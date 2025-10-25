package com.example.coachprueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MenuPrincipalActivity extends AppCompatActivity {

    private TextView txtSaludo, txtRutinaActiva, txtTiempo, txtCalorias, txtEjercicios;
    private LinearLayout navCoach, navProgreso, navDeportes;
    private Button btnEnviarRecordatorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        initViews();
        setupBottomNavigation();
        loadUserData();
        setupRecordatorioButton();
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
        btnEnviarRecordatorio = findViewById(R.id.btnEnviarRecordatorio);
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

    private void setupRecordatorioButton() {
        btnEnviarRecordatorio.setOnClickListener(v -> {
            enviarRecordatorioN8N();
        });
    }

    private void enviarRecordatorioN8N() {
        // Ejecutar en un hilo en background
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                // URL del webhook de n8n
                URL url = new URL("https://cristianproyectop2.app.n8n.cloud/webhook-test/recordatorio-fitness");
                connection = (HttpURLConnection) url.openConnection();

                // Configurar la conexión
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setConnectTimeout(15000); // 15 segundos timeout
                connection.setReadTimeout(15000);

                // Crear el JSON body
                String jsonBody = "{\"app\":\"ADAPTIA\",\"accion\":\"recordatorio\",\"usuario\":\"desde_android\"}";

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
                android.util.Log.d("N8N_WEBHOOK", "Response Code: " + responseCode);
                android.util.Log.d("N8N_WEBHOOK", "Response Message: " + responseMessage);
                android.util.Log.d("N8N_WEBHOOK", "URL: " + url.toString());

                // Mostrar resultado en el UI thread
                final int finalResponseCode = responseCode;
                final String finalMessage = responseMessage;
                runOnUiThread(() -> {
                    if (finalResponseCode >= 200 && finalResponseCode < 300) {
                        Toast.makeText(MenuPrincipalActivity.this,
                            "✅ Recordatorio enviado exitosamente",
                            Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MenuPrincipalActivity.this,
                            "❌ Error " + finalResponseCode + ": " + finalMessage,
                            Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                // Log del error
                android.util.Log.e("N8N_WEBHOOK", "Error: " + e.getMessage(), e);

                // Manejar errores en el UI thread
                runOnUiThread(() -> {
                    Toast.makeText(MenuPrincipalActivity.this,
                        "❌ Error de conexión: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
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
