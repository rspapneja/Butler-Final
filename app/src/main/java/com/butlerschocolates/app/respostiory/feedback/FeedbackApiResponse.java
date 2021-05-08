package com.butlerschocolates.app.respostiory.feedback;

import com.butlerschocolates.app.model.feedback.FeedbackApi;

public class FeedbackApiResponse {

    public FeedbackApi data;
    private Throwable error;

    public FeedbackApiResponse(FeedbackApi data) {
        this.data = data;
        this.error = null;
    }
    public FeedbackApiResponse(Throwable error) {
        this.error = error;
        this.data = null;
    }

    public FeedbackApi getData() {
        return data;
    }

    public void setData(FeedbackApi data) {
        this.data = data;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}