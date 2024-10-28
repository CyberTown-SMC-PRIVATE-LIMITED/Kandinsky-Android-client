package bat.konst.kandinskyclient.data.kandinskyApi

import bat.konst.kandinskyclient.data.kandinskyApi.models.FusionBrainVersions
import bat.konst.kandinskyclient.data.kandinskyApi.models.GenerateParams
import bat.konst.kandinskyclient.data.kandinskyApi.models.GenerateRequest
import bat.konst.kandinskyclient.data.kandinskyApi.models.GenerateResult
import bat.konst.kandinskyclient.data.kandinskyApi.models.Styles
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface KandinskyApiService {

    // получение списка стилей
    @GET("https://cdn.fusionbrain.ai/static/styles/key")
    suspend fun getStyles(): Response<Styles>


    // получение списка версий модели
    @GET("key/api/v1/models")
    suspend fun getModels(
        @Header("X-Key") key: String,
        @Header("X-Secret") secret: String,
    ): Response<FusionBrainVersions>


    // запрос на генерацию изображения
    @Multipart
    @POST("/key/api/v1/text2image/run")
    suspend fun sendGgenerateImageRequest(
        @Header("X-Key") key: String,
        @Header("X-Secret") secret: String,
        @Part("model_id") modelId: RequestBody,
        @Part("params") params: RequestBody
    ): Response<GenerateResult>


    // проверка статуса запроса и получение готового изображения
    @GET("/key/api/v1/text2image/status/{id}")
    suspend fun getRequesrtStatusOrImage(
        @Header("X-Key") key: String,
        @Header("X-Secret") secret: String,
        @Path("id") id: String
    ): Response<GenerateResult>

}
