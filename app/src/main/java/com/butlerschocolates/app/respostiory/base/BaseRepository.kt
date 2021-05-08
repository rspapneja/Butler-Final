package com.butlerschocolates.app.respostiory.base


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.butlerschocolates.app.api.ApiHandler
import com.butlerschocolates.app.api.Resource
import com.butlerschocolates.app.global.GlobalConstants
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

open class BaseRepository {

     suspend fun <T > apiCall(call: suspend () -> Response<T>):MutableLiveData<Resource<T>> {
         var homeData = MutableLiveData<Resource<T>>()
         val response: Response<T>
         try {
             response = call.invoke()  //or
             if (response!!.isSuccessful) {
                 homeData.postValue(Resource.success(response.body()));
             }
             else{
                 homeData.postValue(Resource.error(UnknownError(GlobalConstants.ERROR_WENT_WRONG), null))
             }
         }catch (e:Exception)
         {
             homeData.postValue(Resource.error(e, null))
         }
         return homeData
     }






    suspend fun <T > safeApiCall(call: suspend () -> Response<T>)= liveData(Dispatchers.IO) {
      //  var homeData = MutableLiveData<ApiHandler<T>>()
        val response: Response<T>
        try {
            response = call.invoke()  //or
            if (response!!.isSuccessful) {
                emit(ApiHandler.Success(response.body()))
            }
            else{
                emit(ApiHandler.Error(UnknownError(GlobalConstants.ERROR_WENT_WRONG), null))
            }
        }catch (e:Exception)
        {
            emit(ApiHandler.Error(UnknownError(GlobalConstants.ERROR_WENT_WRONG), null))
         //   homeData.postValue(ApiHandler.error(e, null))
        }
    }



     suspend fun <T > apiCallRequest(call: suspend () -> Response<T>)= liveData(Dispatchers.IO) {
         var homeData = MutableLiveData<Resource<T>>()
         val response: Response<T>
         try {
             response = call.invoke()  //or
             if (response!!.isSuccessful) {
                 emit(Resource.success(response.body()))
             }
             else{
                 emit(Resource.error(UnknownError(GlobalConstants.ERROR_WENT_WRONG), null))
             }
         }catch (e:Exception)
         {
             emit(Resource.error(e, null))
            // homeData.postValue(Resource.error(e, null))
         }
     }
 }