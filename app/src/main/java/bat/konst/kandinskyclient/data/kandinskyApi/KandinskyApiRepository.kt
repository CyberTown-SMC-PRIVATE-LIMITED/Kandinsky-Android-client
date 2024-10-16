package bat.konst.kandinskyclient.data.kandinskyApi

import android.util.Log
import bat.konst.kandinskyclient.app.KANDINSKY_GENERATE_RESULT_FAIL
import bat.konst.kandinskyclient.data.kandinskyApi.models.GenerateParams
import bat.konst.kandinskyclient.data.kandinskyApi.models.GenerateRequest
import bat.konst.kandinskyclient.data.kandinskyApi.models.GenerateResult
import bat.konst.kandinskyclient.data.kandinskyApi.models.Style
import bat.konst.kandinskyclient.data.kandinskyApi.models.Styles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class KandinskyApiRepository @Inject constructor(private val apiService: KandinskyApiService) {
    suspend fun getStyles(): Styles {
        var styles = Styles()
        styles.add(
            Style(
                "",
                "DEFAULT",
                "DEFAULT",
                "DEFAULT",
            )
        )

        withContext(Dispatchers.IO) {
            try {
                apiService.getStyles().let {
                    if (it.isSuccessful) {
                        styles = it.body()!!
                    } else {
                        styles = Styles()
                    }
                }
            } catch (e: Exception) {
                Log.d("KandinskyApiRepository", e.toString())
            }
        }
        return styles
    }


    suspend fun sendGgenerateImageRequest(key: String, secret: String, prompt: String, negativePrompt: String, style: String, modelId: String): GenerateResult? {
        var generateResult: GenerateResult? = null
        withContext(Dispatchers.IO) {
            try {
                val params = GenerateRequest(
                    generateParams = GenerateParams(
                        query = prompt,
                    ),
                    negativePromptUnclip = negativePrompt,
                    style = style
                )
                val paramsJson = Json.encodeToString(params)
                apiService.sendGgenerateImageRequest(
                    key = key,
                    secret = secret,
                    modelId = modelId.toRequestBody(),
                    params = paramsJson.toRequestBody(contentType = "application/json".toMediaType())
                ).let {
                    if (it.isSuccessful) {
                        generateResult = it.body()
                    } else {
                        Log.d("KandinskyApiRepository", it.errorBody().toString())
                        Log.d("KandinskyApiRepository", it.code().toString())
                    }
                }
            } catch (e: Exception) {
                Log.d("KandinskyApiRepository", e.toString())
            }
        }
        return generateResult
    }


    suspend fun getRequesrtStatusOrImage(key: String, secret: String, id: String): GenerateResult? {
        // вернёт null в случае проблем с запросом (скорее всего проблема авторизации)
        var generateResult: GenerateResult? = null
        withContext(Dispatchers.IO) {
            try {
                apiService.getRequesrtStatusOrImage(key = key, secret = secret, id = id).let {
                    if (it.isSuccessful) {
                        generateResult = it.body()
                    } else {
                        generateResult = GenerateResult(status = KANDINSKY_GENERATE_RESULT_FAIL)
                    }
                }
            } catch (e: Exception) {
                Log.d("KandinskyApiRepository", e.toString())
            }
        }
        return generateResult
    }

}