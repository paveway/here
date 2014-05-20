package info.paveway.here;

import info.paveway.here.CommonConstants.ENCODING;
import info.paveway.here.CommonConstants.Key;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * 入室サーブレット
 *
 * @version 1.0 新規作成
 */
public class EnterServlet extends HttpServlet {

    /** ロガー */
    private static final Logger logger = Logger.getLogger(EnterServlet.class.getName());

    /**
     * POSTメソッドの処理を行う。
     *
     * @param request HTTPサーブレットリクエスト
     * @param response HTTPサーブレットレスポンス
     * @throws IOException IO例外
     * @throws ServletException サーブレット例外
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        logger.log(Level.INFO, "IN");

        // リクエストからパラメータを取得する。
        String roomNoString = request.getParameter(Key.ROOM_NO );
        String password     = request.getParameter(Key.PASSWORD);
        String userId       = request.getParameter(Key.USER_ID );
        String nickname     = request.getParameter(Key.NICKNAME);

        String responseString = "";
        boolean status = false;

        // パラメータが取得できた場合
        if (StringUtil.isNotNullOrEmpty(roomNoString) &&
            StringUtil.isNotNullOrEmpty(password) &&
            StringUtil.isNotNullOrEmpty(userId) &&
            StringUtil.isNotNullOrEmpty(nickname)) {
            logger.log(Level.CONFIG, "roomNo=[" + roomNoString + "] password=[" + password + "] userId=[" + userId + "] nickname=[" + nickname + "]");

            // 部屋番号を数値に変換する。
            long roomNo = Long.parseLong(roomNoString);

            // パーシステンスマネージャーを取得する。
            PersistenceManager pm = PMF.get().getPersistenceManager();
            List<UsedData> usedDataList = null;

            DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
            Transaction transaction = dss.beginTransaction();
            try {
                // 使用中データを取得する。
                Query usedQuery = pm.newQuery(UsedData.class);
                usedQuery.setFilter("userId == userIdParams");
                usedQuery.declareParameters("String userIdParams");
                usedDataList = (List<UsedData>)usedQuery.execute(userId);

                // 部屋データを取得する。
                RoomData roomData = pm.getObjectById(RoomData.class, roomNo);

                // 使用中データを取得できない場合、未使用とする。
                if ((null == usedDataList) || (0 == usedDataList.size())) {
                    logger.log(Level.CONFIG, "userData not exist.");

                    // 部屋データが取得できた場合
                    if (null != roomData) {
                        logger.log(Level.CONFIG, "roomData exist.");

                        // 部屋データを更新する。
                        roomData.setUsed(true);
                        roomData.setPassword(password);
                        roomData.setUserId(userId);
                        roomData.setNickname(nickname);
                        roomData.setUpdate(new Date());
                        pm.makePersistent(roomData);
                        logger.log(Level.CONFIG, "roomData update.");

                        // 使用中データを登録する。
                        UsedData usedData = new UsedData(roomNo, userId);
                        pm.makePersistent(usedData);
                        logger.log(Level.CONFIG, "usedData regist.");

                        status = true;
                        logger.log(Level.CONFIG, "status true.");
                    } else {
                        logger.log(Level.CONFIG, "roomData not exist.");
                    }

                // 使用中データが取得できた場合、使用中とする。
                } else {
                    logger.log(Level.CONFIG, "userData exist.");

                    // 部屋データが取得できない場合
                    if (null == roomData) {
                        logger.log(Level.CONFIG, "roomData not exist.");

                        // 使用中データを削除する。
                        UsedData usedData = usedDataList.get(0);
                        pm.deletePersistent(usedData);
                        logger.log(Level.CONFIG, "UsedData delete.");

                    // 部屋データを取得できた場合
                    } else {
                        logger.log(Level.CONFIG, "roomData exist.");

                        // パスワードが等しい場合
                        if (password.equals(roomData.getPassword())) {
                            logger.log(Level.CONFIG, "password correct.");

                            status = true;
                            logger.log(Level.CONFIG, "status true.");
                        } else {
                            logger.log(Level.CONFIG, "password not correct.");
                        }
                    }
                }

                transaction.commit();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());

            } finally {
                try {
                    if (transaction.isActive()) {
                        transaction.rollback();
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }

                if ((null != pm) && !pm.isClosed()) {
                    pm.close();
                }
            }

            // レスポンス用にJSON文字列を生成する。
            try {
                JSONObject json = new JSONObject();
                json.put(Key.STATUS, status);
                responseString = json.toString();
            } catch (Exception e) {
              logger.log(Level.SEVERE, e.getMessage());
            }
        }

        // レスポンス文字列を出力する。
        outputResponse(response, responseString);

        logger.log(Level.INFO, "OUT(OK)");
    }

    /**
     * レスポンス文字列を出力する。
     *
     * @param response HTTPサーブレットレスポンス
     * @param responseString レスポンス文字列
     * @throws IOException IO例外
     */
    private void outputResponse(HttpServletResponse response, String responseString) throws IOException {
        logger.log(Level.CONFIG, "IN");

        BufferedWriter bw =
                new BufferedWriter(
                        new OutputStreamWriter(response.getOutputStream(), ENCODING.UTF_8));
        try {
            bw.write(responseString);
            bw.flush();
        } finally {
            bw.close();
        }

        logger.log(Level.CONFIG, "OUT(OK)");
    }
}
