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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.ssshteam.potatocoder228.messenger.viewmodels.RegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun RegistrationPage(
    navController: NavHostController,
    onThemeChange: () -> Unit,
    viewModel: RegistrationViewModel = viewModel { RegistrationViewModel() }
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = viewModel.snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.changeTheme(onThemeChange)
                }
            ) {
                Icon(Icons.Outlined.Brightness6, contentDescription = "Сменить тему")
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("ShhhChat", style = MaterialTheme.typography.headlineLarge)
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
                        text = "Регистрация",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(alignment = CenterHorizontally)
                    )
                    RegistrationForm(
                        navController,
                        Modifier.align(alignment = CenterHorizontally)
                    )
                }
            }
        }
    }

}

@Composable
@Preview
fun RegistrationForm(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: RegistrationViewModel = viewModel { RegistrationViewModel() }
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier.padding(8.dp)
        ) {
            TextField(
                modifier = Modifier.align(CenterHorizontally).padding(10.dp),
                value = viewModel.loginInput.value,
                onValueChange = { viewModel.loginInput.value = it },
                label = { Text("Логин") },
                singleLine = true,
                placeholder = { Text("Логин") },
                trailingIcon = {
                    val image = Icons.Filled.Clear

                    val description = "Очистить"

                    IconButton(onClick = { viewModel.loginInput.value = "" }) {
                        Icon(imageVector = image, description)
                    }
                })
            TextField(
                modifier = Modifier.align(CenterHorizontally).padding(10.dp),
                value = viewModel.passwordInput.value,
                onValueChange = { viewModel.passwordInput.value = it },
                label = { Text("Пароль") },
                singleLine = true,
                placeholder = { Text("Пароль") },
                visualTransformation = if (viewModel.passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (viewModel.passwordVisible.value) Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (viewModel.passwordVisible.value) "Скрыть" else "Показать"

                    IconButton(onClick = {
                        viewModel.passwordVisible.value = !viewModel.passwordVisible.value
                    }) {
                        Icon(imageVector = image, description)
                    }
                })
            TextField(
                modifier = Modifier.align(CenterHorizontally).padding(10.dp),
                value = viewModel.repeatPasswordInput.value,
                onValueChange = { viewModel.repeatPasswordInput.value = it },
                label = { Text("Повторите пароль") },
                singleLine = true,
                isError = viewModel.repeatHasErrors(),
                placeholder = { Text("Повторите пароль") },
                visualTransformation = if (viewModel.repeatPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (viewModel.repeatPasswordVisible.value) Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description =
                        if (viewModel.repeatPasswordVisible.value) "Скрыть пароль" else "Показать пароль"

                    IconButton(onClick = {
                        viewModel.repeatPasswordVisible.value =
                            !viewModel.repeatPasswordVisible.value
                    }) {
                        Icon(imageVector = image, description)
                    }
                })
            Button(
                modifier = Modifier.align(CenterHorizontally).padding(10.dp, 0.dp, 10.dp, 0.dp),
                onClick = {
                    viewModel.registrate(navController)
                }) {
                Text("Зарегистрироваться")
            }
            TextButton(
                modifier = Modifier.align(CenterHorizontally).padding(10.dp, 0.dp, 10.dp, 0.dp),
                onClick = {
                    navController.navigate(PageRoutes.SignInPage.route)
                }) {
                Text("Перейти к авторизации")
            }
        }
    }
}