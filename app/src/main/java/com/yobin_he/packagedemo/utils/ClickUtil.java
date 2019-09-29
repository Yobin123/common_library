package com.yobin_he.packagedemo.utils;

/**
 * @Author: yobin he
 * @Date:2019/8/28 10:07
 * @Email: heyibin@huawenpicture.com
 * @Des : 点击帮助类
 */
public class ClickUtil {
    private static long lastClickTime;

    //是否快速双击
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
