package me.khrystal.weyuereader.widget.theme;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/26
 * update time:
 * email: 723526676@qq.com
 */

public class ColorUiUtil {

    public static void changeTheme(View rootView, Resources.Theme theme) {
        if (rootView instanceof ColorUiInterface) {
            ((ColorUiInterface) rootView).setTheme(theme);
            int count = ((ViewGroup) rootView).getChildCount();
            for (int i = 0; i < count; i++) {
                changeTheme(((ViewGroup) rootView).getChildAt(i), theme);
            }
        }
        if (rootView instanceof AbsListView) {
            try {
                Field localField = AbsListView.class.getDeclaredField("mRecycler");
                localField.setAccessible(true);
                Method localMethod = Class.forName("android.widget.AbsListView$RecycleBin").getDeclaredMethod("clear");
                localMethod.setAccessible(true);
                localMethod.invoke(localField.get(rootView));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            if (rootView instanceof ViewGroup) {
                int count = ((ViewGroup) rootView).getChildCount();
                for (int i = 0; i < count; i++) {
                    changeTheme(((ViewGroup) rootView).getChildAt(i), theme);
                }
            }
            if (rootView instanceof AbsListView) {
                try {
                    Field localField = AbsListView.class.getDeclaredField("mRecycler");
                    localField.setAccessible(true);
                    Method localMethod = Class.forName("android.widget.AbsListView$RecycleBin").getDeclaredMethod("clear");
                    localMethod.setAccessible(true);
                    localMethod.invoke(localField.get(rootView));
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
