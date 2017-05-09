package com.boke.zhbj.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boke.zhbj.R;
import com.boke.zhbj.base.BaseMenuDetailPager;
import com.boke.zhbj.global.Constants;
import com.boke.zhbj.objects.Imgs;
import com.boke.zhbj.utils.XUtilsImageUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by administrator on 2017/4/7.
 */
public class PhotosMenuDetailPager extends BaseMenuDetailPager  implements View.OnClickListener{

    private ListView lvList;
    private GridView gvList;


    private boolean isList = true;// 当前界面状态

    private ImageButton btnDisplay;

    //请求对象数组
    private ArrayList<Imgs> listArray;

    public PhotosMenuDetailPager(Activity activity,ImageButton btnDisplay) {
        super(activity);

        this.btnDisplay = btnDisplay;

        btnDisplay.setOnClickListener(this);

        listArray = new ArrayList<Imgs>();

    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_menu_detail_photo,
                null);
        lvList = (ListView)view.findViewById(R.id.lv_list);
        gvList = (GridView)view.findViewById(R.id.gv_list);


        return view;
    }

    @Override
    public void initData() {

        getDataFromServer();


    }

    /**
     * 请求数据
     */
    private void getDataFromServer() {


        RequestParams params = new RequestParams(Constants.CATEGORIES_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                //System.out.println(result);
                //处理返回数据
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

    //处理返回数据
    protected void processResult(String result) {

        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(result).getAsJsonArray();
        Gson gson = new Gson();

        ArrayList pagerArray = new ArrayList<Imgs>();
        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            Imgs img = gson.fromJson(user, Imgs.class);
            pagerArray.add(img);
        }
        listArray.addAll(pagerArray);


//        listArray.get(0).setImgUrl("http://img.hb.aicdn.com/f5463e0e3eef66b56494041cae126b67cb5137404c505-l8nENq_fw658");
//        listArray.get(1).setImgUrl("http://img.hb.aicdn.com/daa107dcc3c3c9d19d3143dc7ee87325959864255c2fe-9ZVZp9_fw658");
//        listArray.get(2).setImgUrl("http://img.hb.aicdn.com/e06e802fa767e72cca779d4e486876a533c4195f48f61-quIqX7_fw658");
//        listArray.get(3).setImgUrl("http://img.hb.aicdn.com/f5463e0e3eef66b56494041cae126b67cb5137404c505-l8nENq_fw658");
//        listArray.get(4).setImgUrl("http://img.hb.aicdn.com/1826ca26ee9922f105576a20176bfc57ac2ea69951278-BLhSdN_fw658");
//        listArray.get(5).setImgUrl("http://img.hb.aicdn.com/f6d8825ed54b76f9d06b83eb09c9c3040df6308d8a535-eUXyrf_fw658");
//        listArray.get(6).setImgUrl("http://img.hb.aicdn.com/adddb2fd4836d635f5e7da4261b449ce25e6acf14b62d-IuHVgA_fw658");


        //设置listAdapter
        if (listArray != null) {

            lvList.setAdapter(new PhotoAdapter());
            gvList.setAdapter(new PhotoAdapter());
        }



    }



    class PhotoAdapter extends BaseAdapter {

//        //private BitmapUtils mBitmapUtils;
//        private MyBitmapUtils mBitmapUtils;
//
//        public PhotoAdapter() {
//            //mBitmapUtils = new BitmapUtils(mActivity);
//            //mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
//            mBitmapUtils = new MyBitmapUtils();
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
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_photo,
                        null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView) convertView
                        .findViewById(R.id.tv_title);
                holder.ivIcon = (ImageView) convertView
                        .findViewById(R.id.iv_icon);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Imgs item = getItem(position);

            holder.tvTitle.setText(item.getName());


            XUtilsImageUtils.display(holder.ivIcon,listArray.get(position).getImgUrl());

            return convertView;
        }

    }

    static class ViewHolder {
        public TextView tvTitle;
        public ImageView ivIcon;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_display:
                // 如果当前是列表, 要切换成grid, 如果是grid,就切换成列表
                if (isList) {
                    isList = false;

                    lvList.setVisibility(View.GONE);
                    gvList.setVisibility(View.VISIBLE);

                    btnDisplay.setImageResource(R.drawable.icon_pic_list_type);
                } else {
                    isList = true;

                    lvList.setVisibility(View.VISIBLE);
                    gvList.setVisibility(View.GONE);

                    btnDisplay.setImageResource(R.drawable.icon_pic_grid_type);
                }

                break;

            default:
                break;
        }
    }

}
