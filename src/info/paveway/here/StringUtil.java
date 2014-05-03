package info.paveway.here;

public final class StringUtil {

    public static final boolean isNullOrEmpty(String src) {
        if ((null == src) || "".equals(src)) {
            return true;
        } else {
            return false;
        }
    }

    public static final boolean isNotNullOrEmpty(String src) {
        return !isNullOrEmpty(src);
    }
}
