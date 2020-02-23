package com.whu.cloudstudy_server;

public class Response<T> {
    private Integer code;
    private String message;
    private T resData;

    public Response() {
    }

    public Response(Integer code, String message, T resData) {
        this.code = code;
        this.message = message;
        this.resData = resData;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResData() {
        return resData;
    }

    public void setResData(T resData) {
        this.resData = resData;
    }
}
