package com.butlerschocolates.app.respostiory.storelist;


import com.butlerschocolates.app.model.storelist.StoreListApi;

public class StoreListApiResponse {

    public StoreListApi data;
    private Throwable error;

    public StoreListApiResponse(StoreListApi data) {
        this.data = data;
        this.error = null;
    }

    public StoreListApiResponse(Throwable error) {
        this.error = error;
        this.data = null;
    }

    public StoreListApi getData() {
        return data;
    }

    public void setData(StoreListApi posts) {
        this.data = posts;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}