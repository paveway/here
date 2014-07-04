package info.paveway.here.filter;

import info.paveway.here.CommonConstants.ParamKey;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * エンコーディングフィルタークラス
 *
 * @version 1.0 新規作成
 */
public class EncodingFilter implements Filter {

    /** エンコーディング */
    private String encoding;

    /**
     * 初期化時に呼び出される。
     *
     * @param config フィルターコンフィグ
     * @throws ServletException サーブレット例外
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter(ParamKey.ENCODING);
    }

    /**
     * 終了時に呼び出される。
     */
    @Override
    public void destroy() {
        encoding = null;
    }

    /**
     * リクエストのフィルター処理を行う。
     *
     * @param reuqest サーブレットリクエスト
     * @param response サーブレットレスポンス
     * @param chain フィルターチェイン
     * @throws IOException IO例外
     * @throws ServletException サーブレット例外
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        chain.doFilter(request, response);
    }
}
