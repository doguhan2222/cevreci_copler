package com.doguhanay.cevrecicopler

import android.util.Log
import android.widget.Space
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
import com.doguhanay.cevrecicopler.internet.DaoInterface
import com.doguhanay.cevrecicopler.internet.KayitCevap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun KayitEkrani(navController: NavController) {

    val kayitKadi = remember { mutableStateOf("") }
    val kayitSifre = remember { mutableStateOf("") }
    val kayitMail = remember { mutableStateOf("") }
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
                    value = kayitKadi.value,
                    onValueChange = { kayitKadi.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Kullanıcı Adı") }
                )
                Spacer(modifier = Modifier.height(15.dp))
                TextField(
                    value = kayitSifre.value,
                    onValueChange = { kayitSifre.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Şifre") }
                )
                Spacer(modifier = Modifier.height(15.dp))
                TextField(
                    value = kayitMail.value,
                    onValueChange = { kayitMail.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Mail Adresi") }
                )
                Spacer(modifier = Modifier.height(15.dp))
                Button(onClick = {
                    kayitOl(kayitKadi.value, kayitSifre.value, kayitMail.value, navController)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Kaydı Tamamla")
                }
            }
        }
    }

}

fun kayitOl(kadi: String, sifre: String, mail: String, navController: NavController) {
    val daoInterface = ApiUtils.getDaoInterface()
    daoInterface.kayitOl(kadi, sifre, mail).enqueue(object : Callback<KayitCevap> {
        override fun onResponse(call: Call<KayitCevap>, response: Response<KayitCevap>) {
            Log.e("kayit3", response.body().result.toString())
            if (response.body().result.equals("KAYIT BAŞARILI")) {
                navController.navigate("kayitaktiflestirme/$mail")
                Log.e("kayit", response.body().result.toString())
            } else if (response.body().result.equals("KAYIT BAŞARISIZ")) {
                Log.e("kayit2", "Kayit Yapılamadı")
            }
        }

        override fun onFailure(call: Call<KayitCevap>, t: Throwable) {
            Log.e("kayit", t.message.toString())
        }
    })


}