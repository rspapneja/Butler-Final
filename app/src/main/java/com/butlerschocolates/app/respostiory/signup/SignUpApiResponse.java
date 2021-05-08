package com.butlerschocolates.app.respostiory.signup;



import com.butlerschocolates.app.model.signup.SignupApi;

public class SignUpApiResponse {

    public SignupApi data;
    private Throwable error;

    public SignUpApiResponse(SignupApi data) {
        this.data = data;
        this.error = null;
    }
    public SignUpApiResponse(Throwable error) {
        this.error = error;
        this.data = null;
    }

    public SignupApi getData() {
        return data;
    }

    public void setData(SignupApi posts) {
        this.data = posts;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}