package com.yobin_he.packagedemo.net;

import com.yobin_he.net_library.BaseBean.BaseResponse;
import com.yobin_he.packagedemo.beans.GankBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @Author: yobin he
 * @Date:2019/9/27 10:10
 * @Email: heyibin@huawenpicture.com
 * @Des :
 */
public interface MyServerApi {
    //利用Rxjava请求数据
    @GET("福利/{page_size}/{page_index}")
    Observable<BaseResponse<List<GankBean>>> getWelfare(@Path("page_size") int size, @Path("page_index") int index);

    //采用常规call来请求数据
    @GET("福利/{page_size}/{page_index}")
    Call<BaseResponse<List<GankBean>>> getWelfareNoRx(@Path("page_size") int size, @Path("page_index") int index);
}
