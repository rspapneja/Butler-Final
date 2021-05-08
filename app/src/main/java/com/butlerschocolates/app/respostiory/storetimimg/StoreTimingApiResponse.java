package com.butlerschocolates.app.respostiory.storetimimg;




import com.butlerschocolates.app.model.storetiming.StoreTimingAPI;

public class StoreTimingApiResponse {

    public StoreTimingAPI data;
    private Throwable error;

    public StoreTimingApiResponse(StoreTimingAPI data) {
        this.data = data;
        this.error = null;
    }
    public StoreTimingApiResponse(Throwable error) {
        this.error = error;
        this.data = null;
    }
    public StoreTimingAPI getData() {
        return data;
    }
    public void setData(StoreTimingAPI posts) {
        this.data = posts;
    }
    public Throwable getError() {
        return error;
    }
    public void setError(Throwable error) {
        this.error = error;
    }
}