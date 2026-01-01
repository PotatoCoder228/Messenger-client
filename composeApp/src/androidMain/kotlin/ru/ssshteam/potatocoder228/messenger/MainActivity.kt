package ru.ssshteam.potatocoder228.messenger

import android.app.Activity
import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import coil3.filePath
import coil3.toCoilUri
import ru.ssshteam.potatocoder228.messenger.internal.File


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
        );
        super.onCreate(savedInstanceState)
        datastore = DataStore(openOrCreateDatabase("Cookie.db", MODE_PRIVATE, null))
        fileChooser = AndroidFileChooser(this)
        setContent {
            App()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        if (requestCode == 2 && resultCode == RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            data?.data?.also { uri ->
                // Perform operations on the document using its URI.
                val file = uri.toCoilUri().filePath!!.let { java.io.File(it) }
                println(uri.toCoilUri().filePath)
                fileChooser?.addFile(
                    File(
                        uri.toCoilUri().filePath.toString(),
                        file.name,
                        file.extension,
                        file.totalSpace,
                        uri.toCoilUri().toString()
                    )
                )
                fileChooser?.addFileInputStream(
                    uri.toCoilUri().filePath.toString(),
                    contentResolver.openInputStream(uri) as Any
                )
                fileChooser?.unlockChooser()
            }
        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            data?.data?.also { uri ->
                // Perform operations on the document using its URI.
                println(uri.path)
                fileChooser?.addPathForDownloading(uri.path.toString())
                fileChooser?.addFileOutputStream(
                    uri.path.toString(),
                    contentResolver.openOutputStream(uri) as Any
                )
                fileChooser?.unlockChooser()
            }
        }
    }
}

@Preview(device = "spec:width=1920dp,height=1080dp,dpi=160")
@Composable
fun AppAndroidPreview() {
    App()
}