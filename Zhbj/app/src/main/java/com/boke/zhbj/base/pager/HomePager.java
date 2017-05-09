package com.boke.zhbj.base.pager;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.boke.zhbj.R;
import com.boke.zhbj.base.BasePager;


/**
 * Created by administrator on 2017/3/30.
 */
public class HomePager extends BasePager {

    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("首页初始化...");

        tvTitle.setText("主页");
        btnMenu.setVisibility(View.GONE);


        TextView view = new TextView(mActivity);
        view.setText("首页");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);

        flContent.addView(view);



    }
}
