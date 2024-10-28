package bat.konst.kandinskyclient.ui.components.generatingmessage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// bottom bar c Box и сообщением "Идёт генерация изображений"
@Composable
fun GeneratingMessage(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .height(25.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSurface)
            .then(modifier)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 0.dp)
                .align(Alignment.CenterStart),
            color = MaterialTheme.colorScheme.surface
        )
    }
}