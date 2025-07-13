package com.example.bankapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.bankapp.data.ApiService
import com.example.bankapp.data.RetrofitInstance
import com.example.bankapp.data.model.Customer

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailError = validateEmail(email))
        validateForm()
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, passwordError = validatePassword(password))
        validateForm()
    }

    private fun validateEmail(email: String): String? {
        return if (email.isBlank() || !email.contains("@")) "Email inválido" else null
    }

    private fun validatePassword(password: String): String? {
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        return when {
            password.length < 6 -> "A senha deve ter pelo menos 6 caracteres"
            !hasLetter || !hasDigit -> "A senha deve conter letra e número"
            else -> null
        }
    }

    private fun validateForm() {
        val emailValid = _uiState.value.emailError == null && _uiState.value.email.isNotBlank()
        val passwordValid = _uiState.value.passwordError == null && _uiState.value.password.isNotBlank()
        _uiState.value = _uiState.value.copy(isFormValid = emailValid && passwordValid)
    }

    fun login(navController: NavController) {
        _uiState.value = _uiState.value.copy(isLoading = true, loginError = null)
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCustomer()
                if (response.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(isLoading = false, customer = response[0])
                    navController.navigate("payments")
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, loginError = "Usuário não encontrado")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, loginError = "Erro ao conectar com o servidor")
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isFormValid: Boolean = false,
    val isLoading: Boolean = false,
    val loginError: String? = null,
    val customer: Customer? = null
)
