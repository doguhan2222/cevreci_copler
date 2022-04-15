package com.doguhanay.cevrecicopler.internet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GirisCevap(
    @SerializedName("kullanici_id")
    @Expose
    var kullanici_id: Int,
    @SerializedName("kullanici_adi")
    @Expose
    var kullanici_adi: String,
    @SerializedName("kullanici_sifre")
    @Expose
    var kullanici_sifre: String
) {
}