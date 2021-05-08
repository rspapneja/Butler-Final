package com.butlerschocolates.app.model.setting


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Setting(
    @SerializedName("acc_email_status")
    val accEmailStatus: String,
    @SerializedName("acc_message_status")
    val accMessageStatus: String,
    @SerializedName("noti_email_status")
    val notiEmailStatus: String,
    @SerializedName("noti_notification_status")
    val notiNotificationStatus: String,
    @SerializedName("noti_sms_status")
    val notiSmsStatus: String,
    @SerializedName("plc_email_status")
    val plcEmailStatus: String,
    @SerializedName("plc_message_status")
    val plcMessageStatus: String,
    @SerializedName("plc_phone_status")
    val plcPhoneStatus: String
) : Parcelable