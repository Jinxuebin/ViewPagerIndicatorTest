package com.jin.viewpagerindicator;

import android.app.Activity;
import android.content.Context;
import android.test.mock.MockContext;
import android.view.WindowManager;

import com.jin.viewpagerindicator.utils.LogUtils;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by Jin on 2018/1/29.
 */

public class windowManagerTest{

    @Test
    public void testA() {
        Context c = new MockContext();
        assertNotNull(c);
//        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
//        int width = wm.getDefaultDisplay().getWidth();
//        int height = wm.getDefaultDisplay().getHeight();
//        LogUtils.d("width:%d,height:%d",width,height);
    }

}
