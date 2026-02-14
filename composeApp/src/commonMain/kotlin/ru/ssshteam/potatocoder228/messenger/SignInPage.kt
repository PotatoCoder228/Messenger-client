package ru.ssshteam.potatocoder228.messenger


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Brightness6
import androidx.compose.material3.Button
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults.rememberTooltipPositionProvider
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.ssshteam.potatocoder228.messenger.viewmodels.SignInViewModel

@Composable
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
            if (viewModel.topBarModifier.value == null) {
                Modifier.fillMaxWidth().also { viewModel.topBarModifier.value = it }
            }
            authTopBar()
        },
    ) { innerPadding ->
        if (viewModel.cardBoxModifier.value == null) {
            Modifier.padding(innerPadding).fillMaxSize().onPreviewKeyEvent {
                viewModel.onEnterAction(it, navController)
            }.also { viewModel.cardBoxModifier.value = it }
        }
        SignInForm(
            navController
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun authTopBar(
    viewModel: SignInViewModel = viewModel { SignInViewModel() }
) {
    viewModel.topBarModifier.value?.let {
        TopAppBar(
            title = {
                Row(
                    modifier = it, horizontalArrangement = Arrangement.Center
                ) {
                    Text("ShhhChat", style = MaterialTheme.typography.headlineLarge)
                }
            })
    }
}


@Composable
fun SignInForm(
    navController: NavHostController, viewModel: SignInViewModel = viewModel { SignInViewModel() }
) {
    viewModel.cardBoxModifier.value?.let {
        Box(modifier = it) {
            if (viewModel.fieldsTitleModifier.value == null) {
                Modifier.align(alignment = Center).padding(0.dp, 0.dp, 0.dp, 350.dp)
                    .also { viewModel.fieldsTitleModifier.value = it }
            }
            fieldsTitle()
            if (viewModel.fieldsCardModifier.value == null) {
                Modifier.align(Center).padding(8.dp).requiredWidth(300.dp)
                    .also { viewModel.fieldsCardModifier.value = it }
            }
            fieldsCard(navController)
        }
    }
}


@Composable
fun fieldsTitle(
    viewModel: SignInViewModel = viewModel { SignInViewModel() }
) {
    viewModel.fieldsTitleModifier.value?.let {
        Text(
            text = "Авторизация",
            style = MaterialTheme.typography.titleLarge,
            modifier = it,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun fieldsCard(
    navController: NavHostController, viewModel: SignInViewModel = viewModel { SignInViewModel() }
) {
    viewModel.fieldsCardModifier.value?.let {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ), modifier = it,
            colors = CardColors(
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.colorScheme.onSurface,
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.colorScheme.onSurface
            )
        ) {
            if (viewModel.loginFieldModifier.value == null) {
                Modifier.align(CenterHorizontally).padding(10.dp)
                    .also { viewModel.loginFieldModifier.value = it }
            }

            loginField()

            if (viewModel.passwordFieldModifier.value == null) {
                Modifier.align(CenterHorizontally).padding(10.dp)
                    .also { viewModel.passwordFieldModifier.value = it }
            }

            passwordField()

            if (viewModel.authButtonModifier.value == null) {
                Modifier.align(CenterHorizontally).padding(10.dp, 0.dp, 10.dp, 0.dp)
                    .also { viewModel.authButtonModifier.value = it }
            }
            Row(Modifier.width(200.dp).height(35.dp).padding(0.dp, 5.dp)) {
                Checkbox(
                    checked = viewModel.rememberPassChecked.value,
                    modifier = Modifier.padding(3.dp, 0.dp).align(CenterVertically),
                    onCheckedChange = { viewModel.rememberPassChecked.value = it }
                )
                Text(
                    "Запомнить меня",
                    modifier = Modifier.align(CenterVertically),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            authButton(navController)
            if (viewModel.regButtonModifier.value == null) {
                Modifier.align(CenterHorizontally).padding(10.dp, 0.dp, 10.dp, 0.dp)
                    .also { viewModel.regButtonModifier.value = it }
            }
            regNavButton(navController)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun loginField(viewModel: SignInViewModel = viewModel { SignInViewModel() }) {
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

                TooltipBox(
                    positionProvider = rememberTooltipPositionProvider(
                        TooltipAnchorPosition.Above,
                        5.dp
                    ),
                    tooltip = {
                        PlainTooltip { Text("Очистить поле логина") }
                    },
                    state = rememberTooltipState()
                ) {
                    IconButton(onClick = { viewModel.loginInput.value = "" }) {
                        Icon(imageVector = image, description)
                    }
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun passwordField(viewModel: SignInViewModel = viewModel { SignInViewModel() }) {
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

                TooltipBox(
                    positionProvider = rememberTooltipPositionProvider(
                        TooltipAnchorPosition.Above,
                        5.dp
                    ),
                    tooltip = {
                        PlainTooltip { Text("Включить/выключить видимость пароля") }
                    },
                    state = rememberTooltipState()
                ) {
                    IconButton(onClick = {
                        viewModel.passwordVisible.value = !viewModel.passwordVisible.value
                    }) {
                        Icon(imageVector = image, description)
                    }
                }
            })
    }
}

@Composable
fun authButton(
    navController: NavHostController, viewModel: SignInViewModel = viewModel { SignInViewModel() }
) {
    viewModel.authButtonModifier.value?.let {
        Button(
            modifier = it, onClick = {
                viewModel.signIn(navController)
            }) {
            Text("Авторизоваться")
        }
    }
}

@Composable
fun regNavButton(
    navController: NavHostController, viewModel: SignInViewModel = viewModel { SignInViewModel() }
) {
    viewModel.regButtonModifier.value?.let {
        TextButton(
            modifier = it, onClick = {
                navController.navigate(PageRoutes.RegistrationPage.route)
            }) {
            Text("Регистрация")
        }
    }
}