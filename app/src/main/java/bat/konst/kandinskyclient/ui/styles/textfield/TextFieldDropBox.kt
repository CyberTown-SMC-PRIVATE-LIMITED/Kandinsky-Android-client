package bat.konst.kandinskyclient.ui.styles.textfield

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bat.konst.kandinskyclient.ui.screens.newrequest.StyleImage

@ExperimentalMaterial3Api
@Composable
fun TextFieldDropBox(
    value: String,
    styleImageURL: String,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {}
) {
    TextField(
        // The `menuAnchor` modifier must be passed to the text field to handle
        // expanding/collapsing the menu on click. A read-only text field has
        // the anchor type `PrimaryNotEditable`.
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        value = value,
        textStyle = TextStyle(
            // color = Color(0xFF636067),
            fontFamily = FontFamily.SansSerif, // Roboto Font
            fontSize = 16.sp,
            fontWeight = FontWeight(600),
        ),
        onValueChange = onValueChange,
        readOnly = true,
        singleLine = true,
        // label = { Text(stringResource(id = R.string.nrs_style)) },
        // leadingIcon = { StyleImage(state.styleImageURL) },
        trailingIcon = {
            Row() {
                StyleImage(styleImageURL)
                ExposedDropdownMenuDefaults.TrailingIcon(
                    modifier = Modifier.padding(top = 10.dp, start = 5.dp),
                    expanded = expanded
                )
            }

        },
        colors = ExposedDropdownMenuDefaults.textFieldColors(),
    )
}