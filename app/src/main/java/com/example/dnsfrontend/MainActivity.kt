package com.example.dnsfrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.dnsfrontend.ui.theme.DNSFrontEndTheme
import com.google.gson.annotations.SerializedName


class MainActivity : ComponentActivity() {
    private val MyViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DNSFrontEndTheme {
                MainScreen(MyViewModel)
            }
        }
    }
}


data class UpdateRequest(
    @SerializedName("hostnames") val hostnames: List<String>,
    @SerializedName("ip") val ip: String
)


