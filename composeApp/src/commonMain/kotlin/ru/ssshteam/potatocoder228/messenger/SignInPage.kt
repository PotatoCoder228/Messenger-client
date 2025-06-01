package ru.ssshteam.potatocoder228.messenger


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.ssshteam.potatocoder228.messenger.viewmodels.SignInViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SignInPage(
    navController: NavHostController,
    onThemeChange: () -> Unit,
    viewModel: SignInViewModel = viewModel { SignInViewModel() }
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = viewModel.snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.changeTheme(onThemeChange)
                }) {
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
        val columnModifier =
            remember { mutableStateOf(Modifier.fillMaxSize().padding(innerPadding)) }
        Column(
            modifier = columnModifier.value,
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
                        text = "Авторизация",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(alignment = CenterHorizontally),
                    )
                    SignInForm(
                        navController, Modifier.align(alignment = CenterHorizontally)
                    )
                }
            }
        }
    }

}

@Composable
fun SignInForm(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: SignInViewModel = viewModel { SignInViewModel() }
) {
    Box(modifier = modifier.onPreviewKeyEvent {
        when {
            (it.key == Key.Enter && it.type == KeyEventType.KeyDown) -> {
                viewModel.signIn(navController)
                true
            }

            else -> false
        }
    }) {
        if(viewModel.fieldsCardModifier.value == null){
            Modifier.align(Center).padding(10.dp)
                .also { viewModel.fieldsCardModifier.value = it }
        }
        fieldsCard(navController)
    }
}

@Composable
fun fieldsCard(navController: NavHostController, viewModel: SignInViewModel = viewModel{SignInViewModel()}){
    viewModel.fieldsCardModifier.value?.let {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ), modifier = Modifier.padding(8.dp)
        ) {
            if(viewModel.loginFieldModifier.value == null){
                Modifier.align(CenterHorizontally).padding(10.dp)
                    .also { viewModel.loginFieldModifier.value = it }
            }

            loginField()

            if(viewModel.passwordFieldModifier.value == null){
                Modifier.align(CenterHorizontally).padding(10.dp)
                    .also { viewModel.passwordFieldModifier.value = it }
            }

            passwordField()

            if(viewModel.authButtonModifier.value == null){
                Modifier.align(CenterHorizontally).padding(10.dp, 0.dp, 10.dp, 0.dp)
                    .also { viewModel.authButtonModifier.value = it }
            }
            authButton(navController)
            if(viewModel.regButtonModifier.value == null){
                Modifier.align(CenterHorizontally).padding(10.dp, 0.dp, 10.dp, 0.dp)
                    .also { viewModel.regButtonModifier.value = it }
            }
            regNavButton(navController)
        }
    }
}



@Composable
fun loginField(viewModel: SignInViewModel = viewModel{SignInViewModel()}){
    viewModel.loginFieldModifier.value?.let {
        TextField(
            modifier = it,
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
    }
}

@Composable
fun passwordField(viewModel: SignInViewModel = viewModel{SignInViewModel()}){
    viewModel.passwordFieldModifier.value?.let {
        TextField(
            modifier = it,
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

                val description =
                    if (viewModel.passwordVisible.value) "Скрыть пароль" else "Показать пароль"

                IconButton(onClick = {
                    viewModel.passwordVisible.value = !viewModel.passwordVisible.value
                }) {
                    Icon(imageVector = image, description)
                }
            })
    }
}

@Composable
fun authButton(navController: NavHostController, viewModel: SignInViewModel = viewModel{SignInViewModel()}){
    viewModel.authButtonModifier.value?.let {
        Button(
            modifier = it,
            onClick = {
                viewModel.signIn(navController)
            }) {
            Text("Авторизоваться")
        }
    }
}

@Composable
fun regNavButton(navController: NavHostController, viewModel: SignInViewModel = viewModel{SignInViewModel()}){
    viewModel.regButtonModifier.value?.let {
        TextButton(
            modifier = it,
            onClick = {
                navController.navigate(PageRoutes.RegistrationPage.route)
            }) {
            Text("Регистрация")
        }
    }
}