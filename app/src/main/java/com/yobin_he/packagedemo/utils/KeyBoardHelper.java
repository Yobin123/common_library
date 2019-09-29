package com.yobin_he.packagedemo.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Author: liuguoyan
 * DateTime: 2018/12/19  下午2:02
 * Company: http://www.everjiankang.com.cn
 * Illustration:
 */
public final class KeyBoardHelper {

    /**
     * 关闭软键盘
     * @param context
     */
    public static void hintKeyBoard(Activity context ) {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && context.getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (context.getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static void showKeyBoard(View v){
        InputMethodManager imm =(InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE) ;
        if (imm!=null){
            v.requestFocus() ;
            imm.showSoftInput(v ,InputMethodManager.SHOW_FORCED);
        }
    }

}
