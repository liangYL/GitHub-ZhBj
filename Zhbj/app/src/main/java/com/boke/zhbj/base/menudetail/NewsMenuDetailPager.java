package com.boke.zhbj.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ViewUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boke.zhbj.MainActivity;
import com.boke.zhbj.R;
import com.boke.zhbj.base.BaseMenuDetailPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 *
 * ViewPagerIndicator使用流程: 1. 引入Library库 2. 布局文件中配置TabPageIndicator 3.
 * 将指针和Viewpager关联起来 4. 重写getPageTitle方法,返回每个页面的标题(PagerAdapter) 5.
 * 设置activity主题样式 6. 修改源码中的样式(修改图片, 文字颜色)
 *
 * @author Kevin
 * @date 2017-4-7
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener{


    private ViewPager mViewPager;
    private ArrayList<TabDetailPager> mTabPagers;// 页签页面集合
    private ArrayList<String> tags;
    private TabPageIndicator mIndicator;


    public NewsMenuDetailPager(Activity activity) {
        super(activity);

        // 初始化12个页签
        tags = new ArrayList<String>();
        tags.add("北京");
        tags.add("河南");
        tags.add("上海");
        tags.add("开封");
        tags.add("武汉");
        tags.add("景德镇");
        tags.add("江西");
        tags.add("南京");

    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_menu_detail_news,
                null);
        mViewPager =(ViewPager)view.findViewById(R.id.vp_news_detail);
        mIndicator =(TabPageIndicator)view.findViewById( R.id.indicator);

        ImageView nextImg =(ImageView)view.findViewById( R.id.iv_next_page);
        nextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage(v);
            }
        });

        return view;
    }

    @Override
    public void initData() {


        mTabPagers = new ArrayList<TabDetailPager>();
        for (String tagName : tags) {
            // 创建一个页签对象
            TabDetailPager pager = new TabDetailPager(mActivity,tagName);
            mTabPagers.add(pager);
        }
        System.out.println("------11------");

        mViewPager.setAdapter(new NewsMenuAdapter());
        // 此方法在viewpager设置完数据之后再调用
        mIndicator.setViewPager(mViewPager);// 将页面指示器和ViewPager关联起来
        mIndicator.setOnPageChangeListener(this);// 当viewpager和指针绑定时,需要将页面切换监听设置给指针
        System.out.println("------22------");

    }



    class NewsMenuAdapter extends PagerAdapter {
        // 返回页面指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            System.out.println("------33------");

            return tags.get(position);
        }

        @Override
        public int getCount() {
            return tags.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            System.out.println("------44------");

            TabDetailPager pager = mTabPagers.get(position);
            container.addView(pager.mRootView);
            pager.initData();// 初始化数据
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        if (position == 0) {// 在第一个页签,允许侧边栏出现
            // 开启侧边栏
            setSlidingMenuEnable(true);
        } else {// 其他页签,禁用侧边栏, 保证viewpager可以正常向右滑动
            // 关闭侧边栏
            setSlidingMenuEnable(false);
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * 设置侧边栏可用不可用
     *
     * @param enable
     */
    private void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();

        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            // 禁用掉侧边栏滑动效果
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    /**
     * 下一页
     *
     * @param view
     */
    public void nextPage(View view) {
        int currentItem = mViewPager.getCurrentItem();
        currentItem++;
        mViewPager.setCurrentItem(currentItem);
    }




}
