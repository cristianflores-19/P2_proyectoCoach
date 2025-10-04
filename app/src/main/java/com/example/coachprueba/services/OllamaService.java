package com.example.coachprueba.services;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OllamaService {
    private static final String TAG = "OLLAMA_SERVICE";
    private static final String BASE_URL = "http://192.168.1.11:11434"; // IP específica para pruebas
    private static final String MODEL_NAME = "mistral";

    private OkHttpClient client;
    private Gson gson;

    public interface OllamaCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public OllamaService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.gson = new Gson();
    }

    public void enviarMensaje(String mensaje, String contextoUsuario, OllamaCallback callback) {
        Log.d(TAG, "Enviando mensaje a Ollama: " + mensaje);

        // Crear el prompt con contexto de coach deportivo
        String promptCompleto = crearPromptCoach(mensaje, contextoUsuario);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", MODEL_NAME);
        requestBody.addProperty("prompt", promptCompleto);
        requestBody.addProperty("stream", false);
        requestBody.addProperty("options", createOptionsJson());

        RequestBody body = RequestBody.create(
            requestBody.toString(),
            MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/api/generate")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Log.d(TAG, "Request URL: " + BASE_URL + "/api/generate");
        Log.d(TAG, "Request Body: " + requestBody.toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error al conectar con Ollama: " + e.getMessage());
                String errorMsg = "No se pudo conectar con el servidor de IA en " + BASE_URL +
                                ". Verifica que Ollama esté ejecutándose y que la IP sea correcta.";
                callback.onError(errorMsg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "Respuesta recibida. Código: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        Log.d(TAG, "Response body: " + responseBody);

                        JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

                        if (jsonResponse.has("response")) {
                            String respuestaIA = jsonResponse.get("response").getAsString();
                            Log.d(TAG, "Respuesta de Mistral: " + respuestaIA);
                            callback.onSuccess(respuestaIA);
                        } else {
                            Log.e(TAG, "No se encontró el campo 'response' en la respuesta");
                            callback.onError("Respuesta inválida del servidor de IA");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error al procesar respuesta: " + e.getMessage());
                        callback.onError("Error al procesar la respuesta de la IA: " + e.getMessage());
                    }
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Sin cuerpo de respuesta";
                    Log.e(TAG, "Respuesta no exitosa: " + response.code() + " - " + errorBody);
                    callback.onError("Error del servidor de IA (Código: " + response.code() + ")");
                }
            }
        });
    }

    private String crearPromptCoach(String mensajeUsuario, String contextoUsuario) {
        return "Eres ADAPTIA, un coach deportivo profesional, motivador y experto en fitness. " +
               "Tu personalidad es amigable, alentadora y siempre positiva. " +
               "Respondes en español y das consejos prácticos y fáciles de seguir. " +
               "\n\nContexto del usuario: " + contextoUsuario +
               "\n\nPregunta del usuario: " + mensajeUsuario +
               "\n\nInstrucciones:" +
               "\n- Responde como un coach personal experto" +
               "\n- Sé motivador y positivo" +
               "\n- Da consejos específicos y prácticos" +
               "\n- Mantén un tono amigable y profesional" +
               "\n- Limita tu respuesta a 150 palabras máximo" +
               "\n- Si preguntan sobre ejercicios, explica la técnica correcta" +
               "\n- Si preguntan sobre nutrición, da consejos balanceados" +
               "\n\nRespuesta:";
    }

    private String createOptionsJson() {
        JsonObject options = new JsonObject();
        options.addProperty("temperature", 0.7);
        options.addProperty("top_p", 0.9);
        options.addProperty("max_tokens", 200);
        return options.toString();
    }

    public void verificarConexion(OllamaCallback callback) {
        Log.d(TAG, "Verificando conexión con Ollama en: " + BASE_URL);

        Request request = new Request.Builder()
                .url(BASE_URL + "/api/tags")
                .get()
                .addHeader("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Fallo en verificación de conexión: " + e.getMessage());
                String errorDetallado = "No se pudo conectar con Ollama en " + BASE_URL +
                                      "\nError: " + e.getMessage() +
                                      "\n\nVerifica que:\n" +
                                      "1. Ollama esté ejecutándose: ollama serve\n" +
                                      "2. La IP 192.168.1.11 sea accesible\n" +
                                      "3. El puerto 11434 esté abierto";
                callback.onError(errorDetallado);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "Verificación de conexión - Código: " + response.code());

                if (response.isSuccessful()) {
                    String responseBody = response.body() != null ? response.body().string() : "";
                    Log.d(TAG, "Respuesta del servidor: " + responseBody);

                    // Verificar si Mistral está disponible
                    if (responseBody.contains("mistral")) {
                        Log.d(TAG, "✅ Modelo Mistral encontrado");
                        callback.onSuccess("✅ Conexión exitosa con Ollama\n🤖 Modelo Mistral disponible\n🌐 Servidor: " + BASE_URL);
                    } else {
                        Log.w(TAG, "⚠️ Mistral no encontrado en modelos disponibles");
                        callback.onSuccess("✅ Conexión exitosa con Ollama\n⚠️ Modelo Mistral no encontrado\n🌐 Servidor: " + BASE_URL +
                                         "\n\nEjecuta: ollama pull mistral");
                    }
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Sin detalles";
                    Log.e(TAG, "Error del servidor: " + response.code() + " - " + errorBody);
                    callback.onError("❌ Error del servidor Ollama\n" +
                                   "Código: " + response.code() + "\n" +
                                   "URL: " + BASE_URL + "\n" +
                                   "Detalle: " + errorBody);
                }
            }
        });
    }

    // Método específico para probar la conexión básica
    public void probarConexionBasica(OllamaCallback callback) {
        Log.d(TAG, "🔍 Probando conexión básica con " + BASE_URL);

        Request request = new Request.Builder()
                .url(BASE_URL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "❌ Conexión básica falló: " + e.getMessage());
                callback.onError("❌ No se puede alcanzar el servidor\n" +
                               "URL: " + BASE_URL + "\n" +
                               "Error: " + e.getMessage() + "\n\n" +
                               "Verifica que Ollama esté ejecutándose en esa IP");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "✅ Conexión básica exitosa - Código: " + response.code());
                callback.onSuccess("✅ Servidor Ollama responde correctamente\n" +
                                 "URL: " + BASE_URL + "\n" +
                                 "Estado: " + response.code());
            }
        });
    }
}
