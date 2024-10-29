package bat.konst.kandinskyclient.ui.styles.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun TextH3(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        // color = Color(0xFF636067),
        fontFamily = FontFamily.SansSerif, // Roboto Font
        fontWeight = FontWeight(400),
        fontSize = 16.sp,
        modifier = Modifier
            .then(modifier)
    )
}