package com.boke.zhbj.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boke.zhbj.R;
import com.boke.zhbj.base.BaseMenuDetailPager;
import com.boke.zhbj.objects.Imgs;
import com.boke.zhbj.utils.XUtilsImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2017/4/7.
 */
public class InteractMenuDetailPager extends BaseMenuDetailPager {
    private ListView lvList;
    private ViewPager viewPager;
    private List<Imgs> imgs;
    private TextView vp_num;
    private LinearLayout ll_vp;
    private TextView vp_back;


    public InteractMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_menu_detail_interact,
                null);
        lvList = (ListView)view.findViewById(R.id.lv_list);
        viewPager = (ViewPager)view.findViewById(R.id.vp_photos);
        vp_num = (TextView) view.findViewById(R.id.vp_num);
        ll_vp =  (LinearLayout) view.findViewById(R.id.ll_vp);
        vp_back = (TextView) view.findViewById(R.id.vp_back);

        imgs = new ArrayList<Imgs>();
        imgs.add(new Imgs("封面1","http://img.hb.aicdn.com/f5463e0e3eef66b56494041cae126b67cb5137404c505-l8nENq_fw658",0));
        imgs.add(new Imgs("封面2","http://img.hb.aicdn.com/daa107dcc3c3c9d19d3143dc7ee87325959864255c2fe-9ZVZp9_fw658",0));
        imgs.add(new Imgs("封面3","http://img.hb.aicdn.com/e06e802fa767e72cca779d4e486876a533c4195f48f61-quIqX7_fw658",0));
        imgs.add(new Imgs("封面4","http://img.hb.aicdn.com/1826ca26ee9922f105576a20176bfc57ac2ea69951278-BLhSdN_fw658",0));
        imgs.add(new Imgs("封面5","http://img.hb.aicdn.com/f6d8825ed54b76f9d06b83eb09c9c3040df6308d8a535-eUXyrf_fw658",0));
        imgs.add(new Imgs("封面6","http://img.hb.aicdn.com/adddb2fd4836d635f5e7da4261b449ce25e6acf14b62d-IuHVgA_fw658",0));

        //万能适配器
        InteractMenuAdapter mAdapter = new InteractMenuAdapter(imgs);
        lvList.setAdapter(mAdapter);

        photosAdapter mTopNewsAdapter = new photosAdapter();
        viewPager.setAdapter(mTopNewsAdapter);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                viewPager.setCurrentItem(position);
                vp_num.setText((String.valueOf(position+1))+ "/6");

                lvList.setVisibility(View.GONE);
                ll_vp.setVisibility(View.VISIBLE);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                vp_num.setText((String.valueOf(position+1))+ "/6");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        vp_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvList.setVisibility(View.VISIBLE);
                ll_vp.setVisibility(View.GONE);
            }
        });

        return view;
    }


    @Override
    public void initData() {


    }


    class photosAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return imgs.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imgView = new ImageView(mActivity);

            XUtilsImageUtils.display(imgView,imgs.get(position).getImgUrl());
            container.addView(imgView);

            return imgView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }




}
