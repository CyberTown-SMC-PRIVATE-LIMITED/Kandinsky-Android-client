package bat.konst.kandinskyclient.data.kandinskyApi.models

import com.google.gson.annotations.SerializedName

data class GenerateResult(
    @SerializedName("model_status")
    val modelStatus: String = "",  // DISABLED_BY_QUEUE при недоступновти модели
    val uuid: String = "",
    val status: String = "",
    val images: List<String> = listOf(),
    val censored: Boolean = false,
    val generationTime: Int = 0
)
