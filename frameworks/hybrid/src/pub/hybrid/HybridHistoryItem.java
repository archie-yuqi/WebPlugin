package pub.hybrid;

import android.graphics.Bitmap;

/**
 * A convenience class for accessing fields in an entry in the back/forward list
 * of a HybridView. Each HybridHistoryItem is a snapshot of the requested history
 * item. Each history item may be updated during the load of a page.
 * @see HybridBackForwardList
 */
public abstract class HybridHistoryItem {

    /**
     * @hide
     */
    public HybridHistoryItem() {
    }

    public String getUrl() {
        return null;
    }

    public String getOriginalUrl() {
        return null;
    }

    public String getTitle() {
        return null;
    }

    public Bitmap getFavicon() {
        return null;
    }
}
