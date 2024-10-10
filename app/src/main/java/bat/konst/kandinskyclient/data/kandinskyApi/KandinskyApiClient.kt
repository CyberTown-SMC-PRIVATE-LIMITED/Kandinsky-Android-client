package bat.konst.kandinskyclient.data.kandinskyApi

import bat.konst.kandinskyclient.data.kandinskyApi.callbackAdapter.ResultCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
Использование:
        CoroutineScope(Dispatchers.IO).launch {
            KandinskyApiClient.apiService.getSomething()
                .onSuccess {
                    CoroutineScope(Dispatchers.Main).launch {
                        val something = it
                        // обработка результата
                    }
                }
                .onFailure {
                    CoroutineScope(Dispatchers.Main).launch {
                        val error = it
                        // обработка ошибки
                    }
                }
        }

*/

object KandinskyApiClient {
    private const val BASE_URL = "https://api-key.fusionbrain.ai/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ResultCallAdapterFactory()) // Call Adapter для callbacks
            .build()
    }

    val apiService: KandinskyApiService by lazy {
        retrofit.create(KandinskyApiService::class.java)
    }
}
