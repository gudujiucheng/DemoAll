package com.canzhang.sample.manager.view.recyclerView.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.view.recyclerView.bean.AppItemBean;
import com.canzhang.sample.manager.view.recyclerView.bean.PageItem;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.base.view.viewpager.CommonViewPager;
import com.example.base.view.viewpager.ViewPagerHolder;
import com.example.base.view.viewpager.ViewPagerHolderCreator;

import java.util.List;

public class AppAdapter extends BaseMultiItemQuickAdapter<AppItemBean, BaseViewHolder> {

    public AppAdapter(List<AppItemBean> data) {
        super(data);
        addItemType(AppItemBean.APP_ITEM_01, R.layout.sample_app_item_01);
        addItemType(AppItemBean.APP_ITEM_02, R.layout.sample_app_item_02);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final AppItemBean item) {
        switch (holder.getItemViewType()) {
            case AppItemBean.APP_ITEM_01:
                setPageData(holder, item);
                break;
            case AppItemBean.APP_ITEM_02:
                break;
        }
    }

    private void setPageData(BaseViewHolder holder, AppItemBean item) {
        CommonViewPager<PageItem> viewPager =  holder.getView(R.id.view_pager);
        // 设置数据
        viewPager.setPages(item.getPageItems(), new ViewPagerHolderCreator<ViewImageHolder>() {
            @Override
            public ViewImageHolder createViewHolder() {
                // 返回ViewPagerHolder
                return new ViewImageHolder();
            }
        });
    }


    /**
     * 提供ViewPager展示的ViewHolder
     * <P>用于提供布局和绑定数据</P>
     */
    public static class ViewImageHolder implements ViewPagerHolder<PageItem> {
        private ImageView mImageView;
        private TextView mTextView;
        @Override
        public View createView(Context context) {
            // 返回ViewPager 页面展示的布局
            View view = LayoutInflater.from(context).inflate(R.layout.sample_viewpager_item,null);
            mImageView = (ImageView) view.findViewById(R.id.viewPager_item_image);
            mTextView = (TextView) view.findViewById(R.id.item_desc);
            return view;
        }

        @Override
        public void onBind(Context context, int position, PageItem data) {
            // 数据绑定
            // 自己绑定数据，灵活度很大
            mImageView.setImageResource(data.res);
            mTextView.setText(data.text);
        }
    }

}
