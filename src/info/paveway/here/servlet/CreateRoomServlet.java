package info.paveway.here.servlet;

import info.paveway.here.CommonConstants.JSONKey;
import info.paveway.here.CommonConstants.ReqParamKey;
import info.paveway.here.data.PMF;
import info.paveway.here.data.RoomData;
import info.paveway.here.data.UseRoomData;
import info.paveway.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
 * ルーム作成サーブレットクラス
 *
 * @version 1.0 新規作成
 *
 */
@SuppressWarnings("serial")
public class CreateRoomServlet extends AbstractBaseServlet {

    /** ロガー */
    private static final Logger logger = Logger.getLogger(CreateRoomServlet.class.getName());

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
        String roomName  = request.getParameter(ReqParamKey.ROOM_NAME);
        String roomKey   = request.getParameter(ReqParamKey.ROOM_KEY);
        String ownerId   = request.getParameter(ReqParamKey.OWNER_ID);
        String ownerName = request.getParameter(ReqParamKey.OWNER_NAME);

        logger.log(
                Level.CONFIG,
                "roomName=[" + roomName + "] roomKey=[" + roomKey + "] ownerId=[" + ownerId + "] ownerName=[" + ownerName + "]");

        boolean status = false;
        RoomData roomData = null;

        // パラメータが取得できた場合
        if (StringUtil.isNotNullOrEmpty(roomName) &&
            StringUtil.isNotNullOrEmpty(roomKey) &&
            StringUtil.isNotNullOrEmpty(ownerId) &&
            StringUtil.isNotNullOrEmpty(ownerName)) {
            // パーシステンスマネージャーを取得する。
            PersistenceManager pm = PMF.get().getPersistenceManager();

            // トランザクションを開始する。
            DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
            Transaction transaction = dss.beginTransaction();
            try {
                // ルームデータを取得する。
                Query usedQuery = pm.newQuery(RoomData.class);
                usedQuery.setFilter("roomName == roomNameParams");
                usedQuery.declareParameters("String roomNameParams");
                @SuppressWarnings("unchecked")
                List<RoomData> userDataList = (List<RoomData>)usedQuery.execute(roomName);

                // ルーム名が未登録の場合
                if(0 == userDataList.size()) {
                    long updateTime = new Date().getTime();

                    // ルームデータを登録する。
                    long ownerIdLong = Long.parseLong(ownerId);
                    UseRoomData useRoomData = new UseRoomData(ownerIdLong, ownerName, updateTime);
                    roomData = new RoomData(roomName, roomKey, ownerIdLong, ownerName, updateTime);
                    List<UseRoomData> useRoomDataList = new ArrayList<UseRoomData>();
                    useRoomDataList.add(useRoomData);
                    roomData.setUseRoomDataList(useRoomDataList);
                    pm.makePersistent(roomData);
                    logger.log(Level.INFO, "RoomData insert.");

                    // ステータスを成功にする。
                    status = true;
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

            // ステータスが成功の場合
            if (status) {
                // ルームデータを出力する。
                json.put(JSONKey.ROOM_ID,          roomData.getRoomId());
                json.put(JSONKey.ROOM_NAME,        roomData.getRoomName());
                json.put(JSONKey.ROOM_KEY,         roomData.getRoomKey());
                json.put(JSONKey.OWNER_ID,         roomData.getOwnerId());
                json.put(JSONKey.OWNER_NAME,       roomData.getOwnerName());
                json.put(JSONKey.USER_UPDATE_TIME, roomData.getUpdateTime());
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
