package com.example.store.util.response;

import com.google.gson.Gson;

public class Response {

    private boolean success;
    private int error;
    private String message;
    private Object data;

    private int code;

    public Response() {
        this.setSuccess(false);
        this.setMessage("Invalid json format");
        this.setError(-1);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String toJson() {
        return (new Gson()).toJson(this);
    }

    public void setResponseSuccess() {
        this.setSuccess(true);
        this.setError(0);
        this.setMessage("success");
    }

    public void setResponseError() {
        if (error < 1) {
            error = 1;
        } else {
            error++;
        }
    }
}
