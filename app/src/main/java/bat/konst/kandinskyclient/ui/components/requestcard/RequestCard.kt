package bat.konst.kandinskyclient.ui.components.requestcard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bat.konst.kandinskyclient.R
import bat.konst.kandinskyclient.data.room.entity.RequestJoinImage
import bat.konst.kandinskyclient.ui.components.thumbinal.Thumbinal
import bat.konst.kandinskyclient.ui.navigation.Route

// Карточка запроса с главного экрана
@Composable
fun RequestCard(
    request: RequestJoinImage,
    onNavigateTo: (Route) -> Unit = {},
) {
    Box(
        modifier= Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .clickable { onNavigateTo(Route.Request(request.md5)) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 4.dp)
        ) {
            Thumbinal(
                status = request.status,
                imageThumbnailBase64 = request.imageThumbnailBase64,
                modifier = Modifier
                    .height(70.dp)
                    .padding(horizontal = 10.dp)
                    .clip(RoundedCornerShape(20))                       // clip to the circle shape
                    .border(1.dp, Color.Gray, RoundedCornerShape(20)),
                contentScale = ContentScale.FillHeight,
                onClick = { onNavigateTo(Route.Request(request.md5)) }
            )

            Column (
                modifier = Modifier
                    .padding(horizontal = 3.dp)
            ) {
                // Стиль
                Row {
                    Text(
                        text = stringResource(id = R.string.ms_style),
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Light,
                        fontSize = 13.sp
                    )
                    Text(
                        text = request.style,
                        Modifier.padding(horizontal = 4.dp),
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }

                // Промпт
                Text(
                    text = request.prompt,
                    lineHeight = 14.sp,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Отрицательный промпт
                Text(
                    color = Color.Red,
                    text = request.negativePrompt,
                    lineHeight = 14.sp,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )


            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RequestCardPreview() {
    RequestCard(
        request = RequestJoinImage(
            md5 = "",
            prompt = "Промт запроса Промт запроса Промт запроса Промт запроса Промт запроса  Промт запроса Промт запроса Промт запроса Промт запроса Промт запроса Промт запроса ",
            negativePrompt = "Негативный промт Негативный промт Негативный промт Негативный промт Негативный промт Негативный промт Негативный промт Негативный промт Негативный промт Негативный промт",
            style = "DEFAULT",
            status = 0,
            imageThumbnailBase64 = ""
        )
    )
}
