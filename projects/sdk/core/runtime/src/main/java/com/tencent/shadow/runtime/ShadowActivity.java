package com.tencent.shadow.runtime;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public abstract class ShadowActivity extends PluginActivity {

    private int mFragmentManagerHash;

    private PluginFragmentManager mPluginFragmentManager;

    public void setContentView(int layoutResID) {
        if ("merge".equals(XmlPullParserUtil.getLayoutStartTagName(getResources(), layoutResID))) {
            //如果传进来的xml文件的根tag是merge时，需要特殊处理
            View decorView = mHostActivityDelegator.getWindow().getDecorView();
            ViewGroup viewGroup = decorView.findViewById(android.R.id.content);
            LayoutInflater.from(this).inflate(layoutResID, viewGroup);
        } else {
            View inflate = LayoutInflater.from(this).inflate(layoutResID, null);
            mHostActivityDelegator.setContentView(inflate);
        }
    }

    public Intent getIntent() {
        return mHostActivityDelegator.getIntent();
    }

    public void setContentView(View view) {
        mHostActivityDelegator.setContentView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mHostActivityDelegator.setContentView(view, params);
    }

    public void addContentView(View view, ViewGroup.LayoutParams params) {
        mHostActivityDelegator.superAddContentView(view, params);
    }

    public final ShadowApplication getApplication() {
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


    public final ShadowActivity getParent() {
        return null;
    }

    public void overridePendingTransition(int enterAnim, int exitAnim) {
        //如果使用的资源不是系统资源，我们无法支持这个特性。
        if ((enterAnim & 0xFF000000) != 0x01000000) {
            enterAnim = 0;
        }
        if ((exitAnim & 0xFF000000) != 0x01000000) {
            exitAnim = 0;
        }
        mHostActivityDelegator.overridePendingTransition(enterAnim, exitAnim);
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
        final boolean success = mPluginComponentLauncher.startActivityForResult(mHostActivityDelegator, pluginIntent, requestCode);
        if (!success) {
            mHostActivityDelegator.startActivityForResult(intent, requestCode);
        }
    }

    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        final Intent pluginIntent = new Intent(intent);
        pluginIntent.setExtrasClassLoader(mPluginClassLoader);
        final boolean success = mPluginComponentLauncher.startActivityForResult(mHostActivityDelegator, pluginIntent, requestCode, options);
        if (!success) {
            mHostActivityDelegator.startActivityForResult(intent, requestCode, options);
        }
    }


    public final void setResult(int resultCode) {
        mHostActivityDelegator.setResult(resultCode);
    }

    public final void setResult(int resultCode, Intent data) {
        mHostActivityDelegator.setResult(resultCode, data);
    }

    public SharedPreferences getPreferences(int mode) {
        return super.getSharedPreferences(getLocalClassName(), mode);
    }

    public String getLocalClassName() {
        return this.getClass().getName();
    }

    public void recreate() {
        mHostActivityDelegator.recreate();
    }

    public void runOnUiThread(Runnable action) {
        mHostActivityDelegator.runOnUiThread(action);
    }

    public void setTitleColor(int textColor) {
        mHostActivityDelegator.setTitleColor(textColor);
    }

    public final int getTitleColor() {
        return mHostActivityDelegator.getTitleColor();
    }

    public void setTitle(int var1) {
        mHostActivityDelegator.setTitle(var1);
    }

    public CharSequence getTitle() {
        return mHostActivityDelegator.getTitle();
    }

    public void setRequestedOrientation(int requestedOrientation) {
        mHostActivityDelegator.setRequestedOrientation(requestedOrientation);
    }

    public int getRequestedOrientation() {
        return mHostActivityDelegator.getRequestedOrientation();
    }

    public MenuInflater getMenuInflater() {
        return mHostActivityDelegator.getMenuInflater();
    }


    public final void requestPermissions(String[] permissions, int requestCode) {
        mHostActivityDelegator.requestPermissions(permissions, requestCode);
    }

    public ActionBar getActionBar() {
        return mHostActivityDelegator.getActionBar();
    }

    public void setVisible(boolean visible) {
        mHostActivityDelegator.setVisible(visible);
    }

    public void setIntent(Intent newIntent) {
        mHostActivityDelegator.setIntent(newIntent);
    }

    public View getCurrentFocus() {
        return mHostActivityDelegator.getCurrentFocus();
    }

    @Deprecated
    public final Cursor managedQuery(Uri uri, String[] projection, String selection,
                                     String[] selectionArgs, String sortOrder) {
        return mHostActivityDelegator.managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }

    public ComponentName getComponentName() {
        return mHostActivityDelegator.getComponentName();
    }

    public boolean shouldShowRequestPermissionRationale(String permission) {
        return mHostActivityDelegator.shouldShowRequestPermissionRationale(permission);
    }
}