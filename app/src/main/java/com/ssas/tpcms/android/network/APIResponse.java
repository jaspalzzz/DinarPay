package com.ssas.tpcms.android.network;

import com.ssas.dinarpay.android.network.Status;

public class APIResponse<T> {
    Throwable throwable;
    public T object;
    Status status;

    public APIResponse(Status status, Throwable throwable, T object) {
        this.throwable = throwable;
        this.object = object;
        this.status = status;
    }

    public APIResponse() {
    }

    public <T> APIResponse onSuccess(T object) {
        return new APIResponse(Status.SUCCESS,null,object);
    }

    public APIResponse onError(Throwable error) {
        return new APIResponse(Status.ERROR,error,null);
    }

    public APIResponse onLoading() {
     return new APIResponse(Status.LOADING,null,null);
    }

    public Status getStatus() {
        return status;
    }

    public Throwable getError(){
        return throwable;
    }

    public T getResponse(){
        return object;
    }
}
