package com.yobin_he.net_library.RetrofitNet;


import com.yobin_he.net_library.BaseBean.BaseRequest;

/**
 * 用于对数据提前处理，同时也可用来对参数处理
 * 例如合并，map ,zip或者flateMap这样可以将不符合自己参数的数据转换为自己想请求的参数
 * 这一层是属于module内，用于处理数据源，供给presenter作为中间纽带进行调用
 */
public class NetDataLoader extends ObjectLoader {
    private ServerApi api;

    private NetDataLoader() {
        api = RetrofitManager.newInstance().create(ServerApi.class);
    }

    public static NetDataLoader loader;

    public static NetDataLoader get() {
        if (loader == null) {
            synchronized (NetDataLoader.class) {
                if (loader == null) {
                    loader = new NetDataLoader();
                }
            }
        }
        return loader;
    }

    /*******************************请求数据*****************************************/

//    //获取器材列表
//    public Observable<BaseResponse<ListRespBean<EquipItemBean>>> postEquip(int curPage, int pageSize) {
//        ReqPageBean bean = new ReqPageBean();
//        bean.setCur_page(curPage);
//        bean.setPer_page(pageSize);
//        ListReqBean reqBean = new ListReqBean();
//        reqBean.setPage(bean);
//        return observer(api.postEquipList(getRequestBody(reqBean))); //请求参数的初步处理
//    }


    /***********************请求数据modle类*************************************/
    //用于对请求数据model修改为json数据
    private <T> BaseRequest getRequestBody(T data) {
        if (data == null)
            return null;
        BaseRequest<T> requestBody = new BaseRequest<>();
        requestBody.setOptions(data);
        return requestBody;
    }


}
