package com.jin.viewpagerindicator.paint;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

/**
 * 一支专门用来绘制图的画笔
 * image paint
 * Created by Jin on 2017/11/20.
 */

public class IPaint extends MPaint {

    public IPaint() {
        super();
        initPaint();
    }

    public IPaint(@ColorInt int color) {
        super(color,null,Null);
        initPaint();
    }

    public IPaint(@ColorInt int color, @Nullable Paint.Style style) {
        super(color,style,Null);
        initPaint();
    }

    public IPaint(int flag, int color, @Nullable Paint.Style style) {
        super(flag,color,style,Null);
        initPaint();
    }

    private void initPaint() {
        setAntiAlias(true);
    }

    /**
     * 绘制一个圆形的Bitmap
     * @param w 圆形的宽或者画布的宽
     * @param h 圆形的高或者画布的高
     * @return 一个椭圆形的bitmap图像
     */
    public Bitmap makeOval(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        if (getFlags() != ANTI_ALIAS_FLAG) {
            setFlags(ANTI_ALIAS_FLAG);
        }
        c.drawOval(new RectF(0,0,w,h),this);
        return bm;
    }

    /**
     * 绘制一个矩形的Bitmap
     * @param w 矩形的宽或者画布的宽
     * @param h 矩形的高或者画布的高
     * @return 一个矩形的bitmap图像
     */
    public Bitmap makeRect(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        if (getFlags() != ANTI_ALIAS_FLAG) {
            setFlags(ANTI_ALIAS_FLAG);
        }
        c.drawRect(0,0,w,h,this);
        return bm;
    }

    /**
     * 绘制一个固定大小的Bitmap，将图片变形，移动达到固定大小的目的。
     * @param w 固定宽
     * @param h 固定高
     * @param bmp 原始图片
     * @return 固定宽高的图片
     */
    public Bitmap makeFixSizeBitmap(int w, int h, Bitmap bmp) {
        Bitmap fixBmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(fixBmp);
        if (getFlags() != ANTI_ALIAS_FLAG) {
            setFlags(ANTI_ALIAS_FLAG);
        }
        c.drawBitmap(bmp,null,new Rect(0,0,w,h),this);
        return fixBmp;
    }


    public Bitmap makeScaleBitmap(Bitmap source, int sugLen) {
        return makeScaleBitmap(source,sugLen,0,0);
    }

