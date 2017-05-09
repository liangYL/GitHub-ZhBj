package com.boke.zhbj.base.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.boke.zhbj.MainActivity;
import com.boke.zhbj.R;
import com.boke.zhbj.base.BaseFragment;
import com.boke.zhbj.base.pager.NewsCenterPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2017/3/29.
 */
public class LeftFragment extends BaseFragment {

    private ListView lvList;

    public ArrayList<String> dataArray;

    private MenuAdapter mAdapter;


    private int mCurrentPos;//被点击行

    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.fragment_left,null);

        lvList = (ListView) view.findViewById(R.id.lv_list);

        dataArray = new ArrayList<String>();
        dataArray.add("我的生活");
        dataArray.add("我的作品");
        dataArray.add("艺术人生");
        dataArray.add("my  love");

        return view;
    }

    @Override
    public void initData() {


        mAdapter = new MenuAdapter();
        lvList.setAdapter(mAdapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);

                mCurrentPos = position;
                mAdapter.notifyDataSetChanged();//加此句,getView()执行


                // 通知新闻中心,切换页面
                setCurrentMenuDetailPager(position);

                setSlidingMenuEnable(true);

            }
        });

        mCurrentPos = 0;
    }

    /**
     * 切换菜单详情页页面
     *
     * @param position
     */
    protected void setCurrentMenuDetailPager(int position) {
        // 获取新闻中心对象NewsCenterPager
        // 1.先获取MainActivity,
        // 2.通过MainActiivty获取ContentFragment
        // 3.通过ContentFragment获取NewsCenterPager
        MainActivity mainUI = (MainActivity) mActivity;
        ContentFragment contentFragment = mainUI.getContentFragment();
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();

        // 给新闻中心页面的FrameLayout填充布局
        newsCenterPager.setCurrentMenuToDetailPager(position);
    }




    class MenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return dataArray.size();
        }

        @Override
        public Object getItem(int position) {
            return dataArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view =  View.inflate(mActivity,R.layout.list_item_left_menu,null);
            TextView tt = (TextView) view.findViewById(R.id.left_title);
            tt.setText(dataArray.get(position));

            if(mCurrentPos == position){
                tt.setEnabled(true);//可用为红色
                System.out.println("yes");

            }else {
                tt.setEnabled(false);//不可用为白色
                System.out.println("no");

            }
            return view;
        }
    }

    /**
     * 设置侧边栏可用不可用
     *
     * @param enable
     */
    private void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();// 开关(如果状态为开,它就关;如果状态为关,它就开)
    }

}
