package pub.hybrid;

import com.pub.internal.hybrid.HybridManager;

import pub.hybrid.HybridFeature.Mode;

/**
 * Callback interface for hybrid invocation with {@link Mode#CALLBACK}.
 */
public class Callback {

    private HybridManager mManager;
    private PageContext mPageContext;
    private String mJsCallback;

    /**
     * Construct a new instance.
     * 
     * @param manager hybrid manager.
     * @param pageContext page context.
     * @param jsCallback callback.
     */
    public Callback(HybridManager manager, PageContext pageContext, String jsCallback) {
        mManager = manager;
        mPageContext = pageContext;
        mJsCallback = jsCallback;
    }

    /**
     * Invoke callback with specified response.
     * 
     * @param response invocation response.
     */
    public void callback(Response response) {
        mManager.callback(response, mPageContext, mJsCallback);
    }
}
