package com.example.base.view.viewpager;

/**
 *  ViewHolder生成器，用来生成各种ViewHolder:
 * @param <VH>
 */
public interface ViewPagerHolderCreator <VH extends ViewPagerHolder> {

    /**
     * 创建viewHolder
     * @return
     */
    VH createViewHolder();
}
