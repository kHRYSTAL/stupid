package me.khrystal.rmcookies;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.util.HashSet;

public class WebViewUtils {

    private static final String TAG = WebViewUtils.class.getSimpleName();

    public static void deleteCookiesForDomain(Context context, String domain) {
        CookieManager cookieManager = CookieManager.getInstance();
        if (cookieManager == null) return;

        /* http://code.google.com/p/android/issues/detail?id=19294 */
        if (Build.VERSION.SDK_INT < 11) {
            /* Trim leading '.'s */
            if (domain.startsWith(".")) domain = domain.substring(1);
        }

        String cookieGlob = cookieManager.getCookie(domain);
        if (cookieGlob != null) {
            String[] cookies = cookieGlob.split(";");
            for (String cookieTuple : cookies) {
                String[] cookieParts = cookieTuple.split("=");
                HashSet<String> domainSet = getDomainSet(domain);
                for (String dm : domainSet) {
                    Log.e(TAG, dm);
                    /* Set an expire time so that this field will be removed after calling sync() */
                    cookieManager.setCookie(dm, cookieParts[0] + "=; Expires=Wed, 31 Dec 2015 23:59:59 GMT");
                }
            }
            cookieManager.removeSessionCookie();
            cookieManager.removeExpiredCookie();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.flush();
            } else {
                CookieSyncManager.createInstance(context.getApplicationContext());
                CookieSyncManager.getInstance().sync();
            }
        }
    }

    private static HashSet<String> getDomainSet(String domain) {
        HashSet<String> domainSet = new HashSet<>();
        String host = Uri.parse(domain).getHost();

        domainSet.add(host);
        domainSet.add("." + host);
        // exclude domain like "baidu.com"
        if (host.indexOf(".") != host.lastIndexOf(".")) {
            domainSet.add(host.substring(host.indexOf('.')));
        }

        return domainSet;
    }
}