package bat.konst.kandinskyclient.data.kandinskyApi.models

import kotlinx.serialization.Serializable

@Serializable
data class GenerateParams(
    val query: String
)