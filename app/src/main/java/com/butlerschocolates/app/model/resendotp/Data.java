package com.butlerschocolates.app.model.resendotp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("displaytxt")
    @Expose
    private String displaytxt;
    @SerializedName("error")
    @Expose
    private String error;
    public final static Parcelable.Creator<Data> CREATOR = new Creator<Data>() {

        @SuppressWarnings({"unchecked"})
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    };

    protected Data(Parcel in) {
        this.success = ((String) in.readValue((String.class.getClassLoader())));
        this.customerId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.displaytxt = ((String) in.readValue((String.class.getClassLoader())));
        this.error = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Data() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getDisplaytxt() {
        return displaytxt;
    }

    public void setDisplaytxt(String displaytxt) {
        this.displaytxt = displaytxt;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(customerId);
        dest.writeValue(displaytxt);
        dest.writeValue(error);
    }

    public int describeContents() {
        return 0;
    }

}