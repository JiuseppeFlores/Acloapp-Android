package com.stisbolivia.acloapp.api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface actividad {

    @GET("/php/services/sincronizar_actividades.php")
    public void sincronizarActividades(
            Callback<Response> response
    );

}
