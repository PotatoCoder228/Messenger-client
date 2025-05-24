package ru.ssshteam.potatocoder228.messenger.requests

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.ssshteam.potatocoder228.messenger.PageRoutes
import ru.ssshteam.potatocoder228.messenger.dto.UserAuthDTO
import ru.ssshteam.potatocoder228.messenger.httpClient
import ru.ssshteam.potatocoder228.messenger.httpHost

class RegistrationRequests {
    companion object {

        private suspend fun successAction(
            navController: NavHostController, snackbarHostState: SnackbarHostState
        ) {
            snackbarHostState.showSnackbar(
                message = "Registration success",
                actionLabel = "Ok",
                duration = SnackbarDuration.Short
            )
            navController.navigate(PageRoutes.SignInPage.route)
        }

        private suspend fun errorAction(
            httpResponse: HttpResponse, snackbarHostState: SnackbarHostState
        ) {
            snackbarHostState.showSnackbar(
                message = "Registration error. Reason: ${httpResponse.status.description}, ${httpResponse.status.description}",
                actionLabel = "Ok",
                duration = SnackbarDuration.Indefinite
            )
        }

        private suspend fun exceptionAction(e: Throwable, snackbarHostState: SnackbarHostState) {
            snackbarHostState.showSnackbar(
                message = "Registration error. Reason: ${e.message}",
                actionLabel = "Ok",
                duration = SnackbarDuration.Indefinite
            )
        }

        suspend fun registrationRequest(
            userAuthDTO: UserAuthDTO,
            navController: NavHostController,
            snackbarHostState: SnackbarHostState
        ) {
            try {
                val httpResponse: HttpResponse = httpClient.post("$httpHost/auth/signup") {
                    contentType(ContentType.Application.Json)
                    setBody(userAuthDTO)
                }
                if (httpResponse.status.value in 200..299) {
                    successAction(navController, snackbarHostState)
                } else {
                    errorAction(httpResponse, snackbarHostState)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState)
            }
        }
    }
}