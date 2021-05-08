package com.butlerschocolates.app.respostiory.storedetail;


import com.butlerschocolates.app.model.storedetail.StoreDetailApi;
import com.butlerschocolates.app.model.storelist.StoreListApi;

public class StoreDetailApiResponse {

    public StoreDetailApi data;
    private Throwable error;

    public StoreDetailApiResponse(StoreDetailApi data) {
        this.data = data;
        this.error = null;
    }

    public StoreDetailApiResponse(Throwable error) {
        this.error = error;
        this.data = null;
    }

    public StoreDetailApi getData() {
        return data;
    }

    public void setData(StoreDetailApi posts) {
        this.data = posts;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}