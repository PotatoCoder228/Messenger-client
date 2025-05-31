package ru.ssshteam.potatocoder228.messenger.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ru.ssshteam.potatocoder228.messenger.dto.UserAuthDTO
import ru.ssshteam.potatocoder228.messenger.requests.RegistrationRequests.Companion.registrationRequest

class RegistrationViewModel : ViewModel() {
    var loginInput = mutableStateOf("")
    var passwordInput = mutableStateOf("")
    var passwordVisible = mutableStateOf(false)
    var repeatPasswordInput = mutableStateOf("")
    var repeatPasswordVisible = mutableStateOf(false)
    val snackbarHostState = SnackbarHostState()

    fun registrate(navController: NavHostController) {
        viewModelScope.launch {
            registrationRequest(
                UserAuthDTO(loginInput.value, passwordInput.value),
                navController,
                snackbarHostState
            )
        }
    }

    fun changeTheme(onThemeChange: () -> Unit) {
        viewModelScope.launch {
            onThemeChange()
        }
    }
}