package com.example.coachprueba;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.coachprueba.services.OllamaService;
import java.util.ArrayList;
import java.util.List;

public class CoachOnlineActivity extends AppCompatActivity {
    private static final String TAG = "COACH_ONLINE";

    private RecyclerView recyclerViewChat;
    private TextInputEditText editTextMensaje;
    private FloatingActionButton buttonEnviar;
    private TextView txtEstadoConexion;
    private ImageView btnVolverChat;

    private ChatAdapter chatAdapter;
    private List<ChatMessage> mensajes;
    private OllamaService ollamaService;
    private Handler mainHandler;
    private String contextoUsuario;
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_online);

        inicializarVistas();
        configurarChat();
        configurarOllama();
        mostrarMensajeBienvenida();
    }

    private void inicializarVistas() {
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMensaje = findViewById(R.id.editTextMensaje);
        buttonEnviar = findViewById(R.id.buttonEnviar);
        txtEstadoConexion = findViewById(R.id.txtEstadoConexion);
        btnVolverChat = findViewById(R.id.btnVolverChat);

        mainHandler = new Handler(Looper.getMainLooper());

        // Configurar botón volver
        btnVolverChat.setOnClickListener(v -> finish());
    }

    private void configurarChat() {
        mensajes = new ArrayList<>();
        chatAdapter = new ChatAdapter(mensajes);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerViewChat.setLayoutManager(layoutManager);
        recyclerViewChat.setAdapter(chatAdapter);

        buttonEnviar.setOnClickListener(v -> enviarMensaje());

        // Permitir enviar con Enter
        editTextMensaje.setOnEditorActionListener((v, actionId, event) -> {
            enviarMensaje();
            return true;
        });

        // Obtener contexto del usuario
        contextoUsuario = obtenerContextoUsuario();
        Log.d(TAG, "Contexto del usuario: " + contextoUsuario);
    }

    private void configurarOllama() {
        ollamaService = new OllamaService();

        // Actualizar estado de conexión
        txtEstadoConexion.setText("Verificando conexión...");

        // Verificar conexión con Ollama
        ollamaService.verificarConexion(new OllamaService.OllamaCallback() {
            @Override
            public void onSuccess(String response) {
                mainHandler.post(() -> {
                    Log.d(TAG, "Ollama conectado: " + response);
                    isConnected = true;
                    txtEstadoConexion.setText("🟢 En línea");
                    txtEstadoConexion.setTextColor(getResources().getColor(R.color.success_color));
                    buttonEnviar.setEnabled(true);
                });
            }

            @Override
            public void onError(String error) {
                mainHandler.post(() -> {
                    Log.e(TAG, "Error de conexión con Ollama: " + error);
                    isConnected = false;
                    txtEstadoConexion.setText("🔴 Sin conexión");
                    txtEstadoConexion.setTextColor(getResources().getColor(R.color.error_color));
                    buttonEnviar.setEnabled(false);

                    Toast.makeText(CoachOnlineActivity.this,
                        "No se pudo conectar con la IA. Verifica que Ollama esté ejecutándose en 192.168.1.11:11434",
                        Toast.LENGTH_LONG).show();

                    // Mostrar mensaje de error en el chat
                    String mensajeError = "❌ No se pudo conectar con el servidor de IA.\n\n" +
                                        "Para solucionarlo:\n" +
                                        "1. Asegúrate de que Ollama esté instalado\n" +
                                        "2. Ejecuta: ollama serve\n" +
                                        "3. Descarga Mistral: ollama pull mistral\n" +
                                        "4. Verifica que la IP 192.168.1.11 sea correcta\n\n" +
                                        "Error: " + error;
                    agregarMensaje(mensajeError, false);
                });
            }
        });
    }

    private void mostrarMensajeBienvenida() {
        String mensajeBienvenida = "¡Hola! 👋 Soy tu Coach ADAPTIA personal.\n\n" +
                                 "Estoy aquí para ayudarte con:\n" +
                                 "🏋️ Rutinas de ejercicio\n" +
                                 "🥗 Consejos de nutrición\n" +
                                 "💪 Motivación y objetivos\n" +
                                 "📈 Seguimiento de progreso\n\n" +
                                 "¿En qué puedo ayudarte hoy?";
        agregarMensaje(mensajeBienvenida, false);
    }

    private void enviarMensaje() {
        String mensaje = editTextMensaje.getText().toString().trim();
        if (mensaje.isEmpty()) {
            return;
        }

        if (!isConnected) {
            Toast.makeText(this, "No hay conexión con la IA. Verifica la configuración.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Agregar mensaje del usuario
        agregarMensaje(mensaje, true);
        editTextMensaje.setText("");

        // Deshabilitar botón mientras se procesa
        buttonEnviar.setEnabled(false);
        txtEstadoConexion.setText("🤔 Pensando...");

        // Mostrar indicador de "escribiendo..."
        agregarMensaje("Coach ADAPTIA está escribiendo... 💭", false);

        Log.d(TAG, "Enviando mensaje a Ollama: " + mensaje);

        // Enviar a Ollama
        ollamaService.enviarMensaje(mensaje, contextoUsuario, new OllamaService.OllamaCallback() {
            @Override
            public void onSuccess(String response) {
                mainHandler.post(() -> {
                    Log.d(TAG, "Respuesta recibida de Mistral: " + response);

                    // Remover el mensaje de "escribiendo..."
                    removerUltimoMensaje();

                    // Agregar respuesta de la IA
                    agregarMensaje(response, false);

                    // Restaurar estado
                    buttonEnviar.setEnabled(true);
                    txtEstadoConexion.setText("🟢 En línea");
                });
            }

            @Override
            public void onError(String error) {
                mainHandler.post(() -> {
                    Log.e(TAG, "Error al obtener respuesta: " + error);

                    // Remover el mensaje de "escribiendo..."
                    removerUltimoMensaje();

                    // Mostrar mensaje de error amigable
                    String mensajeError = "😔 Lo siento, tuve un problema para procesar tu mensaje.\n\n" +
                                        "Puedes intentar:\n" +
                                        "• Reformular tu pregunta\n" +
                                        "• Verificar la conexión de red\n" +
                                        "• Reiniciar Ollama\n\n" +
                                        "Error técnico: " + error;
                    agregarMensaje(mensajeError, false);

                    // Restaurar estado
                    buttonEnviar.setEnabled(true);
                    txtEstadoConexion.setText("🟢 En línea");
                });
            }
        });
    }

    private void agregarMensaje(String mensaje, boolean esUsuario) {
        ChatMessage chatMessage = new ChatMessage(mensaje, esUsuario, System.currentTimeMillis());
        mensajes.add(chatMessage);
        chatAdapter.notifyItemInserted(mensajes.size() - 1);
        recyclerViewChat.scrollToPosition(mensajes.size() - 1);
    }

    private void removerUltimoMensaje() {
        if (!mensajes.isEmpty()) {
            int lastIndex = mensajes.size() - 1;
            mensajes.remove(lastIndex);
            chatAdapter.notifyItemRemoved(lastIndex);
        }
    }

    private String obtenerContextoUsuario() {
        SharedPreferences prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE);

        String nombreUsuario = prefs.getString("usuario_nombre", "Usuario");
        String objetivo = prefs.getString("usuario_objetivo", "Mejorar condición física");
        String deportesFavoritos = prefs.getString("deportes_favoritos", "Ejercicio general");

        return "Nombre: " + nombreUsuario +
               ". Objetivo principal: " + objetivo +
               ". Deportes de interés: " + deportesFavoritos +
               ". Nivel: Principiante a intermedio.";
    }
}
