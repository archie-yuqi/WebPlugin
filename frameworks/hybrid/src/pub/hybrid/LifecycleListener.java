package pub.hybrid;

import android.app.Activity;
import android.content.Intent;

/**
 * Listener for activity lifecycle. It is an abstract class so any child can
 * override interesting methods only rather than all.
 */
public abstract class LifecycleListener {

    /**
     * Called after web page changed.
     */
    public void onPageChange() {
    }

    /**
     * @see Activity#onStart()
     */
    public void onStart() {
    }

    /**
     * @see Activity#onResume()
     */
    public void onResume() {
    }

    /**
     * @see Activity#onPause()
     */
    public void onPause() {
    }

    /**
     * @see Activity#onStop()
     */
    public void onStop() {
    }

    /**
     * @see Activity#onDestroy()
     */
    public void onDestroy() {
    }

    /**
     * @see Activity#onActivityResult(int, int, Intent)
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
