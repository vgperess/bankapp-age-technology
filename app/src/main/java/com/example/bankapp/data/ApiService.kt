package com.example.bankapp.data

import com.example.bankapp.data.model.Customer
import com.example.bankapp.data.model.Payment
import retrofit2.http.GET

interface ApiService {
    @GET("treinamento/Login")
    suspend fun getCustomer(): List<Customer>

    @GET("treinamento/payments")
    suspend fun getPayments(): List<Payment>
}
