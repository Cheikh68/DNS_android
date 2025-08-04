package com.example.dnsfrontend

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun updateDns(dnsApi: DnsApi, domain: String, ip: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _error.value = null
            _isLoading.value = true
            val request = UpdateRequest(hostnames = listOf(domain), ip = ip)
            try {
                val response = dnsApi.updateDnsRecord(request)
                if (!response.isSuccessful) {
                    _error.value = "Failed: ${response.code()}"
                }
                else {
                    val rawJson = response.body()?.string()

                    val json = JSONObject(rawJson ?: "")
                    _message.value = json.optString("message", "No message")
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}