package com.butlerschocolates.app.respostiory.logout;



import com.butlerschocolates.app.model.logout.LogoutApi;


public class LogoutResponse {

    public LogoutApi data;
    private Throwable error;

    public LogoutResponse(LogoutApi data) {
        this.data = data;
        this.error = null;
    }
    public LogoutResponse(Throwable error) {
        this.error = error;
        this.data = null;
    }
    public LogoutApi getData() {
        return data;
    }
    public void setData(LogoutApi posts) {
        this.data = posts;
    }
    public Throwable getError() {
        return error;
    }
    public void setError(Throwable error) {
        this.error = error;
    }
}