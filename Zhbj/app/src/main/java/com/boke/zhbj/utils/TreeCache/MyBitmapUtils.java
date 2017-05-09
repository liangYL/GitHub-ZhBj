package com.boke.zhbj.utils.TreeCache;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.boke.zhbj.R;

/**
 * 三级缓存工具类
 *
 * @author Kevin
 * @date 2015-8-15
 */
public class MyBitmapUtils {

    // 网络缓存工具类
    private NetCacheUtils mNetUtils;
    // 本地缓存工具类
    private LocalCacheUtils mLocalUtils;
    // 内存缓存工具类
    private MemoryCacheUtils mMemoryUtils;

    public MyBitmapUtils() {
        mMemoryUtils = new MemoryCacheUtils();
        mLocalUtils = new LocalCacheUtils();
        mNetUtils = new NetCacheUtils(mLocalUtils, mMemoryUtils);
    }

    public void display(ImageView imageView, String url) {
        // 设置默认加载图片
        imageView.setImageResource(R.drawable.news_pic_default);

        // 先从内存缓存加载
        Bitmap bitmap = mMemoryUtils.getBitmapFromMemory(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            System.out.println("从内存读取图片啦...");
            return;
        }

        // 再从本地缓存加载
        bitmap = mLocalUtils.getBitmapFromLocal(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            System.out.println("从本地读取图片啦...");
            // 给内存设置图片
            mMemoryUtils.setBitmapToMemory(url, bitmap);
            return;
        }

        // 从网络缓存加载
        mNetUtils.getBitmapFromNet(imageView, url);
    }

}
