package com.doguhanay.cevrecicopler

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.doguhanay.cevrecicopler.internet.ApiUtils
import com.doguhanay.cevrecicopler.internet.KayitCevap
import com.doguhanay.cevrecicopler.ui.theme.ÇevreciÇöplerTheme
import com.google.android.gms.location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//ComponentActivity
//ÇevreciÇöpler

lateinit var fusedLocationProviderClient: FusedLocationProviderClient
lateinit var locationRequest: LocationRequest
val PERMISSION_ID = 1010
var latitude: String = ""
var longitude: String = ""

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            val context = LocalContext.current
            RequestPermission(context)
            getLastLocation(context)
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

//Checkpermission context
//is location enabled context
fun getLastLocation(context: Context) {
    if (CheckPermission(context)) {
        if (isLocationEnabled(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                var location: Location? = task.result
                if (location == null) {
                    NewLocationData(context)
                } else {
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()
                    Log.d(
                        "Debug1:",
                        " lat " + location.latitude.toString() + " long: " + location.longitude.toString()
                    )

                }
            }
        } else {

        }
    } else {
        RequestPermission(context)
    }
}

fun NewLocationData(context: Context) {
    var locationRequest = LocationRequest()
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    locationRequest.interval = 0
    locationRequest.fastestInterval = 0
    locationRequest.numUpdates = 1
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {

        return
    }
    fusedLocationProviderClient!!.requestLocationUpdates(
        locationRequest, locationCallback, Looper.myLooper()
    )
}

private val locationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
        var lastLocation: Location = locationResult.lastLocation
        Log.d(
            "Debug2:",
            " lat " + lastLocation.latitude.toString() + " long: " + lastLocation.longitude.toString()
        )
        Log.d(
            "CALLBACK",
            " lat " + lastLocation.latitude.toString() + " long: " + lastLocation.longitude.toString()
        )
    }
}

private fun CheckPermission(context: Context): Boolean {
    //this function will return a boolean
    //true: if we have permission
    //false if not
    if (
        ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    }

    return false

}

fun RequestPermission(context: Context) {
    //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
    ActivityCompat.requestPermissions(
        context as Activity,
        arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ),
        PERMISSION_ID
    )
}

fun isLocationEnabled(context: Context): Boolean {
    //this function will return to us the state of the location service
    //if the gps or the network provider is enabled then it will return true otherwise it will return false
    var locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
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
        composable(
            "kayitekrani") {
            KayitEkrani(navController = navController)
        }
        composable(
            "sifremiunuttum/{latitude}/{longitude}",
            arguments = listOf(
                navArgument("latitude") { type = NavType.StringType },
                navArgument("longitude") { type = NavType.StringType })
        ) {
            var latitude = it.arguments?.getString("latitude")!!
            var longitude = it.arguments?.getString("longitude")!!

            KonumuGoster(lat = latitude, long = longitude)
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
                        ClickableText(text = AnnotatedString("Şifremi Unuttum"), onClick = {
                            Log.e("aaaa","$latitude"+" "+"$longitude")
                            navController.navigate("sifremiunuttum/$latitude/$longitude")
                        })
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

