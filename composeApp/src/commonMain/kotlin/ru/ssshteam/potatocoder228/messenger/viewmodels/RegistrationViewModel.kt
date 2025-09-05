package ru.ssshteam.potatocoder228.messenger.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
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
    val signUpButtonModifier: MutableState<Modifier?> = mutableStateOf(null)
    val regButtonModifier: MutableState<Modifier?> = mutableStateOf(null)
    val loginFieldModifier: MutableState<Modifier?> = mutableStateOf(null)
    val passwordFieldModifier: MutableState<Modifier?> = mutableStateOf(null)
    val repeatPasswordFieldModifier: MutableState<Modifier?> = mutableStateOf(null)
    val fieldsCardModifier: MutableState<Modifier?> = mutableStateOf(null)
    val fieldsTitleModifier: MutableState<Modifier?> = mutableStateOf(null)
    val cardBoxModifier: MutableState<Modifier?> = mutableStateOf(null)
    val topBarModifier: MutableState<Modifier?> = mutableStateOf(null)
    var approvalDataProcessingChecked = mutableStateOf(false)

    val onEnterAction = { it: KeyEvent, navController: NavHostController ->
        when {
            (it.key == Key.Enter && it.type == KeyEventType.KeyDown) -> {
                signUp(navController)
                true
            }

            else -> false
        }
    }

    fun repeatHasErrors(): Boolean {
        return repeatPasswordInput.value != passwordInput.value || repeatPasswordInput.value.length < 5
    }

    fun signUp(navController: NavHostController) {
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