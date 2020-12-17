package com.oo.onlyone.activity.maintabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow.OnDismissListener;

import com.oo.onlyone.R;
import com.oo.onlyone.base.BasePopupWindow;
import com.oo.onlyone.popupwindow.NearByPopupWindow;
import com.oo.onlyone.view.HeaderLayout;
import com.oo.onlyone.view.HeaderLayout.onMiddleImageButtonClickListener;
import com.oo.onlyone.view.HeaderSpinner;
import com.oo.onlyone.view.SwitcherButton;


public class NearByActivity extends TabItemActivity {

	private HeaderLayout mHeaderLayout;
	private HeaderSpinner mHeaderSpinner;
	private NearByPeopleFragment mPeopleFragment;
	private NearByGroupFragment mGroupFragment;

	private NearByPopupWindow mPopupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby);
		initPopupWindow();
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {
		mHeaderLayout = findViewById(R.id.nearby_header);
		mHeaderLayout.initSearch(new OnSearchClickListener());
		mHeaderSpinner = mHeaderLayout.setTitleNearBy("附近",
				new OnSpinnerClickListener(), "附近群组",
				R.drawable.ic_topbar_search,
				new OnMiddleImageButtonClickListener(), "个人", "群组",
				new OnSwitcherButtonClickListener());
		mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_NEARBY_PEOPLE);
	}

	@Override
	protected void initEvents() {

	}

	@Override
	protected void init() {
		mPeopleFragment = new NearByPeopleFragment(mApplication, this, this);
		mGroupFragment = new NearByGroupFragment(mApplication, this, this);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.nearby_layout_content, mPeopleFragment).commit();
	}

	private void initPopupWindow() {
		mPopupWindow = new NearByPopupWindow(this);
		mPopupWindow.setOnSubmitClickListener(new BasePopupWindow.onSubmitClickListener() {

			@Override
			public void onClick() {
				mPeopleFragment.onManualRefresh();
			}
		});
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				mHeaderSpinner.initSpinnerState(false);
			}
		});
	}

	public class OnSpinnerClickListener implements HeaderSpinner.onSpinnerClickListener {

		@Override
		public void onClick(boolean isSelect) {
			if (isSelect) {
				mPopupWindow
						.showViewTopCenter(findViewById(R.id.nearby_layout_root));
			} else {
				mPopupWindow.dismiss();
			}
		}
	}

	public class OnSearchClickListener implements HeaderLayout.onSearchListener {

		@Override
		public void onSearch(EditText et) {
			String s = et.getText().toString().trim();
			if (TextUtils.isEmpty(s)) {
				showCustomToast("请输入搜索关键字");
				et.requestFocus();
			} else {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(NearByActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						mHeaderLayout.changeSearchState(HeaderLayout.SearchState.SEARCH);
					}

					@Override
					protected Boolean doInBackground(Void... params) {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return false;
					}

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						mHeaderLayout.changeSearchState(HeaderLayout.SearchState.INPUT);
						showCustomToast("未找到搜索的群");
					}
				});
			}
		}

	}

	public class OnMiddleImageButtonClickListener implements
			onMiddleImageButtonClickListener {

		@Override
		public void onClick() {
			mHeaderLayout.showSearch();
		}
	}

	public class OnSwitcherButtonClickListener implements
			SwitcherButton.onSwitcherButtonClickListener {

		@Override
		public void onClick(SwitcherButton.SwitcherButtonState state) {
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.setCustomAnimations(R.anim.fragment_fadein,
					R.anim.fragment_fadeout);
			switch (state) {
			case LEFT:
				mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_NEARBY_PEOPLE);
				ft.replace(R.id.nearby_layout_content, mPeopleFragment)
						.commit();
				break;

			case RIGHT:
				mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_NEARBY_GROUP);
				ft.replace(R.id.nearby_layout_content, mGroupFragment).commit();
				break;
			}
		}

	}

	@Override
	public void onBackPressed() {
		if (mHeaderLayout.searchIsShowing()) {
			clearAsyncTask();
			mHeaderLayout.dismissSearch();
			mHeaderLayout.clearSearch();
			mHeaderLayout.changeSearchState(HeaderLayout.SearchState.INPUT);
		} else {
			finish();
		}
	}
}
