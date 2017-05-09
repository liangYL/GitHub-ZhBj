package com.boke.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.boke.zhbj.utils.PrefUtils;

import java.util.ArrayList;

public class GuideActivity extends Activity implements View.OnClickListener {

    private ViewPager mPage;
    private int[] dataArray = new int[]{R.drawable.hei1,R.drawable.hei2,R.drawable.hei3};

    private LinearLayout llContainer;
    private ImageView ivPoint;

    private int mPointWidth;
    private Button startBtn;

    private ArrayList<ImageView>  imgViews ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

         mPage = (ViewPager) findViewById(R.id.vp_guide);
        llContainer = (LinearLayout)findViewById(R.id.ll_container);

        //小红点
        ivPoint = (ImageView) findViewById(R.id.iv_red_point);
        startBtn = (Button) findViewById(R.id.startBtn);
        //按钮设置监听
        startBtn.setOnClickListener(this);

        imgViews = new ArrayList<ImageView>();
        for(int i = 0;i < dataArray.length;i++){
            ImageView imgView = new ImageView(getApplicationContext());
            imgView.setBackgroundResource(dataArray[i]);
            imgViews.add(imgView);

            //初始化圆点
            ImageView pointImgView = new ImageView(this);
            pointImgView.setImageResource(R.drawable.shape_guide_default);

            //初始化圆点布局
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(i>0){
                params.leftMargin = 10;
            }
             pointImgView.setLayoutParams(params);

             llContainer.addView(pointImgView);
        }

        mPage.setAdapter(new myAdapter());


        ivPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override//layout方法执行结束(位置确定)
            public void onGlobalLayout() {
                //移除,只记录一次
                ivPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //页面绘制结束,计算圆点间距
                mPointWidth = llContainer.getChildAt(1).getLeft()-llContainer.getChildAt(0).getLeft();
            }
        });
        //设置监听事件
        mPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override//页面滑动中(当前位置,偏移百分比,)
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                //计算小红点左边距
                int leftMargin = (int) (mPointWidth * positionOffset + position * mPointWidth);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPoint.getLayoutParams();
                params.leftMargin  = leftMargin;
                ivPoint.setLayoutParams(params);

            }

            @Override
            public void onPageSelected(int position) {
                if(position == dataArray.length-1){
                    startBtn.setVisibility(View.VISIBLE);
                }else {
                    startBtn.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    class myAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return dataArray.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imgv =  imgViews.get(position);
            container.addView(imgv);
            return imgv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    //implements View.OnClickListener自动添加
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startBtn:

                //记录状态,已经启动过了
               PrefUtils.putBoolean(this,"is_guide_show",true);

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

}
