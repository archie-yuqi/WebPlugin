package pub.hybrid;

import android.view.View;

/**
 * Hybrid invocation request. Hold all necessary information needed by feature.
 * 
 * @see HybridFeature
 */
public class Request {

    private String action;
    private String rawParams;
    private pub.hybrid.Callback callback;
    private PageContext pageContext;
    private NativeInterface nativeInterface;
    private View view;

    /**
     * Get action.
     * 
     * @return action.
     */
    public String getAction() {
        return action;
    }

    /**
     * Set action.
     * 
     * @param action action.
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Get raw parameters.
     * 
     * @return raw parameters.
     */
    public String getRawParams() {
        return rawParams;
    }

    /**
     * Set raw parameters.
     * 
     * @param rawParams raw parameters.
     */
    public void setRawParams(String rawParams) {
        this.rawParams = rawParams;
    }

    /**
     * Get callback.
     * 
     * @return callback.
     */
    public pub.hybrid.Callback getCallback() {
        return callback;
    }

    /**
     * Set callback.
     * 
     * @param callback callback.
     */
    public void setCallback(pub.hybrid.Callback callback) {
        this.callback = callback;
    }

    /**
     * Get page context.
     * 
     * @return page context.
     */
    public PageContext getPageContext() {
        return pageContext;
    }

    /**
     * Set page context.
     * 
     * @param pageContext page context.
     */
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    /**
     * Get native interface.
     * 
     * @return native interface.
     */
    public NativeInterface getNativeInterface() {
        return nativeInterface;
    }

    /**
     * Set native interface.
     * 
     * @param nativeInterface native interface.
     */
    public void setNativeInterface(NativeInterface nativeInterface) {
        this.nativeInterface = nativeInterface;
    }

    /**
     * Get view.
     *
     * @return view.
     */
    public View getView() {
        return view;
    }

    /**
     * Set view.
     *
     * @param view view.
     */
    public void setView(View view) {
        this.view = view;
    }
}
