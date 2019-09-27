package com.example.mvp_base_library.contracts;


import com.example.mvp_base_library.presenter.IPresenter;
import com.example.mvp_base_library.view.IView;

/**
 * 锲约类的例子
 */
public class SampleContracts extends BaseContract {
   public interface ISampleModule {
        //用于添加了相关回调接口
//        void login(String userName,String password ,IModuleCallBack<T,E> callBack); 

        //利用返回值来获取相应的数据
        boolean change(boolean isSuccess);

    }

    public  interface ISampleView extends IView {
        void showTip(boolean isSuccess);
    }

   public interface ISamplePresenter extends IPresenter {
        void change(boolean isSuccess);
    }


}
