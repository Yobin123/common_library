package com.yobin_he.packagedemo.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @Author: yobin he
 * @Date:2019/9/11 15:16
 * @Email: heyibin@huawenpicture.com
 * @Des :
 */
public class KeyboardUtil {
    /**
     * 该方法会自动判断当前屏幕上是否有软键盘，
     * 如果有，就隐藏，如没有，就显示软键盘
     */
    public static void controlSoftKeyboard(Activity activity) {
        // 获取InputMethodManager的实例
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 若界面上软键盘打开，则关闭界面上的软键盘
     *
     * @author HuangHaiFei
     * @date 2015-4-8
     */
    public static void closeSoftKeyBoard(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
