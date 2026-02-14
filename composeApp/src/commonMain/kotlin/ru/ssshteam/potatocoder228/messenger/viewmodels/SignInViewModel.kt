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
import ru.ssshteam.potatocoder228.messenger.currentUserId
import ru.ssshteam.potatocoder228.messenger.datastore
import ru.ssshteam.potatocoder228.messenger.dto.UserAuthDTO
import ru.ssshteam.potatocoder228.messenger.requests.SignInRequests.Companion.signInRequest
import ru.ssshteam.potatocoder228.messenger.token
import kotlin.uuid.ExperimentalUuidApi

class SignInViewModel : ViewModel() {
    var loginInput = mutableStateOf("")
    var passwordInput = mutableStateOf("")
    var passwordVisible = mutableStateOf(false)
    val snackbarHostState = SnackbarHostState()

    var rememberPassChecked = mutableStateOf(false)

    val authButtonModifier: MutableState<Modifier?> = mutableStateOf(null)
    val regButtonModifier: MutableState<Modifier?> = mutableStateOf(null)
    val loginFieldModifier: MutableState<Modifier?> = mutableStateOf(null)
    val passwordFieldModifier: MutableState<Modifier?> = mutableStateOf(null)
    val fieldsCardModifier: MutableState<Modifier?> = mutableStateOf(null)
    val fieldsTitleModifier: MutableState<Modifier?> = mutableStateOf(null)
    val cardBoxModifier: MutableState<Modifier?> = mutableStateOf(null)
    val topBarModifier: MutableState<Modifier?> = mutableStateOf(null)
    val onEnterAction = { it: KeyEvent, navController: NavHostController ->
        when {
            (it.key == Key.Enter && it.type == KeyEventType.KeyDown) -> {
                signIn(navController)
                true
            }

            else -> false
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun signIn(navController: NavHostController) {
        viewModelScope.launch {
            signInRequest(
                UserAuthDTO(loginInput.value, passwordInput.value), navController, snackbarHostState
            )
            if (rememberPassChecked.value) {
                datastore?.saveCookie("savePass", "true")
                datastore?.saveCookie("token", token!!.value.token)
                datastore?.saveCookie("refreshToken", token!!.value.refreshToken)
                datastore?.saveCookie("userId", token!!.value.userId.toString())
                currentUserId = token!!.value.userId
            }
        }
    }

    fun changeTheme(onThemeChange: () -> Unit) {
        viewModelScope.launch {
            onThemeChange()
        }
    }
}