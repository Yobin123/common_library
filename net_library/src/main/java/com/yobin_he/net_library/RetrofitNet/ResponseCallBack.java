package com.yobin_he.net_library.RetrofitNet;

import com.yobin_he.net_library.BaseBean.BaseResponse;
import com.yobin_he.net_library.BaseBean.FailureBody;
import com.yobin_he.net_library.utils.GsonUtil;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 统一处理回调回来的函数
 * 1可用于传统Retrofit call回调，最好是用于mvp中在module层回调给presenter层
 *
 * @param <T>
 */
public abstract class ResponseCallBack<T> implements Callback<BaseResponse<T>> {
    @Override
    public void onResponse(Call<BaseResponse<T>> call, Response<BaseResponse<T>> response) {
        if (response.isSuccessful()) {
            if (response.body() != null) {
                if (response.code() == 200) {
                    onSuccess(response.body());
                } else {
                    FailureBody failureBody = new FailureBody();
                    if (response.body() != null) {
                        failureBody.setCode(response.body().getCode());
                        failureBody.setMsg(response.body().getMsg());
                        onFailure(failureBody);
                    }
                }
            }
        } else {
            try {
                String string = response.errorBody().string();
                FailureBody failureBody = GsonUtil.GsonToBean(string, FailureBody.class);
                onFailure(failureBody);
            } catch (IOException e) {
                e.printStackTrace();
                onError(ExceptionHandle.handleException(e));
            }
        }
    }

    @Override
    public void onFailure(Call<BaseResponse<T>> call, Throwable t) {
        onError(ExceptionHandle.handleException(t));
    }

    public abstract void onSuccess(BaseResponse<T> response); //成功的回调

    public abstract void onFailure(FailureBody error); //请求错误的响应

    public abstract void onError(ExceptionHandle.ResponseThrowable throwable); //服务端错误响应
}
