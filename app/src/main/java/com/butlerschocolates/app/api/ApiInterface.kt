package com.butlerschocolates.app.api

import com.butlerschocolates.app.model.changepass.ChangePasswordAPI
import com.butlerschocolates.app.model.changepass.UpdatePasswordAfterForgotPassRequestBody
import com.butlerschocolates.app.model.changepass.UpdatePasswordLoginUserRequestBody
import com.butlerschocolates.app.model.deleteSavedCard.DeleteSavedCard
import com.butlerschocolates.app.model.deleteSavedCard.RemoveSavedCardRequestBody
import com.butlerschocolates.app.model.faq.FaqApi
import com.butlerschocolates.app.model.faq.FaqRequestBady
import com.butlerschocolates.app.model.feedback.FeedbackApi
import com.butlerschocolates.app.model.feedback.FeedbackRequestBody
import com.butlerschocolates.app.model.forgotpass.ForgotPasswordAPI
import com.butlerschocolates.app.model.forgotpass.ForgotPasswordRequestBody
import com.butlerschocolates.app.model.getSavedCard.GetSavedCardApi
import com.butlerschocolates.app.model.getSavedCard.GetSavedCardRequestBody
import com.butlerschocolates.app.model.home.HomeApi
import com.butlerschocolates.app.model.home.HomeRequestBody
import com.butlerschocolates.app.model.login.LoginApi
import com.butlerschocolates.app.model.login.LoginReqestBody
import com.butlerschocolates.app.model.logout.LogoutApi
import com.butlerschocolates.app.model.logout.LogoutRequestBody
import com.butlerschocolates.app.model.loyality.LoyalityApi
import com.butlerschocolates.app.model.loyality.LoyalityRequestBody
import com.butlerschocolates.app.model.loyality.addLoyaltyCard.AddLoyalityCardRequestBody
import com.butlerschocolates.app.model.loyality.addLoyaltyCard.AddLoyaltyCard
import com.butlerschocolates.app.model.loyality.chkloyalty.CheckLoyaltyApi
import com.butlerschocolates.app.model.loyality.chkloyalty.CheckLoyaltyRequestBody
import com.butlerschocolates.app.model.loyality.confirmTopupPayment.ConfirmTopupPaymentRequestBody
import com.butlerschocolates.app.model.loyality.confirmTopupPayment.ConfirmTopupPaymentApi
import com.butlerschocolates.app.model.notification.NotificationApi
import com.butlerschocolates.app.model.notification.NotificationRequestBody
import com.butlerschocolates.app.model.orderInfo.OrderInfoApi
import com.butlerschocolates.app.model.orderInfo.OrderInfoRequestBody
import com.butlerschocolates.app.model.orderlist.OrderListApI
import com.butlerschocolates.app.model.orderlist.OrderListRequestBody
import com.butlerschocolates.app.model.payment.PaymentRequestBody
import com.butlerschocolates.app.model.payment.PaymentApi
import com.butlerschocolates.app.model.query.AddSubmitQueryApi
import com.butlerschocolates.app.model.query.AddSubmitQueryRequestBody
import com.butlerschocolates.app.model.reorder.ReOrderApi
import com.butlerschocolates.app.model.reorder.ReorderRequestBody
import com.butlerschocolates.app.model.resendotp.OtpRequestBody
import com.butlerschocolates.app.model.resendotp.ResendOtpApi
import com.butlerschocolates.app.model.setting.SettingApi
import com.butlerschocolates.app.model.setting.SettingRequestBody
import com.butlerschocolates.app.model.signup.SignupApi
import com.butlerschocolates.app.model.signup.SignupRequestBody
import com.butlerschocolates.app.model.storedetail.StoreDetailApi
import com.butlerschocolates.app.model.storedetail.StoreDetailRequestBody
import com.butlerschocolates.app.model.storelist.StoreListApi
import com.butlerschocolates.app.model.storelist.StoreListRequestBody
import com.butlerschocolates.app.model.storetiming.StoreTimingAPI
import com.butlerschocolates.app.model.storetiming.StoreTimingRequestBody
import com.butlerschocolates.app.model.support.add.comment.AddSupportCommentApi
import com.butlerschocolates.app.model.support.add.comment.AddSupportCommentRequestBody
import com.butlerschocolates.app.model.support.add.ticket.AddSupportTicketApi
import com.butlerschocolates.app.model.support.add.ticket.AddSupportTicketRequestBody
import com.butlerschocolates.app.model.support.detail.SupportTicketDetailApi
import com.butlerschocolates.app.model.support.detail.SupportTicketDetailRequestBody
import com.butlerschocolates.app.model.support.get.orderlist.SupportOrderListApi
import com.butlerschocolates.app.model.support.get.orderlist.SupportOrderListRequestBody
import com.butlerschocolates.app.model.support.get.ticket.list.GetTicketListApi
import com.butlerschocolates.app.model.support.get.ticket.list.SupportTicketListRequestBody
import com.butlerschocolates.app.model.syncloyalty.SyncLoyaltyApi
import com.butlerschocolates.app.model.syncloyalty.SyncLoyaltyRequestBody
import com.butlerschocolates.app.model.updateProfile.UpdateProfileApi
import com.butlerschocolates.app.model.verifyotp.VerifyOtpApi
import com.butlerschocolates.app.model.verifyotp.VerifyOtpRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    @POST("pay")
    fun doCardPayment(@Body requestBody: PaymentRequestBody): Call<PaymentApi?>

    @POST("home")
    fun getHomeApiResponse(@Body requestBody: HomeRequestBody): Call<HomeApi?>

    @POST("cart")
    fun getStoreTimimg(@Body requestBody: StoreTimingRequestBody): Call<StoreTimingAPI?>

    @POST("home")
    suspend fun getHomeApiResponse2(@Body requestBody: HomeRequestBody): Response<HomeApi>

    @POST("register")
    fun signupApiResponse(@Body requestBody: SignupRequestBody): Call<SignupApi?>

    @POST("stores")
    fun getStoreList(@Body requestBody: StoreListRequestBody): Call<StoreListApi?>

    @POST("stores")
    suspend fun getStoreList1(@Body requestBody: StoreListRequestBody): Response<StoreListApi>

    @POST("stores//detail")
    fun getStoreDetail(@Body requestBody: StoreDetailRequestBody): Call<StoreDetailApi?>

    @POST("login/verify")
    fun getVerifyOtpResponse(@Body requestBody: VerifyOtpRequestBody): Call<VerifyOtpApi?>

    @POST("login")
    fun doLogin(@Body requestBody: LoginReqestBody): Call<LoginApi?>

    @POST("logout")
    fun doLogout(@Body requestBody: LogoutRequestBody): Call<LogoutApi?>

    @POST("faqs")
    fun getFaqs(@Body requestBody: FaqRequestBady): Call<FaqApi?>

    @POST("feedback")
    fun sendFeedback(@Body requestBody: FeedbackRequestBody): Call<FeedbackApi?>

    @Multipart
    @POST("customer")
    fun updateProfile(
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: Array<MultipartBody.Part?>
    ): Call<UpdateProfileApi>

    @Multipart
    @POST("customer")
    fun updateProfile(@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>): Call<UpdateProfileApi>

    @POST("orders/info")
    suspend fun getOrderInfo(@Body requestBody: OrderInfoRequestBody): Response<OrderInfoApi>

    @POST("cart/chkloyalty")
    suspend fun checkLoyalty(@Body requestBody: CheckLoyaltyRequestBody): Response<CheckLoyaltyApi>

    @POST("orders")
    suspend fun getOrderList(@Body requestBody: OrderListRequestBody): Response<OrderListApI>

    @POST("orders/reorder")
    suspend fun reOrder(@Body requestBody: ReorderRequestBody): Response<ReOrderApi>

    @POST("support/orderlist")
    suspend fun getSupportOrderList(@Body requestBody: SupportOrderListRequestBody): Response<SupportOrderListApi>

    @POST("support/set")
    suspend fun addSupportTicket(@Body requestBody: AddSupportTicketRequestBody): Response<AddSupportTicketApi>

    @POST("support")
    suspend fun getSupportTicketList(@Body requestBody: SupportTicketListRequestBody): Response<GetTicketListApi>

    @POST("support/addcomment")
    suspend fun addSupportComment(@Body requestBody: AddSupportCommentRequestBody): Response<AddSupportCommentApi>

    @POST("support/detail")
    suspend fun getSupportTicketDetail(@Body requestBody: SupportTicketDetailRequestBody): Response<SupportTicketDetailApi>

    @POST("setting")
    suspend fun getAndUpdateAppSettingDetail(@Body requestBody: SettingRequestBody): Response<SettingApi>

    @POST("forgot")
    suspend fun forgotpass(@Body requestBody: ForgotPasswordRequestBody): Response<ForgotPasswordAPI>

    @POST("changepassword")
    suspend fun updatePasswordAfterForgot(@Body requestBody: UpdatePasswordAfterForgotPassRequestBody): Response<ChangePasswordAPI>

    @POST("changepassword/set")
    suspend fun updatePasswordForLoginUser(@Body requestBody: UpdatePasswordLoginUserRequestBody): Response<ChangePasswordAPI>

    @POST("customer/syncloyalty")
    suspend fun syncLoyalty(@Body requestBody: SyncLoyaltyRequestBody): Response<SyncLoyaltyApi>

    @POST("customer/getcards")
    suspend fun getSavedCards(@Body requestBody: GetSavedCardRequestBody): Response<GetSavedCardApi>

    @POST("customer/removecards")
    suspend fun removeSavedCard(@Body requestBody: RemoveSavedCardRequestBody): Response<DeleteSavedCard>

    @POST("loyalty")
    suspend fun loyalty(@Body requestBody: LoyalityRequestBody): Response<LoyalityApi>

    @POST("loyalty/paystatus")
    suspend fun confirmTopupPayment(@Body requestBody: ConfirmTopupPaymentRequestBody): Response<ConfirmTopupPaymentApi>

    @POST("loyalty/addcard")
    suspend fun addLoyaltyCard(@Body requestBody: AddLoyalityCardRequestBody): Response<AddLoyaltyCard>

    @POST("notifications")
    suspend fun notificationDetail(@Body requestBody: NotificationRequestBody): Response<NotificationApi>

    @POST("faqs/sendquery")
    suspend fun sendQuery(@Body requestBody: AddSubmitQueryRequestBody): Response<AddSubmitQueryApi>

    @POST("forgot/resend")
    suspend fun resendOtp(@Body requestBody: OtpRequestBody): Response<ResendOtpApi>
}


