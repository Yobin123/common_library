package com.yobin_he.net_library.BaseBean;

import com.google.gson.annotations.SerializedName;

/**
 * @Author: yobin he
 * @Date:2019/8/28 17:11
 * @Email: heyibin@huawenpicture.com
 * @Des : 用于处理请求参数错误信息，需要根据自己需要进行处理
 */
public class FailureBody<T> {
    private T data;
    @SerializedName(value = "code", alternate = "status")
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
