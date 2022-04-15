package com.doguhanay.cevrecicopler.internet

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DaoInterface {
    @POST("cevreci_giris_yap.php")
    @FormUrlEncoded
    fun girisYap(
        @Field("kullanici_adi") kullanici_adi: String,
        @Field("kullanici_sifre") kullanici_sifre: String
    ): Call<KayitCevap>

    @POST("cevreci_ilk_kayit_ol.php")
    @FormUrlEncoded
    fun kayitOl(
        @Field("kullanici_adi") kullanici_adi:String,
        @Field("kullanici_sifre") kullanici_sifre:String,
        @Field("kullanici_mail") kullanici_mail:String
    ):Call<KayitCevap>

    @POST("cevreci_kayit_aktifet.php")
    @FormUrlEncoded
    fun aktifEt(
        @Field("kullanici_mail") kullanici_mail:String,
        @Field("kullanici_kod") kullanici_kod:String
    ):Call<KayitCevap>
}