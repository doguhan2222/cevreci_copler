package com.doguhanay.cevrecicopler.internet

class ApiUtils {
    companion object{
        val BASE_URL = "https://androidstudio2222.000webhostapp.com/"
        fun getDaoInterface():DaoInterface{
            return RetrofitClient.getClient(BASE_URL).create(DaoInterface::class.java)
        }
    }
}