package com.butlerschocolates.app.viewmodels.payment

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.butlerschocolates.app.api.Resource
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.loyality.chkloyalty.CheckLoyaltyRequestBody
import com.butlerschocolates.app.model.orderInfo.OrderInfoRequestBody
import com.butlerschocolates.app.model.payment.PaymentRequestBody
import com.butlerschocolates.app.model.storetiming.StoreTimingRequestBody

import com.butlerschocolates.app.respostiory.payment.PaymentApiResponse
import com.butlerschocolates.app.respostiory.payment.PaymentRepository
import com.butlerschocolates.app.respostiory.storetimimg.StoreTimingApiResponse
import com.butlerschocolates.app.respostiory.storetimimg.StoreTimingRepository
import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers

class PaymentVewModel : BaseViewModel() {

    private var mutableLiveData: MutableLiveData<StoreTimingApiResponse?>? = null
    private var repository: StoreTimingRepository? = null

    private var paymentRepository: PaymentRepository? = null
    private var paymentLiveData: MutableLiveData<PaymentApiResponse?>? = null

    fun init()
    {
        if (mutableLiveData != null) {
            return
        }
        repository = StoreTimingRepository.instance;
    }

    fun paymentInit()
    {
        if (paymentLiveData != null) { return }
        paymentRepository = PaymentRepository.instance;
    }

    fun ontStoreTimingRequest(content: FragmentActivity,requestBody: StoreTimingRequestBody): MutableLiveData<StoreTimingApiResponse?>
    {
        setIsLoading(true,content)
        mutableLiveData = repository!!.geStoreTimingResponse(requestBody)
        return mutableLiveData!!
    }

     fun onCardPaymentRequest(content: FragmentActivity, requestBody: PaymentRequestBody): MutableLiveData<PaymentApiResponse?>
    {
        setIsLoading(true,content)
        paymentLiveData = paymentRepository!!.doCardPayment(requestBody)
        return paymentLiveData!!
    }

    fun getOrderInfo(appRequestBody: OrderInfoRequestBody) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = appRepository.getOrderInfo(appRequestBody)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()!!))
            } else {
                emit(Resource.error(UnknownError(GlobalConstants.ERROR_WENT_WRONG), null))
            }
        } catch (e: Exception) {
            emit(Resource.error(e, null))
        }
    }

    fun checkLoyalty(appRequestBody: CheckLoyaltyRequestBody) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = appRepository.checkLoyalty(appRequestBody)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()!!))
            } else {
                emit(Resource.error(UnknownError(GlobalConstants.ERROR_WENT_WRONG), null))
            }
        } catch (e: Exception) {
            emit(Resource.error(e, null))
        }
    }
}