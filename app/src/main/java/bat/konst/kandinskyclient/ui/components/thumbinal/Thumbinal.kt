package bat.konst.kandinskyclient.ui.components.thumbinal

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import bat.konst.kandinskyclient.R
import bat.konst.kandinskyclient.data.room.entity.StatusTypes
import coil.compose.AsyncImage
import java.io.File

@Composable
fun Thumbinal(status: Int, imageThumbnailBase64: String, modifier: Modifier = Modifier, contentScale: ContentScale = ContentScale.None, onClick: ()-> Unit) {
    if (status == StatusTypes.NEW.value) {
        Image(
            painterResource(id = R.drawable.status_0),
            contentScale = contentScale,
            contentDescription = "new image",
            modifier = modifier
        )
        return
    }

    if (status == StatusTypes.PROCESSING.value) {
        Image(
            painterResource(id = R.drawable.status_q),
            contentScale = contentScale,
            contentDescription = "processing image",
            modifier = modifier
        )
        return
    }

    if (status == StatusTypes.ERROR.value) {
        Image(
            painterResource(id = R.drawable.status_e),
            contentScale = contentScale,
            contentDescription = "error generated image",
            modifier = modifier
        )
        return
    }

    if (status == StatusTypes.CENCORED.value) {
        Image(
            painterResource(id = R.drawable.status_cencored),
            contentScale = contentScale,
            contentDescription = "cencored image",
            modifier = modifier
        )
        return
    }

    if (status == StatusTypes.DONE.value) {
        if (imageThumbnailBase64 == "") {
            Image(
                painterResource(id = R.drawable.status_e),
                contentScale = contentScale,
                contentDescription = "error image",
                modifier = modifier
            )
            return
        }
        AsyncImage(
            model = File(imageThumbnailBase64),
            contentDescription = "generated image",
            contentScale = contentScale,
            modifier = modifier.clickable(onClick = onClick
            )
        )
        return
    }
}
