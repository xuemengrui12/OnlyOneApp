package com.oo.onlyone.activity;

import android.os.Bundle;
import android.webkit.WebSettings;

import com.oo.onlyone.base.BaseWebActivity;
import com.oo.onlyone.jni.JniManager;
import com.oo.onlyone.util.NetWorkUtils;


public class ProtocolActivity extends BaseWebActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mNetWorkUtils.getConnectState() != NetWorkUtils.NetWorkState.NONE) {
			mWebView.loadUrl(JniManager.getInstance().getProtocolUrl());
			mWebView.getSettings().setLayoutAlgorithm(
					WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		} else {
			showCustomToast("当前网络不可用,请检查");
		}
	}

	@Override
	protected void onResume() {
		AboutTabsActivity.mHeaderLayout.setDefaultTitle("用户协议", null);
		super.onResume();
	}
}
