package com.example.mist.data.remote

import com.example.mist.domain.Console
import com.example.mist.domain.Cor
import com.example.mist.domain.Marca
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MistService {
    @GET("plataformas")
    suspend fun getAllCon(): List<Console>

    @GET("plataforma/{plataforma_id}")
    suspend fun getByIdConsole(@Path("plataforma_id") id: Long): Console

    @POST("plataforma")
    suspend fun createConsole(@Body console: Console): Console

    @PUT("plataforma/{plataforma_id}")
    suspend fun updateConsole(@Path("plataforma_id") id: Long, @Body console: Console): Console

    @DELETE("plataforma/{plataforma_id}")
    suspend fun deleteConsole(@Path("plataforma_id") id: Long)

    /////////////////////JOGOS//////////////////////
    @GET("jogos")
    suspend fun getAllJogo(): List<Console>

    @GET("jogo/{jogo_id}")
    suspend fun getByIdJogo(@Path("jogo_id") id: Long): Console

    @POST("jogo")
    suspend fun createJogo(@Body console: Console): Console

    @PUT("jogo/{jogo_id}")
    suspend fun updateJogo(@Path("jogo_id") id: Long, @Body console: Console): Console

    @DELETE("jogo/{jogo_id}")
    suspend fun deleteJogo(@Path("jogo_id") id: Long)

    ///////////////////COR///////////////////////////

    @GET("cores")
    suspend fun getAllCor(): List<Cor>

    @GET("cor/{cor_id}")
    suspend fun getByIdCor(@Path("cor_id") id: Long): Cor

    @POST("cor")
    suspend fun createCor(@Body cor: Cor): Cor

    @PUT("cor/{cor_id}")
    suspend fun updateCor(@Path("cor_id") id: Long, @Body cor: Cor): Cor

    ///////////////////MARCA///////////////////////////

    @GET("marcas")
    suspend fun getAllMarca(): List<Marca>

    @GET("marca/{marca_id}")
    suspend fun getByIdMarca(@Path("marca_id") id: Long): Marca

    @POST("marca")
    suspend fun createMarca(@Body marca: Marca): Marca

    @PUT("marca/{marca_id}")
    suspend fun updateMarca(@Path("marca_id") id: Long, @Body marca: Marca): Marca
}