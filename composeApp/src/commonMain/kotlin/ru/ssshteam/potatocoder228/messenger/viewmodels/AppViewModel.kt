package ru.ssshteam.potatocoder228.messenger.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    var theme = mutableStateOf(false)
    val onThemeChange = { theme.value = !theme.value }
}