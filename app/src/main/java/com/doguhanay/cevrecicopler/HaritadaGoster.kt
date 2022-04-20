package com.doguhanay.cevrecicopler

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.foundation.layout.*
import com.doguhanay.cevrecicopler.internet.ApiUtils
import com.doguhanay.cevrecicopler.internet.TumKonumlarCevap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


var listelat: List<String> = emptyList()
var listelong: List<String> = emptyList()

@Composable
fun KonumuGoster(gelenNesne:KonumlarListClass) {

    // Tüm konumların lat ve long unun olduğu 2 listeyi içeren sınıf burada listelere bölünür
    listelat = gelenNesne.listeLat
    listelong = gelenNesne.listeLong
    Log.e("konum9", listelat.toString())
    Log.e("konum9", listelong.toString())

    val kameraFocus = LatLng(listelat[0].toDouble(), listelong[0].toDouble())
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(kameraFocus, 15f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        //bu döngü sayesinde 2 liste kullanılarak latlng oluşturulur ve markera atanarak kaç tane
        //konum varsa o kadar marker oluşturulur
        for(k in listelat.indices){
            var latLng = LatLng(listelat[k].toDouble(), listelong[k].toDouble())
            Marker(position = latLng)
        }
    }
}




















fun konumAlveGoster(): List<TumKonumlarCevap> {
    var listee:List<TumKonumlarCevap> = emptyList()
    val daoInterface = ApiUtils.getDaoInterface()
    daoInterface.konumlariAl().enqueue(object : Callback<List<TumKonumlarCevap>> {
        override fun onResponse(call: Call<List<TumKonumlarCevap>>, response: Response<List<TumKonumlarCevap>>) {
            listee = response.body()
            for (k in listee) {
                Log.e("konum", "---------")
                Log.e("konum", k.kid.toString())
                Log.e("konum", k.klat.toString())
                Log.e("konum", k.klong.toString())
            }


        }
        override fun onFailure(call: Call<List<TumKonumlarCevap>>, t: Throwable) {

        }
    })
    return listee

}
suspend fun listRepos(): List<TumKonumlarCevap> {


    return suspendCancellableCoroutine { cont ->
        val daoInterface = ApiUtils.getDaoInterface()

        daoInterface.konumlariAl().enqueue(object : Callback<List<TumKonumlarCevap>>  {
            override fun onResponse(call: Call<List<TumKonumlarCevap>> , response: Response<List<TumKonumlarCevap>> ) {
                if (response.isSuccessful) {
                    //cont.resume(response.body()!!)
                } else {
                    // just an example

                }
            }

            override fun onFailure(call: Call<List<TumKonumlarCevap>>?, t: Throwable?) {

            }
        })

    }
}

