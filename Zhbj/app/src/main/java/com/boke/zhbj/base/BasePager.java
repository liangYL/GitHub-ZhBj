package com.boke.zhbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.boke.zhbj.MainActivity;
import com.boke.zhbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by administrator on 2017/3/30.
 */
public abstract class BasePager {

    public Activity mActivity;
    // 标签页面的根布局
    public View mRootView;

    public TextView tvTitle;
    public ImageButton btnMenu;
    public ImageButton btnDisplay;


    // 帧布局容器, 将来要动态向里面添加内容
    public FrameLayout flContent;

    public BasePager(Activity activity) {
        mActivity = activity;
        initView();
    }

    private void initView() {

        mRootView = View.inflate(mActivity, R.layout.base_pager, null);
        tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        btnMenu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
        flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);
        btnDisplay = (ImageButton) mRootView.findViewById(R.id.btn_display);

        btnMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }
    /**
     * 侧边栏展开或者收起的方法
     */
    private void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();// 开关(如果状态为开,它就关;如果状态为关,它就开)
    }
    /**
     * 初始化数据
     */
    public abstract void initData();
}
