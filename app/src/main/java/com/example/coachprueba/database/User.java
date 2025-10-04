package com.example.coachprueba.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombreCompleto;
    public int edad;
    public float peso;
    public float altura;
    public String sexo; // "Masculino" o "Femenino"
    public String contraseña;
    public String objetivo; // "Perder peso", "Mejorar resistencia", "Ganar músculo", "Mantenerme activo"
    public String deportesSeleccionados; // JSON string con deportes seleccionados
    public String rutinaActiva; // Deporte con rutina activa actualmente

    public User() {}

    public User(String nombreCompleto, int edad, float peso, float altura, String sexo,
                String contraseña, String objetivo, String deportesSeleccionados) {
        this.nombreCompleto = nombreCompleto;
        this.edad = edad;
        this.peso = peso;
        this.altura = altura;
        this.sexo = sexo;
        this.contraseña = contraseña;
        this.objetivo = objetivo;
        this.deportesSeleccionados = deportesSeleccionados;
        this.rutinaActiva = "";
    }
}
