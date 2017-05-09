package com.boke.zhbj.base.menudetail;

/**
 * Created by administrator on 2017/4/7.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boke.zhbj.R;
import com.boke.zhbj.base.BaseMenuDetailPager;
import com.boke.zhbj.global.Constants;
import com.boke.zhbj.objects.Imgs;
import com.boke.zhbj.utils.CacheUtils;
import com.boke.zhbj.utils.PrefUtils;
import com.boke.zhbj.utils.XUtilsImageUtils;
import com.boke.zhbj.view.HorizontalScrollViewPager;
import com.boke.zhbj.view.RefreshListView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.viewpagerindicator.CirclePageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 *
 * ViewPagerIndicator使用流程:
 * 1. 引入Library库
 * 2. 布局文件中配置TabPageIndicator
 * 3. 将指针和Viewpager关联起来
 * 4. 重写getPageTitle方法,返回每个页面的标题(PagerAdapter)
 * 5. 设置activity主题样式
 * 6. 修改源码中的样式(修改图片, 文字颜色)
 *
 * @author Kevin
 * @date 2015-8-11
 */
public class TabDetailPager extends BaseMenuDetailPager{

    private String tag;


    //轮播图 pagerView
//    @ViewInject(R.id.vp_tab_detail)
    private HorizontalScrollViewPager mViewPager;
//    //轮播图小圆点
//    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator;
//    //listView
    //@ViewInject(R.id.lv_tab_detail)
    private RefreshListView lvList;

    //请求对象数组
    private ArrayList<Imgs> pagerArray;
    //请求对象数组
    private ArrayList<Imgs> listArray;


    // 轮播图适配器
    private TopNewsAdapter mTopNewsAdapter;
    // list适配器
    private NewsAdapter mNewsAdapter;


    private String mMoreUrl;// 下一页的链接

    //轮播图滚动
    private Handler mHandler = null;


