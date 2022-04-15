package com.doguhanay.cevrecicopler

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.doguhanay.cevrecicopler.internet.ApiUtils
import com.doguhanay.cevrecicopler.internet.DaoInterface
import com.doguhanay.cevrecicopler.internet.GirisCevap
import com.doguhanay.cevrecicopler.internet.KayitCevap
import com.doguhanay.cevrecicopler.ui.theme.Shapes
import com.doguhanay.cevrecicopler.ui.theme.ÇevreciÇöplerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ÇevreciÇöplerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SayfaGecisleri()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ÇevreciÇöplerTheme {

    }
}

@Composable
fun SayfaGecisleri() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "girisekrani") {
        composable("girisekrani") {
            GirisEkrani(navController = navController)
        }
        composable("kayitekrani") {
            KayitEkrani(navController = navController)
        }
        composable(
            "kayitaktiflestirme/{mail}",
            arguments = listOf(navArgument("mail") { type = NavType.StringType })
        ) {
            val mail = it.arguments?.getString("mail")!!
            KayitAktifEt(mail = mail)
        }
    }
}

@Composable
fun GirisEkrani(navController: NavController) {
    val context = LocalContext.current
    var sayfaKontrol = remember { mutableStateOf(true) }
    var kadi = remember { mutableStateOf("") }
    var sifre = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.purple_500)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,

        ) {
        Image(
            painter = painterResource(id = R.drawable.giris_ekran_resim),
            contentDescription = "giris_ekran_resim",
            modifier = Modifier
                .size(200.dp)
                .weight(1f)
        )
        Card(
            modifier = Modifier
                .weight(2f)
                .padding(10.dp), shape = RoundedCornerShape(32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            )
            {
                Text(
                    text = "Tekrar Hoşgeldin",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = kadi.value,
                        onValueChange = { kadi.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                text = "Kullanici Adi"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(
                        value = sifre.value,
                        onValueChange = { sifre.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                text = "Sifre"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = {

                        girisYap(kadi.value.toString(), sifre.value.toString(), context)
                    }) {
                        Text(text = "Giris Yap")
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ClickableText(text = AnnotatedString("Şifremi Unuttum"), onClick = {})
                        ClickableText(text = AnnotatedString("Kayıt Ol"), onClick = {
                            if (sayfaKontrol.value) {
                                navController.navigate("kayitekrani")
                                sayfaKontrol.value = false
                                //text = "Disabled"
                            }
                        })

                    }
                }

            }

        }
    }
}

fun girisYap(kullanici_adi: String, kullanici_sifre: String, context: Context) {

    val daoInterface = ApiUtils.getDaoInterface()
    daoInterface.girisYap(kullanici_adi, kullanici_sifre).enqueue(object : Callback<KayitCevap> {
        override fun onResponse(call: Call<KayitCevap>, response: Response<KayitCevap>) {

            Log.e("giris1", response.body().toString())
            if (response.body().result.equals("Giris basarisiz")) {
                Toast.makeText(context, "Giriş yapılamıyor", Toast.LENGTH_SHORT).show()
                Log.e("giris1", response.body().toString())
            } else if (response.body().result.equals("Giris basarili")) {
                Toast.makeText(context, "Giriş Yapılıyor", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<KayitCevap>?, t: Throwable?) {
            Log.e("giris", t?.message.toString())
        }
    })


}

