package com.example.coachprueba;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    private RecyclerView recyclerChat;
    private EditText etMensaje;
    private ImageButton btnEnviar;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> mensajes;
    private OkHttpClient client;
    private static final String OLLAMA_URL = "http://10.0.2.2:11434/api/generate";
    private static final String MODELO = "llama3.2:1b";  // Modelo más pequeño que usa menos de 2GB
    private static final String MODELO_RESPALDO = "gemma2:2b"; // Modelo más pequeño como respaldo
    private boolean usandoModeloRespaldo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_chat);
            inicializarVistas();
            configurarRecyclerView();
            configurarCliente();
            configurarListeners();

            // Mensaje de bienvenida del coach IA
            agregarMensajeIA("¡Hola! Soy tu coach personal de fitness. ¿En qué puedo ayudarte hoy? Puedo darte consejos sobre entrenamiento, nutrición, rutinas personalizadas y mucho más.");
        } catch (Exception e) {
            Log.e(TAG, "Error en onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error al inicializar el chat", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void inicializarVistas() {
        try {
            recyclerChat = findViewById(R.id.recyclerChat);
            etMensaje = findViewById(R.id.etMensaje);
            btnEnviar = findViewById(R.id.btnEnviar);

            if (recyclerChat == null || etMensaje == null || btnEnviar == null) {
                throw new RuntimeException("No se pudieron encontrar las vistas necesarias");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error inicializando vistas: " + e.getMessage(), e);
            throw e;
        }
    }

    private void configurarRecyclerView() {
        try {
            mensajes = new ArrayList<>();
            chatAdapter = new ChatAdapter(mensajes);
            recyclerChat.setLayoutManager(new LinearLayoutManager(this));
            recyclerChat.setAdapter(chatAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error configurando RecyclerView: " + e.getMessage(), e);
            throw e;
        }
    }

    private void configurarCliente() {
        try {
            client = new OkHttpClient.Builder()
                    .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .build();
        } catch (Exception e) {
            Log.e(TAG, "Error configurando cliente HTTP: " + e.getMessage(), e);
            throw e;
        }
    }

    private void configurarListeners() {
        try {
            btnEnviar.setOnClickListener(v -> {
                try {
                    enviarMensaje();
                } catch (Exception e) {
                    Log.e(TAG, "Error al enviar mensaje: " + e.getMessage(), e);
                    mostrarError("Error al enviar mensaje");
                }
            });

            etMensaje.setOnEditorActionListener((v, actionId, event) -> {
                try {
                    if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                        enviarMensaje();
                        return true;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error en editor action: " + e.getMessage(), e);
                    mostrarError("Error al procesar mensaje");
                }
                return false;
            });
        } catch (Exception e) {
            Log.e(TAG, "Error configurando listeners: " + e.getMessage(), e);
            throw e;
        }
    }

    private void enviarMensaje() {
        try {
            String mensaje = etMensaje.getText().toString().trim();
            if (mensaje.isEmpty()) {
                return;
            }

            // Agregar mensaje del usuario
            agregarMensajeUsuario(mensaje);
            etMensaje.setText("");

            // Enviar mensaje a Ollama (solo si hay conexión)
            enviarMensajeAOllama(mensaje);

        } catch (Exception e) {
            Log.e(TAG, "Error en enviarMensaje: " + e.getMessage(), e);
            mostrarError("Error al procesar el mensaje");
            btnEnviar.setEnabled(true);
        }
    }

    private void agregarMensajeUsuario(String mensaje) {
        try {
            ChatMessage chatMessage = new ChatMessage(mensaje, true, System.currentTimeMillis());
            chatAdapter.agregarMensaje(chatMessage);
            scrollToBottom();
        } catch (Exception e) {
            Log.e(TAG, "Error agregando mensaje usuario: " + e.getMessage(), e);
        }
    }

    private void agregarMensajeIA(String mensaje) {
        try {
            ChatMessage chatMessage = new ChatMessage(mensaje, false, System.currentTimeMillis());
            chatAdapter.agregarMensaje(chatMessage);
            scrollToBottom();
        } catch (Exception e) {
            Log.e(TAG, "Error agregando mensaje IA: " + e.getMessage(), e);
        }
    }

    private void scrollToBottom() {
        try {
            if (chatAdapter.getItemCount() > 0) {
                recyclerChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error haciendo scroll: " + e.getMessage(), e);
        }
    }

    private void enviarMensajeAOllama(String mensaje) {
        btnEnviar.setEnabled(false);

        try {
            // Crear el prompt personalizado para un coach de fitness
            String promptPersonalizado = crearPromptCoach(mensaje);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", MODELO);  // Usar directamente llama3.2:1b
            jsonBody.put("prompt", promptPersonalizado);
            jsonBody.put("stream", false);

            // Configuración más simple y compatible
            JSONObject options = new JSONObject();
            options.put("temperature", 0.7);
            options.put("num_predict", 200);  // Reducir para evitar problemas de memoria
            jsonBody.put("options", options);

            Log.d(TAG, "Enviando a Ollama: " + jsonBody.toString());

            RequestBody body = RequestBody.create(
                    jsonBody.toString(),
                    MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(OLLAMA_URL)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Error de conexión con Ollama: " + e.getMessage(), e);
                    runOnUiThread(() -> {
                        if (e.getMessage().contains("Connection refused") || e.getMessage().contains("timeout")) {
                            mostrarError("No se pudo conectar con Ollama. Verifica que esté ejecutándose con OLLAMA_HOST=0.0.0.0:11434");
                        } else {
                            mostrarError("Error de conexión: " + e.getMessage());
                        }
                        btnEnviar.setEnabled(true);
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(() -> btnEnviar.setEnabled(true));

                    String responseData = null;
                    try {
                        responseData = response.body().string();
                        Log.d(TAG, "Código de respuesta: " + response.code());
                        Log.d(TAG, "Respuesta completa de Ollama: " + responseData);

                        if (!response.isSuccessful()) {
                            Log.e(TAG, "Respuesta no exitosa: " + response.code() + " - " + response.message());

                            runOnUiThread(() -> {
                                if (response.code() == 500) {
                                    // Error 500 - intentar con configuración más simple
                                    mostrarError("Error 500. Modelo llama3.2:1b no responde. Intentando solución alternativa...");
                                    // Intentar con un prompt más simple
                                    enviarMensajeSimple(mensaje);
                                } else if (response.code() == 404) {
                                    mostrarError("Modelo 'llama3.2:1b' no encontrado. Ejecuta: ollama pull llama3.2:1b");
                                } else {
                                    mostrarError("Error del servidor IA: " + response.code() + " - " + response.message());
                                }
                            });
                            return;
                        }

                        // Verificar si la respuesta contiene datos válidos
                        if (responseData == null || responseData.trim().isEmpty()) {
                            runOnUiThread(() -> {
                                mostrarError("Respuesta vacía del servidor");
                            });
                            return;
                        }

                        JSONObject jsonResponse = new JSONObject(responseData);

                        // Verificar si hay un error en la respuesta JSON
                        if (jsonResponse.has("error")) {
                            String errorMsg = jsonResponse.getString("error");
                            Log.e(TAG, "Error en respuesta JSON: " + errorMsg);
                            runOnUiThread(() -> {
                                mostrarError("Error de IA: " + errorMsg);
                            });
                            return;
                        }

                        String respuestaIA = jsonResponse.optString("response", "");

                        if (respuestaIA.trim().isEmpty()) {
                            respuestaIA = "Lo siento, no pude generar una respuesta. Intenta reformular tu pregunta.";
                        }

                        final String respuestaFinal = respuestaIA;
                        runOnUiThread(() -> {
                            agregarMensajeIA(respuestaFinal);
                        });

                    } catch (JSONException e) {
                        Log.e(TAG, "Error procesando JSON: " + e.getMessage(), e);
                        if (responseData != null) {
                            Log.e(TAG, "Respuesta que causó error: " + responseData);
                        }
                        runOnUiThread(() -> {
                            mostrarError("Error al interpretar la respuesta de la IA");
                        });
                    } catch (IOException e) {
                        Log.e(TAG, "Error leyendo respuesta: " + e.getMessage(), e);
                        runOnUiThread(() -> {
                            mostrarError("Error al leer la respuesta del servidor");
                        });
                    }
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "Error creando JSON: " + e.getMessage(), e);
            mostrarError("Error al crear la consulta");
            btnEnviar.setEnabled(true);
        } catch (Exception e) {
            Log.e(TAG, "Error general en enviarMensajeAOllama: " + e.getMessage(), e);
            mostrarError("Error inesperado: " + e.getMessage());
            btnEnviar.setEnabled(true);
        }
    }

    // Método alternativo con configuración más simple para casos de error 500
    private void enviarMensajeSimple(String mensaje) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", MODELO);  // Usar el modelo configurado (llama3.2:1b)
            jsonBody.put("prompt", mensaje);  // Prompt directo sin personalización
            jsonBody.put("stream", false);

            Log.d(TAG, "Enviando mensaje simple a Ollama: " + jsonBody.toString());

            RequestBody body = RequestBody.create(
                    jsonBody.toString(),
                    MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(OLLAMA_URL)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Error en mensaje simple: " + e.getMessage(), e);
                    runOnUiThread(() -> {
                        mostrarError("Error de conexión incluso con configuración simple");
                        btnEnviar.setEnabled(true);
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d(TAG, "Respuesta simple: " + responseData);

                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonResponse = new JSONObject(responseData);
                            String respuestaIA = jsonResponse.optString("response", "Respuesta recibida pero sin contenido");

                            runOnUiThread(() -> {
                                agregarMensajeIA("✅ Conexión exitosa: " + respuestaIA);
                                btnEnviar.setEnabled(true);
                            });
                        } catch (JSONException e) {
                            runOnUiThread(() -> {
                                mostrarError("Error procesando respuesta simple");
                                btnEnviar.setEnabled(true);
                            });
                        }
                    } else {
                        runOnUiThread(() -> {
                            mostrarError("Error " + response.code() + " incluso con configuración simple");
                            btnEnviar.setEnabled(true);
                        });
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error en enviarMensajeSimple: " + e.getMessage(), e);
            mostrarError("Error creando mensaje simple");
            btnEnviar.setEnabled(true);
        }
    }

    private String crearPromptCoach(String mensajeUsuario) {
        return "Eres un coach personal de fitness experimentado y motivador. Tu nombre es CoachIA. " +
               "Respondes de manera amigable, profesional y siempre con consejos prácticos. " +
               "Puedes ayudar con: rutinas de ejercicio, nutrición, motivación, técnicas de entrenamiento, " +
               "recuperación, y objetivos fitness. Mantén tus respuestas concisas pero informativas. " +
               "Si no tienes información específica sobre algo, recomienda consultar a un profesional. " +
               "Aquí está la pregunta del usuario: " + mensajeUsuario;
    }

    private void mostrarError(String mensaje) {
        try {
            Log.w(TAG, "Mostrando error: " + mensaje);
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
            // También agregar como mensaje del sistema en el chat
            agregarMensajeIA("❌ " + mensaje);
        } catch (Exception e) {
            Log.e(TAG, "Error mostrando mensaje de error: " + e.getMessage(), e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            if (client != null) {
                client.dispatcher().executorService().shutdown();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error en onDestroy: " + e.getMessage(), e);
        }
    }
}
