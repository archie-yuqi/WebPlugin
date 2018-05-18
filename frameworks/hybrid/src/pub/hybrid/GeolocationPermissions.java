package pub.hybrid;

public abstract class GeolocationPermissions {

    /**
     * @hide Only for use by AbsWebChromeClient implementations
     */
    public GeolocationPermissions() {
    }

    public interface Callback {

        void invoke(String origin, boolean allow, boolean retain);
    }
}
