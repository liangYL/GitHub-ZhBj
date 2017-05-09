package com.boke.zhbj.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.boke.zhbj.base.BaseMenuDetailPager;

/**
 * Created by administrator on 2017/4/7.
 */
public class TopicMenuDetailPager extends BaseMenuDetailPager{
    public TopicMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView view = new TextView(mActivity);
        view.setText("我的作品");
        view.setTextColor(Color.GREEN);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
