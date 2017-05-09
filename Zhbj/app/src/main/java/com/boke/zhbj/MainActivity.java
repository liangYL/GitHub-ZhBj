package com.boke.zhbj;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.boke.zhbj.base.fragment.ContentFragment;
import com.boke.zhbj.base.fragment.LeftFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainActivity extends SlidingFragmentActivity {

    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private static final String TAG_CONTENT = "TAG_CONTENT";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.left_menu);

        //获取侧边栏对象
        SlidingMenu slidingMenu = getSlidingMenu();
        //全屏触摸
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //展示模式(左/右/全)
        slidingMenu. setMode(SlidingMenu.LEFT);
        //设置右侧栏目
//        slidingMenu.setSecondaryMenu(R.layout.right_menu);
        //修改侧边栏宽度
        slidingMenu.setBehindOffset(200);
        // 设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.35f);

        initFragment();

    /*
        // configure the SlidingMenu
        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.color.colorAccent);

        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);

        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        menu.setMenu(R.layout.left_menu);
     */
    }

    private void  initFragment() {

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();//开始事务
        transaction.replace(R.id.fl_content,new ContentFragment(),TAG_CONTENT);
        transaction.replace(R.id.fl_left,new LeftFragment(),TAG_LEFT_MENU);
        transaction.commit();//提交事务



    }

    /**
     * 获取侧边栏对象
     *
     * @return
     */
    public LeftFragment getLeftMenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        LeftFragment fragment = (LeftFragment) fm
                .findFragmentByTag(TAG_LEFT_MENU);
        return fragment;
    }

    /**
     * 获取主页对象
     *
     * @return
     */
    public ContentFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm
                .findFragmentByTag(TAG_CONTENT);
        return fragment;
    }

}
