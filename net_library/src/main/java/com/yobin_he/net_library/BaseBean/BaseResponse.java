package com.yobin_he.net_library.BaseBean;

import com.google.gson.annotations.SerializedName;

//需要根据自己的需要自己去定义baseResponse
public class BaseResponse<T> {

    @SerializedName(value = "code", alternate = "status")
    private int code; //返回码
    @SerializedName(value = "data", alternate = "results")
    private T data; //具体数据结果
    private String msg; //返回信息

    private boolean error; // 

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
