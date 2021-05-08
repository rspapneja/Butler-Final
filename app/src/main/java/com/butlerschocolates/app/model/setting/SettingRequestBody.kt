package com.butlerschocolates.app.model.setting

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SettingRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("auth_token")
    @Expose
     var auth_token: String? = null
    @SerializedName("type")
    @Expose
     var type: String? = null
    @SerializedName("noti_email_status")
    @Expose
     var noti_email_status: Int? = 0

    @SerializedName("noti_sms_status")
    @Expose
    var noti_sms_status: Int? = 0

    @SerializedName("acc_email_status")
    @Expose
    var acc_email_status: Int? = 0

    @SerializedName("noti_notification_status")
    @Expose
    var noti_notification_status: Int? = 0

    @SerializedName("acc_message_status")
    @Expose
    var acc_message_status: Int? = 0

    @SerializedName("plc_email_status")
    @Expose
    var plc_email_status: Int? = 0

    @SerializedName("plc_message_status")
    @Expose
    var plc_message_status: Int? = 0

    @SerializedName("plc_phone_status")
    @Expose
    var plc_phone_status: Int? = 0
}