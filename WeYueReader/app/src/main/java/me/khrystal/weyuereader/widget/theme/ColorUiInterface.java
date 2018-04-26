package me.khrystal.weyuereader.widget.theme;

import android.content.res.Resources;
import android.view.View;

/**
 * usage: 换肤接口
 * author: kHRYSTAL
 * create time: 18/4/26
 * update time:
 * email: 723526676@qq.com
 */

public interface ColorUiInterface {
    View getView();
    void setTheme(Resources.Theme themeId);
}
