package pub.hybrid.ui;

import com.pub.internal.hybrid.provider.AbsWebChromeClient;
import com.pub.internal.hybrid.provider.AbsWebView;
import com.pub.internal.hybrid.HybridManager;
import com.pub.internal.hybrid.WebContainerView;
import com.pub.internal.hybrid.provider.AbsWebViewClient;
import com.pub.internal.hybrid.provider.WebViewFactory;
import com.pub.internal.hybrid.provider.WebViewFactoryProvider;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import pub.hybrid.HybridBackForwardList;
import pub.hybrid.HybridChromeClient;
import pub.hybrid.HybridSettings;
import pub.hybrid.HybridViewClient;
import pub.hybrid.R;

public class HybridView extends FrameLayout {

    private AbsWebView mWebView;
    private ProgressBar mProgressView;
    private HybridProgressView mHorizontalProgressView;
    private ViewGroup mReloadView;
    private Button mBtnRetry;
    private TextView mTextProvider;
    private WebContainerView mWebContainer;

    private WebViewFactoryProvider mFactory;
    private HybridSettings mWebSettings;
    private HybridManager mManager;

    private int mProgressBarStyle;
    private boolean mShowErrorPage;
    private boolean mPullable;

    private boolean mLoadingError;

    private static final int PROGRESS_BAR_NONE = 0;
    private static final int PROGRESS_BAR_CIRCULAR = 1;
    private static final int PROGRESS_BAR_HORIZONTAL = 2;

    /**
     * Hidden constructor to prevent clients from creating a new HybridView
     * instance or deriving the class.
     *
     * @hide
     */
    public HybridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.HybridViewStyle, 0, 0);

        mProgressBarStyle = a.getInt(R.styleable.HybridViewStyle_hybridProgressBar,
                PROGRESS_BAR_NONE);
        mShowErrorPage = a.getBoolean(R.styleable.HybridViewStyle_hybridErrorPage, true);
        mPullable = a.getBoolean(R.styleable.HybridViewStyle_hybridPullable, true);

        a.recycle();

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hybrid_view_layout, this, true);

        mFactory = WebViewFactory.getProvider(context);
        mWebView = mFactory.createWebView(context, this);

        mWebContainer = (WebContainerView) findViewById(R.id.webContainer);
        mWebContainer.setWebView(mWebView.getBaseWebView());

        if (mProgressBarStyle == PROGRESS_BAR_CIRCULAR) {
            mProgressView = (ProgressBar) findViewById(R.id.progress_circular);
        } else if (mProgressBarStyle == PROGRESS_BAR_HORIZONTAL) {
            mHorizontalProgressView = (HybridProgressView) findViewById(R.id.progress_horizontal);
        }

        mTextProvider = (TextView) findViewById(R.id.hybrid_provider);
        if (mPullable) {
            mTextProvider.setVisibility(VISIBLE);
        }
    }

    public HybridManager getHybridManager() {
        return mManager;
    }

    public void setHybridManager(HybridManager manager) {
        mManager = manager;
    }

    public AbsWebView getWebView() {
        return mWebView;
    }

    public void setHybridViewClient(HybridViewClient client) {
        client.setHybridManager(mManager);
        AbsWebViewClient webViewClient = mFactory.createWebViewClient(client, this);
        mWebView.setWebViewClient(webViewClient);
    }

    public void setHybridChromeClient(HybridChromeClient client) {
        client.setHybridManager(mManager);
        AbsWebChromeClient webChromeClient = mFactory.createWebChromeClient(client, this);
        mWebView.setWebChromeClient(webChromeClient);
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public void addJavascriptInterface(Object object, String name) {
        mWebView.addJavascriptInterface(object, name);
    }

    public HybridSettings getSettings() {
        if (mWebSettings == null) {
            mWebSettings = mWebView.getSettings();
        }
        return mWebSettings;
    }

    public void destroy() {
        mWebView.destroy();
    }

    public void reload() {
        mWebView.reload();
    }

    public void clearCache(boolean includeDiskFiles) {
        mWebView.clearCache(includeDiskFiles);
    }

    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    public void goBack() {
        mWebView.goBack();
    }

    public String getUrl() {
        return mWebView.getUrl();
    }

    public String getTitle() {
        return mWebView.getTitle();
    }

    public void setProgress(int progress) {
        if (progress > 80 && !mLoadingError) {
            hideReloadView();
        }

        if (progress == 100) {
            mWebContainer.setBackground(null);
        }

        if (mProgressView == null && mHorizontalProgressView == null) {
            return;
        }

        if (mProgressBarStyle == PROGRESS_BAR_CIRCULAR) {
            if (mProgressView == null) {
                return;
            }

            if (mProgressView.getVisibility() == GONE) {
                mProgressView.setVisibility(VISIBLE);
            }

            mProgressView.setProgress(progress);
            if (progress == mProgressView.getMax()) {
                mProgressView.setVisibility(GONE);
            }
        } else if (mProgressBarStyle == PROGRESS_BAR_HORIZONTAL) {
            if (mHorizontalProgressView == null) {
                return;
            }

            if (mHorizontalProgressView.getVisibility() == GONE) {
                mHorizontalProgressView.setVisibility(VISIBLE);
            }

            mHorizontalProgressView.setProgress(progress);
            if (progress == mHorizontalProgressView.getMax()) {
                mHorizontalProgressView.setVisibility(GONE);
            }
        }
    }

    public void setLoadingError(boolean loadingError) {
        mLoadingError = loadingError;
    }

    public void showReloadView() {
        if (!mShowErrorPage) {
            return;
        }

        if (mReloadView == null) {
            ViewStub stub = (ViewStub) findViewById(R.id.webview_reload_stub);
            mReloadView = (ViewGroup) stub.inflate();
            mBtnRetry = (Button) mReloadView.findViewById(R.id.reload);
            mBtnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reload();
                    setReloadContentVisibility(View.GONE);
                }
            });
        }
        mReloadView.setVisibility(View.VISIBLE);
        setReloadContentVisibility(View.VISIBLE);
        mWebView.setVisibility(GONE);
    }

    public void hideReloadView() {
        if (!mShowErrorPage) {
            return;
        }

        if (mReloadView != null) {
            mReloadView.setVisibility(View.GONE);
        }
        mWebView.setVisibility(VISIBLE);
    }

    private void setReloadContentVisibility(int visibility) {
        int childCount = mReloadView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            mReloadView.getChildAt(i).setVisibility(visibility);
        }
    }

    public void setWebProvider(String url) {
        Uri uri = Uri.parse(url);
        String host = uri.getHost();

        if (TextUtils.isEmpty(host)) {
            mTextProvider.setText("");
        } else {
            mTextProvider.setText(String.format(getContext().getString(R.string
                    .hybrid_provider), host));
        }
    }

    public int getContentHeight() {
        return mWebView.getContentHeight();
    }

    public float getScale() {
        return mWebView.getScale();
    }

    public HybridBackForwardList copyBackForwardList() {
        return mWebView.copyBackForwardList();
    }
}
