package pub.hybrid;

import android.app.Activity;

import com.pub.internal.hybrid.HybridManager;

/**
 * The interface exposed to feature to access internal methods.
 */
public class NativeInterface {

    private HybridManager mManager;

    /**
     * Construct a new instance.
     * 
     * @param manager hybrid manager.
     */
    public NativeInterface(HybridManager manager) {
        mManager = manager;
    }

    /**
     * Get current activity.
     * 
     * @return current activity.
     */
    public Activity getActivity() {
        return mManager.getActivity();
    }

    /**
     * add activity lifecycle listener.
     * 
     * @param listener listener.
     */
    public void addLifecycleListener(LifecycleListener listener) {
        mManager.addLifecycleListener(listener);
    }

    /**
     * remove activity lifecycle listener.
     * 
     * @param listener listener.
     */
    public void removeLifecycleListener(LifecycleListener listener) {
        mManager.removeLifecycleListener(listener);
    }
}
