package com.eeesys.frame.listview.inter;

import android.view.View;

public interface IGroupViewHolder<T> {
	
	public  T getGroupViewHolder();
	
	public  void initGroupViewHolder(T holder, View view);
      
}
