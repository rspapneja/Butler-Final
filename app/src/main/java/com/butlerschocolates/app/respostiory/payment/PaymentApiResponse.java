package com.butlerschocolates.app.respostiory.payment;



import com.butlerschocolates.app.model.payment.PaymentApi;

public class PaymentApiResponse {

    public PaymentApi data;
    private Throwable error;

    public PaymentApiResponse(PaymentApi data) {
        this.data = data;
        this.error = null;
    }
    public PaymentApiResponse(Throwable error) {
        this.error = error;
        this.data = null;
    }
    public PaymentApi getData() {
        return data;
    }
    public void setData(PaymentApi posts) {
        this.data = posts;
    }
    public Throwable getError() {
        return error;
    }
    public void setError(Throwable error) {
        this.error = error;
    }
}