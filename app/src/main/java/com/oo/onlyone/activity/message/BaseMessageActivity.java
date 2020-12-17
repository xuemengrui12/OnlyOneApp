package com.oo.onlyone.activity.message;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oo.onlyone.R;
import com.oo.onlyone.activity.OtherProfileActivity;
import com.oo.onlyone.adapter.ChatAdapter;
import com.oo.onlyone.base.BaseActivity;
import com.oo.onlyone.base.BaseDialog;
import com.oo.onlyone.dialog.SimpleListDialog;
import com.oo.onlyone.entity.Message;
import com.oo.onlyone.entity.NearByPeople;
import com.oo.onlyone.entity.NearByPeopleProfile;
import com.oo.onlyone.popupwindow.ChatPopupWindow;
import com.oo.onlyone.util.PhotoUtils;
import com.oo.onlyone.view.ChatListView;
import com.oo.onlyone.view.EmoteInputView;
import com.oo.onlyone.view.EmoticonsEditText;
import com.oo.onlyone.view.HeaderLayout;
import com.oo.onlyone.view.ScrollLayout;


public abstract class BaseMessageActivity extends BaseActivity implements
		ScrollLayout.OnScrollToScreenListener, OnClickListener, OnTouchListener,
		TextWatcher, ChatPopupWindow.onChatPopupItemClickListener {

	protected HeaderLayout mHeaderLayout;
	protected ChatListView mClvList;
	protected ScrollLayout mLayoutScroll;
	protected LinearLayout mLayoutRounds;
	protected EmoteInputView mInputView;

	protected ImageButton mIbTextDitorPlus;
	protected ImageButton mIbTextDitorKeyBoard;
	protected ImageButton mIbTextDitorEmote;
	protected EmoticonsEditText mEetTextDitorEditer;
	protected Button mBtnTextDitorSend;
	protected ImageView mIvTextDitorAudio;

	protected ImageButton mIbAudioDitorPlus;
	protected ImageButton mIbAudioDitorKeyBoard;
	protected ImageView mIvAudioDitorAudioBtn;

	protected LinearLayout mLayoutFullScreenMask;
	protected LinearLayout mLayoutMessagePlusBar;
	protected LinearLayout mLayoutMessagePlusPicture;
	protected LinearLayout mLayoutMessagePlusCamera;
	protected LinearLayout mLayoutMessagePlusLocation;
	protected LinearLayout mLayoutMessagePlusGift;

	protected List<Message> mMessages = new ArrayList<Message>();
	protected ChatAdapter mAdapter;

	protected NearByPeople mPeople;
	protected NearByPeopleProfile mProfile;

	protected Bitmap mRoundsSelected;
	protected Bitmap mRoundsNormal;

	private ChatPopupWindow mChatPopupWindow;
	private int mWidth;
	private int mHeaderHeight;

	protected SimpleListDialog mDialog;
	protected int mCheckId = 0;

	protected BaseDialog mSynchronousDialog;

	protected String mCameraImagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		initViews();
		initEvents();
	}

	protected class OnMiddleImageButtonClickListener implements
			HeaderLayout.onMiddleImageButtonClickListener {

		@Override
		public void onClick() {
			Intent intent = new Intent(BaseMessageActivity.this,
					OtherProfileActivity.class);
			intent.putExtra("uid", mPeople.getUid());
			intent.putExtra("name", mPeople.getName());
			intent.putExtra("avatar", mPeople.getAvatar());
			intent.putExtra("entity_people", mPeople);
			startActivity(intent);
			finish();
		}
	}

	protected class OnRightImageButtonClickListener implements
			HeaderLayout.onRightImageButtonClickListener {

		@Override
		public void onClick() {
			mChatPopupWindow.showAtLocation(mHeaderLayout, Gravity.RIGHT
					| Gravity.TOP, -10, mHeaderHeight + 10);
		}
	}

	protected void showKeyBoard() {
		if (mInputView.isShown()) {
			mInputView.setVisibility(View.GONE);
		}
		mEetTextDitorEditer.requestFocus();
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.showSoftInput(mEetTextDitorEditer, 0);
	}

	protected void hideKeyBoard() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(BaseMessageActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}

	protected void showPlusBar() {
		mLayoutFullScreenMask.setEnabled(true);
		mLayoutMessagePlusBar.setEnabled(true);
		mLayoutMessagePlusPicture.setEnabled(true);
		mLayoutMessagePlusCamera.setEnabled(true);
		mLayoutMessagePlusLocation.setEnabled(true);
		mLayoutMessagePlusGift.setEnabled(true);
		Animation animation = AnimationUtils.loadAnimation(
				BaseMessageActivity.this, R.anim.controller_enter);
		mLayoutMessagePlusBar.setAnimation(animation);
		mLayoutMessagePlusBar.setVisibility(View.VISIBLE);
		mLayoutFullScreenMask.setVisibility(View.VISIBLE);
	}

	protected void hidePlusBar() {
		mLayoutFullScreenMask.setEnabled(false);
		mLayoutMessagePlusBar.setEnabled(false);
		mLayoutMessagePlusPicture.setEnabled(false);
		mLayoutMessagePlusCamera.setEnabled(false);
		mLayoutMessagePlusLocation.setEnabled(false);
		mLayoutMessagePlusGift.setEnabled(false);
		mLayoutFullScreenMask.setVisibility(View.GONE);
		Animation animation = AnimationUtils.loadAnimation(
				BaseMessageActivity.this, R.anim.controller_exit);
		animation.setInterpolator(AnimationUtils.loadInterpolator(
				BaseMessageActivity.this,
				android.R.anim.anticipate_interpolator));
		mLayoutMessagePlusBar.setAnimation(animation);
		mLayoutMessagePlusBar.setVisibility(View.GONE);
	}

	protected void initRounds() {
		mRoundsSelected = PhotoUtils.getRoundBitmap(BaseMessageActivity.this,
				getResources().getColor(R.color.msg_short_line_selected));
		mRoundsNormal = PhotoUtils.getRoundBitmap(BaseMessageActivity.this,
				getResources().getColor(R.color.msg_short_line_normal));
		for (int i = 0; i < mLayoutScroll.getChildCount(); i++) {
			ImageView imageView = (ImageView) LayoutInflater.from(
					BaseMessageActivity.this).inflate(
					R.layout.include_message_shortline, null);
			imageView.setImageBitmap(mRoundsNormal);
			mLayoutRounds.addView(imageView);
		}
		((ImageView) mLayoutRounds.getChildAt(0))
				.setImageBitmap(mRoundsSelected);
	}

	protected void initPopupWindow() {
		mWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				130, getResources().getDisplayMetrics());
		mHeaderHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 48, getResources()
						.getDisplayMetrics());
		mChatPopupWindow = new ChatPopupWindow(this, mWidth,
				LayoutParams.WRAP_CONTENT);
		mChatPopupWindow.setOnChatPopupItemClickListener(this);
	}

	protected void initSynchronousDialog() {
		mSynchronousDialog = BaseDialog.getDialog(BaseMessageActivity.this,
				"提示", "成为陌陌会员即可同步好友聊天记录", "查看详情",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				}, "取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		mSynchronousDialog
				.setButton1Background(R.drawable.btn_default_popsubmit);
	}

	protected class OnVoiceModeDialogItemClickListener implements
			SimpleListDialog.onSimpleListItemClickListener {

		@Override
		public void onItemClick(int position) {
			mCheckId = position;
		}
	}
}
