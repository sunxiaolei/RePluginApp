package xiaolei.repluginmain.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.qihoo360.replugin.RePlugin;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Flowable;
import sunxl8.myutils.SPUtils;
import xiaolei.repluginmain.Constant;
import xiaolei.repluginmain.R;
import xiaolei.repluginmain.base.BaseActivity;
import xiaolei.repluginmain.db.DatabaseHelper;
import xiaolei.repluginmain.db.model.PluginModel;
import xiaolei.repluginmain.utils.SchedulersCompat;
import xiaolei.repluginmain.utils.SnackBarUtils;

import static android.os.Environment.getExternalStorageDirectory;
import static xiaolei.repluginmain.Constant.PLUGIN_PATH;

/**
 * Created by sunxl8 on 2017/8/2.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawer_main)
    FlowingDrawer mDrawer;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.rv_main)
    RecyclerView rvMain;
    @BindView(R.id.switch_mode)
    SwitchCompat switchMode;

    private MainAdapter mAdapter;

    private MaterialDialog mPermissionDialog;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        //添加插件
        RxView.clicks(ivAdd)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(o -> {

                });
        rvMain.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new MainAdapter(this);
        rvMain.setAdapter(mAdapter);

        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(rvMain);

        mAdapter.enableDragItem(itemTouchHelper, R.id.layout_item_main, true);
        mAdapter.setOnItemDragListener(new OnItemDragListener() {

            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                DatabaseHelper.updatePluginPos(mAdapter.getData());
            }
        });

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            new RxPermissions(this)
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            PluginModel item = (PluginModel) adapter.getItem(position);
                            if (RePlugin.isPluginInstalled(item.getPkgName())) {
                                startPlugin(item);
                            } else {
                                loadAndStart(item);
                            }
                        } else {
                            mPermissionDialog.show();
                        }
                    });
        });

        switchMode.setChecked(SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.SP_KEY_MODE, false));
        switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            SPUtils.getInstance(Constant.SP_NAME).put(Constant.SP_KEY_MODE, isChecked);
            recreate();
        });

        mPermissionDialog = new MaterialDialog.Builder(this)
                .content("跳转到设置去授予必需权限")
                .positiveText(R.string.sure)
                .negativeText(R.string.cancel)
                .onPositive((dialog, which) -> {
                    Intent localIntent = new Intent();
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivity(localIntent);
                })
                .build();

        getPluginList();

        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        loadPlugins(mAdapter.getData());
                    } else {
                        mPermissionDialog.show();
                    }
                });

    }

    private void loadAndStart(PluginModel item) {
        loadPluginsAndStart(mAdapter.getData(), item);
    }

    private void startPlugin(PluginModel item) {
        RePlugin.startActivity(MainActivity.this, RePlugin.createIntent(item.getPkgName(), item.getPkgName() + ".ui.MainActivity"));
    }

    /**
     * 获取插件列表
     */
    private void getPluginList() {
        List<PluginModel> test = DatabaseHelper.queryPluginList();
        if (test.size() == 0) {
            PluginModel pToolbox = new PluginModel();
            pToolbox.setName("工具箱");
            pToolbox.setOrder(1);
            pToolbox.setPkgName("xiaolei.plugintoolbox");
            pToolbox.setIcRes(R.mipmap.ic_launcher);
            test.add(pToolbox);
            DataSupport.saveAll(test);
        }
        mAdapter.setNewData(test);
    }

    private void loadPlugins(List<PluginModel> plugins) {
        for (int i = 0; i < plugins.size(); i++) {
            File apk = new File(getExternalStorageDirectory(), PLUGIN_PATH + plugins.get(i).getPkgName() + ".apk");
            RePlugin.install(apk.getAbsolutePath());
        }
    }

    private void loadPluginsAndStart(List<PluginModel> plugins, PluginModel item) {
        for (int i = 0; i < plugins.size(); i++) {
            File apk = new File(getExternalStorageDirectory(), PLUGIN_PATH + plugins.get(i).getPkgName() + ".apk");
            RePlugin.install(apk.getAbsolutePath());
        }
        startPlugin(item);
    }

    private boolean exitApp = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (exitApp) {
                finish();
            } else {
                SnackBarUtils.showShort(getWindow().getDecorView(), "再按一次退出应用程序", "退出", v -> finish());
                exitApp = true;
                Flowable.timer(2000, TimeUnit.MILLISECONDS)
                        .compose(SchedulersCompat.applyIoSchedulers())
                        .subscribe(aLong -> exitApp = false);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
