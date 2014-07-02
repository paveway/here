package info.paveway.here.servlet;

import info.paveway.here.CommonConstants.Encoding;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ここにいるサーバー
 * 抽象基底サーブレットクラス
 *
 * @version 1.0 新規作成
 *
 */
@SuppressWarnings("serial")
public class AbstractBaseServlet extends HttpServlet {

    /** ロガー */
    private static final Logger logger = Logger.getLogger(AbstractBaseServlet.class.getName());

    /**
     * POSTメソッドの処理を行う。
     *
     * @param request HTTPサーブレットリクエスト
     * @param response HTTPサーブレットレスポンス
     * @throws IOException IO例外
     * @throws ServletException サーブレット例外
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        logger.log(Level.INFO, "IN");
        logger.log(Level.INFO, "OUT(OK)");
    }

    /**
     * レスポンス文字列を出力する。
     *
     * @param response HTTPサーブレットレスポンス
     * @param responseString レスポンス文字列
     * @throws IOException IO例外
     */
    protected void outputResponse(HttpServletResponse response, String responseString) throws IOException {
        logger.log(Level.CONFIG, "IN responseString=[" + responseString + "]");

        BufferedWriter bw =
                new BufferedWriter(
                        new OutputStreamWriter(response.getOutputStream(), Encoding.UTF_8));
        try {
            bw.write(responseString);
            bw.flush();
        } finally {
            bw.close();
        }

        logger.log(Level.CONFIG, "OUT(OK)");
    }
}
