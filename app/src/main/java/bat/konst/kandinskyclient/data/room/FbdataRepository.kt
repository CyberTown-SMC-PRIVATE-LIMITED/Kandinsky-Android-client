package bat.konst.kandinskyclient.data.room

import bat.konst.kandinskyclient.data.room.entity.Request
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FbdataRepository(private val fbdataDao: FbdataDao)    {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    suspend fun addRequest(request: Request, onSuccess: () -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            fbdataDao.addRequest(request)
            coroutineScope.launch(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    suspend fun updateRequest(request: Request, onSuccess: () -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            fbdataDao.updateRequest(request)
            coroutineScope.launch(Dispatchers.Main) {
                onSuccess()
            }
        }
    }


    suspend fun deleteRequest(request: Request, onSuccess: () -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            fbdataDao.deleteRequest(request)
            coroutineScope.launch(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    suspend fun getRequest(md5: String, onSuccess: (Request) -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val request = fbdataDao.getRequest(md5)
            coroutineScope.launch(Dispatchers.Main) {
                onSuccess(request)
            }
        }
    }

}