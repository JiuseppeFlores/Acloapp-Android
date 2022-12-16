package com.stisbolivia.acloapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.ViewHolder> {

    private ArrayList<TareaAclo> listaTareas;
    private Context context;

    public TareaAdapter(Context context, ArrayList<TareaAclo> listaTareas){
        this.context = context;
        this.listaTareas = listaTareas;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_tareas,parent,false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TareaAclo listaTarea = listaTareas.get(position);
        holder.txtNombre.setText(listaTarea.getNombre().toUpperCase());
        holder.txtDescripcion.setText(listaTarea.getDescripcion());
        holder.txtHora.setText(listaTarea.getHora());
        holder.txtFecha.setText(listaTarea.getFechaDias());
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNombre;
        private TextView txtDescripcion;
        private TextView txtHora;
        private TextView txtFecha;

        public ViewHolder(@NonNull View view){
            super(view);
            this.txtNombre = (TextView) itemView.findViewById(R.id.txt_nombre);
            this.txtDescripcion = (TextView) itemView.findViewById(R.id.txt_descripcion);
            this.txtHora = (TextView) itemView.findViewById(R.id.txt_hora);
            this.txtFecha = (TextView) itemView.findViewById(R.id.txt_fecha);
        }

    }
}
