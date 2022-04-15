package com.doguhanay.cevrecicopler.internet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class KayitCevap(
    @SerializedName("result")
    @Expose
    var result:String
    ) {

}