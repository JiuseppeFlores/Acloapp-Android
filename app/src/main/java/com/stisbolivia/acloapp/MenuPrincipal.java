package com.stisbolivia.acloapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stisbolivia.acloapp.api.actividad;
import com.stisbolivia.acloapp.db.DBTareas;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MenuPrincipal extends AppCompatActivity {

    public static final String BASEURL="http://acloapp.org/app/";

    FloatingActionButton /*fabCamera, */fabCalendar;
    ExtendedFloatingActionButton efabFunctions;
    TextView /*txtFabCamera,*/txtFabCalendar;

    Boolean isAllFabVisible;

    private WebView mWebView;
    private int req_permission = 4643;
    android.webkit.PermissionRequest mRequest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);
        getSupportActionBar().hide();

        efabFunctions = findViewById(R.id.functions_fab);
        //fabCamera = findViewById(R.id.camera_fab);
        fabCalendar = findViewById(R.id.calendar_fab);

        //txtFabCamera = findViewById(R.id.text_camera_fab);
        txtFabCalendar = findViewById(R.id.text_calendar_fab);

        //fabCamera.setVisibility(View.GONE);
        fabCalendar.setVisibility(View.GONE);
        //txtFabCamera.setVisibility(View.GONE);
        txtFabCalendar.setVisibility(View.GONE);

        isAllFabVisible = false;

        efabFunctions.shrink();

        efabFunctions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isAllFabVisible){
                    //fabCamera.show();
                    fabCalendar.show();
                    //txtFabCamera.setVisibility(View.VISIBLE);
                    txtFabCalendar.setVisibility(View.VISIBLE);
                    efabFunctions.extend();

                    isAllFabVisible = true;
                }else{
                    //fabCamera.hide();
                    fabCalendar.hide();
                    //txtFabCamera.setVisibility(View.GONE);
                    txtFabCalendar.setVisibility(View.GONE);
                    efabFunctions.shrink();

                    isAllFabVisible = false;

                }
            }
        });

        fabCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPrincipal.this, MenuTareas.class);
                startActivity(intent);
            }
        });

        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        //mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(android.webkit.PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //Log.d("dispositivos","entra");
                    request.grant(request.getResources());
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

        });

        mWebView.loadUrl("https://acloapp.org/app/");

        //MANEJO DEL CALENDARIO
        sincronizarActividades();
        //setAlarm("PRUEBA ACLOAPP","2022-12-16","03:30 PM");

    }

    public void sincronizarActividades(){
        RestAdapter restAdapter =new RestAdapter.Builder().setEndpoint(BASEURL).build();
        actividad serviceapi = restAdapter.create(actividad.class);
        serviceapi.sincronizarActividades(
                new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        BufferedReader reader = null;
                        String result = "";
                        try {
                            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                            result = reader.readLine();
                            JSONObject object = new JSONObject(result.trim());
                            boolean status = object.getBoolean("status");
                            if(status){
                                JSONArray data = object.getJSONArray("data");
                                sincronizarTareas(data);
                            }else{
                                Log.d("SINCRONIZACION","Error en el servidor");
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                            Log.d("SINCRONIZACIÓN","Error: "+ex.getMessage().toString());
                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("SINCRONIZACION", "Retrofit: Error al conectarse con el servidor");
                        error.printStackTrace();
                    }
                }
        );
    }

    public void sincronizarTareas(JSONArray data){
        int lengthData = data.length();
        DBTareas dbTareas = new DBTareas(this);
        TareaAclo[] tareasAclo = dbTareas.obtenerTareas();
        int lengthTareasAclo = 0;
        if(tareasAclo == null){
            lengthTareasAclo = 0;
        }else{
            lengthTareasAclo = tareasAclo.length;
        }
        for(int i = 0 ; i < lengthData ; i++){
            JSONObject tarea = null;
            try{
                tarea = data.getJSONObject(i);
                boolean sw = false;
                for(int j = 0 ; j < lengthTareasAclo ; j++){
                    if(Integer.parseInt(tarea.getString("id")) == tareasAclo[j].getId()){
                        sw = true;
                        break;
                    }
                }
                if(!sw){
                    int id = tarea.getInt("id");
                    String nombre = tarea.getString("nombre");
                    String descripcion = tarea.getString("descripcion");
                    String fecha = tarea.getString("fecha");

                    TareaAclo newTarea = new TareaAclo(id,nombre,descripcion,fecha);
                    Log.d("SINCRONIZACION","Actividad no registrada: ID "+tarea.getString("id"));
                    registrarCalendario(newTarea);
                }else{
                    Log.d("SINCRONIZACION","Actividad ya registrada: ID "+tarea.getString("id"));
                }
            }catch(Exception e){
                Log.d("SINCRONIZACION",e.getMessage().toString());
            }
        }
    }

    public void registrarCalendario(TareaAclo tarea) {
        Calendar calendar = Calendar.getInstance();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            calendar.setTime(sdf.parse(tarea.getFecha()));

            ContentResolver cr = this.getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(CalendarContract.Events.TITLE,tarea.getNombre());
            cv.put(CalendarContract.Events.DESCRIPTION,tarea.getDescripcion());
            cv.put(CalendarContract.Events.EVENT_LOCATION,"");
            cv.put(CalendarContract.Events.DTSTART,calendar.getTimeInMillis());
            cv.put(CalendarContract.Events.DTEND,calendar.getTimeInMillis()+60*60*1000*6);
            cv.put(CalendarContract.Events.HAS_ALARM, true);
            cv.put(CalendarContract.Events.CALENDAR_ID,1);
            cv.put(CalendarContract.Events.EVENT_TIMEZONE,Calendar.getInstance().getTimeZone().getID());

            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI,cv);

            /*long eventId = Long.parseLong(uri.getLastPathSegment());

            ContentValues reminder = new ContentValues();
            reminder.put(CalendarContract.Reminders.EVENT_ID, eventId);
            reminder.put(CalendarContract.Reminders.MINUTES, 1);
            reminder.put(CalendarContract.Reminders.METHOD, 1);
            getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminder);*/

            DBTareas dbTareas = new DBTareas(this);
            dbTareas.insertarTarea(tarea);

            //Toast.makeText(this, "Evento Registrado con exito", Toast.LENGTH_SHORT).show();
            Log.d("CALENDARIO","Tarea registrada: "+tarea.getId());

        }catch(Exception e){
            e.printStackTrace();
            Log.d("CALENDARIO","Error: "+e.getMessage().toString());
        }

        /*RECORDATORIOS*/

        /*long eventId = Long.parseLong(uri.getLastPathSegment());

        ContentValues reminder = new ContentValues();
        reminder.put(CalendarContract.Reminders.EVENT_ID, eventId);
        reminder.put(CalendarContract.Reminders.MINUTES, 1);
        reminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminder);*/


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==req_permission){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                String[] request = {android.webkit.PermissionRequest.RESOURCE_VIDEO_CAPTURE};
                mRequest.grant(request);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack())
            mWebView.goBack();
        else
            super.onBackPressed();
    }

    /*private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(this.ALARM_SERVICE);                   //assigining alaram manager object to set alaram

        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("event", text);                                                       //sending data to alarm class to create channel and notification
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + time;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Toast.makeText(getApplicationContext(), "Alaram", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }
                                                             //navigates from adding reminder activity ot mainactivity
        getIntent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }*/
}