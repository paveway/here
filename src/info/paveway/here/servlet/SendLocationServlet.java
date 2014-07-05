package info.paveway.here.servlet;

import info.paveway.here.CommonConstants.ParamKey;
import info.paveway.here.data.PMF;
import info.paveway.here.data.RoomData;
import info.paveway.here.data.UseRoomData;
import info.paveway.util.StringUtil;

import java.io.IOException;
import java.util.Date;
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
 * 位置情報送信サーブレットクラス
 *
 * @version 1.0 新規作成
 *
 */
@SuppressWarnings("serial")
public class SendLocationServlet extends AbstractBaseServlet {

    /** ロガー */
    private static final Logger logger = Logger.getLogger(SendLocationServlet.class.getName());

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
        String roomName  = request.getParameter(ParamKey.ROOM_NAME);
        String roomKey   = request.getParameter(ParamKey.ROOM_KEY);
        String userId    = request.getParameter(ParamKey.USER_ID);
        String userName  = request.getParameter(ParamKey.USER_NAME);
        String latitude  = request.getParameter(ParamKey.LATITUDE);
        String longitude = request.getParameter(ParamKey.LONGITUDE);
        logger.log(
                Level.CONFIG,
                "roomName=[" + roomName + "] roomKey=[" + roomKey + "] userId=[" + userId + "] userName=[" + userName + "] latitude=[" + latitude + "] logitude=[" + longitude + "]");

        boolean status = false;
        RoomData roomData = null;

        // パラメータが取得できた場合
        if (StringUtil.isNotNullOrEmpty(roomName) &&
            StringUtil.isNotNullOrEmpty(roomKey) &&
            StringUtil.isNotNullOrEmpty(userId) &&
            StringUtil.isNotNullOrEmpty(userName) &&
            StringUtil.isNotNullOrEmpty(latitude) &&
            StringUtil.isNotNullOrEmpty(longitude)) {
            // パーシステンスマネージャーを取得する。
            PersistenceManager pm = PMF.get().getPersistenceManager();
            try {
                // ルームデータを取得する。
                Query roomDataQuery = pm.newQuery(RoomData.class);
                roomDataQuery.setFilter("roomName == roomNameParams");
                roomDataQuery.declareParameters("String roomNameParams");
                List<RoomData> roomDataList = (List<RoomData>)roomDataQuery.execute(roomName);

                // ルームデータがある場合
                if(1 == roomDataList.size()) {
                    // ルームデータを取得する。
                    roomData = roomDataList.get(0);

                    // ルーム使用データリストを取得する。
                    List<UseRoomData> useRoomDataList = roomData.getUseRoomDataList();

                    // ルーム使用中かチェックする。
                    UseRoomData useRoomData = null;
                    // ルームキーが等しい場合
                    if (roomData.getRoomKey().equals(roomKey)) {
                        // ルーム使用データ数分繰り返す。
                        int useRoomDataNum = useRoomDataList.size();
                        for (int i = 0; i < useRoomDataNum; i++) {
                            // ルーム使用中の場合
                            if (useRoomDataList.get(i).getUserName().equals(userName)) {
                                // ルーム使用中データを取得する。
                                useRoomData = useRoomDataList.get(i);
                                break;
                            }
                        }
                    }

                    // ルーム使用中の場合
                    if (null != useRoomData) {
                        // 更新日時を取得する。
                        long updateTime = new Date().getTime();

                        // 更新日時、緯度、経度を設定する。
                        useRoomData.setUpdateTime(updateTime);
                        useRoomData.setLatitude(Double.parseDouble(latitude));
                        useRoomData.setLongitude(Double.parseDouble(longitude));

                        // ルームデータを更新する。
                        roomData.setUseRoomDataList(useRoomDataList);
                        roomData.setUpdateTime(updateTime);
                        pm.makePersistent(roomData);

                        // ステータスを成功にする。
                        status = true;
                    }
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());

                // ステータスをエラーにする。
                status = false;

            } finally {
                // パーシスタントマネージャが有効かつクローズされていない場合
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
                json.put(ParamKey.ROOM_ID,    String.valueOf(roomData.getRoomId()));
                json.put(ParamKey.ROOM_NAME,  roomData.getRoomName());
                json.put(ParamKey.OWNER_ID,   String.valueOf(roomData.getOwnerId()));
                json.put(ParamKey.OWNER_NAME, roomData.getOwnerName());

                // 位置データ数を出力する。
                List<UseRoomData> useRoomDataList = roomData.getUseRoomDataList();
                int useRoomNum = useRoomDataList.size();
                json.put(ParamKey.LOCATION_DATA_NUM, String.valueOf(useRoomNum));

                // 位置データ配列を生成する。
                JSONObject[] locationDataArray = new JSONObject[useRoomNum];

                // ルーム使用データ数分繰り返す。
                for (int i = 0; i < useRoomNum; i++) {
                    // ルーム使用データを取得する。
                    UseRoomData useRoomData = useRoomDataList.get(i);

                    // 位置データを設定する。
                    Map<String, String> locationDataMap = new HashMap<String, String>();
                    locationDataMap.put(ParamKey.USER_ID,              String.valueOf(useRoomData.getUserId()));
                    locationDataMap.put(ParamKey.USER_NAME,                           useRoomData.getUserName());
                    locationDataMap.put(ParamKey.LATITUDE,             String.valueOf(useRoomData.getLatitude()));
                    locationDataMap.put(ParamKey.LONGITUDE,            String.valueOf(useRoomData.getLongitude()));
                    locationDataMap.put(ParamKey.LOCATION_UPDATE_TIME, String.valueOf(useRoomData.getUpdateTime()));

                    locationDataArray[i] = new JSONObject(locationDataMap);
                    json.put(ParamKey.LOCATION_DATAS, locationDataArray);
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
