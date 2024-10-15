package bat.konst.kandinskyclient.data.kandinskyApi

import android.util.Log
import bat.konst.kandinskyclient.data.kandinskyApi.models.Style
import bat.konst.kandinskyclient.data.kandinskyApi.models.Styles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
}