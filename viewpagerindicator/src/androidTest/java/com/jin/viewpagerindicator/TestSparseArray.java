package com.jin.viewpagerindicator;

import android.util.SparseArray;

import com.jin.viewpagerindicator.utils.LogUtils;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Jin on 2018/1/28.
 */

public class TestSparseArray {

    private SparseArray<String> s;

    @Before
    public void testA() {
        s = new SparseArray<>();
        s.put(0,"我是0");
        s.put(1,"我是1");
        s.put(2,"我是2");
        s.put(3,"我是3");
        s.put(4,"我是4");
        s.put(5,"我是5");
    }

    @Test
    public void testList() {
        for (int i = 0; i < s.size(); i++) {
            LogUtils.e("第%d个key：%s",i,s.keyAt(i));
            LogUtils.e("第%d个元素：%s",i,s.valueAt(i));
        }
    }
}
