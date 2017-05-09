package com.boke.zhbj.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by administrator on 2017/4/7.
 */
public abstract class BaseMenuDetailPager {


    public Activity mActivity;
    // 菜单详情页根布局
    public View mRootView;

    public BaseMenuDetailPager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }

    public abstract View initView();

    public void initData() {

    }



}
