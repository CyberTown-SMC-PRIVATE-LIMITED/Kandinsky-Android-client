package bat.konst.kandinskyclient.data.kandinskyApi.models

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class GenerateRequest @OptIn(ExperimentalSerializationApi::class) constructor(
    val generateParams: GenerateParams,
    val negativePromptUnclip: String,
    val style: String,
    @EncodeDefault
    val height: Int = 1024,
    @EncodeDefault
    val numImages: Int = 1,
    @EncodeDefault
    val type: String = "GENERATE",
    @EncodeDefault
    val width: Int = 1024
)