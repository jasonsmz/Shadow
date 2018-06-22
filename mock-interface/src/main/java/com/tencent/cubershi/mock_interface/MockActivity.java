package com.tencent.cubershi.mock_interface;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public abstract class MockActivity extends PluginActivity {

    private MixPackageManager mMixPackageManager;

    private int mFragmentManagerHash;

    private PluginFragmentManager mPluginFragmentManager;

    public void setContentView(int layoutResID) {
        final View inflate = LayoutInflater.from(this).inflate(layoutResID, null);
        mHostActivityDelegator.setContentView(inflate);
    }

    public Intent getIntent() {
        return mHostActivityDelegator.getIntent();
    }

    public void setContentView(View view) {
        mHostActivityDelegator.setContentView(view);
    }

    public final MockApplication getApplication() {
        return mPluginApplication;
    }

    public PluginFragmentManager getFragmentManager() {
        FragmentManager fragmentManager = mHostActivityDelegator.getFragmentManager();
        int hash = System.identityHashCode(fragmentManager);
        if (hash != mFragmentManagerHash) {
            mFragmentManagerHash = hash;
            mPluginFragmentManager = new PluginFragmentManager(fragmentManager);
        }
        return mPluginFragmentManager;
    }

    public Object getLastNonConfigurationInstance() {
        return mHostActivityDelegator.getLastNonConfigurationInstance();
    }

    public Window getWindow() {
        return mHostActivityDelegator.getWindow();
    }

    public <T extends View> T findViewById(int id) {
        return mHostActivityDelegator.findViewById(id);
    }

    public WindowManager getWindowManager() {
        return mHostActivityDelegator.getWindowManager();
    }

    @Override
    public void setPluginPackageManager(PackageManager pluginPackageManager) {
        super.setPluginPackageManager(pluginPackageManager);
        mMixPackageManager = new MixPackageManager(super.getPackageManager(), pluginPackageManager);
    }

    @Override
    public PackageManager getPackageManager() {
        return mMixPackageManager;
    }

    public final MockActivity getParent() {
        return null;
    }

    public void overridePendingTransition(int enterAnim, int exitAnim) {
        if (enterAnim != 0 || exitAnim != 0) {
            Toast.makeText(this, "暂不支持插件资源overridePendingTransition", Toast.LENGTH_SHORT).show();
        } else {
            mHostActivityDelegator.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public void setTitle(CharSequence title) {
        mHostActivityDelegator.setTitle(title);
    }

    public boolean isFinishing() {
        return mHostActivityDelegator.isFinishing();
    }

    public boolean isDestroyed() {
        return mHostActivityDelegator.isDestroyed();
    }

    public final boolean requestWindowFeature(int featureId) {
        return mHostActivityDelegator.requestWindowFeature(featureId);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        final Intent pluginIntent = new Intent(intent);
        pluginIntent.setExtrasClassLoader(mPluginClassLoader);
        final boolean success = mPluginActivityLauncher.startActivityForResult(mHostActivityDelegator, pluginIntent, requestCode);
        if (!success) {
            mHostActivityDelegator.startActivityForResult(intent, requestCode);
        }
    }

    public final void setResult(int resultCode) {
        mHostActivityDelegator.setResult(resultCode);
    }

    public SharedPreferences getPreferences(int mode) {
        return super.getSharedPreferences(getLocalClassName(),mode);
    }

    public String getLocalClassName() {
        return this.getClass().getName();
    }
}
