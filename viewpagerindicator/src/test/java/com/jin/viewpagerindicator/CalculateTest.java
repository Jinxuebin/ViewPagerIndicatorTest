package com.jin.viewpagerindicator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jin on 2018/1/28.
 */
public class CalculateTest {

    private Calculate calculate;

    @Before
    public void setUp() throws Exception {
        calculate = new Calculate();
    }


    @Test
    public void sum() throws Exception {
        assertEquals(6d,calculate.sum(1d,5d),0);
    }

    @Test
    public void substarct() throws Exception {

    }

    @Test
    public void divide() throws Exception {

    }

    @Test
    public void multiply() throws Exception {

    }

}