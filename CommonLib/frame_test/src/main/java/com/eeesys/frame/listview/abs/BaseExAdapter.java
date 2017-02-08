package com.eeesys.frame.listview.abs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.eeesys.frame.listview.model.CViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public abstract class BaseExAdapter<T, E> extends BaseExpandableListAdapter {

    private Context context;
    protected List<T> groupList;
    protected List<List<E>> childList;

    public BaseExAdapter(Context context, List<T> groupList, List<List<E>> childList) {
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public T getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public E getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        CViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(getGroupLayoutId(), null);
            holder = new CViewHolder();
            findGroupView(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (CViewHolder) convertView.getTag();
        }

        initGroupView(holder, groupList.get(groupPosition),groupPosition);

        return convertView;
    }

    protected abstract void initGroupView(CViewHolder holder, T t, int groupPosition);

    protected abstract void findGroupView(CViewHolder holder, View convertView);

    protected abstract int getGroupLayoutId();

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(getChildLayoutId(), null);
            holder =new CViewHolder();
            findChildView(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (CViewHolder) convertView.getTag();
        }

        initChildView(holder, childList.get(groupPosition).get(childPosition),groupPosition,childPosition);

        return convertView;
    }

    protected abstract void initChildView(CViewHolder holder, E e, int groupPosition, int childPosition);

    protected abstract void findChildView(CViewHolder holder, View convertView);

    protected abstract int getChildLayoutId();

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
