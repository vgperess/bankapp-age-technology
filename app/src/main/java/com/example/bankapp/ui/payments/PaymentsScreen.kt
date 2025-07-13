package com.example.bankapp.ui.payments

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import java.util.Calendar
import java.util.Locale
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PaymentsScreen(navController: NavController, viewModel: PaymentsViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPayments()
    }

    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = { showDialog = true }) {
                Text("Sair")
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Sair da conta") },
                text = { Text("Tem certeza que deseja sair?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        navController.navigate("login") {
                            popUpTo("payments") { inclusive = true }
                        }
                    }) {
                        Text("Sim")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
        Text("Pagamentos", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        uiState.customer?.let { customer ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Nome: ${customer.customerName}")
                    Text("Agência: ${customer.branchNumber}")
                    Text("Conta: ${customer.accountNumber}")
                    Text("Saldo: R$ ${"%.2f".format(customer.checkingAccountBalance)}")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
        Text("Contas pagas", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
            }
            else -> {
                val ano = Calendar.getInstance().get(Calendar.YEAR)
                val meses = listOf("05", "06", "07")
                val pagamentos = listOf(
                    Pair("R$ 1.000,00", "25/${meses[0]}/$ano"),
                    Pair("R$ 1.000,00", "25/${meses[1]}/$ano"),
                    Pair("R$ 1.000,00", "25/${meses[2]}/$ano")
                )
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(pagamentos) { (valor, data) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Conta de luz", modifier = Modifier.weight(1f))
                            Text(valor, modifier = Modifier.weight(1f))
                            Text(data, modifier = Modifier.weight(1f), fontWeight = FontWeight.Light)
                        }
                        Divider()
                    }
                }
            }
        }
    }
}
