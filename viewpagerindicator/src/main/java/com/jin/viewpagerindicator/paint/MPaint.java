package com.jin.viewpagerindicator.paint;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jin.viewpagerindicator.utils.LogUtils;

/**
 * 一个画笔工具类
 * Created by Jin on 2017/11/10.
 */

public class MPaint extends Paint{

    public static int Null = -1;

    private int dx; //波浪动画的偏移量
    private ValueAnimator mWaveAnimator;
    private int dxLen = 0;

    public MPaint() {
        this(Null, null, Null);
    }

    public MPaint(@ColorInt int color, @Nullable Style style , int width) {
        this(Null,color,style,width);
    }

    public MPaint(int flag, int color, @Nullable Style style, int width) {
        initPaint(flag,color,style,width);
    }

    public static Paint generatePaint(@ColorInt int color, @Nullable Style style ,int width) {
        return generatePaint(Null, color, style, width);
    }

    public static Paint generatePaint(int flag, int color, @Nullable Style style, int width) {
        Paint paint = new Paint();
        if (flag != Null) {
            paint.setFlags(flag);
        }
        if (color != Null) {
            paint.setColor(color);
        }
        if (style != null) {
            paint.setStyle(style);
        }
        if (width != Null) {
            paint.setStrokeWidth(width);
        }
        paint.setAntiAlias(true);
        return paint;
    }

    private void initPaint(int flag, @ColorInt int color, @Nullable Style style ,int width) {

        if (flag != Null) {
            setFlags(flag);
        }
        if (color != Null) {
            setColor(color);
        }

        if (style != null) {
            setStyle(style);
        }

        if (width != Null) {
            setStrokeWidth(width);
        }
        setAntiAlias(true);
    }



    public static void drawRegion(Canvas canvas, Region rgn, Paint paint) {
        RegionIterator iter = new RegionIterator(rgn);
        Rect r = new Rect();
        int i = 0;
        while (iter.next(r)) {
            LogUtils.e("第%d次重复",i++);
            canvas.drawRect(r,paint);
        }
    }



    /**
     * 注意：该方法会频繁调用！
     * 返回一个波浪的路径,该波浪是一个动态的波浪需要实时绘制，调用率比较高，
     * 所以要尽量避免new对象
     * @param path 绘制波浪的画笔
     * @param waveLen 波浪的波长
     * @param waveHeight 波浪的高度
     * @param screenWidth 屏幕的宽度
     * @param seaWidth 海的宽度
     * @param seaHeight 海的高度
     * @return 返回一个海和波浪的完整
     */
    public Path makeWave(Path path,int waveLen, int waveHeight, int screenWidth, int seaWidth, int seaHeight) {
        if (this.dxLen <= 0) {
            this.dxLen = waveLen;
        }
        path.reset();
        int originY = seaHeight >> 1;
        int halfWaveLen = waveLen >> 1;
        path.moveTo(-waveLen + dx, originY);
        for (int i = -waveLen; i <= screenWidth + waveLen; i += waveLen) {
            path.rQuadTo(halfWaveLen >> 1, - waveHeight, halfWaveLen , 0);
            path.rQuadTo(halfWaveLen >> 1, waveHeight, halfWaveLen, 0);
        }
        path.lineTo(seaWidth,seaHeight);
        path.lineTo(0,seaHeight);
        path.close();
        return path;
    }

    /**
     * 该方法应该在自定义View中特别声明一个方法，供用户在外部调用，动画的开启由开发者决定
     * @param v 执行动画的view
     * @param args 动画的偏移距离，开发者大可忽略掉该参数，因为，该参数中的dxLen早在绘制View的时候已经赋值，
     *             如果开发者参数传递了具体的参数，则以开发者的参数为准。
     *             当用户在设置其他的View动画时，也要给dxLen赋初值。
     */
    public void startWaveAnimator(final View v,int ... args) {
        if (args.length == 0) {
            mWaveAnimator = ValueAnimator.ofInt(0, dxLen);
        } else {
            mWaveAnimator = ValueAnimator.ofInt(args);
        }
        mWaveAnimator.setDuration(2000);
        mWaveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mWaveAnimator.setInterpolator(new LinearInterpolator());
        mWaveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                v.postInvalidate();
            }
        });
        mWaveAnimator.start();
    }

    /**
     * 请在自定义View中保留startWaveAnimator()和cancleWaveAnimator()方法，并向外部提供接口
     * 如果不取消该动画，该动画还会继续执行，影响性能
     */
    public void cancleWaveAnimator() {
        if (mWaveAnimator != null) {
            mWaveAnimator.cancel();
        }
    }

}
