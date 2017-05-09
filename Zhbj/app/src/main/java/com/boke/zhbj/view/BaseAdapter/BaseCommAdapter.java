package com.boke.zhbj.view.BaseAdapter;

/**
 * Created by administrator on 2017/5/3.
 */
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
/**
 * 通用适配器基类
 * @author 漆可
 * @date 2016-6-28 下午4:23:51
 * @param <T>
 */
public abstract class BaseCommAdapter<T> extends BaseAdapter
{

    private List<T> mDatas;

    public BaseCommAdapter(List<T> datas)
    {
        mDatas = datas;
    }

    @Override
    public int getCount()
    {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position)
    {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = ViewHolder
                .newsInstance(convertView, parent.getContext(), getLayoutId());

        setUI(holder,position,parent.getContext());

        return holder.getConverView();
    }

    /**
     * 设置UI
     * @author 漆可
     * @date 2016-6-28 下午4:07:23
     * @param holder
     * @param position
     * @param context
     */
    protected abstract void setUI(ViewHolder holder, int position, Context context);

    /**
     * 获取适配器的布局id
     * @author 漆可
     * @date 2016-6-28 下午3:12:52
     * @return
     */
    protected abstract int getLayoutId();
}
