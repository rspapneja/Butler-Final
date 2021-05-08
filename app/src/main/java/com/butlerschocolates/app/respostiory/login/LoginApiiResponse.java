package com.butlerschocolates.app.respostiory.login;


import com.butlerschocolates.app.model.login.LoginApi;

public class LoginApiiResponse {

    public LoginApi data;
    private Throwable error;

    public LoginApiiResponse(LoginApi data) {
        this.data = data;
        this.error = null;
    }
    public LoginApiiResponse(Throwable error) {
        this.error = error;
        this.data = null;
    }
    public LoginApi getData() {
        return data;
    }
    public void setData(LoginApi posts) {
        this.data = posts;
    }
    public Throwable getError() {
        return error;
    }
    public void setError(Throwable error) {
        this.error = error;
    }
}