package com.doguhanay.cevrecicopler

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.doguhanay.cevrecicopler.internet.ApiUtils
import com.doguhanay.cevrecicopler.internet.KayitCevap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun KayitAktifEt(mail: String) {


    val kayitGelenMail = remember { mutableStateOf("$mail") }
    val kayitAktifKod = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.purple_500)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Image(
            painter = painterResource(id = R.drawable.giris_ekran_resim),
            contentDescription = "kayit_ekran_resim",
            modifier = Modifier
                .size(220.dp)
                .padding(start = 40.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            backgroundColor = Color.White,
            shape = RoundedCornerShape(32.dp)
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextField(
                    value = kayitGelenMail.value,
                    onValueChange = { kayitGelenMail.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Mail Adresiniz") }
                )
                Spacer(modifier = Modifier.height(15.dp))
                TextField(
                    value = kayitAktifKod.value,
                    onValueChange = { kayitAktifKod.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Onay Kodunuz") }
                )

                Spacer(modifier = Modifier.height(15.dp))
                Button(onClick = {
                    aktifEt(kayitGelenMail.value, kayitAktifKod.value)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Aktif Et")
                }
            }
        }
    }
}

fun aktifEt(mail: String, kod: String) {
    val daoInterface = ApiUtils.getDaoInterface()

    daoInterface.aktifEt(mail, kod).enqueue(object : Callback<KayitCevap> {
        override fun onResponse(call: Call<KayitCevap>, response: Response<KayitCevap>) {
            if (response.body().result.equals("ONAY BASARILI...")) {
                Log.e("kayit2", "kayit basarili")
            }

        }

        override fun onFailure(call: Call<KayitCevap>, t: Throwable) {
            Log.e("aktif", t.message.toString())
        }
    })


}