package pub.hybrid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.KeyEvent;

import com.pub.internal.hybrid.HybridManager;

import pub.hybrid.ui.HybridView;

public class HybridChromeClient {

    private HybridManager mManager;

    /**
     * @hide
     */
    public void setHybridManager(HybridManager manager) {
        mManager = manager;
    }

    public boolean onJsAlert(HybridView view, String url, String message, final JsResult result) {
        new AlertDialog.Builder(mManager.getActivity()).setMessage(message)
                .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        result.cancel();
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            result.confirm();
                            return false;
                        }
                        return true;
                    }
                }).show();
        return true;
    }

    public boolean onJsConfirm(HybridView view, String url, String message, final JsResult result) {
        new AlertDialog.Builder(mManager.getActivity()).setMessage(message)
                .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        result.cancel();
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            result.confirm();
                            return false;
                        }
                        return true;
                    }
                }).show();
        return true;
    }

    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
    }

    public void onProgressChanged(HybridView view, int progress) {
        view.setProgress(progress);
    }

    public void onReceivedTitle(HybridView view, String title) {
    }

    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        uploadFile.onReceiveValue(null);
    }
}
