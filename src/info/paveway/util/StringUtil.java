package info.paveway.util;

/**
 * ここにいる
 *
 * @version 1.0 新規作成
 *
 */
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
