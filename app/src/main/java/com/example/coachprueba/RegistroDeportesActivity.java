package com.example.coachprueba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.coachprueba.models.Usuario;
import com.example.coachprueba.utils.SimpleUsuarioManager;
import com.example.coachprueba.utils.UsuarioManager;
import java.util.ArrayList;
import java.util.List;

public class RegistroDeportesActivity extends AppCompatActivity {

    private Button btnGym, btnCiclismo, btnFutbol, btnTenis, btnBasquetbol,
                   btnNatacion, btnCorrer, btnBoxeo, btnVoleibol;
    private Button btnVolver, btnFinalizar;
    private TextView txtDeportesSeleccionados;

    private final List<String> deportesSeleccionados = new ArrayList<>();
    private String nombreCompleto, sexo, contrasena, objetivo;
    private int edad;
    private double peso, altura;
    private SimpleUsuarioManager simpleUsuarioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_deportes);

        // Recibir datos del paso anterior
        obtenerDatosUsuario();

        initViews();
        simpleUsuarioManager = new SimpleUsuarioManager(this);
        setupListeners();
        updateSelectedSports();
    }

    private void obtenerDatosUsuario() {
        Intent intent = getIntent();
        nombreCompleto = intent.getStringExtra("usuario_nombre");
        edad = intent.getIntExtra("usuario_edad", 0);
        peso = intent.getDoubleExtra("usuario_peso", 0);
        altura = intent.getDoubleExtra("usuario_altura", 0);
        sexo = intent.getStringExtra("usuario_sexo");
        contrasena = intent.getStringExtra("usuario_contrasena");
        objetivo = intent.getStringExtra("usuario_objetivo");

        // Debug: Verificar que todos los datos lleguen correctamente
        android.util.Log.d("REGISTRO_DEPORTES", "=== DATOS RECIBIDOS DEL INTENT ===");
        android.util.Log.d("REGISTRO_DEPORTES", "Nombre: " + (nombreCompleto != null ? nombreCompleto : "NULL"));
        android.util.Log.d("REGISTRO_DEPORTES", "Edad: " + edad);
        android.util.Log.d("REGISTRO_DEPORTES", "Peso: " + peso);
        android.util.Log.d("REGISTRO_DEPORTES", "Altura: " + altura);
        android.util.Log.d("REGISTRO_DEPORTES", "Sexo: " + (sexo != null ? sexo : "NULL"));
        android.util.Log.d("REGISTRO_DEPORTES", "Contraseña: " + (contrasena != null ? contrasena : "NULL"));
        android.util.Log.d("REGISTRO_DEPORTES", "Objetivo: " + (objetivo != null ? objetivo : "NULL"));

        // Validar que los datos esenciales no sean nulos
        if (nombreCompleto == null || nombreCompleto.isEmpty() ||
            contrasena == null || contrasena.isEmpty() ||
            sexo == null || sexo.isEmpty() ||
            objetivo == null || objetivo.isEmpty() ||
            edad == 0 || peso == 0 || altura == 0) {

            android.util.Log.e("REGISTRO_DEPORTES", "ERROR: Datos incompletos recibidos del intent");
            Toast.makeText(this, "Error: Datos incompletos. Vuelve al paso anterior.", Toast.LENGTH_LONG).show();
            finish();
        }
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
        btnFinalizar = findViewById(R.id.btnFinalizar);
        txtDeportesSeleccionados = findViewById(R.id.txtDeportesSeleccionados);
    }

    private void setupListeners() {
        // Listeners para botones de deportes
        btnGym.setOnClickListener(v -> toggleDeporte("GYM", btnGym));
        btnCiclismo.setOnClickListener(v -> toggleDeporte("Ciclismo", btnCiclismo));
        btnFutbol.setOnClickListener(v -> toggleDeporte("Futbol", btnFutbol));
        btnTenis.setOnClickListener(v -> toggleDeporte("Tenis", btnTenis));
        btnBasquetbol.setOnClickListener(v -> toggleDeporte("Basquetbol", btnBasquetbol));
        btnNatacion.setOnClickListener(v -> toggleDeporte("Natación", btnNatacion));
        btnCorrer.setOnClickListener(v -> toggleDeporte("Correr", btnCorrer));
        btnBoxeo.setOnClickListener(v -> toggleDeporte("Boxeo", btnBoxeo));
        btnVoleibol.setOnClickListener(v -> toggleDeporte("Voleibol", btnVoleibol));

        // Listeners para botones de navegación
        btnVolver.setOnClickListener(v -> finish());

        btnFinalizar.setOnClickListener(v -> finalizarRegistro());
    }

    private void toggleDeporte(String deporte, Button boton) {
        if (deportesSeleccionados.contains(deporte)) {
            // Deseleccionar deporte
            deportesSeleccionados.remove(deporte);
            boton.setBackgroundResource(R.drawable.sport_button_unselected);
            boton.setSelected(false);
        } else {
            // Seleccionar deporte
            deportesSeleccionados.add(deporte);
            boton.setBackgroundResource(R.drawable.sport_button_selected);
            boton.setSelected(true);
        }

        updateSelectedSports();
    }

    private void updateSelectedSports() {
        if (deportesSeleccionados.isEmpty()) {
            txtDeportesSeleccionados.setText("No has seleccionado deportes");
            btnFinalizar.setEnabled(false);
        } else {
            String deportesTexto = "Deportes seleccionados: " + String.join(", ", deportesSeleccionados);
            txtDeportesSeleccionados.setText(deportesTexto);
            btnFinalizar.setEnabled(true);
        }
    }

    private void finalizarRegistro() {
        android.util.Log.d("REGISTRO_SIMPLE", "=== INICIANDO FINALIZAR REGISTRO ===");

        if (deportesSeleccionados.isEmpty()) {
            android.util.Log.d("REGISTRO_SIMPLE", "Error: No hay deportes seleccionados");
            Toast.makeText(this, "Debes seleccionar al menos un deporte", Toast.LENGTH_SHORT).show();
            return;
        }

        android.util.Log.d("REGISTRO_SIMPLE", "Deportes seleccionados: " + deportesSeleccionados.toString());

        // Crear usuario completo con deportes seleccionados
        String deportesFavoritos = String.join(",", deportesSeleccionados);

        android.util.Log.d("REGISTRO_SIMPLE", "Creando usuario con datos:");
        android.util.Log.d("REGISTRO_SIMPLE", "- Nombre: " + nombreCompleto);
        android.util.Log.d("REGISTRO_SIMPLE", "- Contraseña: " + contrasena);
        android.util.Log.d("REGISTRO_SIMPLE", "- Deportes: " + deportesFavoritos);

        Usuario usuario = new Usuario(nombreCompleto, contrasena, edad, peso, altura, sexo, objetivo, deportesFavoritos);

        // Guardar usuario usando el sistema simple
        boolean registroExitoso = simpleUsuarioManager.registrarUsuario(usuario);
        android.util.Log.d("REGISTRO_SIMPLE", "Resultado del registro: " + (registroExitoso ? "EXITOSO" : "FALLÓ"));

        if (registroExitoso) {
            android.util.Log.d("REGISTRO_SIMPLE", "Registro completado exitosamente");
            Toast.makeText(this, "¡Registro completado exitosamente! Ahora puedes iniciar sesión.", Toast.LENGTH_LONG).show();

            // Ir al login con el nombre prellenado
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("usuario_prellenado", nombreCompleto);

            startActivity(intent);
            finish();

        } else {
            android.util.Log.e("REGISTRO_SIMPLE", "Error al registrar usuario - posiblemente ya existe");
            Toast.makeText(this, "Error: Usuario ya existe o problema al registrar. Intenta con otro nombre.", Toast.LENGTH_LONG).show();
        }
    }
}
