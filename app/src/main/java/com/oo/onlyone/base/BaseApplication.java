package com.oo.onlyone.base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.oo.onlyone.R;
import com.oo.onlyone.entity.NearByGroup;
import com.oo.onlyone.entity.NearByPeople;

public class BaseApplication extends Application {
    private Bitmap mDefaultAvatar;
    private static final String AVATAR_DIR = "avatar/";
    private static final String PHOTO_ORIGINAL_DIR = "photo/original/";
    private static final String PHOTO_THUMBNAIL_DIR = "photo/thumbnail/";
    private static final String STATUS_PHOTO_DIR = "statusphoto/";
    public Map<String, SoftReference<Bitmap>> mAvatarCache = new HashMap<>();
    public Map<String, SoftReference<Bitmap>> mPhotoOriginalCache = new HashMap<>();
    public Map<String, SoftReference<Bitmap>> mPhotoThumbnailCache = new HashMap<>();
    public Map<String, SoftReference<Bitmap>> mStatusPhotoCache = new HashMap<>();

    public List<NearByPeople> mNearByPeoples = new ArrayList<>();
    public List<NearByGroup> mNearByGroups = new ArrayList<>();

    public static List<String> mEmoticons = new ArrayList<>();
    public static Map<String, Integer> mEmoticonsId = new HashMap<>();
    public static List<String> mEmoticons_Zem = new ArrayList<>();
    public static List<String> mEmoticons_Zemoji = new ArrayList<>();

    public LocationClient mLocationClient;
    public double mLongitude;
    public double mLatitude;
    private String[] mAvatars = new String[]{"welcome_0", "welcome_1",
            "welcome_2", "welcome_3", "welcome_4", "welcome_5"};

    @Override
    public void onCreate() {
        super.onCreate();
        mDefaultAvatar = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_common_def_header);
        for (int i = 1; i < 64; i++) {
            String emoticonsName = "[zem" + i + "]";
            int emoticonsId = getResources().getIdentifier("zem" + i,
                    "drawable", getPackageName());
            mEmoticons.add(emoticonsName);
            mEmoticons_Zem.add(emoticonsName);
            mEmoticonsId.put(emoticonsName, emoticonsId);
        }
        for (int i = 1; i < 59; i++) {
            String emoticonsName = "[zemoji" + i + "]";
            int emoticonsId = getResources().getIdentifier("zemoji_e" + i,
                    "drawable", getPackageName());
            mEmoticons.add(emoticonsName);
            mEmoticons_Zemoji.add(emoticonsName);
            mEmoticonsId.put(emoticonsName, emoticonsId);
        }

        // 获取当前用户位置
        mLocationClient = new LocationClient(getApplicationContext());
//		mLocationClient.set("evrzsBIGFLtPXse2OxMq3oTM");
        mLocationClient.registerLocationListener(new BDLocationListener() {

            public void onReceivePoi(BDLocation arg0) {

            }

            @Override
            public void onReceiveLocation(BDLocation arg0) {
                mLongitude = arg0.getLongitude();
                mLatitude = arg0.getLatitude();
                Log.i("地理位置", "经度:" + mLongitude + ",纬度:" + mLatitude);
                mLocationClient.stop();
            }
        });
        mLocationClient.start();
//		mLocationClient.requestOfflineLocation();
        System.out.println("开始获取");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e("BaseApplication", "onLowMemory");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e("BaseApplication", "onTerminate");
    }


    public Bitmap getAvatar(String imageName) {
        if (mAvatarCache.containsKey(imageName)) {
            Reference<Bitmap> reference = mAvatarCache.get(imageName);
            if (reference.get() == null || reference.get().isRecycled()) {
                mAvatarCache.remove(imageName);
            } else {
                return reference.get();
            }
        }
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = getAssets().open(AVATAR_DIR + imageName);
            bitmap = BitmapFactory.decodeStream(is);
            if (bitmap == null) {
                throw new FileNotFoundException(imageName + "is not find");
            }
            mAvatarCache.put(imageName, new SoftReference<>(bitmap));
            return bitmap;
        } catch (Exception e) {
            return mDefaultAvatar;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {

            }
        }
    }



    public Bitmap getPhotoOriginal(String imageName) {
        if (mPhotoOriginalCache.containsKey(imageName)) {
            Reference<Bitmap> reference = mPhotoOriginalCache.get(imageName);
            if (reference.get() == null || reference.get().isRecycled()) {
                mPhotoOriginalCache.remove(imageName);
            } else {
                return reference.get();
            }
        }
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = getAssets().open(PHOTO_ORIGINAL_DIR + imageName);
            bitmap = BitmapFactory.decodeStream(is);
            if (bitmap == null) {
                throw new FileNotFoundException(imageName + "is not find");
            }
            mPhotoOriginalCache.put(imageName,
                    new SoftReference<Bitmap>(bitmap));
            return bitmap;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {

            }
        }
    }

    public Bitmap getPhotoThumbnail(String imageName) {
        if (mPhotoThumbnailCache.containsKey(imageName)) {
            Reference<Bitmap> reference = mPhotoThumbnailCache.get(imageName);
            if (reference.get() == null || reference.get().isRecycled()) {
                mPhotoThumbnailCache.remove(imageName);
            } else {
                return reference.get();
            }
        }
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = getAssets().open(PHOTO_THUMBNAIL_DIR + imageName);
            bitmap = BitmapFactory.decodeStream(is);
            if (bitmap == null) {
                throw new FileNotFoundException(imageName + "is not find");
            }
            mPhotoThumbnailCache.put(imageName, new SoftReference<Bitmap>(
                    bitmap));
            return bitmap;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {

            }
        }
    }

    public Bitmap getStatusPhoto(String imageName) {
        if (mStatusPhotoCache.containsKey(imageName)) {
            Reference<Bitmap> reference = mStatusPhotoCache.get(imageName);
            if (reference.get() == null || reference.get().isRecycled()) {
                mStatusPhotoCache.remove(imageName);
            } else {
                return reference.get();
            }
        }
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = getAssets().open(STATUS_PHOTO_DIR + imageName);
            bitmap = BitmapFactory.decodeStream(is);
            if (bitmap == null) {
                throw new FileNotFoundException(imageName + "is not find");
            }
            mStatusPhotoCache.put(imageName, new SoftReference<Bitmap>(bitmap));
            return bitmap;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {

            }
        }
    }
}
