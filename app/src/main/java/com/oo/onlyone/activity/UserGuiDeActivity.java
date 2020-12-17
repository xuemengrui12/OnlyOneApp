package com.oo.onlyone.activity;

import android.os.Bundle;

import com.oo.onlyone.R;
import com.oo.onlyone.adapter.UserGuiDeAdapter;
import com.oo.onlyone.base.BaseActivity;
import com.oo.onlyone.view.ScrollViewPager;


public class UserGuiDeActivity extends BaseActivity {
	private ScrollViewPager mSvpPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userguide);
		initViews();
		initEvents();
	}

	@Override
	protected void initViews() {
		mSvpPager = (ScrollViewPager) findViewById(R.id.userguide_svp_pager);
		mSvpPager.setEnableTouchScroll(true);
		mSvpPager.setAdapter(new UserGuiDeAdapter(this));
	}

	@Override
	protected void initEvents() {

	}
}
