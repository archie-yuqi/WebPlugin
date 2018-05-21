package pub.hybrid.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.pub.internal.hybrid.HybridManager;

import java.util.HashSet;
import java.util.Set;

import pub.hybrid.R;

/**
 * Base activity for application to use hybrid feature.
 */
public class HybridActivity extends Activity {

    /**
     * The name of the extra used for start url that passed to
     * {@link HybridActivity} or {@link HybridFragment}.
     */
    public static final String EXTRA_URL = "com.hybrid.extra.URL";

    private Set<HybridView> mHybridViews = new HashSet<HybridView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        View hybridView = findViewById(R.id.hybrid_view);
        if (hybridView != null && hybridView instanceof HybridView) {
            String url = null;
            if (savedInstanceState != null) {
                url = savedInstanceState.getString(EXTRA_URL);
            }
            if (url == null) {
                Intent intent = getIntent();
                if (intent != null) {
                    url = intent.getStringExtra(EXTRA_URL);
                }
            }
            registerHybridView((HybridView) hybridView, getConfigResId(), url);
        }
    }

    /**
     * Get content view for current activity. It can be overridden to provide
     * custom view. If override this method, the hybrid view should be added
     * manually.
     * 
     * @see #registerHybridView(View)
     * @return content view.
     */
    protected View getContentView() {
        return getLayoutInflater().inflate(R.layout.hybrid_main, null);
    }

    /**
     * Get resource id of config file. By default, it will use the value of meta
     * data <code>com.miui.sdk.hybrid.config</code> defined in
     * AndroidManifest.xml, or res/xml/miui_hybrid_config.xml if no such value
     * found. It can be overridden to provide another config file for current
     * activity.
     * 
     * @return resource id of config file.
     */
    protected int getConfigResId() {
        return 0;
    }

    /**
     * Register a hybrid view to be managed by current activity. Note the hybrid
     * view will not be added to UI in this method.
     * <p>
     * A hybrid view can be retrieved from the layout file
     * {@link R.layout.hybrid_view}.
     * </p>
     * 
     * @param view the hybrid view to be managed.
     * @throws IllegalArgumentException if view is not a hybrid view.
     */
    protected final void registerHybridView(View view) {
        registerHybridView(view, getConfigResId());
    }

    /**
     * Register a hybrid view to be managed by current activity. Note the hybrid
     * view will not be added to UI in this method.
     * <p>
     * A hybrid view can be retrieved from the layout file
     * {@link R.layout.hybrid_view}.
     * </p>
     * 
     * @param view the hybrid view to be managed.
     * @param configResId resource id of config file for current hybrid view.
     * @throws IllegalArgumentException if view is not a hybrid view.
     */
    protected final void registerHybridView(View view, int configResId) {
        registerHybridView(view, configResId, null);
    }

    /**
     * Register a hybrid view to be managed by current activity. Note the hybrid
     * view will not be added to UI in this method.
     * <p>
     * A hybrid view can be retrieved from the layout file
     * {@link R.layout.hybrid_view}.
     * </p>
     * 
     * @param view the hybrid view to be managed.
     * @param configResId resource id of config file for current hybrid view.
     * @param url url to be loaded.
     * @throws IllegalArgumentException if view is not a hybrid view.
     */
    protected final void registerHybridView(View view, int configResId, String url) {
        if (!(view instanceof HybridView)) {
            throw new IllegalArgumentException("view being registered is not a hybrid view");
        }
        HybridView hybridView = (HybridView) view;
        HybridManager manager = new HybridManager(this, hybridView);
        hybridView.setHybridManager(manager);
        mHybridViews.add(hybridView);
        manager.init(configResId, url);
    }

    /**
     * Unregister a hybrid view being managed by current activity. Note the
     * hybrid view will not be removed from UI in this method.
     * 
     * @param view the hybrid view being managed.
     * @throws IllegalArgumentException if view is not a hybrid view.
     */
    protected final void unregisterHybridView(View view) {
        if (!(view instanceof HybridView)) {
            throw new IllegalArgumentException("view being unregistered is not a hybrid view");
        }
        mHybridViews.remove(view);
    }

    private void destroyHybridView() {
        for (HybridView view : mHybridViews) {
            if (view != null) {
                if (view.getParent() != null) {
                    ((ViewGroup) view.getParent()).removeView(view);
                }
                view.destroy();
            }
        }
        mHybridViews.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (HybridView view : mHybridViews) {
            HybridManager manager = view.getHybridManager();
            manager.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (HybridView view : mHybridViews) {
            HybridManager manager = view.getHybridManager();
            manager.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (HybridView view : mHybridViews) {
            HybridManager manager = view.getHybridManager();
            manager.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (HybridView view : mHybridViews) {
            HybridManager manager = view.getHybridManager();
            manager.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (HybridView view : mHybridViews) {
            HybridManager manager = view.getHybridManager();
            manager.onDestroy();
        }
        destroyHybridView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (HybridView view : mHybridViews) {
            HybridManager manager = view.getHybridManager();
            manager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            for (HybridView view : mHybridViews) {
                if (view.canGoBack() && !view.getHybridManager().isDetached()) {
                    view.goBack();
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
