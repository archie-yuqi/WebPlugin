package pub.hybrid;

import android.graphics.Bitmap;
import android.net.http.SslError;

import com.pub.internal.hybrid.HybridManager;
import com.pub.internal.webkit.WebViewClientDelegate;

import java.io.IOException;
import java.util.UUID;

public class HybridViewClient {

    private static final String ASSET_PATH = "hybrid/";
    private static final String VIRTUAL_PATH = "android_asset/" + ASSET_PATH;

    private HybridManager mManager;
    private WebViewClientDelegate mWebViewClientDelegate;

    public HybridViewClient() {
        mWebViewClientDelegate = new WebViewClientDelegate();
    }

    /**
     * @hide
     */
    public void setHybridManager(HybridManager manager) {
        mManager = manager;
    }

    public void onPageStarted(pub.hybrid.HybridView view, String url, Bitmap favicon) {
        PageContext pageContext = new PageContext();
        pageContext.setId(UUID.randomUUID().toString());
        pageContext.setUrl(url);
        mManager.setPageContext(pageContext);
        mManager.onPageChange();

        view.setWebProvider(url);
        view.setLoadingError(false);

        mWebViewClientDelegate.onPageStarted(view.getWebView(), url, favicon);
    }

    public void onPageFinished(pub.hybrid.HybridView view, String url) {
        if (mManager.getActivity().getActionBar() != null) {
            mManager.getActivity().getActionBar().setTitle(view.getTitle());
        }

        mWebViewClientDelegate.onPageFinished(view.getWebView(), url);
    }

    public HybridResourceResponse shouldInterceptRequest(pub.hybrid.HybridView view, String url) {
        HybridResourceResponse response = null;
        if (url != null && url.startsWith("http")) {
            int index = url.indexOf(VIRTUAL_PATH);
            if (index >= 0 && index + VIRTUAL_PATH.length() < url.length()) {
                String assetPath = url.substring(index + VIRTUAL_PATH.length());
                try {
                    response = new HybridResourceResponse(null, null,
                            mManager.getActivity().getAssets().open(ASSET_PATH + assetPath));
                } catch (IOException e) {
                    response = null;
                }
            }
        }
        return response;
    }

    public boolean shouldOverrideUrlLoading(final pub.hybrid.HybridView view, String url) {
        return mWebViewClientDelegate.shouldOverrideUrlLoading(view.getWebView(), url);
    }

    public void onReceivedSslError(pub.hybrid.HybridView view, SslErrorHandler handler, SslError error) {
        handler.cancel();
    }

    public void onReceivedError(pub.hybrid.HybridView view, int errorCode, String description, String
            failingUrl) {
        view.setLoadingError(true);
        view.showReloadView();
    }

    public void onReceivedLoginRequest(pub.hybrid.HybridView view, String realm, String account, String args) {
        mWebViewClientDelegate.onReceivedLoginRequest(view.getWebView(), realm, account, args);
    }
}
