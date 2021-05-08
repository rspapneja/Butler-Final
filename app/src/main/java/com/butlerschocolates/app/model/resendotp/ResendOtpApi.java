package com.butlerschocolates.app.model.resendotp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResendOtpApi implements Parcelable
{
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private Data data;
    public final static Parcelable.Creator<ResendOtpApi> CREATOR = new Creator<ResendOtpApi>() {

        @SuppressWarnings({"unchecked"})
        public ResendOtpApi createFromParcel(Parcel in) {
            return new ResendOtpApi(in);
        }

        public ResendOtpApi[] newArray(int size) {
            return (new ResendOtpApi[size]);
        }
    };

    protected ResendOtpApi(Parcel in) {
        this.code = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(code);
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }

}
