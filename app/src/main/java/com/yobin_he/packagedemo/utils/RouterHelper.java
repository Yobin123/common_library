package com.yobin_he.packagedemo.utils;

import android.content.Context;
import android.content.Intent;

/**
 * @Author: yobin he
 * @Date:2019/9/18 7:54
 * @Email: heyibin@huawenpicture.com
 * @Des : 路径跳转
 */
public class RouterHelper {
    //无携带参数跳转
    public static void startActivity(Context context, Class clz) {
        if (context != null) {
            Intent intent = new Intent(context, clz);
            context.startActivity(intent);
        }
    }

    //携带参数进行跳转
    public static void startActivity(Context context, Intent intent, Class clz) {
        intent.setClass(context, clz);
        context.startActivity(intent);

    }


}
