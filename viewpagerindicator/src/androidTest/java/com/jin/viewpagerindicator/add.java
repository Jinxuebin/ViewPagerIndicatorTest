package com.jin.viewpagerindicator;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.jin.viewpagerindicator.utils.LogUtils;

/**
 * Created by Jin on 2018/1/28.
 */

public class add extends AndroidTestCase{

    public int runadd(int a, int b) {
        Log.e("Log","this is test debug");
        return a + b;
    }


    public void testA() {
        Context c = getContext();
        assertNotNull(c);
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        LogUtils.d("width:%d,height:%d",width,height);
    }

    public void testB() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        LogUtils.d("width:%d,height:%d",width,height);
    }

    public void testC() {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        LogUtils.d("width:%d,height:%d,density:%f",width,height,density);
    }
}
