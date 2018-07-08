package com.liye.mycontacts.myContacts;

/**
 * Created by dell-pc on 2018.7.7.
 */

import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.liye.mycontacts.R;

public class MainActivity extends ActionBarActivity implements OnClickListener {

    private TextView tv_item_one;
    private TextView tv_item_two;
    private TextView tv_item_three;
    private ViewPager myViewPager;
    private FirstFragment oneFragment;
    private SecondFragment twoFragment;
    private ThirdFragment threeFragment;
    private List<Fragment> list;
    private TabFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();

// 设置菜单栏的点击事件
        tv_item_one.setOnClickListener(this);
        tv_item_two.setOnClickListener(this);
        tv_item_three.setOnClickListener(this);
        myViewPager.setOnPageChangeListener(new MyPagerChangeListener());

//把Fragment添加到List集合里面
        list = new ArrayList<>();
        oneFragment = new FirstFragment();
        twoFragment = new SecondFragment();
        threeFragment = new ThirdFragment();
        oneFragment.SetContext(this);
        oneFragment.setAct(this);
        twoFragment.SetContext(this);
        list.add(oneFragment);
        list.add(twoFragment);
        list.add(threeFragment);
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list);
        myViewPager.setAdapter(adapter);
        myViewPager.setCurrentItem(0);  //初始化显示第一个页面
        tv_item_one.setBackgroundColor(Color.WHITE);
        tv_item_one.setTextColor(Color.rgb(128,128,128));
    }

    /**
     * 初始化控件
     */
    private void InitView() {
        tv_item_one = (TextView) findViewById(R.id.tv_item_one);
        tv_item_two = (TextView) findViewById(R.id.tv_item_two);
        tv_item_three = (TextView) findViewById(R.id.tv_item_three);
        myViewPager = (ViewPager) findViewById(R.id.myViewPager);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_item_one:
                myViewPager.setCurrentItem(0);
                tv_item_one.setBackgroundColor(Color.WHITE);
                tv_item_two.setBackgroundColor(Color.rgb(44,162,192));
                tv_item_three.setBackgroundColor(Color.rgb(44,162,192));
                tv_item_one.setTextColor(Color.rgb(128,128,128));
                tv_item_two.setTextColor(Color.WHITE);
                tv_item_three.setTextColor(Color.WHITE);
                break;
            case R.id.tv_item_two:
                myViewPager.setCurrentItem(1);
                tv_item_one.setBackgroundColor(Color.rgb(44,162,192));
                tv_item_two.setBackgroundColor(Color.WHITE);
                tv_item_three.setBackgroundColor(Color.rgb(44,162,192));
                tv_item_two.setTextColor(Color.rgb(128,128,128));
                tv_item_one.setTextColor(Color.WHITE);
                tv_item_three.setTextColor(Color.WHITE);
                break;
            case R.id.tv_item_three:
                myViewPager.setCurrentItem(2);
                tv_item_one.setBackgroundColor(Color.rgb(44,162,192));
                tv_item_two.setBackgroundColor(Color.rgb(44,162,192));
                tv_item_three.setBackgroundColor(Color.WHITE);
                tv_item_three.setTextColor(Color.rgb(128,128,128));
                tv_item_one.setTextColor(Color.WHITE);
                tv_item_two.setTextColor(Color.WHITE);
                break;
        }
    }

    /**
     * 设置一个ViewPager的侦听事件，当左右滑动ViewPager时菜单栏被选中状态跟着改变
     *
     */
    public class MyPagerChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    tv_item_one.setBackgroundColor(Color.WHITE);
                    tv_item_two.setBackgroundColor(Color.rgb(44,162,192));
                    tv_item_three.setBackgroundColor(Color.rgb(44,162,192));
                    tv_item_one.setTextColor(Color.rgb(128,128,128));
                    tv_item_two.setTextColor(Color.WHITE);
                    tv_item_three.setTextColor(Color.WHITE);
                    break;
                case 1:
                    tv_item_one.setBackgroundColor(Color.rgb(44,162,192));
                    tv_item_two.setBackgroundColor(Color.WHITE);
                    tv_item_three.setBackgroundColor(Color.rgb(44,162,192));
                    tv_item_two.setTextColor(Color.rgb(128,128,128));
                    tv_item_one.setTextColor(Color.WHITE);
                    tv_item_three.setTextColor(Color.WHITE);
                    break;
                case 2:
                    tv_item_one.setBackgroundColor(Color.rgb(44,162,192));
                    tv_item_two.setBackgroundColor(Color.rgb(44,162,192));
                    tv_item_three.setBackgroundColor(Color.WHITE);
                    tv_item_three.setTextColor(Color.rgb(128,128,128));
                    tv_item_one.setTextColor(Color.WHITE);
                    tv_item_two.setTextColor(Color.WHITE);
                    break;
            }
        }
    }

}