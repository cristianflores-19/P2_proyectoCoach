package com.example.coachprueba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coachprueba.models.Rutina;
import java.util.List;

public class RutinaAdapter extends RecyclerView.Adapter<RutinaAdapter.RutinaViewHolder> {

    private List<Rutina> rutinas;
    private OnRutinaClickListener listener;

    public interface OnRutinaClickListener {
        void onRutinaClick(Rutina rutina);
    }

    public RutinaAdapter(List<Rutina> rutinas, OnRutinaClickListener listener) {
        this.rutinas = rutinas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RutinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rutina, parent, false);
        return new RutinaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RutinaViewHolder holder, int position) {
        Rutina rutina = rutinas.get(position);
        holder.bind(rutina, listener, this);
    }

    @Override
    public int getItemCount() {
        return rutinas.size();
    }

    static class RutinaViewHolder extends RecyclerView.ViewHolder {
        TextView txtDeporte, txtNombreRutina, txtDuracion, txtCalorias, txtDescripcion;
        Button btnActivar, btnVerDetalles;

        RutinaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDeporte = itemView.findViewById(R.id.txtDeporte);
            txtNombreRutina = itemView.findViewById(R.id.txtNombreRutina);
            txtDuracion = itemView.findViewById(R.id.txtDuracion);
            txtCalorias = itemView.findViewById(R.id.txtCalorias);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            btnActivar = itemView.findViewById(R.id.btnActivar);
            btnVerDetalles = itemView.findViewById(R.id.btnVerDetalles);
        }

        void bind(Rutina rutina, OnRutinaClickListener listener, RutinaAdapter adapter) {
            txtDeporte.setText(rutina.getDeporte());
            txtNombreRutina.setText(rutina.getNombre());
            txtDuracion.setText(rutina.getDuracion() + " min");
            txtCalorias.setText(rutina.getCaloriasEstimadas() + " kcal");
            txtDescripcion.setText(rutina.getDescripcion());

            // Actualizar estado del botón según si está activa
            if (rutina.isActiva()) {
                btnActivar.setText("Activada");
                btnActivar.setBackgroundResource(R.drawable.button_activated);
                btnActivar.setEnabled(false);
            } else {
                btnActivar.setText("Activar");
                btnActivar.setBackgroundResource(R.drawable.button_primary);
                btnActivar.setEnabled(true);
            }

            btnActivar.setOnClickListener(v -> {
                if (!rutina.isActiva()) {
                    rutina.setActiva(true);
                    adapter.notifyDataSetChanged(); // Actualizar vista usando la referencia del adapter
                }
            });

            itemView.setOnClickListener(v -> listener.onRutinaClick(rutina));
            btnVerDetalles.setOnClickListener(v -> listener.onRutinaClick(rutina));
        }
    }
}
