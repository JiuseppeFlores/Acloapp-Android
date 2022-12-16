package com.stisbolivia.acloapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stisbolivia.acloapp.db.DBTareas;

import java.util.ArrayList;

public class MenuTareas extends AppCompatActivity {

    private ArrayList<TareaAclo> listaTareasAclo;
    TextView txtMessage;
    RecyclerView rvTareasAclo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tareas);

        txtMessage = findViewById(R.id.txt_message);
        rvTareasAclo = findViewById(R.id.rv_actividades);

        listaTareasAclo = new ArrayList<TareaAclo>();

        DBTareas dbTareas = new DBTareas(this);
        TareaAclo[] tareasAclo = dbTareas.obtenerTareas();

        if(tareasAclo != null){
            rvTareasAclo.setVisibility(View.VISIBLE);
            txtMessage.setVisibility(View.GONE);
            for(int i = 0 ; i < tareasAclo.length ; i++){
                listaTareasAclo.add(tareasAclo[i]);
            }
            Log.d("LISTADO",tareasAclo.length+"");

            TareaAdapter adapterTareasAclo = new TareaAdapter(MenuTareas.this, listaTareasAclo);
            rvTareasAclo.setHasFixedSize(true);
            rvTareasAclo.setLayoutManager(new LinearLayoutManager(MenuTareas.this));
            rvTareasAclo.setAdapter(adapterTareasAclo);

            //listarTareas();

        }else{
            rvTareasAclo.setVisibility(View.GONE);
            txtMessage.setVisibility(View.VISIBLE);
        }

    }

    public void listarTareas(){
        //RecyclerView rvTareasAclo = (RecyclerView) findViewById(R.id.rv_actividades);
        TareaAdapter adapterTareasAclo = new TareaAdapter(MenuTareas.this,listaTareasAclo);
        rvTareasAclo.setHasFixedSize(true);
        rvTareasAclo.setLayoutManager(new LinearLayoutManager(MenuTareas.this));
        rvTareasAclo.setAdapter(adapterTareasAclo);
    }
}
