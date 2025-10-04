package com.example.coachprueba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coachprueba.models.Ejercicio;
import java.util.List;

public class EjercicioAdapter extends RecyclerView.Adapter<EjercicioAdapter.EjercicioViewHolder> {

    private List<Ejercicio> ejercicios;

    public EjercicioAdapter(List<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }

    @NonNull
    @Override
    public EjercicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ejercicio, parent, false);
        return new EjercicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EjercicioViewHolder holder, int position) {
        Ejercicio ejercicio = ejercicios.get(position);
        holder.bind(ejercicio);
    }

    @Override
    public int getItemCount() {
        return ejercicios != null ? ejercicios.size() : 0;
    }

    public class EjercicioViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNombreEjercicio, txtDetalleEjercicio;
        private ImageView imgEjercicio;
        private CheckBox checkBoxCompletado;

        public EjercicioViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreEjercicio = itemView.findViewById(R.id.txtNombreEjercicio);
            txtDetalleEjercicio = itemView.findViewById(R.id.txtDetalleEjercicio);
            imgEjercicio = itemView.findViewById(R.id.imgEjercicio);
            checkBoxCompletado = itemView.findViewById(R.id.checkBoxCompletado);
        }

        public void bind(Ejercicio ejercicio) {
            txtNombreEjercicio.setText(ejercicio.getNombre());

            // Formatear el detalle del ejercicio
            String detalle = "";
            if (ejercicio.getSeries() > 0) {
                detalle += ejercicio.getSeries() + " series";
            }
            if (ejercicio.getRepeticiones() > 0) {
                if (!detalle.isEmpty()) detalle += " x ";
                detalle += ejercicio.getRepeticiones() + " repeticiones";
            }
            if (ejercicio.getDuracion() > 0) {
                if (!detalle.isEmpty()) detalle += " - ";
                detalle += ejercicio.getDuracion() + " segundos";
            }

            txtDetalleEjercicio.setText(detalle.isEmpty() ? "Sin especificar" : detalle);

            // Configurar el checkbox
            checkBoxCompletado.setChecked(ejercicio.isCompletado());
            checkBoxCompletado.setOnCheckedChangeListener((buttonView, isChecked) -> {
                ejercicio.setCompletado(isChecked);
            });

            // Configurar imagen por defecto
            imgEjercicio.setImageResource(R.drawable.ic_fitness);
        }
    }
}
