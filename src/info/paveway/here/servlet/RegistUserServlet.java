package info.paveway.here.servlet;

import info.paveway.here.CommonConstants.ParamKey;
import info.paveway.here.data.PMF;
import info.paveway.here.data.UserData;
import info.paveway.util.StringUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * ここにいるサーバー
 * ユーザ登録サーブレットクラス
 *
 * @version 1.0 新規作成
 *
 */
@SuppressWarnings("serial")
public class RegistUserServlet extends AbstractBaseServlet {

    /** ロガー */
    private static final Logger logger = Logger.getLogger(RegistUserServlet.class.getName());

    /**
     * POSTメソッドの処理を行う。
     *
     * @param request HTTPサーブレットリクエスト
     * @param response HTTPサーブレットレスポンス
     * @throws IOException IO例外
     * @throws ServletException サーブレット例外
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        logger.log(Level.INFO, "IN");

        // リクエストからパラメータを取得する。
        String userName = request.getParameter(ParamKey.USER_NAME);
        String password = request.getParameter(ParamKey.USER_PASSWORD);
        logger.log(Level.CONFIG, "userName=[" + userName + "] password=[" + password + "]");

        boolean status = false;

        UserData userData = null;

        // パラメータが取得できた場合
        if (StringUtil.isNotNullOrEmpty(userName) &&
            StringUtil.isNotNullOrEmpty(password)) {
            // パーシステンスマネージャーを取得する。
            PersistenceManager pm = PMF.get().getPersistenceManager();
            try {
                // ユーザデータを取得する。
                Query usedQuery = pm.newQuery(UserData.class);
                usedQuery.setFilter("userName == userNameParams");
                usedQuery.declareParameters("String userNameParams");
                @SuppressWarnings("unchecked")
                List<UserData> userDataList = (List<UserData>)usedQuery.execute(userName);

                // ユーザ名が未登録の場合
                if(0 == userDataList.size()) {
                    // ユーザデータを登録する。
                    userData = new UserData(userName, password, true, new Date().getTime());
                    pm.makePersistent(userData);

                    // ステータスを成功にする。
                    status = true;
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());

                // ステータスをエラーにする。
                status = false;

            } finally {
                // パーシステンスマネージャが有効かつクローズされていない場合
                if ((null != pm) && !pm.isClosed()) {
                    // クローズする。
                    pm.close();
                }
            }
        }

        // レスポンス用にJSON文字列を生成する。
        String responseString = "";
        try {
            JSONObject json = new JSONObject();

            // ステータスを出力する。
            json.put(ParamKey.STATUS, status);

            // ステータスが成功の場合
            if (status) {
                // ユーザデータを出力する。
                json.put(ParamKey.USER_ID,          userData.getUserId());
                json.put(ParamKey.USER_NAME,        userData.getUserName());
                json.put(ParamKey.USER_PASSWORD,    userData.getPassword());
                json.put(ParamKey.USER_LOGGED,      userData.getLogged());
                json.put(ParamKey.USER_UPDATE_TIME, userData.getUpdateTime());
            }

            // レスポンス文字列を生成する。
            responseString = json.toString();
            logger.log(Level.CONFIG, "responseString=[" + responseString + "]");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        // レスポンス文字列を出力する。
        outputResponse(response, responseString);

        logger.log(Level.INFO, "OUT(OK)");
    }
}
