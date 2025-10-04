package com.example.coachprueba;

public class ChatMessage {
    private String mensaje;
    private boolean esUsuario;
    private long timestamp;

    public ChatMessage(String mensaje, boolean esUsuario, long timestamp) {
        this.mensaje = mensaje;
        this.esUsuario = esUsuario;
        this.timestamp = timestamp;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isEsUsuario() {
        return esUsuario;
    }

    public void setEsUsuario(boolean esUsuario) {
        this.esUsuario = esUsuario;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
