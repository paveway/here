package info.paveway.here.servlet;

import info.paveway.here.CommonConstants.ParamKey;
import info.paveway.here.data.PMF;
import info.paveway.here.data.RoomData;
import info.paveway.here.data.UseRoomData;
import info.paveway.util.StringUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        logger.log(Level.INFO, "IN");

        // リクエストからパラメータを取得する。
        String roomName = request.getParameter(ParamKey.ROOM_NAME);
        String roomKey  = request.getParameter(ParamKey.ROOM_KEY);
        String userId   = request.getParameter(ParamKey.USER_ID);
        String userName = request.getParameter(ParamKey.USER_NAME);

        logger.log(
                Level.CONFIG,
                "roomName=[" + roomName + "] roomKey=[" + roomKey + "] userId=[" + userId + "] userName=[" + userName + "]");

        boolean status = false;
        List<RoomData> newRoomDataList = null;

        // パラメータが取得できた場合
        if (StringUtil.isNotNullOrEmpty(roomName) &&
            StringUtil.isNotNullOrEmpty(roomKey) &&
            StringUtil.isNotNullOrEmpty(userId) &&
            StringUtil.isNotNullOrEmpty(userName)) {
            // パーシステンスマネージャーを取得する。
            PersistenceManager pm = PMF.get().getPersistenceManager();
            try {
                // ルームデータを取得する。
                Query roomQuery = pm.newQuery(RoomData.class);
                roomQuery.setFilter("roomName == roomNameParams");
                roomQuery.declareParameters("String roomNameParams");
                List<RoomData> roomDataList = (List<RoomData>)roomQuery.execute(roomName);

                // ルームデータが1件ある場合
                if (1 == roomDataList.size()) {
                    // ルームデータを取得する。
                    RoomData roomData = roomDataList.get(0);

                    // ルームのパスワードが等しくかつルームのオーナーの場合
                    if (roomData.getRoomKey().equals(roomKey) && roomData.getOwnerName().equals(userName)) {
                        // ルーム使用データを取得する。
                        List<UseRoomData> useRoomDataList = roomData.getUseRoomDataList();

                        // ルーム使用データが1件の場合
                        if (1 == useRoomDataList.size()) {
                            // ルーム使用データを削除する。
                            useRoomDataList.remove(0);
                            roomData.setUseRoomDataList(useRoomDataList);

                            // ルームデータを削除する。
                            pm.deletePersistent(roomData);

                            // ルームデータを取得する。
                            Query newRoomQuery = pm.newQuery(RoomData.class);
                            newRoomDataList = (List<RoomData>)newRoomQuery.execute();

                            // ステータスを成功に設定する。
                            status = true;
                        }
                    }
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
                // ルームデータを出力する。
                int roomNum = newRoomDataList.size();

                // ルームデータ数を出力する。
                json.put(ParamKey.ROOM_DATA_NUM, String.valueOf(roomNum));

                // ルームデータ配列を生成する。
                JSONObject[] roomDataArray = new JSONObject[roomNum];

                // ルームデータ数分繰り返す。
                for (int i = 0; i < roomNum; i++) {
                    // ルームデータを取得する。
                    RoomData roomData = newRoomDataList.get(i);

                    // ルームデータを設定する。
                    Map<String, String> roomDataMap = new HashMap<String, String>();
                    roomDataMap.put(ParamKey.ROOM_ID,          String.valueOf(roomData.getRoomId()));
                    roomDataMap.put(ParamKey.ROOM_NAME,                       roomData.getRoomName());
                    roomDataMap.put(ParamKey.ROOM_KEY,                        roomData.getRoomKey());
                    roomDataMap.put(ParamKey.OWNER_ID,         String.valueOf(roomData.getOwnerId()));
                    roomDataMap.put(ParamKey.OWNER_NAME,                      roomData.getOwnerName());
                    roomDataMap.put(ParamKey.ROOM_UPDATE_TIME, String.valueOf(roomData.getUpdateTime()));

                    roomDataArray[i] = new JSONObject(roomDataMap);
                    json.put(ParamKey.ROOM_DATAS, roomDataArray);
                }
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