    /**
     * 将一个原始的Bitmap图片修剪成一个以图片中心为原点的1:1矩形，边长为sugLen，如果sugLen不合理，则程序会自动忽略该参数
     * 不合理的情况有：sugLen大于图片的宽或者高，sugLen小于0或等于0；
     * 当sugLen为不合理时，程序会自动使用宽高中最小的边长代替它。
     * 建议sugLen设置为控件的宽度（开发者不必考虑它的长度是否合理）
     * @param source 原始图片
     * @param sugLen 建议边长
     * @param sx 开始x坐标
     * @param sy 开始y坐标
     * @return 裁剪的图片
     */
    public Bitmap makeScaleBitmap(Bitmap source, int sugLen, int sx, int sy) {
        int w = source.getWidth();
        int h = source.getHeight();
        float[] f;
        if (sugLen <= 0) {
            f = calculateScaleXY(w, h);
        } else {
            f = calculateScaleXY(w, h, sugLen);
        }

        // 原图的所取的rect
        int l = (int) f[0];
        int t = (int) f[1];
        int r = l + (int) f[2];
        int b = t + (int) f[2];

        int vw = (int)f[2] + sx;
        int vh = (int)f[2] + sy;

        // 新图的放置rect
        int nl = sx;
        int nt = sy;
        int nr = nl + (int)f[2];
        int nb = nt + (int)f[2];
        Bitmap endBmp = Bitmap.createBitmap(vw, vh, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(endBmp);
        if (getFlags() != ANTI_ALIAS_FLAG) {
            setFlags(ANTI_ALIAS_FLAG);
        }
        c.drawBitmap(source,new Rect(l,t,r,b),new Rect(nl, nt, nr, nb),this);
        return endBmp;
    }

    /**
     * 该方法请使用paint.setShader(Shader shader)解决。或者
     * 使用paint.setXfermode()方法
     * @param bmp 原始图片
     * @param sugLen 建议边长
     * @return 圆形的bitmap
     */
    @Deprecated
    public Bitmap makeScaleOvalBitmap(Bitmap bmp, int sugLen) {
        //根据原始图片返回一个圆形的bitmap
//        iPaint.setShader(new BitmapShader(mBitmapBG, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
//        canvas.drawCircle(mDx,mDy,150,iPaint);
        return null;
    }

    /**
     * 使用一个建议的边长计算出以图片中心的1:1图片
     * @param w 图片宽度
     * @param h 图片高度
     * @param sugLen 建议边长
     * @return 一个float数组。
     */
    private float[] calculateScaleXY(int w, int h, int sugLen) {
        float[] f = new float[3];
        if (sugLen > Math.min(w,h)) {
            return calculateScaleXY(w, h);
        } else {
            f[0] = w - sugLen >> 1;
            f[1] = h - sugLen >> 1;
            f[2] = sugLen;
            return f;
        }
    }

    /**
     * 计算不成比例的图片的起点A，
     * 可以根据用户提交不成比例的图片，以宽高比1:1的形式计算出该截取图片的起点A
     * @param w 原始图片的宽
     * @param h 原始图片的高
     * @return 一个float数组，开始截取点A（f[0],f[1]）,截取宽高f[2];坐标的原点是图片的左上角
     *          f[0]:A点的x坐标
     *          f[1]:A点的y坐标
     *          f[2]:图片的最佳宽高
     */
    private float[] calculateScaleXY(int w, int h) {
        float[] f = new float[3];
        int l = Math.min(w, h);
        f[0] = w - l >> 1;
        f[1] = h - l >> 1;
        f[2] = l;
        return f;
    }

    public float[] calculateViewRect(int w, int h, int bw) {
        return calculateViewRect(w,h,bw,0,0);
    }

    /**
     * 计算控件view的Rect
     * @param w 该控件view的宽
     * @param h 该控件view的高
     * @param bw 该控件border的宽度
     * @return float[],数组中的元素对应rect的l,t,r,b,
     *          中心坐标（f[4],f[5])，f[6]实际半径，给绘制圆形提供便利。
     */
    public float[] calculateViewRect(int w, int h, int bw, int sx, int sy) {

        //view的实际显示宽高
        int vw = w - 2 * bw;
        int vy = h - 2 * bw;
        //view的l,t,r,b
        float[] v = new float[7];
        v[0] = bw + sx;
        v[1] = bw + sy;
        v[2] = bw + vw + sx;
        v[3] = bw + vy + sy;
        //中心坐标
        v[4] = (w >> 1) + sx ;
        v[5] = (h >> 1) + sy;
        v[6] = Math.min(vw,vy) >> 1;
        return v;
    }

    public float[] calculateBorderRect(int w,int h, int bw) {
        return calculateViewRect(w,h,bw,0,0);
    }


    /**
     * 计算控件view的border的Rect
     * @param w 该控件view的宽
     * @param h 该控件view的高
     * @param bw 该控件border的宽度
     * @param sx 该控件的起始x坐标
     * @param sy 该控件的起始y坐标
     * @return float[],数组中的元素对应rect的l,t,r,b,
     *          中心坐标（f[4],f[5])，f[6]实际半径，给绘制圆形提供便利。
     */
    public float[] calculateBorderRect(int w,int h, int bw, int sx, int sy) {
        int vw = w - 2 * bw;
        int vy = h - 2 * bw;
        //border的l,t,r,b
        float[] b = new float[7];
        b[0] = (bw >> 1) + sx;
        b[1] = (bw >> 1) + sy;
        b[2] = bw * 3 / 2 + vw + sx;
        b[3] = bw * 3 / 2 + vy + sy;
        //中心坐标
        b[4] = (w >> 1) + sx;
        b[5] = (h >> 1) + sy;
        b[6] = Math.min(vw,vy) + bw >> 1;
        return b;
    }

}
