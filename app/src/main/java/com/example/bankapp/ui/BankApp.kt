package com.example.bankapp.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bankapp.ui.login.LoginScreen
import com.example.bankapp.ui.payments.PaymentsScreen

@Composable
fun BankApp() {
    val navController = rememberNavController()
    MaterialTheme {
        Surface {
            NavHost(navController = navController, startDestination = "login") {
                composable("login") {
                    LoginScreen(navController)
                }
                composable("payments") {
                    PaymentsScreen(navController)
                }
            }
        }
    }
}
