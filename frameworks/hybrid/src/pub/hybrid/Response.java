package pub.hybrid;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Hybrid invocation response. Hold a status code and detail content.
 * 
 * @see HybridFeature
 */
public class Response {

    /**
     * Successful invocation.
     */
    public static final int CODE_SUCCESS = 0;

    /**
     * {@link HybridFeature.Mode#SYNC} invocation.
     */
    public static final int CODE_SYNC = 1;

    /**
     * {@link HybridFeature.Mode#ASYNC} invocation.
     */
    public static final int CODE_ASYNC = 2;

    /**
     * {@link HybridFeature.Mode#CALLBACK} invocation.
     */
    public static final int CODE_CALLBACK = 3;

    /**
     * Invocation is cancelled.
     */
    public static final int CODE_CANCEL = 100;

    /**
     * Invocation occurs generic error.
     */
    public static final int CODE_GENERIC_ERROR = 200;

    /**
     * Invocation occurs configuration error.
     */
    public static final int CODE_CONFIG_ERROR = 201;

    /**
     * Invocation occurs signature error.
     */
    public static final int CODE_SIGNATURE_ERROR = 202;

    /**
     * Invocation occurs permission error.
     */
    public static final int CODE_PERMISSION_ERROR = 203;

    /**
     * Invocation occurs feature error.
     */
    public static final int CODE_FEATURE_ERROR = 204;

    /**
     * Invocation occurs action error.
     */
    public static final int CODE_ACTION_ERROR = 205;

    private static final String CODE = "code";
    private static final String CONTENT = "content";

    private int mCode;
    private String mContent;

    private JSONObject mJson = new JSONObject();

    /**
     * Construct a new instance with specified code and empty content.
     * 
     * @param code status code.
     */
    public Response(int code) {
        this(code, "");
    }

    /**
     * Construct a new instance with code {@link #CODE_SUCCESS} and specified
     * content.
     * 
     * @param content detail content.
     */
    public Response(String content) {
        this(CODE_SUCCESS, content);
    }

    /**
     * Construct a new instance with specified code and content.
     * 
     * @param code status code.
     * @param content detail content.
     */
    public Response(int code, String content) {
        mCode = code;
        mContent = content;
        try {
            mJson.put(CODE, mCode);
            mJson.put(CONTENT, content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Construct a new instance with code {@link #CODE_SUCCESS} and specified
     * content in JSON format.
     * 
     * @param content detail content.
     */
    public Response(JSONObject content) {
        this(CODE_SUCCESS, content);
    }

    /**
     * Construct a new instance with specified code and content in JSON format.
     * 
     * @param code status code.
     * @param content detail content.
     */
    public Response(int code, JSONObject content) {
        mCode = code;
        mContent = content.toString();
        try {
            mJson.put(CODE, mCode);
            mJson.put(CONTENT, content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get response code.
     * 
     * @return response code.
     */
    public int getCode() {
        return mCode;
    }

    /**
     * Get response content.
     * 
     * @return response content.
     */
    public String getContent() {
        return mContent;
    }

    /**
     * Get response in JSON format.
     * 
     * @return response in JSON format.
     */
    public JSONObject getJson() {
        return mJson;
    }

    @Override
    public String toString() {
        return mJson.toString();
    }
}
