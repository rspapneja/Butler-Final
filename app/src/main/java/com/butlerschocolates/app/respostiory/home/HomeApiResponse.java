package com.butlerschocolates.app.respostiory.home;


import com.butlerschocolates.app.model.home.HomeApi;

public class HomeApiResponse {

    public HomeApi data;
    private Throwable error;

    public HomeApiResponse(HomeApi data) {
        this.data = data;
        this.error = null;
    }
    public HomeApiResponse(Throwable error) {
        this.error = error;
        this.data = null;
    }
    public HomeApi getData() {
        return data;
    }
    public void setData(HomeApi posts) {
        this.data = posts;
    }
    public Throwable getError() {
        return error;
    }
    public void setError(Throwable error) {
        this.error = error;
    }
}