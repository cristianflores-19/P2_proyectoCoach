package com.example.coachprueba.models;

import java.util.UUID;

public class Ejercicio {
    private String id;
    private String nombre;
    private String descripcion;
    private int series;
    private int repeticiones;
    private int duracion; // en segundos
    private String instrucciones;
    private boolean completado;

    // Constructor principal con series y repeticiones
    public Ejercicio(String nombre, String descripcion, int series, int repeticiones, String instrucciones) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.series = series;
        this.repeticiones = repeticiones;
        this.duracion = 0;
        this.instrucciones = instrucciones;
        this.completado = false;
    }

    // Constructor con duración (para ejercicios por tiempo)
    public Ejercicio(String nombre, String descripcion, int duracion, String instrucciones) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.series = 0;
        this.repeticiones = 0;
        this.duracion = duracion;
        this.instrucciones = instrucciones;
        this.completado = false;
    }

    // Constructor completo
    public Ejercicio(String nombre, String descripcion, int series, int repeticiones, int duracion, String instrucciones) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.series = series;
        this.repeticiones = repeticiones;
        this.duracion = duracion;
        this.instrucciones = instrucciones;
        this.completado = false;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getSeries() {
        return series;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public int getDuracion() {
        return duracion;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public boolean isCompletado() {
        return completado;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    // Métodos de utilidad
    public String getDetalleFormateado() {
        StringBuilder detalle = new StringBuilder();

        if (series > 0) {
            detalle.append(series).append(" series");
        }

        if (repeticiones > 0) {
            if (detalle.length() > 0) detalle.append(" x ");
            detalle.append(repeticiones).append(" repeticiones");
        }

        if (duracion > 0) {
            if (detalle.length() > 0) detalle.append(" - ");
            if (duracion >= 60) {
                int minutos = duracion / 60;
                int segundos = duracion % 60;
                if (segundos > 0) {
                    detalle.append(minutos).append("m ").append(segundos).append("s");
                } else {
                    detalle.append(minutos).append(" minutos");
                }
            } else {
                detalle.append(duracion).append(" segundos");
            }
        }

        return detalle.length() > 0 ? detalle.toString() : "Sin especificar";
    }

    public boolean esPorTiempo() {
        return duracion > 0 && series == 0 && repeticiones == 0;
    }

    public boolean esPorSeries() {
        return series > 0;
    }

    @Override
    public String toString() {
        return "Ejercicio{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", series=" + series +
                ", repeticiones=" + repeticiones +
                ", duracion=" + duracion +
                ", completado=" + completado +
                '}';
    }
}
