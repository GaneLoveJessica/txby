package com.eeesys.frame.listview.inter;

import android.view.View;

public interface IChildViewHolder<T> {
	
	public  T getChildViewHolder();
	
	public  void initChildViewHolder(T holder, View view);
      
}
