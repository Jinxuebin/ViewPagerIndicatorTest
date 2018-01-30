package com.jin.viewpagerindicatortest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.Toast;

import com.jin.viewpagerindicator.LinePageIndicator;

public class MainActivity extends AppCompatActivity {

    private LinePageIndicator linePagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        linePagerIndicator = (LinePageIndicator) findViewById(R.id.viewpagerindicator);
        linePagerIndicator.setIndicatorMap(initIndicator());
        linePagerIndicator.setUnderlineProportion(0.5f);
        linePagerIndicator.setIndicatorListener(new LinePageIndicator.IndicatorListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(MainActivity.this,"你点击了tab" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private SparseArray<String> initIndicator() {
        SparseArray<String> sparse = new SparseArray<>();
        sparse.put(0,"安装应用");
        sparse.put(1,"系统应用");
        sparse.put(2,"系统应用2");
        sparse.put(3,"系统应用3");
        sparse.put(4,"系统应用4");
        sparse.put(5,"系统应用5");
        sparse.put(6,"系统应用6");
        sparse.put(7,"系统应用7");
        sparse.put(8,"系统应用8");
        sparse.put(9,"系统应用9");
        sparse.put(10,"系统应用10");
//        sparse.put(11,"系统应用11");
//        sparse.put(12,"系统应用12");
//        sparse.put(13,"系统应用13");
//        sparse.put(14,"系统应用14");
//        sparse.put(15,"系统应用15");
//        sparse.put(16,"系统应用16");
        return sparse;
    }
}
