package com.stisbolivia.acloapp;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Toast;

import com.onesignal.OSMutableNotification;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal.OSRemoteNotificationReceivedHandler;
import com.stisbolivia.acloapp.db.DBTareas;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

@SuppressWarnings("unused")
public class NotificationServiceExtension implements OSRemoteNotificationReceivedHandler {

    @Override
    public void remoteNotificationReceived(Context context, OSNotificationReceivedEvent notificationReceivedEvent) {
        OSNotification notification = notificationReceivedEvent.getNotification();

        // Example of modifying the notification's accent color
        OSMutableNotification mutableNotification = notification.mutableCopy();
        mutableNotification.setExtender(builder -> {
            // Sets the accent color to Green on Android 5+ devices.
            // Accent color controls icon and action buttons on Android 5+. Accent color does not change app title on Android 10+
            builder.setColor(new BigInteger("FF00FF00", 16).intValue());
            // Sets the notification Title to Red
            Spannable spannableTitle = new SpannableString(notification.getTitle());
            spannableTitle.setSpan(new ForegroundColorSpan(Color.RED),0,notification.getTitle().length(),0);
            builder.setContentTitle(spannableTitle);
            // Sets the notification Body to Blue
            Spannable spannableBody = new SpannableString(notification.getBody());
            spannableBody.setSpan(new ForegroundColorSpan(Color.BLUE),0,notification.getBody().length(),0);
            builder.setContentText(spannableBody);
            //Force remove push from Notification Center after 30 seconds
            builder.setTimeoutAfter(30000);
            return builder;
        });

        JSONObject data = notification.getAdditionalData();
        Log.i("NOTIFICACION BACKGROUND", "Data: " + data);
        /*try {
            int id_tarea = data.getInt("id_tarea");
            String nombre = data.getString("nombre");
            String descripcion = data.getString("descripcion");
            String fecha = data.getString("fecha");
            TareaAclo newTarea = new TareaAclo(id_tarea,nombre,descripcion,fecha);
            DBTareas dbTareas = new DBTareas(context);
            if(dbTareas.verificarIdTarea(newTarea.getId())){
                Log.d("NEW NOTIFICATION","Tarea registrada con anterioridad");
            }else{
                Log.d("NEW NOTIFICATION","Tarea sin registrar");
                if(dbTareas.insertarTarea(newTarea)){
                    Log.d("NEW NOTIFICATION","Tarea registrada exitosamente");
                    dbTareas.mostrarTareas();
                }else{
                    Log.d("NEW NOTIFICATION","Error al insertar nueva tarea");
                    dbTareas.mostrarTareas();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("NEW NOTIFICATION","Error "+e.getMessage().toString());
        }*/
        // If complete isn't call within a time period of 25 seconds, OneSignal internal logic will show the original notification
        // To omit displaying a notification, pass `null` to complete()
        notificationReceivedEvent.complete(mutableNotification);

    }
}