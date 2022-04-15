package com.doguhanay.cevrecicopler.internet

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DaoInterface {
    @POST("cevreci_giris_yap.php")
    @FormUrlEncoded
    fun girisYap(
        @Field("kullanici_adi") kullanici_adi: String,
        @Field("kullanici_sifre") kullanici_sifre: String
    ): Call<GirisCevap>
}