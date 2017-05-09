package com.boke.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.boke.zhbj.utils.PrefUtils;



public class LaunchActivity extends Activity {


    private RelativeLayout rl_root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);


        rl_root = (RelativeLayout) findViewById(R.id.rl_root);

        //旋转
        RotateAnimation rotAni = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotAni.setDuration(1000);//延时时间
        rotAni.setFillAfter(true);//显示

        //缩放
        ScaleAnimation scaAni = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaAni.setDuration(1000);//延时时间
        scaAni.setFillAfter(true);//显示

        //渐变
        AlphaAnimation alpAni = new AlphaAnimation(0,1);
        alpAni.setDuration(2000);//延时时间
        alpAni.setFillAfter(true);//显示


        //动画集
        AnimationSet animSet = new AnimationSet(true);
        animSet.addAnimation(rotAni);
        animSet.addAnimation(scaAni);
        animSet.addAnimation(alpAni);

        rl_root.startAnimation(animSet);

        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //判断是否需要新手引导
                boolean isGuideShow = PrefUtils.getBoolean(getApplicationContext(),"is_guide_show",false);

                if(isGuideShow){
                    //跳转到主界面
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else {
                    //调到新手引导
                    startActivity(new Intent(getApplicationContext(), GuideActivity.class));
                }

                //销毁
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }


}
