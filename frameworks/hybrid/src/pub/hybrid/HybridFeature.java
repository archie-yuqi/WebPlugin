package pub.hybrid;

import java.util.Map;

/**
 * The interface of hybrid features. Any feature should implements this
 * interface.
 */
public interface HybridFeature {

    /**
     * Set all parameters used by this feature.
     * 
     * @param params parameters used by this feature.
     */
    void setParams(Map<String, String> params);

    /**
     * Invoke specified action in this feature.
     * 
     * @param request invocation request.
     * @return invocation response. <code>null</code> if current {@link Mode} is
     *         {@link Mode#CALLBACK}
     */
    Response invoke(Request request);

    /**
     * Get the invocation mode of specified action.
     * 
     * @param request invocation request.
     * @return invocation mode.
     * @see Mode#SYNC
     * @see Mode#ASYNC
     * @see Mode#CALLBACK
     */
    Mode getInvocationMode(Request request);

    /**
     * Invocation mode.
     */
    public enum Mode {
        /**
         * Synchronous invocation. When calling actions in such mode, caller
         * will get response until invocation finished.
         */
        SYNC,
        /**
         * Asynchronous invocation. When calling actions in such mode, caller
         * will get an empty response immediately, but wait in a different
         * thread to get response until invocation finished.
         */
        ASYNC,
        /**
         * Callback invocation. When calling actions in such mode, caller will
         * get an empty response immediately, but receive response through
         * callback when invocation finished.
         */
        CALLBACK
    }
}
