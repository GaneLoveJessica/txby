package com.eeesys.frame.listview.inter;

import android.content.Context;
import android.support.annotation.LayoutRes;

/**
 * Created by Administrator on 2016/5/18.
 */
public interface IExpandableListItem<T, V, E, F> {
    @LayoutRes
    int getGroupLayoutResId();

    @LayoutRes
    int getchildLayoutResId();

    void handleGroupData(T holder, Context context, E data, int type);

    void handleChildData(V holder, Context context, F data, int type);
}
