package com.yobin_he.packagedemo.mvp.contracts;

import com.example.mvp_base_library.contracts.BaseContract;
import com.example.mvp_base_library.presenter.IPresenter;
import com.example.mvp_base_library.view.IView;
import com.yobin_he.net_library.RetrofitNet.MyObserver;
import com.yobin_he.net_library.RetrofitNet.ResponseCallBack;
import com.yobin_he.packagedemo.beans.GankBean;

import java.util.List;

/**
 * @Author: yobin he
 * @Date:2019/9/27 11:51
 * @Email: heyibin@huawenpicture.com
 * @Des :
 */
public class WelfareContract extends BaseContract {
    public interface IWelfareModule {
        void requestWelfale(int pageSize, int pageIndex, MyObserver<List<GankBean>> myObserver);

        void requestWalfale(int pageSize, int pageIndex, ResponseCallBack<List<GankBean>> callBack);
    }

    public interface IWelfareView extends IView {
        void onNetStart();

        void onNetFinish();

        void onNetFail(int code, String msg);

        void onNetSuccess(List<GankBean> beans);
    }

    public interface IWelfarePresenter extends IPresenter {
        void requestWelfale(int pageSize, int pageIndex);
    }
}
