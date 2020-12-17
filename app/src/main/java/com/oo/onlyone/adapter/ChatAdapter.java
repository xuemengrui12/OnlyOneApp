package com.oo.onlyone.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.oo.onlyone.activity.message.MessageItem;
import com.oo.onlyone.base.BaseApplication;
import com.oo.onlyone.base.BaseObjectListAdapter;
import com.oo.onlyone.entity.Entity;
import com.oo.onlyone.entity.Message;


public class ChatAdapter extends BaseObjectListAdapter {

	public ChatAdapter(BaseApplication application, Context context,
					   List<? extends Entity> datas) {
		super(application, context, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message msg = (Message) getItem(position);
		MessageItem messageItem = MessageItem.getInstance(msg, mContext);
		messageItem.fillContent();
		View view = messageItem.getRootView();
		return view;
	}
}
