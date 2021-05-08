package com.butlerschocolates.app.respostiory

import com.butlerschocolates.app.api.ApiClient
import com.butlerschocolates.app.api.ApiInterface
import com.butlerschocolates.app.application.ButlersApplication
import com.butlerschocolates.app.model.changepass.UpdatePasswordAfterForgotPassRequestBody
import com.butlerschocolates.app.model.changepass.UpdatePasswordLoginUserRequestBody
import com.butlerschocolates.app.model.deleteSavedCard.RemoveSavedCardRequestBody
import com.butlerschocolates.app.model.forgotpass.ForgotPasswordRequestBody
import com.butlerschocolates.app.model.getSavedCard.GetSavedCardRequestBody
import com.butlerschocolates.app.model.home.HomeRequestBody
import com.butlerschocolates.app.model.loyality.LoyalityRequestBody
import com.butlerschocolates.app.model.loyality.addLoyaltyCard.AddLoyalityCardRequestBody
import com.butlerschocolates.app.model.loyality.chkloyalty.CheckLoyaltyRequestBody
import com.butlerschocolates.app.model.loyality.confirmTopupPayment.ConfirmTopupPaymentRequestBody
import com.butlerschocolates.app.model.notification.NotificationRequestBody
import com.butlerschocolates.app.model.orderInfo.OrderInfoRequestBody
import com.butlerschocolates.app.model.orderlist.OrderListRequestBody
import com.butlerschocolates.app.model.query.AddSubmitQueryRequestBody
import com.butlerschocolates.app.model.reorder.ReorderRequestBody
import com.butlerschocolates.app.model.resendotp.OtpRequestBody
import com.butlerschocolates.app.model.setting.SettingRequestBody
import com.butlerschocolates.app.model.support.add.comment.AddSupportCommentRequestBody
import com.butlerschocolates.app.model.support.add.ticket.AddSupportTicketRequestBody
import com.butlerschocolates.app.model.support.detail.SupportTicketDetailRequestBody
import com.butlerschocolates.app.model.support.get.orderlist.SupportOrderListRequestBody
import com.butlerschocolates.app.model.support.get.ticket.list.SupportTicketListRequestBody
import com.butlerschocolates.app.model.syncloyalty.SyncLoyaltyRequestBody
import com.butlerschocolates.app.respostiory.base.BaseRepository


class AppRepository : BaseRepository() {

    var apiInterface =
        ApiClient.getClient(ButlersApplication.instance!!).create(ApiInterface::class.java)

    suspend fun getOrderInfo(appRequestBody: OrderInfoRequestBody) =
        apiInterface.getOrderInfo(appRequestBody)

    suspend fun checkLoyalty(appRequestBody: CheckLoyaltyRequestBody) =
        apiInterface.checkLoyalty(appRequestBody)

    suspend fun getOrderStatusInfo(appRequestBody: OrderInfoRequestBody) = apiCallRequest {
        apiInterface.getOrderInfo(appRequestBody)
    }

    suspend fun getOrderList(appRequestBody: OrderListRequestBody) = apiCallRequest {
       apiInterface.getOrderList(appRequestBody)
    }

    suspend fun getHomeResponse2(homeRequestBody: HomeRequestBody) = safeApiCall {
        apiInterface.getHomeApiResponse2(homeRequestBody)
    }

    suspend fun getHomeResponse1(homeRequestBody: HomeRequestBody) = apiCallRequest {
        apiInterface.getHomeApiResponse2(homeRequestBody)
    }

    suspend fun reOrder(appRequestBody: ReorderRequestBody) = apiCallRequest {
        apiInterface.reOrder(appRequestBody)
    }

    suspend fun getSupportOrderList(appRequestBody: SupportOrderListRequestBody) = apiCallRequest {
        apiInterface.getSupportOrderList(appRequestBody)
    }

    suspend fun addSupportTicket(appRequestBody: AddSupportTicketRequestBody) = apiCallRequest {
        apiInterface.addSupportTicket(appRequestBody)
    }

    suspend fun getSupportTicketList(appRequestBody: SupportTicketListRequestBody) = apiCallRequest {
        apiInterface.getSupportTicketList(appRequestBody)
    }

    suspend fun addSupportComment(appRequestBody: AddSupportCommentRequestBody) = apiCallRequest {
        apiInterface.addSupportComment(appRequestBody)
    }

    suspend fun getSupportTicketDetail(appRequestBody: SupportTicketDetailRequestBody) = apiCallRequest {
        apiInterface.getSupportTicketDetail(appRequestBody)
    }

    suspend fun getAndUpdateAppSettingDetail(appRequestBody: SettingRequestBody) = apiCallRequest {
        apiInterface.getAndUpdateAppSettingDetail(appRequestBody)
    }

    suspend fun forgotPass(appRequestBody: ForgotPasswordRequestBody) = apiCallRequest {
        apiInterface.forgotpass(appRequestBody)
    }

    suspend fun updatePasswordAfterForgot(appRequestBody: UpdatePasswordAfterForgotPassRequestBody) = apiCallRequest {
        apiInterface.updatePasswordAfterForgot(appRequestBody)
    }

    suspend fun updatePasswordForLoginUser(appRequestBody: UpdatePasswordLoginUserRequestBody) = apiCallRequest {
        apiInterface.updatePasswordForLoginUser(appRequestBody)
    }

    suspend fun syncLoyalty(appRequestBody: SyncLoyaltyRequestBody) = apiCallRequest {
        apiInterface.syncLoyalty(appRequestBody)
    }


    suspend fun getSavedCards(appRequestBody: GetSavedCardRequestBody) = apiCallRequest {
        apiInterface.getSavedCards(appRequestBody)
    }

    suspend fun removeSavedCard(appRequestBody: RemoveSavedCardRequestBody) = apiCallRequest {
        apiInterface.removeSavedCard(appRequestBody)
    }

    suspend fun loyalty(appRequestBody: LoyalityRequestBody) = apiCallRequest {
        apiInterface.loyalty(appRequestBody)
    }

    suspend fun confirmTopupPayment(appRequestBody: ConfirmTopupPaymentRequestBody) = apiCallRequest {
        apiInterface.confirmTopupPayment(appRequestBody)
    }


    suspend fun notificationDetail(appRequestBody: NotificationRequestBody) = apiCallRequest {
        apiInterface.notificationDetail(appRequestBody)
    }

    suspend fun addLoyaltyCard(appRequestBody: AddLoyalityCardRequestBody) = apiCallRequest {
        apiInterface.addLoyaltyCard(appRequestBody)
    }

    suspend fun sendQuery(appRequestBody: AddSubmitQueryRequestBody) = apiCallRequest {
        apiInterface.sendQuery(appRequestBody)
    }

    suspend fun requestOtp(appRequestBody: OtpRequestBody) = apiCallRequest {
        apiInterface.resendOtp(appRequestBody)
    }
}