    public TabDetailPager(Activity activity,String tagName) {
        super(activity);
        tag = tagName;
        listArray = new ArrayList<Imgs>();

    }
    @Override
    public View initView() {
        //初始化根布局
        View aview = View.inflate(mActivity,R.layout.pager_tab_detail,null);
        //初始化轮播图布局
        View header = View.inflate(mActivity, R.layout.list_header_topnews, null);
        //初始化列表
        lvList = (RefreshListView) aview.findViewById(R.id.lv_tab_detail);
        //将轮播图作为列表头部添加
        lvList.addHeaderView(header);

        //轮播图ViewPager初始化
        mViewPager = (HorizontalScrollViewPager) aview.findViewById(R.id.vp_tab_detail);
        //轮播图指针初始化
        mIndicator = (CirclePageIndicator) aview.findViewById(R.id.indicator);

        // 设置下拉刷新监听
        lvList.setOnRefreshListener(new RefreshListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // 从网络加载数据
                getDataFromServer(false);
            }

            @Override
            public void loadMore() {
                getDataFromServer(true);

//                // 加载更多数据
//                if (mMoreUrl != null) {
//                    System.out.println("加载下一页数据...");
//                    getDataFromServer(true);
//                } else {
//                    lvList.onRefreshComplete(true);// 收起加载更多布局
//                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT)
//                            .show();
//                }
            }
        });

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("pos:" + position);

                // 当前点击的item的标题颜色置灰
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(Color.GRAY);

                Imgs img = listArray.get(position);

                // 将已读状态持久化到本地
                // key: read_ids; value: 1324,1325,1326
                String readIds = PrefUtils.getString(mActivity,"read_ids", "");
                if (!readIds.contains(String.valueOf(img.getId()))){// 以前没有添加过,才添加进来
                    readIds = readIds + img.getId() + ",";// 1324,1325,
                    PrefUtils.putString(mActivity,"read_ids", readIds);
                }
                // 跳到详情页
                Intent intent = new Intent(mActivity, NewsDetailWebActivity.class);
                intent.putExtra("url", img.getImgUrl());
                mActivity.startActivity(intent);
            }
        });



        return aview;
    }

    @Override
    public void initData() {

        String cache = CacheUtils.getCache(Constants.CATEGORIES_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            processResult(cache, false);
        }
        //请求数据
        getDataFromServer(false);

    }
    /**
     * get请求数据
     */
    private void getDataFromServer(final boolean isMore) {


        RequestParams params = new RequestParams(Constants.CATEGORIES_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                //System.out.println(result);
                //处理返回数据
                processResult(result, isMore);
                CacheUtils.setCache(Constants.CATEGORIES_URL, result, mActivity);

                // 收起下拉刷新控件
                lvList.onRefreshComplete(true);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // 收起下拉刷新控件
                lvList.onRefreshComplete(false);
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

    //处理返回数据
    protected void processResult(String result,boolean isMore) {

        if(!isMore){
            listArray.clear();
        }
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(result).getAsJsonArray();
        Gson gson = new Gson();

        pagerArray = new ArrayList<Imgs>();
        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            Imgs img = gson.fromJson(user, Imgs.class);
            pagerArray.add(img);
        }
        listArray.addAll(pagerArray);

        if(!isMore){
            //设置pagerAdapter
            if (pagerArray != null) {
                mTopNewsAdapter = new TopNewsAdapter();
                mViewPager.setAdapter(mTopNewsAdapter);
                mIndicator.setViewPager(mViewPager);// 将指示器和viewpager绑定
            }
            //设置listAdapter
            if (listArray != null) {
                mNewsAdapter = new NewsAdapter();
                lvList.setAdapter(mNewsAdapter);
            }

            //启动轮播方法
            if (mHandler == null) {
                mHandler = new Handler() {
                    public void handleMessage(android.os.Message msg) {
                        int currentItem = mViewPager.getCurrentItem();
                        if (currentItem < pagerArray.size() - 1) {
                            currentItem++;
                        } else {
                            currentItem = 0;
                        }
                        mViewPager.setCurrentItem(currentItem);
                        mHandler.sendEmptyMessageDelayed(0, 2000);
                    };
                };
                // 延时2秒切换广告条
                mHandler.sendEmptyMessageDelayed(0, 2000);

                mViewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                System.out.println("ACTION_DOWN");
                                // 删除所有消息
                                mHandler.removeCallbacksAndMessages(null);
                                break;
                            case MotionEvent.ACTION_CANCEL:// 事件取消(当按下后,然后移动下拉刷新,导致抬起后无法响应ACTION_UP,
                                // 但此时会响应ACTION_CANCEL,也需要继续播放轮播条)
                            case MotionEvent.ACTION_UP:
                                // 延时2秒切换广告条
                                mHandler.sendEmptyMessageDelayed(0, 2000);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
            }

        }else {
            mNewsAdapter.notifyDataSetChanged();// 刷新listview
        }

    }

    class TopNewsAdapter extends PagerAdapter {

//        BitmapUtils mBitmapUtils;
//
//        public TopNewsAdapter() {
//            // 初始化xutils中的加载图片的工具
//            mBitmapUtils = new BitmapUtils(mActivity);
//            // 设置默认加载图片
//            mBitmapUtils
//                    .configDefaultLoadingImage(R.drawable.topnews_item_default);
//        }


        @Override
        public int getCount() {
            return pagerArray.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView  imgView = new ImageView(mActivity);
            //view.setScaleType(ImageView.ScaleType.FIT_XY);// 设置图片填充效果, 表示填充父窗体
            // 获取图片链接, 使用链接下载图片, 将图片设置给ImageView, 考虑内存溢出问题, 图片本地缓存
            //mBitmapUtils.display(view, mTopNewsList.get(position).topimage);

            XUtilsImageUtils.display(imgView,pagerArray.get(position).getImgUrl());
            container.addView(imgView);
            return imgView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }



    class NewsAdapter extends BaseAdapter {

//        public BitmapUtils mBitmapUtils;
//
//        public NewsAdapter() {
//            mBitmapUtils = new BitmapUtils(mActivity);
//            mBitmapUtils
//                    .configDefaultLoadingImage(R.drawable.pic_item_list_default);
//        }

        @Override
        public int getCount() {
            return listArray.size();
        }

        @Override
        public Imgs getItem(int position) {
            return listArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_news,
                        null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView) convertView
                        .findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView
                        .findViewById(R.id.tv_date);
                holder.ivIcon = (ImageView) convertView
                        .findViewById(R.id.iv_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Imgs img= getItem(position);
            holder.tvTitle.setText(img.getName());

            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            holder.tvDate.setText(date);

            //mBitmapUtils.display(holder.ivIcon, news.listimage);
            XUtilsImageUtils.display(holder.ivIcon,listArray.get(position).getImgUrl());

            // 标记已读和未读
            String readIds = PrefUtils.getString(mActivity,"read_ids", "");
            if (readIds.contains(String.valueOf(img.getId()))) {
                // 已读
                holder.tvTitle.setTextColor(Color.GRAY);
            } else {
                // 未读
                holder.tvTitle.setTextColor(Color.BLACK);
            }

            return convertView;
        }

    }

    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public ImageView ivIcon;
    }










}
