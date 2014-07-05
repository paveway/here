package info.paveway.util;

/**
 * ここにいるサーバー
 * 文字列ユーティリティクラス
 *
 * @version 1.0 新規作成
 *
 */
public final class StringUtil {

    /**
     * 文字列がnullか空文字列か判定する。
     *
     * @param src 元の文字列
     * @return 判定結果 true:nullまたは空文字列 / false:null、空文字列ではない。
     */
    public static final boolean isNullOrEmpty(String src) {
        if ((null == src) || "".equals(src)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 文字列がnull、空文字列ではないか判定する。
     *
     * @param src 元の文字列
     * @return 判定結果 true:/null、空文字列ではない / false:nullまたは空文字列
     */
    public static final boolean isNotNullOrEmpty(String src) {
        return !isNullOrEmpty(src);
    }
}
