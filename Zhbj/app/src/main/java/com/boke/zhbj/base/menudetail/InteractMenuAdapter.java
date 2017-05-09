package com.boke.zhbj.base.menudetail;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.boke.zhbj.R;
import com.boke.zhbj.objects.Imgs;
import com.boke.zhbj.utils.XUtilsImageUtils;
import com.boke.zhbj.view.BaseAdapter.BaseCommAdapter;
import com.boke.zhbj.view.BaseAdapter.ViewHolder;

import java.util.List;

/**
 * Created by administrator on 2017/5/3.
 */
public class InteractMenuAdapter extends BaseCommAdapter<Imgs>{
    public InteractMenuAdapter(List<Imgs> datas) {
        super(datas);
    }

    @Override
    protected void setUI(ViewHolder holder, int position, Context context) {
        Imgs imgs = getItem(position);

        TextView tv_title = holder.getItemView(R.id.tv_title);
        tv_title.setText(imgs.getName());

        ImageView iv_head = holder.getItemView(R.id.iv_icon);
        XUtilsImageUtils.display(iv_head,imgs.getImgUrl());

    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_item_interact;
    }
}
