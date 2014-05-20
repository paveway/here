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
 * 退室サーブレット
 *
 * @version 1.0 新規作成
 */
public class ExitServlet extends HttpServlet {

    /** ロガー */
    private static final Logger logger = Logger.getLogger(ExitServlet.class.getName());

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
        String userId       = request.getParameter(Key.USER_ID );

        String responseString = "";
        boolean status = false;

        // パラメータが取得できた場合
        if (StringUtil.isNotNullOrEmpty(roomNoString) &&
            StringUtil.isNotNullOrEmpty(userId)) {
            logger.log(Level.CONFIG, "roomNo=[" + roomNoString + "] userId=[" + userId + "]");

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

                // 使用中データがある場合
                if ((null != usedDataList) && (0 != usedDataList.size())) {
                    // 部屋データ解放フラグをクリアする。
                    boolean releaseFlg = false;

                    // 使用中データ数分繰り返す。
                    for (UsedData usedData : usedDataList) {
                        // 該当の部屋番号の場合
                        if (roomNo == usedData.getRoomNo()) {
                            // 使用中データを削除する。
                            pm.deletePersistent(roomNo);

                            // 使用中データが1つだった場合
                            if (1 == usedDataList.size()) {
                                // 部屋データ解放フラグを設定する。
                                releaseFlg = true;
                            }

                            // ループを終了する。
                            break;
                        }
                    }

                    // 位置データを削除する。
                    LocationData locationData = pm.getObjectById(LocationData.class, userId);
                    pm.deletePersistent(locationData);

                    // 部屋データ解放の場合
                    if (releaseFlg) {
                        // 部屋データを取得する。
                        RoomData roomData = pm.getObjectById(RoomData.class, roomNo);

                        // 部屋データが取得できた場合
                        if (null != roomData) {
                            // 部屋データの使用中フラグを未使用に設定して更新する。
                            roomData.setUsed(false);
                            roomData.setUpdate(new Date());
                            pm.makePersistent(roomData);
                        }
                    }
                }

                status = true;

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
