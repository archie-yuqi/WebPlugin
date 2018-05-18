package pub.hybrid;

/**
 * This class contains the back/forward list for a HybridView.
 * HybridView.copyBackForwardList() will return a copy of this class used to
 * inspect the entries in the list.
 */
public abstract class HybridBackForwardList {

    /**
     * @hide Only for use by AbsWebView implementations.
     */
    public HybridBackForwardList() {
    }

    public HybridHistoryItem getCurrentItem() {
        return null;
    }

    public int getCurrentIndex() {
        return -1;
    }

    public HybridHistoryItem getItemAtIndex(int index) {
        return null;
    }

    public int getSize() {
        return 0;
    }
}
