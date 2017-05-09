package com.boke.zhbj.base.pager;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boke.zhbj.MainActivity;
import com.boke.zhbj.R;
import com.boke.zhbj.base.BaseMenuDetailPager;
import com.boke.zhbj.base.BasePager;
import com.boke.zhbj.base.fragment.LeftFragment;
import com.boke.zhbj.base.menudetail.InteractMenuDetailPager;
import com.boke.zhbj.base.menudetail.NewsMenuDetailPager;
import com.boke.zhbj.base.menudetail.PhotosMenuDetailPager;
import com.boke.zhbj.base.menudetail.TopicMenuDetailPager;
import com.boke.zhbj.global.Constants;
import com.boke.zhbj.objects.Imgs;
import com.boke.zhbj.utils.CacheUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by administrator on 2017/3/30.
 */
public class NewsCenterPager extends BasePager {


    private ArrayList<Imgs>  imgs;
    // 菜单详情页集合
    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;


    public NewsCenterPager(Activity activity) {
        super(activity);
        imgs = new ArrayList<Imgs>();


    }

    @Override
    public void initData() {
        tvTitle.setText("新闻");
        //btnMenu.setVisibility(View.GONE);


        // 1.首先先看本地有没有缓存
        // 2.有缓存,直接加载缓存
        String cache = CacheUtils.getCache(Constants.CATEGORIES_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            // 有缓存
            System.out.println("发现缓存....");
            processResult(cache);
        }

        // 即使发现有缓存,仍继续调用网络, 获取最新数据
        getDataFromServer();

        // 初始化4个菜单详情页
        mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity,btnDisplay));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));

        setCurrentMenuToDetailPager(0);
    }

    // 给新闻中心页面的FrameLayout填充布局
    public void setCurrentMenuToDetailPager(int position) {
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position);
        // 移除之前所有的view对象, 清理屏幕
        flContent.removeAllViews();
        flContent.addView(pager.mRootView);
        pager.initData();// 初始化数据

        // 更改标题
//        tvTitle.setText(mNewsMenuData.data.get(position).title);

        System.out.println("------------------1----------------");
        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        LeftFragment leftMenuFragment = mainUI.getLeftMenuFragment();
       if(leftMenuFragment.dataArray != null && !leftMenuFragment.dataArray.isEmpty()){
           String ti =  leftMenuFragment.dataArray.get(position);
           tvTitle.setText(ti);
       }else {
           System.out.println("------------------数组 null----------------");
       }

        // 组图页面需要显示切换按钮
        if (pager instanceof PhotosMenuDetailPager) {
            btnDisplay.setVisibility(View.VISIBLE);
        } else {
            btnDisplay.setVisibility(View.GONE);
        }


    }


    private void getDataFromServer() {

        RequestParams params = new RequestParams(Constants.CATEGORIES_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println(result);

                processResult(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("------error------" + ex);

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void  processResult(String result){
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(result).getAsJsonArray();
        Gson gson = new Gson();

        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            Imgs img = gson.fromJson(user, Imgs.class);
            imgs.add(img);
        }
        System.out.println("------------" + imgs.size());

        initView();
    }


    private void initView(){

        ImageView aa = (ImageView) mRootView.findViewById(R.id.imageView2);

        ImageOptions imageOptions = new ImageOptions.Builder()
//                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
//                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true)
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                //设置加载过程中的图片
                .setLoadingDrawableId(R.drawable.ic_launcher)
                //设置加载失败后的图片
                .setFailureDrawableId(R.drawable.ic_launcher)
                //设置使用缓存
                .setUseMemCache(true)
                //设置支持gif
                .setIgnoreGif(false)
                //设置显示圆形图片
                .setCircular(false)
                .setSquare(true)
                .build();
        int num = (int) (Math.random()*5);
        System.out.println("------imgs------");
        x.image().bind(aa, imgs.get(num).getImgUrl(), imageOptions);
    }

}
