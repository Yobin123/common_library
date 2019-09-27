package com.yobin_he.net_library.BaseBean;

/**
 * 请求参数基础类,同时可以添加相应的头部信息
 * 需要根据自己需要自行进行相应处理
 */
public class BaseRequest<T> {

    private T options;

    public T getOptions() {
        return options;
    }

    public void setOptions(T options) {
        this.options = options;
    }
}
