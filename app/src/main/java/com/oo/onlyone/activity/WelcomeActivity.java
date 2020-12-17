package com.oo.onlyone.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.oo.onlyone.R;
import com.oo.onlyone.activity.register.RegisterActivity;
import com.oo.onlyone.base.BaseActivity;
import com.oo.onlyone.base.BaseApplication;
import com.oo.onlyone.view.HandyTextView;


public class WelcomeActivity extends BaseActivity implements OnClickListener {

    private LinearLayout mLinearCtrlbar;
    private LinearLayout mLinearAvatars;
    private Button mBtnRegister;
    private Button mBtnLogin;
    private ImageButton mIbtnAbout;

    private View[] mMemberBlocks;
    //头像
    private String[] mAvatars = new String[]{"welcome_0", "welcome_1",
            "welcome_2", "welcome_3", "welcome_4", "welcome_5"};
    private String[] mDistances = new String[]{"0.84km", "1.02km", "1.34km",
            "1.88km", "2.50km", "2.78km"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mApplication = (BaseApplication) getApplication();

        initViews();
        initEvents();
        initAvatarsItem();
        showWelcomeAnimation();
    }

//	@Override
//	protected int layout() {
//		return R.layout.activity_welcome;
//	}

    @Override
    protected void initViews() {
        mLinearCtrlbar = findViewById(R.id.welcome_linear_ctrlbar);
        //头像布局
        mLinearAvatars = findViewById(R.id.welcome_linear_avatars);
        mBtnRegister = findViewById(R.id.welcome_btn_register);
        mBtnLogin = findViewById(R.id.welcome_btn_login);
        mIbtnAbout = findViewById(R.id.welcome_ibtn_about);
    }

    @Override
    protected void initEvents() {
        mBtnRegister.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mIbtnAbout.setOnClickListener(this);
    }

    /**
     * 初始化每个头像布局
     */
    private void initAvatarsItem() {
        initMemberBlocks();
        for (int i = 0; i < mMemberBlocks.length; i++) {
            mMemberBlocks[i].findViewById(R.id.welcome_item_iv_avatar)
                    .setBackground(new BitmapDrawable(getResources(), mApplication.getAvatar(mAvatars[i])));
//					.set(mApplication.getAvatar(mAvatars[i]));
            ((HandyTextView) mMemberBlocks[i]
                    .findViewById(R.id.welcome_item_htv_distance))
                    .setText(mDistances[i]);
        }
    }

    /**
     * 每个头像布局
     */
    private void initMemberBlocks() {
        mMemberBlocks = new View[6];
        mMemberBlocks[0] = findViewById(R.id.welcome_include_member_avatar_block0);
        mMemberBlocks[1] = findViewById(R.id.welcome_include_member_avatar_block1);
        mMemberBlocks[2] = findViewById(R.id.welcome_include_member_avatar_block2);
        mMemberBlocks[3] = findViewById(R.id.welcome_include_member_avatar_block3);
        mMemberBlocks[4] = findViewById(R.id.welcome_include_member_avatar_block4);
        mMemberBlocks[5] = findViewById(R.id.welcome_include_member_avatar_block5);

        int margin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        int widthAndHeight = (mScreenWidth - margin * 12) / 6;
        for (int i = 0; i < mMemberBlocks.length; i++) {
            ViewGroup.LayoutParams params = mMemberBlocks[i].findViewById(R.id.welcome_item_iv_avatar).getLayoutParams();
            params.width = widthAndHeight;
            params.height = widthAndHeight;
            mMemberBlocks[i].findViewById(R.id.welcome_item_iv_avatar).setLayoutParams(params);
        }
        mLinearAvatars.invalidate();
    }

    private void showWelcomeAnimation() {
        Animation animation = AnimationUtils.loadAnimation(
                WelcomeActivity.this, R.anim.welcome_ctrlbar_slideup);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mLinearAvatars.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mLinearAvatars.setVisibility(View.VISIBLE);
                    }
                }, 800);
            }
        });
        mLinearCtrlbar.startAnimation(animation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.welcome_btn_register:
                startActivity(RegisterActivity.class);
                break;

            case R.id.welcome_btn_login:
                startActivity(LoginActivity.class);
                break;

            case R.id.welcome_ibtn_about:
                startActivity(AboutTabsActivity.class);
                break;
        }
    }
}
