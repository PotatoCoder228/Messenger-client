package ru.ssshteam.potatocoder228.messenger.requests


import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.ssshteam.potatocoder228.messenger.PageRoutes
import ru.ssshteam.potatocoder228.messenger.datastore
import ru.ssshteam.potatocoder228.messenger.dto.TokenDTO
import ru.ssshteam.potatocoder228.messenger.dto.UserAuthDTO
import ru.ssshteam.potatocoder228.messenger.httpClient
import ru.ssshteam.potatocoder228.messenger.httpHost
import ru.ssshteam.potatocoder228.messenger.token
import kotlin.uuid.ExperimentalUuidApi

class SignInRequests {
    companion object {

        @OptIn(ExperimentalUuidApi::class)
        private suspend fun successAction(
            httpResponse: HttpResponse,
            navController: NavHostController
        ) {
            val tokenDto: TokenDTO = httpResponse.body()

            datastore?.saveCookie("token", tokenDto.token)
            datastore?.saveCookie("userId", tokenDto.userId.toString())

            token = mutableStateOf(tokenDto)
            navController.navigate(PageRoutes.MessagesPage.route)
        }

        private suspend fun errorAction(
            httpResponse: HttpResponse, snackbarHostState: SnackbarHostState
        ) {
            snackbarHostState.showSnackbar(
                message = "Sign in error. Reason: ${httpResponse.status.description}, ${httpResponse.body<String>()}",
                actionLabel = "Ok",
                duration = SnackbarDuration.Indefinite
            )
        }

        private suspend fun exceptionAction(e: Throwable, snackbarHostState: SnackbarHostState) {
            snackbarHostState.showSnackbar(
                message = "Sign in error. Reason: ${e.message}",
                actionLabel = "Ok",
                duration = SnackbarDuration.Indefinite
            )
        }

        suspend fun signInRequest(
            userAuthDTO: UserAuthDTO,
            navController: NavHostController,
            snackbarHostState: SnackbarHostState
        ) {
            try {
                val httpResponse: HttpResponse = httpClient.post("$httpHost/auth/signin") {
                    contentType(ContentType.Application.Json)
                    setBody(userAuthDTO)
                }
                if (httpResponse.status.value in 200..299) {
                    successAction(httpResponse, navController)
                } else {
                    errorAction(httpResponse, snackbarHostState)
                }
            } catch (e: Throwable) {
                exceptionAction(e, snackbarHostState)
            }
        }
    }
}