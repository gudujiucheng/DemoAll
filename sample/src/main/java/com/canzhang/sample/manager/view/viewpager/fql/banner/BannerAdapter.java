package com.canzhang.sample.manager.view.viewpager.fql.banner;

import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @Author:Owenli
 * @Create:2019/3/5
 * @Description:每个item类型必须一致！
 */
public abstract class BannerAdapter<T> extends PagerAdapter {

    private Queue<View> viewPool = new LinkedList<>();
    private List<T> mList;

    public BannerAdapter(List<T> list) {
        mList = list;
    }

//先不支持设置
//    public void setData(List<T> list) {
//        mList = list;
//        notifyDataSetChanged();
//    }

    public List<T> getData() {
        return mList;
    }

    @Override
    public int getCount() {
        if (mList == null || mList.size() == 0) {
            return 0;
        }
        return Banner.VIEWPAGER_TOTAL_NUMBER;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        viewPool.offer((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mList != null && mList.size() > 0) {
            position = position % mList.size();
            T t = mList.get(position);

            View v;
            if (viewPool.size() > 0) {
                v = viewPool.poll();
            } else {
                v = createView(container, t, position);
            }
            bindData(v, t, position);
            container.addView(v);
            return v;
        } else {
            return new View(container.getContext());
        }
    }

    public abstract View createView(ViewGroup container, T t, int position);

    public abstract void bindData(View view, T t, int position);

}
