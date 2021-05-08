package com.butlerschocolates.app.model.feedback

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FeedbackRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("easy_to_use")
    @Expose
    var easy_to_use: String? = null
    @SerializedName("design")
    @Expose
    var design: String? = null
    @SerializedName("overall")
    @Expose
    var overall: String? = null
    @SerializedName("feeling_today")
    @Expose
    var feeling_today: String? = null
    @SerializedName("comment")
    @Expose
    var comment: String? = null
}