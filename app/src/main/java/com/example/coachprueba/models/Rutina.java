package com.example.coachprueba.models;

import java.util.List;
import java.util.UUID;

public class Rutina {
    private String id;
    private String deporte;
    private String nombre;
    private int duracion; // en minutos
    private int calorias;
    private List<Ejercicio> ejercicios;
    private String descripcion;
    private boolean completada;
    private boolean activa; // Nueva propiedad para indicar si la rutina está activa

    // Constructor principal
    public Rutina(String deporte, String nombre, int duracion, int calorias, List<Ejercicio> ejercicios) {
        this.id = UUID.randomUUID().toString();
        this.deporte = deporte;
        this.nombre = nombre;
        this.duracion = duracion;
        this.calorias = calorias;
        this.ejercicios = ejercicios;
        this.descripcion = "Rutina de " + nombre.toLowerCase() + " para " + deporte.toLowerCase();
        this.completada = false;
        this.activa = false; // Por defecto no está activa
    }

    // Constructor con descripción personalizada
    public Rutina(String deporte, String nombre, int duracion, int calorias, List<Ejercicio> ejercicios, String descripcion) {
        this.id = UUID.randomUUID().toString();
        this.deporte = deporte;
        this.nombre = nombre;
        this.duracion = duracion;
        this.calorias = calorias;
        this.ejercicios = ejercicios;
        this.descripcion = descripcion;
        this.completada = false;
        this.activa = false; // Por defecto no está activa
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getDeporte() {
        return deporte;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDuracion() {
        return duracion;
    }

    public int getCalorias() {
        return calorias;
    }

    public List<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isCompletada() {
        return completada;
    }

    public boolean isActiva() {
        return activa;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public void setEjercicios(List<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    // Método de compatibilidad para el adapter
    public int getCaloriasEstimadas() {
        return calorias;
    }

    // Métodos adicionales de utilidad
    public int getTotalEjercicios() {
        return ejercicios != null ? ejercicios.size() : 0;
    }

    public int getEjerciciosCompletados() {
        if (ejercicios == null) return 0;
        int count = 0;
        for (Ejercicio ejercicio : ejercicios) {
            if (ejercicio.isCompletado()) {
                count++;
            }
        }
        return count;
    }

    public double getProgresoCompletado() {
        if (ejercicios == null || ejercicios.isEmpty()) return 0.0;
        return (double) getEjerciciosCompletados() / getTotalEjercicios() * 100;
    }

    public boolean estaCompleta() {
        return completada || (ejercicios != null && getEjerciciosCompletados() == getTotalEjercicios());
    }

    @Override
    public String toString() {
        return "Rutina{" +
                "id='" + id + '\'' +
                ", deporte='" + deporte + '\'' +
                ", nombre='" + nombre + '\'' +
                ", duracion=" + duracion +
                ", calorias=" + calorias +
                ", completada=" + completada +
                ", ejercicios=" + (ejercicios != null ? ejercicios.size() : 0) +
                '}';
    }
}
