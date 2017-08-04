package me.khrystal.util.mediamonitor;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/8/4
 * update time:
 * email: 723526676@qq.com
 */

public class MediaMonitor {
    private static final long INTERVAL_FAULT_TOLERANT = 10;

    private Context mContext;
    private List<Directory> mRuleList;
    private
    @MediaType
    int mMediaType = MediaType.ALL;

    private MediaMonitor(Context context) {
        this(context, MediaType.ALL);
    }

    private MediaMonitor(Context context, @MediaType int mediaType) {
        this(context, mediaType, null);
    }

    private MediaMonitor(Context context, @MediaType int mediaType, List<Directory> rules) {
        this.mContext = context;
        this.mMediaType = mediaType;
        if (rules != null) {
            if (mRuleList == null) {
                mRuleList = new ArrayList<>();
            }
            mRuleList.addAll(rules);
        }
    }

    public static Observable<String> listen(Context context) {
        return new MediaMonitor(context).listen();
    }

    public static Observable<String> listen(Context context, @MediaType int mediaType) {
        return new MediaMonitor(context, mediaType).listen();
    }

    public static Observable<String> listen(Context context, @MediaType int mediaType, List<Directory> rules) {
        return new MediaMonitor(context, mediaType, rules).listen();
    }

    private Observable<String> listen() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                callOnSubscribe(subscriber);
            }
        }).subscribeOn(Schedulers.io());
    }

    private void callOnSubscribe(final Subscriber<? super String> subscriber) {
        final ContentResolver contentResolver = this.mContext.getContentResolver();
        if (contentResolver == null) {
            subscriber.onError(new Throwable("Content resolver is null"));
            return;
        }

        ContentObserver contentObserver = new ContentObserver(null) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                if (matchMediaType(uri)) {
                    Cursor cursor = null;
                    try {
                        cursor = contentResolver.query(uri, new String[]{
                                MediaStore.Images.Media.DISPLAY_NAME,
                                MediaStore.Images.Media.DATA,
                                MediaStore.Images.Media.DATE_ADDED
                        }, null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");

                        if (cursor != null && cursor.moveToFirst()) {
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            long imgInsertedTimeMillis = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                            long currentTimeMillis = System.currentTimeMillis() / 1000;
                            if (matchDirectory(path) && matchInsertedTime(currentTimeMillis, imgInsertedTimeMillis)) {
                                subscriber.onNext(path);
                            }
                        }
                    } finally {
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                    }

                }
            }
        };

        if (MediaType.ALL == mMediaType || MediaType.MEDIA_TYPE_IMAGE == mMediaType) {
            contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    , true, contentObserver);
        }

        if (MediaType.ALL == mMediaType || MediaType.MEDIA_TYPE_AUDIO == mMediaType) {
            contentResolver.registerContentObserver(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    , true, contentObserver);
        }

        if (MediaType.ALL == mMediaType || MediaType.MEDIA_TYPE_VIDEO == mMediaType) {
            contentResolver.registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    , true, contentObserver);
        }
    }

    private boolean matchMediaType(Uri uri) {
        switch (mMediaType) {
            case MediaType.ALL:
                return uri.toString().startsWith(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString())
                        || uri.toString().startsWith(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString())
                        || uri.toString().startsWith(MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toString());
            case MediaType.MEDIA_TYPE_IMAGE:
                return uri.toString().startsWith(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
            case MediaType.MEDIA_TYPE_AUDIO:
                return uri.toString().startsWith(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString());
            case MediaType.MEDIA_TYPE_VIDEO:
                return uri.toString().startsWith(MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toString());
            default:
                return false;
        }
    }

    private boolean matchDirectory(@NonNull String path) {
        if (mRuleList != null) {
            for (Directory directory : mRuleList) {
                if (Rule.LIKE == directory.getRule()) {
                    if (path.toLowerCase().contains(directory.getDirectory())) {
                        return true;
                    }
                } else {
                    if (path.equals(directory.getDirectory())) {
                        return true;
                    }
                }
            }
        } else {
            return true;
        }
        return false;
    }

    private boolean matchInsertedTime(long currentTimeMillis, long imgInsertedTimeMillis) {
        return Math.abs(currentTimeMillis - imgInsertedTimeMillis) <= INTERVAL_FAULT_TOLERANT;
    }


}
