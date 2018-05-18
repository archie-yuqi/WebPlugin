package pub.hybrid;

/**
 * Hybrid invocation context for current page. Hold necessary information needed
 * by feature.
 */
public class PageContext {

    private String id;
    private String url;

    /**
     * Get id.
     * 
     * @return id.
     */
    public String getId() {
        return id;
    }

    /**
     * Set id.
     * 
     * @param id id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get url.
     * 
     * @return url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set url.
     * 
     * @param url url.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PageContext other = (PageContext) obj;
        if (id == other.id) {
            return true;
        }
        if (id == null || other.id == null || !id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
