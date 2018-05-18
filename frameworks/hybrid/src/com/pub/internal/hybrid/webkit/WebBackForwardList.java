package com.pub.internal.hybrid.webkit;

import pub.hybrid.HybridBackForwardList;
import pub.hybrid.HybridHistoryItem;

public class WebBackForwardList extends HybridBackForwardList {

    private android.webkit.WebBackForwardList mWebBackForwardList;

    public WebBackForwardList(android.webkit.WebBackForwardList webBackForwardList) {
        mWebBackForwardList = webBackForwardList;
    }

    public HybridHistoryItem getCurrentItem() {
        return new WebHistoryItem(mWebBackForwardList.getCurrentItem());
    }

    public int getCurrentIndex() {
        return mWebBackForwardList.getCurrentIndex();
    }

    public HybridHistoryItem getItemAtIndex(int index) {
        return new WebHistoryItem(mWebBackForwardList.getItemAtIndex(index));
    }

    public int getSize() {
        return mWebBackForwardList.getSize();
    }
}
