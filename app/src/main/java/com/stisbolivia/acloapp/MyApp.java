package com.stisbolivia.acloapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.onesignal.OSMutableNotification;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;
import com.onesignal.OneSignalNotificationManager;
import com.stisbolivia.acloapp.db.DBAcloAppHelper;
import com.stisbolivia.acloapp.db.DBTareas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MyApp extends Application {

    private static final String ONESIGNAL_APP_ID = "0fbbf640-354a-427e-91fd-92ba89a37909";

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // ACCIONES QUE SE REALIZAN CUANDO LA NOTIFICACION ES ABIERTA
        /*OneSignal.setNotificationOpenedHandler(
                new OneSignal.OSNotificationOpenedHandler() {
                    @Override
                    public void notificationOpened(OSNotificationOpenedResult result) {
                        String actionId = result.getAction().getActionId();
                        String type = String.valueOf(result.getAction().getType()); // "ActionTaken" | "Opened"

                        String title = result.getNotification().getTitle();

                        //String datos = result.toJSONObject().toString();

                        JSONObject response = null;
                        try {
                            response = result.toJSONObject().getJSONObject("notification").getJSONObject("additionalData");
                            Log.d("NOTIFICATION DATA", response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("NOTIFICATION DATA",e.getMessage().toString());
                        }
                    }
                });

        // ACCIONES QUE SE REALIZAN CUANDO LA NOTIFICACION LLEGA EN SEGUNDO PLANO
        OneSignal.setNotificationWillShowInForegroundHandler(new OneSignal.OSNotificationWillShowInForegroundHandler() {
            @Override
            public void notificationWillShowInForeground(OSNotificationReceivedEvent osNotificationReceivedEvent) {
                Log.d("NOTIFICACION FOREGROUND","llego");
            }
        });*/

        /*CREACION DE LA BASE DE DATOS*/
        DBAcloAppHelper dbAcloAppHelper = new DBAcloAppHelper(this);
        SQLiteDatabase db = dbAcloAppHelper.getWritableDatabase();
        if(db != null){
            Log.d("DATABASE","Base de datos creada correctamente");
            mostrarTareas();
        }else{
            Log.d("DATABASE", "Error al crear la base de datos");
        }

    }

    public boolean registrarTareaAclo(TareaAclo tarea){
        DBAcloAppHelper dbAcloAppHelper = new DBAcloAppHelper(this);
        SQLiteDatabase db = dbAcloAppHelper.getWritableDatabase();
        if(db != null){
            DBTareas dbTareas = new DBTareas(this);
            if(dbTareas.verificarIdTarea(tarea.getId())){
                long query = dbTareas.insertarTareas(tarea.getId(),tarea.getNombre(),tarea.getDescripcion(), tarea.getFecha());
                Log.d("QUERY DATABASE", String.valueOf(query));
                if(query > 0){
                    Log.d("DATABASE","No se insertaron los datos");
                    return false;
                }else{
                    Log.d("DATABASE","Tarea registrada exitosamente");
                    return true;
                }
            }else{
                Log.d("DATABASE", "Tarea registrada con anterioridad");
                return false;
            }
        }else{
            Log.d("DATABASE", "Error en la creación de la base de datos");
            return false;
        }
    }

    public void mostrarTareas(){
        DBAcloAppHelper dbAcloAppHelper = new DBAcloAppHelper(this);
        SQLiteDatabase db = dbAcloAppHelper.getWritableDatabase();
        if(db != null){
            DBTareas dbTareas = new DBTareas(this);
            TareaAclo[] tareas = dbTareas.obtenerTareas();
            if(tareas != null){
                for(int i = 0;i< tareas.length;i++){
                    Log.d("TAREAS ACLO",tareas[i].toString());
                }
            }else{
                Log.d("TAREAS ACLO","Sin tareas disponibles");
            }
        }else{
            Log.d("DATABASE","Error en la creación de la base de datos");
        }
    }
}
