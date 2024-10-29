package bat.konst.kandinskyclient.ui.styles.textfields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextFieldPrompt(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {}
){
    TextField(
        maxLines = 5,
        minLines = 4,
        value = value,
        textStyle = TextStyle(
            fontFamily = FontFamily.SansSerif, // Roboto Font
            fontSize = 16.sp,
            fontWeight = FontWeight(600),
        ),
    onValueChange = onValueChange,
    modifier = Modifier
        .fillMaxWidth()
        .height(120.dp)
        .verticalScroll(rememberScrollState())
        .then(modifier)
    )
}