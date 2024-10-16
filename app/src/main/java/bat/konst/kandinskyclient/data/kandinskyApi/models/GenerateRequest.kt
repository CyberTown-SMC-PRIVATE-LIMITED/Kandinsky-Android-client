package bat.konst.kandinskyclient.data.kandinskyApi.models

data class GenerateRequest(
    val generateParams: GenerateParams,
    val negativePromptUnclip: String,
    val style: String,
    val height: Int = 1024,
    val numImages: Int = 1,
    val type: String = "GENERATE",
    val width: Int = 1024
)