package info.paveway.here.servlet;

import info.paveway.here.CommonConstants.ParamKey;
import info.paveway.here.data.PMF;
import info.paveway.here.data.RoomData;

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
 * ルーム一覧サーブレットクラス
 *
 * @version 1.0 新規作成
 *
 */
@SuppressWarnings("serial")
public class RoomListServlet extends AbstractBaseServlet {

    /** ロガー */
    private static final Logger logger = Logger.getLogger(RoomListServlet.class.getName());

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        logger.log(Level.INFO, "IN");

        boolean status = false;
        List<RoomData> roomDataList = null;

        // パーシステンスマネージャーを取得する。
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            // ルームデータを取得する。
            Query usedQuery = pm.newQuery(RoomData.class);
            roomDataList = (List<RoomData>)usedQuery.execute();

            // ステータスを成功にする。
            status = true;
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

        // レスポンス用にJSON文字列を生成する。
        String responseString = "";
        try {
            JSONObject json = new JSONObject();

            // ステータスを出力する。
            json.put(ParamKey.STATUS, status);

            // ステータスが成功の場合
            if (status) {
                // ルームデータを出力する。
                int roomNum = roomDataList.size();

                // ルームデータ数を出力する。
                json.put(ParamKey.ROOM_DATA_NUM, String.valueOf(roomNum));

                // ルームデータ配列を生成する。
                JSONObject[] roomDataArray = new JSONObject[roomNum];

                // ルームデータ数分繰り返す。
                for (int i = 0; i < roomNum; i++) {
                    // ルームデータを取得する。
                    RoomData roomData = roomDataList.get(i);

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
