package com.butlerschocolates.app.respostiory.verifyotp;


import com.butlerschocolates.app.model.verifyotp.VerifyOtpApi;

public class VerfiyOtpApiResponse {

    public VerifyOtpApi data;
    private Throwable error;

    public VerfiyOtpApiResponse(VerifyOtpApi data) {
        this.data = data;
        this.error = null;
    }
    public VerfiyOtpApiResponse(Throwable error) {
        this.error = error;
        this.data = null;
    }
    public VerifyOtpApi getData() {
        return data;
    }
    public void setData(VerifyOtpApi posts) {
        this.data = posts;
    }
    public Throwable getError() {
        return error;
    }
    public void setError(Throwable error) {
        this.error = error;
    }
}