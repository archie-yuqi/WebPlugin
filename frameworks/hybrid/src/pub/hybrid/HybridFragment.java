package pub.hybrid;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pub.internal.hybrid.HybridManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Base fragment for application to use hybrid feature.
 */
public class HybridFragment extends Fragment {

    private Set<pub.hybrid.HybridView> mHybridViews = new HashSet<pub.hybrid.HybridView>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getContentView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View hybridView = view.findViewById(R.id.hybrid_view);
        if (hybridView != null && hybridView instanceof pub.hybrid.HybridView) {
            String url = null;
            if (savedInstanceState != null) {
                url = savedInstanceState.getString(HybridActivity.EXTRA_URL);
            }
            if (url == null) {
                Intent intent = getActivity().getIntent();
                if (intent != null) {
                    url = intent.getStringExtra(HybridActivity.EXTRA_URL);
                }
            }
            registerHybridView((pub.hybrid.HybridView) hybridView, getConfigResId(), url);
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
        return getActivity().getLayoutInflater().inflate(R.layout.hybrid_main, null);
    }

    /**
     * Get resource id of config file. By default, it will use the value of meta
     * data <code>com.miui.sdk.hybrid.config</code> defined in
     * AndroidManifest.xml, or res/xml/miui_hybrid_config.xml if no such value
     * found. It can be overridden to provide another config file for current
     * fragment.
     * 
     * @return resource id of config file.
     */
    protected int getConfigResId() {
        return 0;
    }

    /**
     * Register a hybrid view to be managed by current fragment. Note the hybrid
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
     * Register a hybrid view to be managed by current fragment. Note the hybrid
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
     * Register a hybrid view to be managed by current fragment. Note the hybrid
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
        if (!(view instanceof pub.hybrid.HybridView)) {
            throw new IllegalArgumentException("view being registered is not a hybrid view");
        }
        pub.hybrid.HybridView hybridView = (pub.hybrid.HybridView) view;
        HybridManager manager = new HybridManager(getActivity(), hybridView);
        hybridView.setHybridManager(manager);
        mHybridViews.add(hybridView);
        manager.init(configResId, url);
    }

    /**
     * Unregister a hybrid view being managed by current fragment. Note the
     * hybrid view will not be removed from UI in this method.
     * 
     * @param view the hybrid view being managed.
     * @throws IllegalArgumentException if view is not a hybrid view.
     */
    protected final void unregisterHybridView(View view) {
        if (!(view instanceof pub.hybrid.HybridView)) {
            throw new IllegalArgumentException("view being unregistered is not a hybrid view");
        }
        mHybridViews.remove(view);
    }

    private void destroyHybridView() {
        for (pub.hybrid.HybridView view : mHybridViews) {
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
    public void onStart() {
        super.onStart();
        for (pub.hybrid.HybridView view : mHybridViews) {
            HybridManager manager = view.getHybridManager();
            manager.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (pub.hybrid.HybridView view : mHybridViews) {
            HybridManager manager = view.getHybridManager();
            manager.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (pub.hybrid.HybridView view : mHybridViews) {
            HybridManager manager = view.getHybridManager();
            manager.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (pub.hybrid.HybridView view : mHybridViews) {
            HybridManager manager = view.getHybridManager();
            manager.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (pub.hybrid.HybridView view : mHybridViews) {
            HybridManager manager = view.getHybridManager();
            manager.onDestroy();
        }
        destroyHybridView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (pub.hybrid.HybridView view : mHybridViews) {
            HybridManager manager = view.getHybridManager();
            manager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
