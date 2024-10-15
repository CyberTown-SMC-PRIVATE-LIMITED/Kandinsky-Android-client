package bat.konst.kandinskyclient.di

import bat.konst.kandinskyclient.data.kandinskyApi.KandinskyApiRepository
import bat.konst.kandinskyclient.data.kandinskyApi.KandinskyApiService
import bat.konst.kandinskyclient.data.kandinskyApi.callbackAdapter.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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

@Module
@InstallIn(SingletonComponent::class)
object KandinskyApiModule {
    @Provides
    fun baseUrl() = "https://api-key.fusionbrain.ai/"

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): KandinskyApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // .addCallAdapterFactory(ResultCallAdapterFactory())
            .build()
            .create(KandinskyApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideKandinskyApiRepository(kandinskyApiService: KandinskyApiService): KandinskyApiRepository {
        return KandinskyApiRepository(kandinskyApiService)
    }

}
