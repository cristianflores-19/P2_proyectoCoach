package com.example.coachprueba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachprueba.models.Usuario;
import com.example.coachprueba.utils.UsuarioManager;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombreCompleto, etEdad, etPeso, etAltura, etContrasena;
    private RadioGroup rgSexo, rgObjetivo;
    private Button btnContinuar;
    private TextView txtVolver;
    private UsuarioManager usuarioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        initViews();
        usuarioManager = new UsuarioManager(this);
        setupClickListeners();
    }

    private void initViews() {
        etNombreCompleto = findViewById(R.id.etNombreCompleto);
        etEdad = findViewById(R.id.etEdad);
        etPeso = findViewById(R.id.etPeso);
        etAltura = findViewById(R.id.etAltura);
        etContrasena = findViewById(R.id.etContrasena);
        rgSexo = findViewById(R.id.rgSexo);
        rgObjetivo = findViewById(R.id.rgObjetivo);
        btnContinuar = findViewById(R.id.btnContinuar);
        txtVolver = findViewById(R.id.txtVolver);
    }

    private void setupClickListeners() {
        btnContinuar.setOnClickListener(v -> continuarRegistro());

        txtVolver.setOnClickListener(v -> {
            finish(); // Volver al login
        });
    }

    private void continuarRegistro() {
        // Validar todos los campos
        if (!validarCampos()) {
            return;
        }

        // Obtener datos del formulario
        String nombreCompleto = etNombreCompleto.getText().toString().trim();
        String contrasena = etContrasena.getText().toString().trim();
        int edad = Integer.parseInt(etEdad.getText().toString().trim());
        double peso = Double.parseDouble(etPeso.getText().toString().trim());
        double altura = Double.parseDouble(etAltura.getText().toString().trim());

        String sexo = obtenerSexoSeleccionado();
        String objetivo = obtenerObjetivoSeleccionado();

        // Crear usuario con deportes vacíos (se seleccionarán en la siguiente pantalla)
        Usuario usuario = new Usuario(nombreCompleto, contrasena, edad, peso, altura, sexo, objetivo, "");

        // Verificar si el usuario ya existe
        if (usuarioManager.existeUsuario(nombreCompleto)) {
            Toast.makeText(this, "Ya existe un usuario con ese nombre. Intenta con otro nombre.", Toast.LENGTH_LONG).show();
            etNombreCompleto.requestFocus();
            return;
        }

        // Continuar a la selección de deportes
        Intent intent = new Intent(this, RegistroDeportesActivity.class);
        intent.putExtra("usuario_nombre", nombreCompleto);
        intent.putExtra("usuario_contrasena", contrasena);
        intent.putExtra("usuario_edad", edad);
        intent.putExtra("usuario_peso", peso);
        intent.putExtra("usuario_altura", altura);
        intent.putExtra("usuario_sexo", sexo);
        intent.putExtra("usuario_objetivo", objetivo);
        startActivity(intent);
    }

    private boolean validarCampos() {
        // Validar nombre completo
        String nombre = etNombreCompleto.getText().toString().trim();
        if (nombre.isEmpty()) {
            etNombreCompleto.setError("El nombre completo es obligatorio");
            etNombreCompleto.requestFocus();
            return false;
        }

        // Validar edad
        String edadStr = etEdad.getText().toString().trim();
        if (edadStr.isEmpty()) {
            etEdad.setError("La edad es obligatoria");
            etEdad.requestFocus();
            return false;
        }

        try {
            int edad = Integer.parseInt(edadStr);
            if (edad < 12 || edad > 100) {
                etEdad.setError("La edad debe estar entre 12 y 100 años");
                etEdad.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etEdad.setError("Ingresa una edad válida");
            etEdad.requestFocus();
            return false;
        }

        // Validar peso
        String pesoStr = etPeso.getText().toString().trim();
        if (pesoStr.isEmpty()) {
            etPeso.setError("El peso es obligatorio");
            etPeso.requestFocus();
            return false;
        }

        try {
            double peso = Double.parseDouble(pesoStr);
            if (peso < 20 || peso > 300) {
                etPeso.setError("El peso debe estar entre 20 y 300 kg");
                etPeso.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etPeso.setError("Ingresa un peso válido");
            etPeso.requestFocus();
            return false;
        }

        // Validar altura
        String alturaStr = etAltura.getText().toString().trim();
        if (alturaStr.isEmpty()) {
            etAltura.setError("La altura es obligatoria");
            etAltura.requestFocus();
            return false;
        }

        try {
            double altura = Double.parseDouble(alturaStr);
            if (altura < 100 || altura > 250) {
                etAltura.setError("La altura debe estar entre 100 y 250 cm");
                etAltura.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etAltura.setError("Ingresa una altura válida");
            etAltura.requestFocus();
            return false;
        }

        // Validar contraseña
        String contrasena = etContrasena.getText().toString().trim();
        if (contrasena.isEmpty()) {
            etContrasena.setError("La contraseña es obligatoria");
            etContrasena.requestFocus();
            return false;
        }

        if (contrasena.length() < 4) {
            etContrasena.setError("La contraseña debe tener al menos 4 caracteres");
            etContrasena.requestFocus();
            return false;
        }

        // Validar sexo
        if (rgSexo.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecciona tu sexo", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar objetivo
        if (rgObjetivo.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecciona tu objetivo", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private String obtenerSexoSeleccionado() {
        int selectedId = rgSexo.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton.getText().toString();
    }

    private String obtenerObjetivoSeleccionado() {
        int selectedId = rgObjetivo.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton.getText().toString();
    }
}
