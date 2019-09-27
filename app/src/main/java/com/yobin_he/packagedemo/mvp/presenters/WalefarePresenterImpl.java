package com.yobin_he.packagedemo.mvp.presenters;

import com.example.mvp_base_library.presenter.base.BasePresenter;
import com.yobin_he.net_library.BaseBean.BaseResponse;
import com.yobin_he.net_library.BaseBean.FailureBody;
import com.yobin_he.net_library.RetrofitNet.ExceptionHandle;
import com.yobin_he.net_library.RetrofitNet.MyObserver;
import com.yobin_he.net_library.RetrofitNet.ResponseCallBack;
import com.yobin_he.packagedemo.beans.GankBean;
import com.yobin_he.packagedemo.mvp.contracts.WelfareContract;
import com.yobin_he.packagedemo.mvp.modules.WaleFareModuleImpl;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @Author: yobin he
 * @Date:2019/9/27 15:53
 * @Email: heyibin@huawenpicture.com
 * @Des :
 */
public class WalefarePresenterImpl extends BasePresenter<WelfareContract.IWelfareView> implements WelfareContract.IWelfarePresenter {
    private WaleFareModuleImpl module;

    public WalefarePresenterImpl(WelfareContract.IWelfareView view) {
        super(view);
        this.module = new WaleFareModuleImpl();
    }

    @Override
    public void requestWelfale(int pageSize, int pageIndex) {
//        getData1(pageSize, pageIndex);
        getData2(pageSize, pageIndex);
    }

    private void getData1(int pageSize, int pageIndex) {
        if (isViewAttach()) {
            mvpRef.get().onNetStart();
            module.requestWelfale(pageSize, pageIndex, new MyObserver<List<GankBean>>() {
                @Override
                public void onSuccess(BaseResponse<List<GankBean>> t) {
                    mvpRef.get().onNetFinish();
                    if (!t.isError()) {
                        mvpRef.get().onNetSuccess(t.getData());
                    } else {
                        mvpRef.get().onNetFail(t.getCode(), t.getMsg());
                    }
                }

                @Override
                public void onFail(ExceptionHandle.ResponseThrowable throwable) {
                    mvpRef.get().onNetFinish();
                    mvpRef.get().onNetFail(throwable.code, throwable.message);
                }

                @Override
                public void onCompleted() {
                    mvpRef.get().onNetFinish();
                }

                @Override
                public void onDisposable(Disposable d) {

                }
            });
        }
    }

    private void getData2(int pageSize, int pageIndex) {
        if (isViewAttach()) {
            mvpRef.get().onNetStart();
            module.requestWalfale(pageSize, pageIndex, new ResponseCallBack<List<GankBean>>() {
                @Override
                public void onSuccess(BaseResponse<List<GankBean>> data) {
                    mvpRef.get().onNetFinish();
                    if (!data.isError()) {
                        mvpRef.get().onNetSuccess(data.getData());
                    } else {
                        mvpRef.get().onNetFail(data.getCode(), data.getMsg());
                    }
                }

                @Override
                public void onFailure(FailureBody error) {
                    mvpRef.get().onNetFinish();
                    mvpRef.get().onNetFail(error.getCode(), error.getMsg());
                }

                @Override
                public void onError(ExceptionHandle.ResponseThrowable throwable) {
                    mvpRef.get().onNetFinish();
                    mvpRef.get().onNetFail(throwable.code, throwable.message);
                }
            });
        }
    }
}
