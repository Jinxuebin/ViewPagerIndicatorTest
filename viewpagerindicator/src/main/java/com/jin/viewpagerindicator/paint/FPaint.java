package com.jin.viewpagerindicator.paint;

import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

/**
 * 一个画笔工具类，专门用来写文字的画笔
 * font paint
 * Created by Jin on 2017/11/20.
 */

public class FPaint extends MPaint {

    public FPaint() {
        super();
    }

    public FPaint(@ColorInt int color, @Nullable Style style , int width) {
        super(color,style,width);
    }

    public FPaint(@ColorInt int color, @Nullable Style style , int width, int textSize) {
        super(color,style,width);
        setTextSize(textSize);
    }


    //找到文字区域
    public Rect findTextRegion() {
        return  null;
    }

    /**
     * 将文字下基线变成文字中基线
     * @param y 文字下基线
     * @return 文字中心基线
     */
    public float findCenterBaseY(float y) {
        FontMetricsInt fm = getFontMetricsInt();
        return (- fm.top - fm.bottom  >> 1) + y;
    }

    //将文字下基线变成文字中基线
    public float findTopBaseY(float y) {
        return y;
    }

    /**
     * 得到字符串的宽度
     * @param text 字符串
     * @return 字符串的宽度
     */
    public int getTextWidth(String text) {
        return (int)measureText(text);
    }

    /**
     * 得到字体的高度
     * @return 字体的高度
     */
    public float getTextHeight() {
        FontMetrics fm = getFontMetrics();
        return fm.descent - fm.ascent;
    }
}
