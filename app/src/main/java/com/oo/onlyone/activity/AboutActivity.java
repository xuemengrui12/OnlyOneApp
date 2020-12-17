package com.oo.onlyone.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.oo.onlyone.R;
import com.oo.onlyone.base.BaseActivity;
import com.oo.onlyone.view.HandyTextView;


public class AboutActivity extends BaseActivity implements OnClickListener {

	private HandyTextView mHtvVersionName;
	private Button mBtnCheckNewVersion;
	private Button mBtnIntroduction;
	private Button mBtnGoOfficialWebsite;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initViews();
		initEvents();
		mHandler = new Handler();
	}

	@Override
	protected void initViews() {
		mHtvVersionName = findViewById(R.id.about_htv_versionname);
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			mHtvVersionName.setText("版本: Android " + packageInfo.versionName);
		} catch (NameNotFoundException e) {
			mHtvVersionName.setVisibility(View.GONE);
		}
		mBtnCheckNewVersion =  findViewById(R.id.about_btn_checknewversion);
		mBtnIntroduction =  findViewById(R.id.about_btn_introduction);
		mBtnGoOfficialWebsite =  findViewById(R.id.about_btn_go_official_website);
	}

	@Override
	protected void initEvents() {
		mBtnCheckNewVersion.setOnClickListener(this);
		mBtnIntroduction.setOnClickListener(this);
		mBtnGoOfficialWebsite.setOnClickListener(this);
	}

	private void markMomo() {
		String str = "market://details?id=com.immomo.momo";
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(str));
			startActivity(intent);
		} catch (Exception e) {
			showCustomToast("找不到应用市场,无法对OO评分");
		}
	}

	@Override
	protected void onResume() {

		AboutTabsActivity.mHeaderLayout.setDefaultTitle("关于OO", null);
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_btn_checknewversion:
			showLoadingDialog(null);
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					dismissLoadingDialog();
					showCustomToast("当前已是最新版");

				}
			}, 1500);
			break;

		case R.id.about_btn_introduction:
			startActivity(UserGuiDeActivity.class);
			break;

		case R.id.about_btn_go_official_website:
			markMomo();
			break;
		}
	}
}
