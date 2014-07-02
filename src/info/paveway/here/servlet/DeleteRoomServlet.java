package info.paveway.here.servlet;

import info.paveway.here.CommonConstants.JSONKey;
import info.paveway.here.CommonConstants.ReqParamKey;
import info.paveway.here.data.PMF;
import info.paveway.here.data.RoomData;
import info.paveway.here.data.UseRoomData;
import info.paveway.util.StringUtil;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * ここにいるサーバー
 * ルーム削除サーブレットクラス
 *
 * @version 1.0 新規作成
 *
 */
@SuppressWarnings("serial")
public class DeleteRoomServlet extends AbstractBaseServlet {

    /** ロガー */
    private static final Logger logger = Logger.getLogger(DeleteRoomServlet.class.getName());

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
        String roomName = request.getParameter(ReqParamKey.ROOM_NAME);
        String roomKey  = request.getParameter(ReqParamKey.ROOM_KEY);
        String userId   = request.getParameter(ReqParamKey.USER_ID);
        String userName = request.getParameter(ReqParamKey.USER_NAME);

        logger.log(
                Level.CONFIG,
                "roomName=[" + roomName + "] roomKey=[" + roomKey + "] userId=[" + userId + "] userName=[" + userName + "]");

        boolean status = false;

        // パラメータが取得できた場合
        if (StringUtil.isNotNullOrEmpty(roomName) &&
            StringUtil.isNotNullOrEmpty(roomKey) &&
            StringUtil.isNotNullOrEmpty(userId) &&
            StringUtil.isNotNullOrEmpty(userName)) {
            // パーシステンスマネージャーを取得する。
            PersistenceManager pm = PMF.get().getPersistenceManager();

            // トランザクションを開始する。
            DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
            Transaction transaction = dss.beginTransaction();
            try {
                // ルームデータを取得する。
                Query roomQuery = pm.newQuery(RoomData.class);
                roomQuery.setFilter("roomName == roomNameParams");
                roomQuery.declareParameters("String roomNameParams");
                @SuppressWarnings("unchecked")
                List<RoomData> roomDataList = (List<RoomData>)roomQuery.execute(roomName);

                // ルームデータが1件ある場合
                if (1 == roomDataList.size()) {
                    // ルームデータを取得する。
                    RoomData roomData = roomDataList.get(0);

                    // ルームのパスワードが等しくかつルームのオーナーの場合
                    if (roomData.getRoomKey().equals(roomKey) && roomData.getOwnerName().equals(userName)) {
                        // ルーム使用データを取得する。
                        Query useRoomQuery = pm.newQuery(UseRoomData.class);
                        useRoomQuery.setFilter("roomName == roomNameParam");
                        useRoomQuery.declareParameters("String roomNameParams");
                        @SuppressWarnings("unchecked")
                        List<UseRoomData> useRoomDataList =
                                (List<UseRoomData>)useRoomQuery.execute(roomName);

                        // ルーム使用データが1件の場合
                        if (1 == useRoomDataList.size()) {
                            // ルームデータを削除する。
                            pm.deletePersistent(roomData);

                            // ルーム使用中データを削除する。
                            pm.deletePersistent(useRoomDataList.get(0));
                        }
                    }
                }

                // コミットする。
                transaction.commit();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());

                // ステータスをエラーにする。
                status = false;

            } finally {
                try {
                    // トランザクションが有効のままの場合
                    if (transaction.isActive()) {
                        // ステータスをエラーにする。
                        status = false;

                        // ロールバックする。
                        transaction.rollback();
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.getMessage());

                    // ステータスをエラーにする。
                    status = false;
                }

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
            json.put(JSONKey.STATUS, status);

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
