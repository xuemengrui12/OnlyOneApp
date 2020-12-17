package com.oo.onlyone.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;

import com.oo.onlyone.R;
import com.oo.onlyone.adapter.OtherFeedListAdapter;
import com.oo.onlyone.base.BaseActivity;
import com.oo.onlyone.entity.Feed;
import com.oo.onlyone.entity.NearByPeople;
import com.oo.onlyone.entity.NearByPeopleProfile;
import com.oo.onlyone.util.JsonResolveUtils;
import com.oo.onlyone.view.HeaderLayout;
import com.oo.onlyone.view.MoMoRefreshListView;


public class OtherFeedListActivity extends BaseActivity implements
		MoMoRefreshListView.OnRefreshListener, MoMoRefreshListView.OnCancelListener {

	private HeaderLayout mHeaderLayout;
	private MoMoRefreshListView mMmrlvList;
	private OtherFeedListAdapter mAdapter;
	private NearByPeople mPeople;
	private NearByPeopleProfile mProfile;

	private List<Feed> mFeeds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_otherfeedlist);
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.otherfeedlist_header);
		mHeaderLayout.init(HeaderLayout.HeaderStyle.DEFAULT_TITLE);
		mMmrlvList = (MoMoRefreshListView) findViewById(R.id.otherfeedlist_mmrlv_list);
	}

	@Override
	protected void initEvents() {
		mMmrlvList.setOnRefreshListener(this);
		mMmrlvList.setOnCancelListener(this);
	}

	private void init() {
		mMmrlvList.setItemsCanFocus(false);
		mProfile = getIntent().getParcelableExtra("entity_profile");
		mPeople = getIntent().getParcelableExtra("entity_people");
		mHeaderLayout.setDefaultTitle(mProfile.getName() + "的动态", null);
		getStatus();
	}

	private void getStatus() {
		if (mFeeds == null) {
			putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					showLoadingDialog("正在加载,请稍后...");
				}

				@Override
				protected Boolean doInBackground(Void... params) {
					mFeeds = new ArrayList<Feed>();
					return JsonResolveUtils.resolveNearbyStatus(
							OtherFeedListActivity.this, mFeeds,
							mProfile.getUid());
				}

				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);
					dismissLoadingDialog();
					if (!result) {
						showCustomToast("数据加载失败...");
					} else {
						mAdapter = new OtherFeedListAdapter(mProfile, mPeople,
								mApplication, OtherFeedListActivity.this,
								mFeeds);
						mMmrlvList.setAdapter(mAdapter);
					}
				}

			});
		}
	}

	@Override
	public void onCancel() {
		clearAsyncTask();
		mMmrlvList.onRefreshComplete();
	}

	@Override
	public void onRefresh() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {

				}
				return null;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				mMmrlvList.onRefreshComplete();
			}
		});
	}
}
