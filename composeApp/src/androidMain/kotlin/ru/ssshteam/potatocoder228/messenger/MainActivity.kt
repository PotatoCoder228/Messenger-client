package ru.ssshteam.potatocoder228.messenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        datastore = DataStore(openOrCreateDatabase("Cookie.db", MODE_PRIVATE, null))
        setContent {
            App()
        }
    }
}

@Preview(device = "spec:width=1920dp,height=1080dp,dpi=160")
@Composable
fun AppAndroidPreview() {
    App()
}