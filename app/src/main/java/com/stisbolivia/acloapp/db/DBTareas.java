package com.stisbolivia.acloapp.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.stisbolivia.acloapp.TareaAclo;

import org.jetbrains.annotations.Nullable;

public class DBTareas extends DBAcloAppHelper{

    Context context;

    public DBTareas (@Nullable Context context){
        super(context);
        this.context = context;
    }

    public long insertarTareas(int id_tarea,String nombre, String descripcion, String fecha){
        DBAcloAppHelper dbAcloAppHelper = new DBAcloAppHelper(context);
        SQLiteDatabase db = dbAcloAppHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_tarea", id_tarea);
        values.put("nombre", nombre);
        values.put("descripcion", descripcion);
        values.put("fecha", fecha);

        long id = db.insert("tbl_tareas", null, values);

        return id;
    }

    public boolean insertarTarea(TareaAclo tarea){
        DBAcloAppHelper dbAcloAppHelper = new DBAcloAppHelper(context);
        SQLiteDatabase db = dbAcloAppHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_tarea", tarea.getId());
        values.put("nombre", tarea.getNombre());
        values.put("descripcion", tarea.getDescripcion());
        values.put("fecha", tarea.getFecha());

        long id = db.insert("tbl_tareas", null, values);

        return (id > 0);
    }

    public boolean verificarIdTarea(int id_tarea){
        DBAcloAppHelper dbAcloAppHelper = new DBAcloAppHelper(context);
        SQLiteDatabase db = dbAcloAppHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_tareas WHERE id_tarea = "+id_tarea,null);
        int count = cursor.getCount();
        if(count > 0){
            Log.d("DATABASE ID TAREA","Tarea Registrada en la BD");
            return true;
        }else{
            Log.d("DATABASE ID TAREA","Tarea no registrada");
            return false;
        }
    }

    public TareaAclo[] obtenerTareas(){
        DBAcloAppHelper dbAcloAppHelper = new DBAcloAppHelper(context);
        SQLiteDatabase db = dbAcloAppHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_tareas WHERE estado = 'ACTIVO'",null);
        int count = cursor.getCount();
        if(count > 0){
            TareaAclo[] tarea = new TareaAclo[count];
            int i = 0;
            while(cursor.moveToNext()){
                @SuppressLint("Range") int id_tarea = cursor.getInt(cursor.getColumnIndex("id_tarea"));
                @SuppressLint("Range") String titulo = cursor.getString(cursor.getColumnIndex("nombre"));
                @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex("descripcion"));
                @SuppressLint("Range") String fecha = cursor.getString(cursor.getColumnIndex("fecha"));
                tarea[i] = new TareaAclo(id_tarea,titulo,descripcion,fecha);
                i++;
            }
            return tarea;
        }else{
            return null;
        }
    }

    public void mostrarTareas(){
        DBAcloAppHelper dbAcloAppHelper = new DBAcloAppHelper(context);
        SQLiteDatabase db = dbAcloAppHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_tareas WHERE estado = 'ACTIVO'",null);
        int count = cursor.getCount();
        if(count > 0){
            TareaAclo tarea = null;
            int i = 0;
            while(cursor.moveToNext()){
                @SuppressLint("Range") int id_tarea = cursor.getInt(cursor.getColumnIndex("id_tarea"));
                @SuppressLint("Range") String titulo = cursor.getString(cursor.getColumnIndex("nombre"));
                @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex("descripcion"));
                @SuppressLint("Range") String fecha = cursor.getString(cursor.getColumnIndex("fecha"));
                tarea = new TareaAclo(id_tarea,titulo,descripcion,fecha);
                i++;
                Log.d("MOSTRAR TAREAS",tarea.toString());
            }
        }else{
            Log.d("MOSTRAR TAREAS","Sin tareas por mostrar");
        }
    }
}
