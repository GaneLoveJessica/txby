package com.eeesys.frame.listview.abs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.eeesys.frame.listview.model.CViewHolder;

import java.util.List;

public abstract class BaseListAdapter<V> extends BaseAdapter {

    protected List<V> list;
    protected Context context;
    private LayoutInflater mInflater;

    public BaseListAdapter(Context context, List<V> list) {
        this.list = list;
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public abstract void showView(CViewHolder holder, V v, int position);

    @SuppressWarnings("unchecked")
    public View getItemView(int position, View convertView, ViewGroup parent) {
        CViewHolder holder = null;
        if (convertView == null) {
            holder = new CViewHolder();
            if (getLayoutId() > 0) {
                convertView = mInflater.inflate(getLayoutId(), parent, false);
            } else {
                convertView = getContentView();
            }
            initViewHolder(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (CViewHolder) convertView.getTag();
        }
        showView(holder, list.get(position), position);
        return convertView;
    }

    protected abstract int getLayoutId();

    protected abstract void initViewHolder(CViewHolder holder, View view);

    public View getContentView() {
        return null;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemView(position, convertView, parent);
    }

}
