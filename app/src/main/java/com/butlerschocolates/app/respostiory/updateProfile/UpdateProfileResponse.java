package com.butlerschocolates.app.respostiory.updateProfile;



import com.butlerschocolates.app.model.updateProfile.UpdateProfileApi;

public class UpdateProfileResponse {

    public UpdateProfileApi data;
    private Throwable error;

    public UpdateProfileResponse(UpdateProfileApi data) {
        this.data = data;
        this.error = null;
    }
    public UpdateProfileResponse(Throwable error) {
        this.error = error;
        this.data = null;
    }
    public UpdateProfileApi getData() {
        return data;
    }
    public void setData(UpdateProfileApi posts) {
        this.data = posts;
    }
    public Throwable getError() {
        return error;
    }
    public void setError(Throwable error) {
        this.error = error;
    }
}