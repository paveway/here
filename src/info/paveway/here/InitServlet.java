package info.paveway.here;

import info.paveway.here.CommonConstants.ENCODING;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * 初期化サーブレット
 *
 * @version 1.0 新規作成
 */
public class InitServlet extends HttpServlet {

    /** ロガー */
    private static final Logger logger = Logger.getLogger(InitServlet.class.getName());

    /** ルーム最大数 */
    private static final int ROOM_MAX = 10;

    /**
     * GETメソッドの処理を行う。
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

        // 部屋データリストを取得する。
        List<RoomData> roomDataList = getRoomDataList();

        // レスポンス文字列を生成する。
        String responseString = createResponseString(roomDataList);

        // レスポンス文字列を出力する。
        outputResponse(response, responseString);

        logger.log(Level.INFO, "OUT(OK)");
    }

    /**
     * 部屋データリストを取得する。
     *
     * @return 部屋データリスト
     */
    @SuppressWarnings("unchecked")
    private List<RoomData> getRoomDataList() {
        logger.log(Level.CONFIG, "IN");

        // パーシステンスマネージャーを取得する。
        PersistenceManager pm = PMF.get().getPersistenceManager();
        List<RoomData> roomDataList = null;
        try {
            // ルームデータを全件検索する。
            Query querys = pm.newQuery(RoomData.class);
            roomDataList = (List<RoomData>)querys.execute();

            // ルームデータを取得できない場合(初期状態)
            if ((null == roomDataList) || (0 == roomDataList.size())) {
                // ルームデータリストを生成する。
                roomDataList = new ArrayList<RoomData>();

                // 現在日時を取得する。
                Date now = new Date();

                // ルームデータを登録する。
                for (int i = 1; i <= ROOM_MAX; i++) {
                    RoomData roomData = new RoomData((long)i, false, "", "", "", now);
                    pm.makePersistent(roomData);
                    roomDataList.add(roomData);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            roomDataList = null;

        } finally {
            if ((null != pm) && !pm.isClosed()) {
                pm.close();
            }
        }

        logger.log(Level.CONFIG, "OUT(OK)");
        return roomDataList;
    }

    /**
     * レスポンス文字列を生成する。
     *
     * @param roomDataList 部屋データリスト
     * @return レスポンス文字列
     */
    private String createResponseString(List<RoomData> roomDataList) {
        logger.log(Level.CONFIG, "IN");

        JSONObject json = new JSONObject();
        try {
            // 部屋データが取得できた場合
            if (null != roomDataList) {
                json.put("status", true);

                // ルームデータリストのサイズを取得する。
                int size = roomDataList.size();

                // レスポンス用のJSON文字列を生成する。
                JSONObject[] roomDatas = new JSONObject[size];
                for (int i = 0; i < size; i++) {
                    RoomData roomData = roomDataList.get(i);

                    Map<String, String> roomDataMap = new HashMap<String, String>();
                    roomDataMap.put("roomNo",   String.valueOf(roomData.getRoomNo()) );
                    roomDataMap.put("used",     String.valueOf(roomData.getUsed())   );
                    roomDataMap.put("password",                roomData.getPassword());
                    roomDataMap.put("userId",                  roomData.getUserId()  );
                    roomDataMap.put("nickname",                roomData.getNickname());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    roomDataMap.put("update",   sdf.format(    roomData.getUpdate()) );

                    roomDatas[i] = new JSONObject(roomDataMap);

                    json.put("rooms", roomDatas);
                }

            // 部屋データが取得できない場合
            } else {
                json.put("status", false);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        logger.log(Level.CONFIG, "OUT(OK)");
        return json.toString();
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
