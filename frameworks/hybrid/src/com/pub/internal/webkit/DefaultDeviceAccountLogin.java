package com.pub.internal.webkit;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.pub.internal.hybrid.provider.AbsWebView;

import pub.hybrid.R;

public class DefaultDeviceAccountLogin extends DeviceAccountLogin {

    private static final String DIALOG_FRAGMENT_TAG = "dialog";

    private static final int MSG_SHOW_DIALOG = 0;
    private static final int MSG_LOGIN_FINISH = 1;
    private static final int SHOW_DIALOG_DELAY = 500;

    private AbsWebView mWebView;

    private Handler mHandler;
    private ProgressDialogFragment mDialogFragment;

    public DefaultDeviceAccountLogin(Activity activity, AbsWebView webView) {
        super(activity);

        mWebView = webView;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_SHOW_DIALOG) {
                    showLoginProgress();
                } else if (msg.what == MSG_LOGIN_FINISH ) {
                    dismissDialog();
                    mWebView.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    @Override
    public void onLoginStart() {
        mHandler.sendEmptyMessageDelayed(MSG_SHOW_DIALOG, SHOW_DIALOG_DELAY);
    }

    @Override
    public void onLoginSuccess(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void onLoginFail() {
        dismissDialog();
        mWebView.setVisibility(View.VISIBLE);
        Toast.makeText(mActivity, R.string.web_sso_login_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginCancel() {
        dismissDialog();
        mWebView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoginPageFinished() {
        mHandler.sendEmptyMessageDelayed(MSG_LOGIN_FINISH, SHOW_DIALOG_DELAY);
    }

    public static class ProgressDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            ProgressDialog mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage(getString(R.string.web_sso_login_message));
            mDialog.setIndeterminate(true);
            mDialog.setCancelable(false);
            return mDialog;
        }

    }

    private void showLoginProgress() {
        dismissDialog();
        mDialogFragment = new ProgressDialogFragment();
        try {
            mDialogFragment.show(mActivity.getFragmentManager(), DIALOG_FRAGMENT_TAG);
        }  catch (IllegalStateException e) {
            // ignore
        }
    }

    private void dismissDialog() {
        mHandler.removeMessages(MSG_SHOW_DIALOG);
        if (mDialogFragment != null && mDialogFragment.isAdded()) {
            mDialogFragment.dismissAllowingStateLoss();
        }
        mDialogFragment = null;
    }

}
