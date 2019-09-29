package com.yobin_he.packagedemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SharePreferenceUtil {
    private static SharedPreferences sp;
    private static String SPXMLNAME = "sp_config";

    /**
     * @param context 上下文环境
     * @param key     要从config.xml移除节点的name的名称
     */
    public static void removeKey(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences(SPXMLNAME, Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();
    }


    ///////////////////////////////////////////////////////////////////////////
    // 设置和获取布尔数据
    ///////////////////////////////////////////////////////////////////////////
    public static void putBoolean(Context context, String key, boolean value) {
        if (sp == null) {
            sp = context.getSharedPreferences(SPXMLNAME, Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false); // 默认为false
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(SPXMLNAME, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 设置和获取字符串
    ///////////////////////////////////////////////////////////////////////////
    public static void putString(Context context, String key, String value) {
        if (sp == null) {
            sp = context.getSharedPreferences(SPXMLNAME, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return getString(context, key, ""); // 默认为false
    }

    public static String getString(Context context, String key, String defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(SPXMLNAME, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 设置和获取int值
    ///////////////////////////////////////////////////////////////////////////
    public static void putInt(Context context, String key, int value) {
        if (sp == null) {
            sp = context.getSharedPreferences(SPXMLNAME, Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).commit();
    }

    public static Integer getInt(Context context, String key) {
        return getInt(context, key, 0); // 默认为false
    }

    public static Integer getInt(Context context, String key, int defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(SPXMLNAME, Context.MODE_PRIVATE);
        }
        return sp.getInt(key, defValue);
    }


    public static <T> void setDataList(Context context, String key, List<T> dataList) {
        Gson gson = new Gson();
        String strJson = gson.toJson(dataList);
        if (sp == null) {
            sp = context.getSharedPreferences(SPXMLNAME, Context.MODE_PRIVATE);
        }

        sp.edit().putString(key, strJson).commit();
    }

    /**
     * 获取List
     *
     * @param key
     * @return
     */
    public static <T> List<T> getDataList(Context context, String key) {
        List<T> datalist = new ArrayList<T>();
        if (sp == null) {
            sp = context.getSharedPreferences(SPXMLNAME, Context.MODE_PRIVATE);
        }
        String strJson = sp.getString(key, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return datalist;

    }
}

