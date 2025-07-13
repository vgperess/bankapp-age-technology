package com.example.bankapp.ui.payments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.data.RetrofitInstance
import com.example.bankapp.data.model.Customer
import com.example.bankapp.data.model.Payment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentsUiState())
    val uiState: StateFlow<PaymentsUiState> = _uiState

    fun loadPayments() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val customer = getCustomer()
                val payments = RetrofitInstance.api.getPayments()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    customer = customer,
                    payments = payments
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Erro ao carregar pagamentos")
            }
        }
    }

    private suspend fun getCustomer(): Customer? {
        return try {
            val response = RetrofitInstance.api.getCustomer()
            response.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
}

data class PaymentsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val customer: Customer? = null,
    val payments: List<Payment> = emptyList()
)
