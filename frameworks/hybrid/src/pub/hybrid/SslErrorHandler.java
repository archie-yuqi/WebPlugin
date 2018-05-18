package pub.hybrid;

public abstract class SslErrorHandler {

    /**
     * @hide Only for use by AbsWebViewClient implementations.
     */
    protected SslErrorHandler() {
    }

    public void cancel(){
    }

    public void proceed(){
    }
}
