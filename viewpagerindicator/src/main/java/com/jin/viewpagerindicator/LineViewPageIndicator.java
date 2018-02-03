package com.jin.viewpagerindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.jin.viewpagerindicator.paint.FPaint;
import com.jin.viewpagerindicator.paint.IPaint;
import com.jin.viewpagerindicator.utils.LogUtils;

import java.util.List;


/**
 * line page indicator
 * Created by Jin on 2018/1/27.
 */

public class LineViewPageIndicator extends View {

    /**
     * 左边距
     */
    private int paddingLeft;

    /**
     * 右边距
     */
    private int paddingRight;

    /**
     * 字体大小
     */
    private int fontSize;

    /**
     * 字体颜色
     */
    private int fontColor;

    /**
     * 下划线颜色
     */
    private int underlineColor;

    /**
     * 整个view的下划线高度
     */
    private int underlineHeight = 2;

    /**
     * 选中的tab的下划线的高度
     */
    private int selectUnderlineHeight = 10;

    /**
     * 当前选中的tab
     */
    private int currentTab = 0;

    /**
     * 背景
     */
    private int background;

    /**
     * 背景是否是颜色
     */
    private boolean isColor;

    /**
     * 选中的下划线占选中区域的比重。
     */
    private float underlineProportion = 1;

    /**
     * 专门用来绘制图片的画笔
     */
    private IPaint iPaint;

    /**
     * 专门用来绘制文字的画笔
     */
    private FPaint fPaint;

    /**
     * 用来存放每一个指针
     */
    private SparseArray<String> indicatorArray;
    private float startX;
    private float startY;
    private float viewX;
    private float viewY;
    private Scroller mScroller;
    private long startTimeMillis;
    private float downX;

    IndicatorListener mIndicatorListener;
    private ViewPager mViewPager;

    public LineViewPageIndicator(Context context) {
        this(context,null);
    }

    public LineViewPageIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineViewPageIndicator(Context c, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);

