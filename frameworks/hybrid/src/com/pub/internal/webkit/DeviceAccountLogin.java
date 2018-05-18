package com.pub.internal.webkit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.lang.ref.WeakReference;

public class DeviceAccountLogin {

    private static final String TAG = "DeviceAccountLogin";

    protected Activity mActivity;
    protected AccountManager mAccountManager;
    private AccountManagerCallback<Bundle> mCallback;

    public DeviceAccountLogin(Activity activity) {
        mActivity = activity;
        // 不直接使用mActivity，避免mActivity被mAccountManager引用而不能及时释放
        mAccountManager = AccountManager.get(mActivity.getApplicationContext());
        mCallback = new LoginCallback(this);
    }

    public void login(String realm, String accountName, String args) {
        Account[] accounts = mAccountManager.getAccountsByType(realm);
        // No need to display UI if there are no accounts.
        if (accounts.length == 0) {
            onLoginCancel();
            return;
        }

        // Verify the account before using it.
        Account account = null;
        if (TextUtils.isEmpty(accountName)) {
            account = accounts[0];
        } else {
            for (Account a : accounts) {
                if (a.name.equals(accountName)) {
                    account = a;
                    break;
                }
            }
        }

        if (account != null) {
            onLoginStart();
            // Handle the automatic login case where the service gave us an
            // account to use.
            String authToken = "weblogin:" + args;
            // activity参数故意传一个null，避免mActivity被mAccountManager引用而不能及时释放
            // NOTE: 文档说activity参数不能为null，但是查看代码发现null仅会导致不启动登录帐号的activity，没有其他负面影响
            mAccountManager.getAuthToken(account, authToken, null, null, mCallback, null);
        } else {
            onLoginCancel();
        }
    }

    public void onLoginStart() {
    }

    public void onLoginCancel() {
    }

    public void onLoginSuccess(String url) {
    }

    public void onLoginFail() {
    }

    public void onLoginPageFinished() {
    }

    private static class LoginCallback implements AccountManagerCallback<Bundle> {
        private WeakReference<DeviceAccountLogin> mLoginRef;

        public LoginCallback(DeviceAccountLogin login) {
            mLoginRef = new WeakReference<DeviceAccountLogin>(login);
        }

        @Override
        public void run(AccountManagerFuture<Bundle> value) {
            DeviceAccountLogin login = mLoginRef.get();
            if (login == null) {
                return;
            }

            try {
                String result = value.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                if (result == null) {
                    login.onLoginFail();
                } else {
                    login.onLoginSuccess(result);
                }
            } catch (Exception e) {
                Log.e(TAG, "Fail to login", e);
                login.onLoginFail();
            }
        }
    }
}
