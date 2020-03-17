package com.wyk.bookeeping.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * 覆盖字体工具类
 * Created by leict on 2017/2/28.
 */

public final class FontsOverride {
    /**
     * 设置默认字体
     *
     * @param context
     * @param staticTypefaceFieldName
     * @param fontAssetName
     */
    public static void setDefaultFont(Context context, String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(), fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    /**
     * 替换字体
     *
     * @param staticTypefaceFieldName
     * @param newTypeface
     */
    protected static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
