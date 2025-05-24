package ru.ssshteam.potatocoder228.messenger


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Brightness6
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.ssshteam.potatocoder228.messenger.dto.UserAuthDTO
import ru.ssshteam.potatocoder228.messenger.requests.RegistrationRequests.Companion.registrationRequest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
@Preview
fun RegistrationPage(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { },
                icon = { Icon(Icons.Outlined.Brightness6, contentDescription = "Brightness Mode") },
                onClick = {
                    scope.launch {

                    }
                }
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("ShhhChat", style = MaterialTheme.typography.headlineMedium)
                    }
                })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxSize().align(CenterHorizontally),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.align(
                        CenterVertically
                    )
                ) {
                    Text(
                        text = "Registration",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                    )
                    RegistrationForm(
                        navController,
                        snackbarHostState,
                        Modifier.align(alignment = Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
@Preview
fun RegistrationForm(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier
) {
    val rowWidth = 200.dp
    var loginInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var repeatPasswordInput by remember { mutableStateOf("") }
    var repeatPasswordVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier.padding(8.dp)
        ) {
            TextField(
                modifier = Modifier.align(CenterHorizontally).padding(10.dp),
                value = loginInput,
                onValueChange = { loginInput = it },
                label = { Text("Login") },
                singleLine = true,
                placeholder = { Text("Login") },
                trailingIcon = {
                    val image = Icons.Filled.Clear

                    val description = "Clear"

                    IconButton(onClick = { loginInput = "" }) {
                        Icon(imageVector = image, description)
                    }
                })
            TextField(
                modifier = Modifier.align(CenterHorizontally).padding(10.dp),
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text("Password") },
                singleLine = true,
                placeholder = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                })
            TextField(
                modifier = Modifier.align(CenterHorizontally).padding(10.dp),
                value = repeatPasswordInput,
                onValueChange = { repeatPasswordInput = it },
                label = { Text("RepeatPassword") },
                singleLine = true,
                placeholder = { Text("RepeatPassword") },
                visualTransformation = if (repeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (repeatPasswordVisible) Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description =
                        if (repeatPasswordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { repeatPasswordVisible = !repeatPasswordVisible }) {
                        Icon(imageVector = image, description)
                    }
                })
            Button(
                modifier = Modifier.align(CenterHorizontally).padding(10.dp, 0.dp, 10.dp, 0.dp),
                onClick = {
                    scope.launch {
                        registrationRequest(UserAuthDTO(loginInput, passwordInput), navController, snackbarHostState)
                    }
                }) {
                Text("Registration")
            }
            TextButton(
                modifier = Modifier.align(CenterHorizontally).padding(10.dp, 0.dp, 10.dp, 0.dp),
                onClick = {
                    navController.navigate(PageRoutes.SignInPage.route)
                }) {
                Text("Sign in")
            }
        }
    }
}