        initAttrs(c,attrs);
        initTools(c);
        initData();
    }

    private void initAttrs(Context c, AttributeSet attrs) {
        TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.LinePageIndicator);
        paddingLeft = (int) typedArray.getDimension(R.styleable.LinePageIndicator_lpi_padding_left, 40);
        paddingRight = (int) typedArray.getDimension(R.styleable.LinePageIndicator_lpi_padding_right, 40);
        fontSize = (int) typedArray.getDimension(R.styleable.LinePageIndicator_lpi_font_size, 42);
        fontColor = typedArray.getColor(R.styleable.LinePageIndicator_lpi_font_color, Color.BLACK);
        underlineColor = typedArray.getColor(R.styleable.LinePageIndicator_lpi_underline_color, Color.BLACK);
        background = typedArray.getResourceId(R.styleable.LinePageIndicator_lpi_background, -1);
        if (background == -1) {
            isColor = true;
            background = typedArray.getColor(R.styleable.LinePageIndicator_lpi_background, Color.parseColor("#00000000"));
        }
        underlineProportion = typedArray.getFloat(R.styleable.LinePageIndicator_lpi_underline_proportion, 1);
        if (underlineProportion <= 0 || underlineProportion > 1) {
            underlineProportion = 1;
        }
        typedArray.recycle();
    }

    private void initTools(Context c) {
        iPaint = new IPaint();
        fPaint = new FPaint();
        fPaint.setTextSize(fontSize);
        fPaint.setColor(fontColor);
        mScroller = new Scroller(c);
    }

    private void initData() {
        indicatorArray = new SparseArray<>();
    }

    public void setIndicatorMap(SparseArray<String> indicatorMap) {
        this.indicatorArray = indicatorMap;
        invalidate();
    }

    public SparseArray<String> getIndicatorMap() {
        return indicatorArray;
    }

    @Override
    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
        invalidate();
    }

    public void addData(String tabName) {
        int size = indicatorArray.size();
        indicatorArray.put(size, tabName);
    }

    @Override
    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
        invalidate();
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        fPaint.setTextSize(fontSize);
        invalidate();
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineProportion(float underlineProportion) {
        this.underlineProportion = underlineProportion > 1 || underlineProportion <=0 ? 1 : underlineProportion;
        invalidate();
    }

    public float getUnderlineProportion() {
        return underlineProportion;
    }

    public void setUnderlineHeight(int underlineHeight) {
        this.underlineHeight = underlineHeight;invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setSelectUnderlineHeight(int selectUnderlineHeight) {
        this.selectUnderlineHeight = selectUnderlineHeight;
        invalidate();
    }

    public int getSelectUnderlineHeight() {
        return selectUnderlineHeight;
    }

    public int getTabCount() {
        return indicatorArray != null ? indicatorArray.size() : 0;
    }

    /**
     * CurrentTab变化引起ViewPager变化
     * @param currentTab tab
     */
    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
        invalidate();
        if (mViewPager != null) {
            int childCount = mViewPager.getChildCount();
            if (currentTab >= childCount) {
                LogUtils.e("viewPager的个数的Indicator的个数不匹配");
                return;
            }
            mViewPager.setCurrentItem(currentTab);
        }
    }

    /**
     * viewPager变化引起CurrentTab变化
     * @param currentTab tab
     */
    public void viewPagerSetCurrentTab(int currentTab) {

        if (currentTab >= getTabCount()) {
            LogUtils.e("viewPager的个数的Indicator的个数不匹配");
            return;
        }
        this.currentTab = currentTab;
        invalidate();
    }

    public int getCurrentTab() {
        return currentTab;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //1.绘制背景
        if (isColor) {
            canvas.drawColor(background);
        } else {
            Bitmap bgImg = BitmapFactory.decodeResource(getResources(), background);
            canvas.drawBitmap(bgImg,null,getArea(),iPaint);
        }
        //2.绘制文字
        if (indicatorArray.size() > 0) {

            int c = getHeight() / 2;
            if (calculateViewWidth() <= getScreenWidth()) {
                //足够容纳, tab平分空间
                float tabWidth = (calculateViewWidth() - getPaddingLeft() - getPaddingRight()) / indicatorArray.size();
                for (int i = 0; i < indicatorArray.size(); i ++) {
                    float left = getPaddingLeft() + i * tabWidth + (tabWidth - getTextWidth(indicatorArray.valueAt(i))) / 2;
                    fPaint.setColor(i == currentTab ? underlineColor : fontColor);
                    canvas.drawText(indicatorArray.valueAt(i), left, fPaint.findCenterBaseY(c),fPaint);
                }
            } else {
                //不容纳
                float l = getPaddingLeft();
                for (int i = 0; i < indicatorArray.size(); i ++) {
                    float lineWidth = getTextWidth(indicatorArray.valueAt(i)) / getUnderlineProportion();
                    float left = (lineWidth - getTextWidth(indicatorArray.valueAt(i))) / 2;
                    fPaint.setColor(i == currentTab ? underlineColor : fontColor);
                    canvas.drawText(indicatorArray.valueAt(i),l + left,fPaint.findCenterBaseY(c),fPaint);
                    l += lineWidth;
                }
            }
        }
        //3.绘制下划线
        canvasLine(canvas);
        //4.绘制选中下划线
        canvasSelectLine(canvas,currentTab);
        //计算scroll
        computeScroll();
    }

    private void canvasLine(Canvas canvas) {
        iPaint.setStrokeWidth(getUnderlineHeight());
        iPaint.setColor(underlineColor);
        iPaint.setStyle(Paint.Style.FILL);
        canvas.drawLine(0,getHeight() - getUnderlineHeight() / 2, getWidth(), getHeight() - getUnderlineHeight() / 2,iPaint);
    }

    private void canvasSelectLine(Canvas canvas, int tab) {
        iPaint.setStrokeWidth(getSelectUnderlineHeight());
        iPaint.setColor(underlineColor);

        float[] p = getPosition(tab);
        canvas.drawLine(p[0], getHeight() - (getSelectUnderlineHeight() + getUnderlineHeight())/2, p[1], getHeight() - (getSelectUnderlineHeight() + getUnderlineHeight()) / 2, iPaint);
    }

    /**
     * 得到第i个元素的起始和结束的坐标
     * @param i 第i个元素
     * @return float[]  f[0] 开始的x坐标，f[1] 结束的y坐标
     */
    private float[] getPosition(int i) {
        float[] p = new float[2];
        if (i > indicatorArray.size() || i < 0) {
            p[0] = p[1] = 0;
            return p;
        }
        p[0] = getPaddingLeft();
        if (calculateViewWidth() <= getScreenWidth()) {
            //屏幕空间足够容纳
            float tw = (calculateViewWidth() - getPaddingLeft() - getPaddingRight()) / indicatorArray.size();
            float rw = tw * getUnderlineProportion();
            p[0] += i * tw + (tw - rw) / 2;
            p[1] = p[0] + rw;
        } else {
            for (int j = 0; j < i; j ++) {
                p[0] += getTextWidth(indicatorArray.valueAt(j)) / getUnderlineProportion();
            }
            p[1] = p[0] + getTextWidth(indicatorArray.valueAt(i)) / getUnderlineProportion();
        }
        return p;
    }

    /**
     * 计算view的总长度
     * @return View的总长度
     */
    private float calculateViewWidth() {
        float sw = getScreenWidth();
        int w = getPaddingLeft() + getPaddingRight();
        for (int i = 0; i < indicatorArray.size(); i++) {
            w += getTextWidth(indicatorArray.valueAt(i)) / getUnderlineProportion();
        }

        return w <= sw ? sw : w;
    }

    private float getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private RectF getArea() {
        RectF area = new RectF();
        int l = 0;
        int t = 0;
        int r = l + getWidth();
        int b = l + getHeight();
        area.set(l,t,r,b);
        return area;
    }

    private int getTextWidth(String text) {
        return fPaint.getTextWidth(text);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int scrollX = ((View) getParent()).getScrollX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = startX = event.getRawX();
                startY = event.getRawY();
                startTimeMillis = SystemClock.currentThreadTimeMillis();
                viewX = event.getX();
                viewY = event.getY();
                return true;

            case MotionEvent.ACTION_MOVE:
                float dx = event.getRawX() - startX;
                float dy = event.getRawY() - startY;

                if (Math.abs(dx) > Math.abs(dy) && calculateViewWidth() > getScreenWidth()) {
                    ((View)getParent()).scrollBy(- getOffsetX(dx,scrollX),0);
                    invalidate();
                }
                startX = event.getRawX();
                startY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                //点击事件的逻辑
                if (viewX - event.getX() == 0 && viewY - event.getY() == 0 && viewY >=0 && viewY <= getHeight()) {
                    for (int i = 0; i < indicatorArray.size(); i++) {
                        float[] position = getPosition(i);
                        if (viewX > position[0] && viewX < position[1]) {
                            float sl = position[1] - scrollX;
                            float wi = getTextWidth(indicatorArray.valueAt(i)) / getUnderlineProportion();
                            float ml = getScreenWidth() / 2 - sl + wi / 2;
                            //将第i个tab置于屏幕中间
                            mScroller.startScroll(scrollX,getScrollY(),- getOffsetX(ml,scrollX),0);
                            setCurrentTab(i);
                            if (this.mIndicatorListener != null) {
                                mIndicatorListener.onPageSelected(i);
                            }
                            break;
                        }
                    }
                } else {
                    //处理滑动事件的逻辑 释放后根据滑动的速度继续滑动一段距离。
                    long costTime = SystemClock.currentThreadTimeMillis() - startTimeMillis;
                    float v = (event.getRawX() - downX) / costTime;
                    if (Math.abs(v) <= 10 ) {
                        break;
                    } else if (Math.abs(v) <=70) {
                        float p = v / 70;
                        mScroller.startScroll(scrollX,getScrollY(), -getOffsetX(p * calculateViewWidth(), scrollX),0);

                    } else if (Math.abs(v) > 70) {
                        mScroller.startScroll(scrollX,getScrollY(), getOffsetX(v/Math.abs(v) * calculateViewWidth(), scrollX),0);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 禁止滑动越界
     * rl 属于[0, calculateViewWidth()-getScreenWidth()]
     * @param dx 要移动的距离
     * @param rl view左边偏移屏幕的距离
     * @return 推荐移动的距离
     */
    private int getOffsetX(float dx, float rl) {

        float romain = calculateViewWidth() - getScreenWidth();
        if (dx > 0 && rl - dx < 0) {
            dx = rl;
        }

        if (dx < 0 && rl - dx > romain) {
            dx = rl - romain;
        }
        return (int)dx;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            ((View)getParent()).scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wm = MeasureSpec.getMode(widthMeasureSpec);
        int hm = MeasureSpec.getMode(heightMeasureSpec);
        int ws = MeasureSpec.getSize(widthMeasureSpec);
        int hs = MeasureSpec.getSize(heightMeasureSpec);
        int width = (int)calculateViewWidth();
        setMeasuredDimension(wm == MeasureSpec.EXACTLY ? ws : width,hm == MeasureSpec.EXACTLY ? hs : 120);
    }

    public void bindViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mIndicatorListener != null) {
                    mIndicatorListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                viewPagerSetCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mIndicatorListener != null) {
                    mIndicatorListener.onPageScrollStateChanged(state);
                }
            }
        });
    }

    public void bindViewPager(ViewPager viewPager, FragmentManager fm, List<Fragment> mFragments) {
        if (mViewPager == null) {
            bindViewPager(viewPager);
        }
        if (mViewPager != null) {
            mViewPager.setAdapter(new MyFragmentPagerAdapter(fm,mFragments));
        }
    }

    public void setmIndicatorListener(IndicatorListener listener) {
        this.mIndicatorListener = listener;
    }

    public void removeIndicatorListner() {
        this.mIndicatorListener = null;
    }

    public interface IndicatorListener{
        void onPageSelected(int position);
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        void onPageScrollStateChanged(int state);
    }

}
