package com.yobin_he.packagedemo.mvp.modules;

import com.yobin_he.net_library.RetrofitNet.MyObserver;
import com.yobin_he.net_library.RetrofitNet.ResponseCallBack;
import com.yobin_he.packagedemo.beans.GankBean;
import com.yobin_he.packagedemo.mvp.contracts.WelfareContract;
import com.yobin_he.packagedemo.net.MyDataLoader;

import java.util.List;

/**
 * @Author: yobin he
 * @Date:2019/9/27 11:57
 * @Email: heyibin@huawenpicture.com
 * @Des :
 */
public class WaleFareModuleImpl implements WelfareContract.IWelfareModule {

    @Override
    public void requestWelfale(int pageSize, int pageIndex, MyObserver<List<GankBean>> observer) {
        MyDataLoader.get().getWarefare(pageSize, pageIndex).subscribe(observer);
    }

    @Override
    public void requestWalfale(int pageSize, int pageIndex, ResponseCallBack<List<GankBean>> callBack) {
        MyDataLoader.get().getWalfare(pageSize, pageIndex).enqueue(callBack);
    }


}
