package com.jin.viewpagerindicator;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

import static org.junit.Assert.assertNotEquals;

/**
 * Created by Jin on 2018/1/28.
 */

public class TestJunit{

    @Before
    public void testA() {
        Log.d("Lc","before");
    }

    @Category(Category.class)
    @Test
    public void testB() {
        add a = new add();
        int r = a.runadd(2,2);
        assertNotEquals(r,3);
    }

    @After
    public void testD() {
        Log.d("Lc","after");
    }

}
