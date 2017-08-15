package xiaolei.repluginmain.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatDelegate;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginCallbacks;
import com.qihoo360.replugin.RePluginConfig;

import org.litepal.LitePal;

import sunxl8.myutils.SPUtils;
import sunxl8.myutils.Utils;
import xiaolei.repluginmain.Constant;

/**
 * Created by sunxl8 on 2017/8/7.
 */

public class BaseApplication extends Application  {

    public static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mContext = base;
        RePlugin.App.attachBaseContext(this);
        RePluginConfig config = new RePluginConfig();
        config.setCallbacks(new RePluginCallbacks(base){

        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RePlugin.App.onCreate();
        Utils.init(this);
        LitePal.initialize(this);
        if (SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.SP_KEY_MODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }
}
