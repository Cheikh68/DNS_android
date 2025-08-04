package com.example.dnsfrontend

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DnsApi {
    @POST("update_ip")
    suspend fun updateDnsRecord(
        @Body request: UpdateRequest
    ): Response<ResponseBody>
}