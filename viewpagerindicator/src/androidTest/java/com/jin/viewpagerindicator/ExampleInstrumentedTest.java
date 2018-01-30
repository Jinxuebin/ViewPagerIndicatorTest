package com.jin.viewpagerindicator;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.cache.LoadingCache;
import android.support.test.runner.AndroidJUnit4;

import com.jin.viewpagerindicator.utils.LogUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.jin.viewpagerindicator.test", appContext.getPackageName());
    }

    @Test
    public void testsparseArray() throws Exception {
        LogUtils.e("hahahhskdfsdfjksdlf");
        assertEquals(1,1,0);
    }





}
