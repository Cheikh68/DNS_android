package com.example.dnsfrontend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun MainScreen(viewModel: MainViewModel) {
    var domain by remember { mutableStateOf("") }
    var ip by remember { mutableStateOf("") }
    val error by viewModel.error.collectAsState()
    val message by viewModel.message.collectAsState()
    var baseUrl by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()


    Box(modifier = Modifier.padding(16.dp)) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Enter information", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp))
            Spacer(modifier = Modifier.height(32.dp))

            Text("Base URL (e.g. http://xxx.xxx.x.xx:xxxx/)")
            TextField(
                value = baseUrl,
                onValueChange = { baseUrl = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Domain (example.com)")
            Row {
                TextField(
                    value = domain,
                    onValueChange = { domain = it },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("New IP address")
            Row {
                TextField(
                    value = ip,
                    onValueChange = { ip = it },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                OutlinedButton(
                    onClick = {
                        val retrofit = createRetrofitInstance(baseUrl)
                        val dnsApi = retrofit.create(DnsApi::class.java)
                        viewModel.updateDns(dnsApi, domain, ip) },
                    enabled = domain.isNotBlank() && ip.isNotBlank()
                ) {
                    Text("Update")
                }

                Spacer(modifier = Modifier.width(16.dp))

                if (isLoading) {
                    CircularProgressIndicator()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (error != null) {
                Text(error!!, color = Color.Red)
            }
            if(message != null)
                Text("Success: $message!!", color = Color.Green)
        }
    }
}


fun createRetrofitInstance(baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
