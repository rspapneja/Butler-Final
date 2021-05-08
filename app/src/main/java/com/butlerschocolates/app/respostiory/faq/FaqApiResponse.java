package com.butlerschocolates.app.respostiory.faq;


import com.butlerschocolates.app.model.faq.FaqApi;

public class FaqApiResponse {

    public FaqApi data;
    private Throwable error;

    public FaqApiResponse(FaqApi data) {
        this.data = data;
        this.error = null;
    }
    public FaqApiResponse(Throwable error) {
        this.error = error;
        this.data = null;
    }
    public FaqApi getData() {
        return data;
    }
    public void setData(FaqApi posts) {
        this.data = posts;
    }
    public Throwable getError() {
        return error;
    }
    public void setError(Throwable error) {
        this.error = error;
    }
}