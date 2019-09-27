package com.yobin_he.net_library;

import android.content.Context;

/**
 * @Author: yobin he
 * @Date:2019/9/27 11:18
 * @Email: heyibin@huawenpicture.com
 * @Des :用于配置相关信息，这样可以用于
 */
public class NetConfig {
    public static void initRetrofitConfig(Context context, boolean isDebug) {
        
        NetConstance.IS_DEBUG = isDebug; //
    }
}
