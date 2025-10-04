package com.example.coachprueba.models;

public class Usuario {
    private int id;
    private String nombreCompleto;
    private String contrasena;
    private int edad;
    private double peso;
    private double altura;
    private String sexo;
    private String objetivo;
    private String deportesFavoritos;

    // Constructor completo
    public Usuario(String nombreCompleto, String contrasena, int edad, double peso,
                   double altura, String sexo, String objetivo, String deportesFavoritos) {
        this.nombreCompleto = nombreCompleto;
        this.contrasena = contrasena;
        this.edad = edad;
        this.peso = peso;
        this.altura = altura;
        this.sexo = sexo;
        this.objetivo = objetivo;
        this.deportesFavoritos = deportesFavoritos;
    }

    // Constructor con ID (para usuarios de BD)
    public Usuario(int id, String nombreCompleto, String contrasena, int edad, double peso,
                   double altura, String sexo, String objetivo, String deportesFavoritos) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.contrasena = contrasena;
        this.edad = edad;
        this.peso = peso;
        this.altura = altura;
        this.sexo = sexo;
        this.objetivo = objetivo;
        this.deportesFavoritos = deportesFavoritos;
    }

    // Getters
    public int getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getContrasena() { return contrasena; }
    public int getEdad() { return edad; }
    public double getPeso() { return peso; }
    public double getAltura() { return altura; }
    public String getSexo() { return sexo; }
    public String getObjetivo() { return objetivo; }
    public String getDeportesFavoritos() { return deportesFavoritos; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setEdad(int edad) { this.edad = edad; }
    public void setPeso(double peso) { this.peso = peso; }
    public void setAltura(double altura) { this.altura = altura; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }
    public void setDeportesFavoritos(String deportesFavoritos) { this.deportesFavoritos = deportesFavoritos; }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", edad=" + edad +
                ", peso=" + peso +
                ", altura=" + altura +
                ", sexo='" + sexo + '\'' +
                ", objetivo='" + objetivo + '\'' +
                '}';
    }
}
