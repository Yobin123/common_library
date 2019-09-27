package com.yobin_he.packagedemo.net;

import com.yobin_he.net_library.BaseBean.BaseResponse;
import com.yobin_he.net_library.RetrofitNet.NetDataLoader;
import com.yobin_he.net_library.RetrofitNet.ObjectLoader;
import com.yobin_he.net_library.RetrofitNet.RetrofitManager;
import com.yobin_he.packagedemo.beans.GankBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;

/**
 * @Author: yobin he
 * @Date:2019/9/27 10:07
 * @Email: heyibin@huawenpicture.com
 * @Des :用于数据请求类，重写
 */
public class MyDataLoader extends ObjectLoader {
    private MyServerApi api;

    public MyDataLoader() {
        api = RetrofitManager.newInstance().create(MyServerApi.class);
    }

    public static MyDataLoader loader;

    public static MyDataLoader get() {
        if (loader == null) {
            synchronized (NetDataLoader.class) {
                if (loader == null) {
                    loader = new MyDataLoader();
                }
            }
        }
        return loader;
    }

    /**************************网络请求相关方法 star*******************************/

    /*请求福利*/
    public Observable<BaseResponse<List<GankBean>>> getWarefare(int pageSize, int pageIndex) {
        return observer(api.getWelfare(pageSize, pageIndex));
    }

    //请求福利
    public Call<BaseResponse<List<GankBean>>> getWalfare(int pageSiz, int pageIndex) {
        return api.getWelfareNoRx(pageSiz, pageIndex);
    }


}
