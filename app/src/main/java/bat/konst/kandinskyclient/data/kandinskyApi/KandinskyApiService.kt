package bat.konst.kandinskyclient.data.kandinskyApi

import bat.konst.kandinskyclient.data.kandinskyApi.models.Styles
import retrofit2.Response
import retrofit2.http.GET

interface KandinskyApiService {
    // получение списка стилей
    @GET("https://cdn.fusionbrain.ai/static/styles/key")
    suspend fun getStyles(): Response<Styles>
}