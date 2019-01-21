package com.example.base.view.viewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 链接：https://www.jianshu.com/p/fb24e3343a2e
 *
 * @param <T>
 */
public class CommonViewPagerAdapter<T> extends PagerAdapter {
    private List<T> mDatas;
    private ViewPagerHolderCreator mCreator;//ViewHolder生成器

    public CommonViewPagerAdapter(List<T> datas, ViewPagerHolderCreator creator) {
        mDatas = datas;
        mCreator = creator;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getView(position, container);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 获取viewPager 页面展示View
     *
     * @param position
     * @param container
     * @return
     */
    private View getView(int position, ViewGroup container) {

        //创建Holder
        ViewPagerHolder holder = mCreator.createViewHolder();
        View view = holder.createView(container.getContext());
        if (mDatas != null && mDatas.size() > 0) {
            // 数据绑定
            holder.onBind(container.getContext(), position, mDatas.get(position));
        }
        return view;
    }

}
