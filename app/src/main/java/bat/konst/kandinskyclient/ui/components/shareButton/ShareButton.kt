package bat.konst.kandinskyclient.ui.components.shareButton

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import bat.konst.kandinskyclient.R
import java.io.File

// Кнопка "Поделиться"
@Composable
fun ShareButton(filePath: String, context: Context) {
    FloatingActionButton(
        shape = CircleShape,
        onClick = {
            // https://readmedium.com/android-kotlin-jetpack-compose-file-sharing-using-fileprovider-and-android-sharesheet-3b8a7b9fb82d
            // Ко всему этому нужно provider (разрешение) в Manifest
            // и добавить в res/xml filepaths.xml !!!!

            try {
                val imageFile = File(filePath)
                val uri = FileProvider.getUriForFile(
                    context,
                    "bat.konst.kandinskyclient.fileprovider",
                    imageFile
                )
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uri)
                    type = "image/jpeg"
                }
                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val shareIntent = Intent.createChooser(sendIntent, null)
                ContextCompat.startActivity(context, shareIntent, null)
            } catch (e: Exception) {
                Log.d("KandinskyClient", e.toString())
            }
        }) {
        Icon(imageVector = Icons.Default.Share, contentDescription = stringResource(id = R.string.ps_share))
    }
}
