package bat.konst.kandinskyclient.ui.styles.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun TextDropBox(text: String) {
    Text(
        text,
        fontFamily = FontFamily.SansSerif, // Roboto Font
        fontSize = 16.sp,
        fontWeight = FontWeight(600)
    )
}