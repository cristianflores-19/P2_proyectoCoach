package com.example.coachprueba.utils;

import com.example.coachprueba.models.Ejercicio;
import com.example.coachprueba.models.Rutina;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RutinaManager {
    private static Map<String, List<Rutina>> rutinasDeportes = new HashMap<>();

    static {
        inicializarRutinas();
    }

    private static void inicializarRutinas() {
        // RUTINAS DE GYM
        List<Rutina> rutinasGym = new ArrayList<>();

        // Rutina Pecho y Hombros
        List<Ejercicio> ejerciciosPechoHombros = new ArrayList<>();
        ejerciciosPechoHombros.add(new Ejercicio("Press inclinado", "4 series x 10-12 reps", 15,
            "En banco inclinado a 30°, empuja la barra desde el pecho superior. Enfócate en la parte alta del pecho."));
        ejerciciosPechoHombros.add(new Ejercicio("Elevaciones laterales", "3 series x 12-15 reps", 10,
            "Con mancuernas, levanta los brazos a los lados hasta la altura de los hombros. Controla el descenso."));
        ejerciciosPechoHombros.add(new Ejercicio("Fondos", "3 series x 8-12 reps", 8,
            "En paralelas o banco, baja el cuerpo flexionando los brazos y empuja hacia arriba. Mantén el torso ligeramente inclinado."));
        rutinasGym.add(new Rutina("GYM", "Pecho y Hombros", 50, 300, ejerciciosPechoHombros));

        // Rutina Espalda y Bíceps
        List<Ejercicio> ejerciciosEspaldaBiceps = new ArrayList<>();
        ejerciciosEspaldaBiceps.add(new Ejercicio("Dominadas", "4 series x 6-10 reps", 12,
            "Cuelga de la barra con agarre pronado, pull hacia arriba hasta que el mentón pase la barra."));
        ejerciciosEspaldaBiceps.add(new Ejercicio("Remo con barra", "4 series x 8-10 reps", 12,
            "Inclínate hacia adelante, mantén la espalda recta y tira la barra hacia el abdomen."));
        ejerciciosEspaldaBiceps.add(new Ejercicio("Curl de bíceps", "3 series x 10-12 reps", 10,
            "Con mancuernas, flexiona los brazos llevando el peso hacia los hombros. Controla el movimiento."));
        rutinasGym.add(new Rutina("GYM", "Espalda y Bíceps", 45, 280, ejerciciosEspaldaBiceps));

        // Rutina Piernas
        List<Ejercicio> ejerciciosPiernas = new ArrayList<>();
        ejerciciosPiernas.add(new Ejercicio("Sentadillas", "4 series x 12-15 reps", 15,
            "Baja como si te fueras a sentar, mantén la espalda recta y empuja con los talones."));
        ejerciciosPiernas.add(new Ejercicio("Peso muerto", "4 series x 8-10 reps", 12,
            "Levanta la barra desde el suelo manteniendo la espalda recta, empuja con las caderas."));
        ejerciciosPiernas.add(new Ejercicio("Zancadas", "3 series x 10 por pierna", 10,
            "Da un paso largo hacia adelante y baja hasta formar 90° en ambas rodillas."));
        rutinasGym.add(new Rutina("GYM", "Piernas", 55, 350, ejerciciosPiernas));

        rutinasDeportes.put("GYM", rutinasGym);

        // RUTINAS DE CICLISMO
        List<Rutina> rutinasCiclismo = new ArrayList<>();

        List<Ejercicio> ejerciciosResistencia = new ArrayList<>();
        ejerciciosResistencia.add(new Ejercicio("Calentamiento", "Pedaleo suave", 10,
            "Pedalea a ritmo cómodo para preparar músculos y articulaciones."));
        ejerciciosResistencia.add(new Ejercicio("Intervalos de resistencia", "5 intervalos x 3 min", 20,
            "Pedalea a ritmo moderado-alto por 3 minutos, descansa 1 minuto entre intervalos."));
        ejerciciosResistencia.add(new Ejercicio("Enfriamiento", "Pedaleo muy suave", 10,
            "Pedalea muy suave para bajar pulsaciones gradualmente."));
        rutinasCiclismo.add(new Rutina("Ciclismo", "Resistencia", 40, 400, ejerciciosResistencia));

        List<Ejercicio> ejerciciosVelocidad = new ArrayList<>();
        ejerciciosVelocidad.add(new Ejercicio("Calentamiento", "Pedaleo progresivo", 15,
            "Empieza suave y aumenta intensidad gradualmente."));
        ejerciciosVelocidad.add(new Ejercicio("Sprints", "8 sprints x 30 seg", 15,
            "Pedalea al máximo por 30 segundos, descansa 90 segundos entre sprints."));
        ejerciciosVelocidad.add(new Ejercicio("Recuperación activa", "Pedaleo suave", 10,
            "Mantén un pedaleo suave para recuperar."));
        rutinasCiclismo.add(new Rutina("Ciclismo", "Velocidad", 40, 380, ejerciciosVelocidad));

        rutinasDeportes.put("Ciclismo", rutinasCiclismo);

        // RUTINAS DE FÚTBOL
        List<Rutina> rutinasFutbol = new ArrayList<>();

        List<Ejercicio> ejerciciosTecnicoTactico = new ArrayList<>();
        ejerciciosTecnicoTactico.add(new Ejercicio("Calentamiento con balón", "Trote y estiramientos", 15,
            "Trote suave con toques de balón y estiramientos dinámicos."));
        ejerciciosTecnicoTactico.add(new Ejercicio("Pases precisos", "Ejercicios de precisión", 20,
            "Practica pases cortos y largos con ambos pies, enfócate en la precisión."));
        ejerciciosTecnicoTactico.add(new Ejercicio("Tiros a portería", "Definición desde diferentes ángulos", 15,
            "Practica tiros desde distintas posiciones, trabajando precisión y potencia."));
        ejerciciosTecnicoTactico.add(new Ejercicio("Juego reducido", "3v3 o 4v4", 20,
            "Partido en espacio reducido para mejorar toma de decisiones."));
        rutinasFutbol.add(new Rutina("Futbol", "Técnico-Táctico", 70, 450, ejerciciosTecnicoTactico));

        List<Ejercicio> ejerciciosFisico = new ArrayList<>();
        ejerciciosFisico.add(new Ejercicio("Calentamiento", "Movilidad articular", 10,
            "Ejercicios de movilidad para preparar el cuerpo."));
        ejerciciosFisico.add(new Ejercicio("Velocidad", "Carreras de 20-30m", 15,
            "Sprints cortos con máxima intensidad, recuperación completa entre series."));
        ejerciciosFisico.add(new Ejercicio("Agilidad", "Escalera y conos", 15,
            "Ejercicios de coordinación y cambios de dirección rápidos."));
        ejerciciosFisico.add(new Ejercicio("Resistencia", "Intervalos aeróbicos", 15,
            "Carreras a ritmo medio-alto con descansos activos."));
        rutinasFutbol.add(new Rutina("Futbol", "Preparación Física", 55, 380, ejerciciosFisico));

        rutinasDeportes.put("Futbol", rutinasFutbol);

        // RUTINAS DE TENIS
        List<Rutina> rutinasTenis = new ArrayList<>();

        List<Ejercicio> ejerciciosTecnicaTenis = new ArrayList<>();
        ejerciciosTecnicaTenis.add(new Ejercicio("Calentamiento", "Movilidad de hombros", 10,
            "Ejercicios específicos para hombros, muñecas y cadera."));
        ejerciciosTecnicaTenis.add(new Ejercicio("Golpes básicos", "Drive y revés", 25,
            "Practica golpes de fondo con técnica correcta, enfócate en la consistencia."));
        ejerciciosTecnicaTenis.add(new Ejercicio("Servicio", "Técnica de saque", 20,
            "Practica el movimiento completo del servicio, trabaja precisión y potencia."));
        ejerciciosTecnicaTenis.add(new Ejercicio("Juego de puntos", "Situaciones de partido", 15,
            "Simula situaciones reales de partido para aplicar la técnica."));
        rutinasTenis.add(new Rutina("Tenis", "Técnica", 70, 400, ejerciciosTecnicaTenis));

        rutinasDeportes.put("Tenis", rutinasTenis);

        // RUTINAS DE BÁSQUETBOL
        List<Rutina> rutinasBasquet = new ArrayList<>();

        List<Ejercicio> ejerciciosPartido = new ArrayList<>();
        ejerciciosPartido.add(new Ejercicio("Calentamiento", "Dribleo y tiros libres", 15,
            "Dribleo con ambas manos y series de tiros libres para entrar en ritmo."));
        ejerciciosPartido.add(new Ejercicio("5vs5", "Partido completo", 60,
            "Juego completo aplicando fundamentos técnicos y tácticos."));
        ejerciciosPartido.add(new Ejercicio("Tiros finales", "Práctica de definición", 15,
            "Series de tiros desde diferentes posiciones cuando hay fatiga."));
        rutinasBasquet.add(new Rutina("Basquetbol", "Partido", 90, 500, ejerciciosPartido));

        List<Ejercicio> ejerciciosFundamentos = new ArrayList<>();
        ejerciciosFundamentos.add(new Ejercicio("Dribleo", "Control de balón", 20,
            "Practica dribleo con ambas manos, cambios de ritmo y dirección."));
        ejerciciosFundamentos.add(new Ejercicio("Pases", "Precisión y velocidad", 15,
            "Pases de pecho, pique y por encima de la cabeza con precisión."));
        ejerciciosFundamentos.add(new Ejercicio("Tiros", "Mecánica de tiro", 25,
            "Tiros desde diferentes distancias, enfócate en la mecánica correcta."));
        rutinasBasquet.add(new Rutina("Basquetbol", "Fundamentos", 60, 350, ejerciciosFundamentos));

        rutinasDeportes.put("Basquetbol", rutinasBasquet);

        // RUTINAS DE NATACIÓN
        List<Rutina> rutinasNatacion = new ArrayList<>();

        List<Ejercicio> ejerciciosTecnicaNado = new ArrayList<>();
        ejerciciosTecnicaNado.add(new Ejercicio("Calentamiento", "Nado suave 400m", 10,
            "Nado cómodo mezclando estilos para preparar músculos."));
        ejerciciosTecnicaNado.add(new Ejercicio("Técnica crol", "8 x 50m técnica", 20,
            "Enfócate en la técnica correcta: respiración bilateral, brazada larga."));
        ejerciciosTecnicaNado.add(new Ejercicio("Técnica espalda", "6 x 50m técnica", 15,
            "Mantén posición horizontal, brazada simétrica y patada constante."));
        ejerciciosTecnicaNado.add(new Ejercicio("Enfriamiento", "200m suave", 10,
            "Nado muy suave para relajar músculos."));
        rutinasNatacion.add(new Rutina("Natación", "Técnica", 55, 320, ejerciciosTecnicaNado));

        List<Ejercicio> ejerciciosResistenciaNado = new ArrayList<>();
        ejerciciosResistenciaNado.add(new Ejercicio("Calentamiento", "600m progresivo", 15,
            "Empieza suave y aumenta intensidad cada 100m."));
        ejerciciosResistenciaNado.add(new Ejercicio("Serie principal", "10 x 100m", 25,
            "Mantén ritmo constante, descanso 20 segundos entre series."));
        ejerciciosResistenciaNado.add(new Ejercicio("Velocidad", "4 x 25m sprint", 8,
            "Nada al máximo, recuperación completa entre series."));
        rutinasNatacion.add(new Rutina("Natación", "Resistencia", 48, 380, ejerciciosResistenciaNado));

        rutinasDeportes.put("Natación", rutinasNatacion);

        // RUTINAS DE CORRER
        List<Rutina> rutinasCorrer = new ArrayList<>();

        List<Ejercicio> ejerciciosCarreraLarga = new ArrayList<>();
        ejerciciosCarreraLarga.add(new Ejercicio("Calentamiento", "Trote 10 min", 10,
            "Comienza con trote muy suave para preparar músculos y articulaciones."));
        ejerciciosCarreraLarga.add(new Ejercicio("Carrera continua", "Ritmo aeróbico", 35,
            "Mantén ritmo cómodo donde puedas mantener conversación."));
        ejerciciosCarreraLarga.add(new Ejercicio("Enfriamiento", "Caminata 5 min", 5,
            "Camina para bajar pulsaciones y haz estiramientos."));
        rutinasCorrer.add(new Rutina("Correr", "Resistencia", 50, 450, ejerciciosCarreraLarga));

        List<Ejercicio> ejerciciosIntervalos = new ArrayList<>();
        ejerciciosIntervalos.add(new Ejercicio("Calentamiento", "Trote progresivo", 15,
            "Trote que va aumentando intensidad gradualmente."));
        ejerciciosIntervalos.add(new Ejercicio("Intervalos", "6 x 3 min fuerte", 25,
            "Corre 3 minutos a ritmo alto, recupera 2 minutos trotando suave."));
        ejerciciosIntervalos.add(new Ejercicio("Vuelta a la calma", "Trote suave", 10,
            "Termina con trote muy suave y estiramientos."));
        rutinasCorrer.add(new Rutina("Correr", "Intervalos", 50, 480, ejerciciosIntervalos));

        rutinasDeportes.put("Correr", rutinasCorrer);

        // RUTINAS DE BOXEO
        List<Rutina> rutinasBoxeo = new ArrayList<>();

        List<Ejercicio> ejerciciosTecnicaBoxeo = new ArrayList<>();
        ejerciciosTecnicaBoxeo.add(new Ejercicio("Calentamiento", "Sombra y movilidad", 10,
            "Ejercicios de movilidad articular y sombra suave."));
        ejerciciosTecnicaBoxeo.add(new Ejercicio("Técnica de golpes", "Saco pesado", 15,
            "Practica jab, cross, ganchos y uppercuts con técnica correcta."));
        ejerciciosTecnicaBoxeo.add(new Ejercicio("Trabajo de pies", "Escalera y desplazamientos", 12,
            "Ejercicios de agilidad y desplazamientos típicos del boxeo."));
        ejerciciosTecnicaBoxeo.add(new Ejercicio("Sombra técnica", "Combinaciones", 15,
            "Practica combinaciones de golpes con buena técnica."));
        rutinasBoxeo.add(new Rutina("Boxeo", "Técnica", 52, 380, ejerciciosTecnicaBoxeo));

        List<Ejercicio> ejerciciosAcondicionamiento = new ArrayList<>();
        ejerciciosAcondicionamiento.add(new Ejercicio("Calentamiento", "Salto de cuerda", 10,
            "Salto de cuerda variando ritmos y estilos."));
        ejerciciosAcondicionamiento.add(new Ejercicio("Circuito", "Fuerza funcional", 20,
            "Burpees, flexiones, sentadillas, abdominales en circuito."));
        ejerciciosAcondicionamiento.add(new Ejercicio("Saco pesado", "Rounds intensos", 15,
            "3 rounds de 3 minutos con intensidad alta."));
        rutinasBoxeo.add(new Rutina("Boxeo", "Acondicionamiento", 45, 420, ejerciciosAcondicionamiento));

        rutinasDeportes.put("Boxeo", rutinasBoxeo);

        // RUTINAS DE VOLEIBOL
        List<Rutina> rutinasVoleibol = new ArrayList<>();

        List<Ejercicio> ejerciciosFundamentosVoley = new ArrayList<>();
        ejerciciosFundamentosVoley.add(new Ejercicio("Calentamiento", "Pase y movilidad", 15,
            "Pases suaves por parejas y ejercicios de movilidad de hombros."));
        ejerciciosFundamentosVoley.add(new Ejercicio("Técnica de recepción", "Antebrazo y toque", 20,
            "Practica recepción de saque y pases precisos."));
        ejerciciosFundamentosVoley.add(new Ejercicio("Ataque", "Remate y bloqueo", 20,
            "Técnica de aproximación, salto y remate. Posición de bloqueo."));
        ejerciciosFundamentosVoley.add(new Ejercicio("Juego dirigido", "6vs6 con correcciones", 25,
            "Partido con paradas para corregir fundamentos técnicos."));
        rutinasVoleibol.add(new Rutina("Voleibol", "Fundamentos", 80, 400, ejerciciosFundamentosVoley));

        rutinasDeportes.put("Voleibol", rutinasVoleibol);
    }

    public static List<Rutina> getRutinasPorDeporte(String deporte) {
        return rutinasDeportes.getOrDefault(deporte, new ArrayList<>());
    }

    public static List<String> getTodosLosDeportes() {
        return new ArrayList<>(rutinasDeportes.keySet());
    }

    public static List<Rutina> getAllRutinas() {
        List<Rutina> todasLasRutinas = new ArrayList<>();
        for (List<Rutina> rutinas : rutinasDeportes.values()) {
            todasLasRutinas.addAll(rutinas);
        }
        return todasLasRutinas;
    }

    public static Rutina getRutinaPorId(String id) {
        for (List<Rutina> rutinas : rutinasDeportes.values()) {
            for (Rutina rutina : rutinas) {
                if (rutina.getId().equals(id)) {
                    return rutina;
                }
            }
        }
        return null;
    }

    public static void actualizarRutina(Rutina rutinaActualizada) {
        for (List<Rutina> rutinas : rutinasDeportes.values()) {
            for (int i = 0; i < rutinas.size(); i++) {
                if (rutinas.get(i).getId().equals(rutinaActualizada.getId())) {
                    rutinas.set(i, rutinaActualizada);
                    return;
                }
            }
        }
    }
}
