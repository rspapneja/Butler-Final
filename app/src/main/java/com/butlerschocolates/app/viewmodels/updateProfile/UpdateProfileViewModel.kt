package com.butlerschocolates.app.viewmodels.updateProfile

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.model.logout.LogoutRequestBody
import com.butlerschocolates.app.respostiory.logout.LogoutRepository
import com.butlerschocolates.app.respostiory.logout.LogoutResponse
import com.butlerschocolates.app.respostiory.updateProfile.UpdateProfileRepository
import com.butlerschocolates.app.respostiory.updateProfile.UpdateProfileResponse
import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel
import com.butlerschocolates.app.viewmodels.savecard.SavedCardViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap

class UpdateProfileViewModel : SavedCardViewModel() {

    private var mutableLiveData: MutableLiveData<UpdateProfileResponse?>? = null
    private var repository: UpdateProfileRepository? = null

    private var logoutRepository: LogoutRepository? = null
    private var logoutLiveData: MutableLiveData<LogoutResponse?>? = null

    fun init()
    {
        if (mutableLiveData != null) { return }
        repository = UpdateProfileRepository.instance;
    }

    fun logoutInitalize()
    {
        if (logoutLiveData != null) { return }
        logoutRepository = LogoutRepository.instance;
    }

    fun updateProfileRequest(@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>, @Part file: Array<MultipartBody.Part?>, activity: FragmentActivity): MutableLiveData<UpdateProfileResponse?>?
    {
        setIsLoading(true,activity)
        mutableLiveData = repository!!.updateProfile(activity, map,file)
        return mutableLiveData
    }

    fun updateProfileRequest(@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>, activity: FragmentActivity): MutableLiveData<UpdateProfileResponse?>?
    {
        setIsLoading(true,activity)
        mutableLiveData = repository!!.updateProfile(activity,map)
        return mutableLiveData
    }

    fun logoutRequest(requestBody: LogoutRequestBody, activity: FragmentActivity): MutableLiveData<LogoutResponse?>?
    {
        setIsLoading(true,activity)
        logoutLiveData = logoutRepository!!.logout(activity,requestBody)
        return logoutLiveData
    }
}