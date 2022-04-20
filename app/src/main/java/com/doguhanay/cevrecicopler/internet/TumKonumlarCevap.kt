package com.doguhanay.cevrecicopler.internet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TumKonumlarCevap(
    @SerializedName("kid")
    @Expose
    var kid: Int,
    @SerializedName("klat")
    @Expose
    var klat: String,
    @SerializedName("klong")
    @Expose
    var klong: String
) {
}