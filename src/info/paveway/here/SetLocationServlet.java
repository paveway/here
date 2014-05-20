package info.paveway.here;

import info.paveway.here.CommonConstants.ENCODING;
import info.paveway.here.CommonConstants.Key;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * ロケーションデータ設定サーブレットクラス
 *
 * @version 1.0 新規作成
 *
 */
@SuppressWarnings("serial")
public class SetLocationServlet extends HttpServlet {

    /** ロガー */
    private static final Logger logger = Logger.getLogger(SetLocationServlet.class.getName());

    /** コンテントタイプ */
    private static final String CONTENT_TYPE = "text/html; charset=" + ENCODING.UTF_8;

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

        // POSメソッドの処理を呼び出す。
        doPost(request, response);

        logger.log(Level.INFO, "OUT(OK)");
    }

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

        // リクエストからデータを取得する。
        String userId       = request.getParameter(Key.USER_ID  );
        String roomNoString = request.getParameter(Key.ROOM_NO  );
        String nickname     = request.getParameter(Key.NICKNAME );
        String latitude     = request.getParameter(Key.LATITUDE );
        String longitude    = request.getParameter(Key.LONGITUDE);
        logger.log(Level.CONFIG, "userId=[" + userId + "] roomNo=[" + roomNoString + "] nickname=[" + nickname + "] latitude=[" + latitude + "] longitude=[" + longitude + "]");

        // データが取得できた場合
        if (StringUtil.isNotNullOrEmpty(userId      ) &&
            StringUtil.isNotNullOrEmpty(roomNoString) &&
            StringUtil.isNotNullOrEmpty(nickname    ) &&
            StringUtil.isNotNullOrEmpty(latitude    ) &&
            StringUtil.isNotNullOrEmpty(longitude)) {

            long roomNo = Long.parseLong(roomNoString);

            // パーシステンスマネージャーを取得する。
            PersistenceManager pm = PMF.get().getPersistenceManager();
            try {
                // 取得したロケーションデータを登録/更新する。
                registLocationData(pm, userId, roomNo, nickname, latitude, longitude);

                // 登録済みのロケーションデータリストを取得する。
                List<LocationData> locationDataList = getLocationDataList(pm);

                // ロケーションデータリストからJSON文字列を取得する。
                String json = createJsonString(locationDataList);

                // コンテントタイプを設定する。
                response.setContentType(CONTENT_TYPE);

                // JSON文字列を出力する。
                outputResponse(response, json);
            } finally {
                // パーシステンスマネージャーをクローズする。
                pm.close();
            }
        }

        logger.log(Level.INFO, "OUT(OK)");
    }

    /**
     * ロケーションデータを登録/更新する。
     *
     * @param pm パーシステンスマネージャー
     * @param userId ユーザーID
     * @param roomNo 部屋番号
     * @param nickname ニックネーム
     * @param latitude 経度
     * @param longitude 緯度
     */
    private void registLocationData(PersistenceManager pm, String userId, long roomNo, String nickname, String latitude, String longitude) {
        logger.log(Level.CONFIG, "IN id=[" + userId + "] roomNo=[" + roomNo + "] nickname=[" + nickname + "] latitude=[" + latitude + "] longitude=[" + longitude + "]");

        // 登録済みロケーションデータを取得する。
        LocationData locationData = getLocationData(pm, userId);

        // ロケーションデータが登録済みの場合
        if (null != locationData) {
            logger.log(Level.CONFIG, "locationData exist.");

            // ニックネーム、緯度、経度データを更新する。
            locationData.setNickname(nickname);
            locationData.setLatitude(latitude);
            locationData.setLongitude(longitude);

        // ロケーションデータが未登録の場合
        } else {
            logger.log(Level.CONFIG, "locationData not exist.");

            locationData = new LocationData(userId, roomNo, nickname, latitude, longitude);
        }

        // ロケーションデータを登録/更新する。
        pm.makePersistent(locationData);
        logger.log(Level.CONFIG, "makePersistent done.");

        logger.log(Level.CONFIG, "OUT(OK)");
    }

    /**
     * ロケーションデータを取得する。
     *
     * @param pm パーシステンスマネージャー
     * @param userId ユーザーID
     * @return ロケーションデータ 未登録の場合はnull
     */
    @SuppressWarnings("unchecked")
    private LocationData getLocationData(PersistenceManager pm, String userId) {
        logger.log(Level.CONFIG, "IN userId=[" + userId + "]");

        Query querys = pm.newQuery(LocationData.class);
        querys.setFilter("userId == userIdParams");
        querys.declareParameters("String userIdParams");
        List<LocationData> locationDataList = (List<LocationData>)querys.execute(userId);
        if (0 < locationDataList.size()) {
            logger.log(Level.CONFIG, "OUT(OK)");
            return locationDataList.get(0);

        } else {
            logger.log(Level.CONFIG, "OUT(NG)");
            return null;
        }
    }

    /**
     * ロケーションデータリストを取得する。
     *
     * @param pm
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<LocationData> getLocationDataList(PersistenceManager pm) {
        logger.log(Level.CONFIG, "IN");

        String query = "select from " + LocationData.class.getName();
        logger.log(Level.CONFIG, "query=[" + query + "]");

        logger.log(Level.CONFIG, "OUT(OK)");
        return (List<LocationData>)pm.newQuery(query).execute();
    }

    private String createJsonString(List<LocationData> locationDataList) {
        logger.log(Level.CONFIG, "IN");

        JSONObject root = new JSONObject();
        int size = locationDataList.size();
        logger.log(Level.CONFIG, "size=[" + size + "]");
        JSONObject[] locations = new JSONObject[size];
        for (int i = 0; i < size; i++) {
            LocationData data = locationDataList.get(i);

            Map<String, String> jsonMap = new HashMap<String, String>();
            jsonMap.put(Key.USER_ID,                 data.getUserId());
            jsonMap.put(Key.ROOM_NO,  String.valueOf(data.getRoomNo()));
            jsonMap.put(Key.NICKNAME,                data.getNickname());
            jsonMap.put(Key.LATITUDE,                data.getLatitude());
            jsonMap.put(Key.LONGITUDE,               data.getLongitude());
            logger.log(Level.CONFIG,
                "userId=["      + data.getUserId()                    +
                "] roomNo=["    + data.getRoomNo()                    +
                "] nickname=["  + data.getNickname()                  +
                "] latitude=["  + String.valueOf(data.getLatitude())  +
                "] longitude=[" + String.valueOf(data.getLongitude()) + "]");

            locations[i] = new JSONObject(jsonMap);
        }
        try {
            root.put(Key.LOCATIONS, locations);
        } catch (JSONException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        logger.log(Level.CONFIG, "OUT(OK)");
        return root.toString();
    }

    private void outputResponse(HttpServletResponse response, String json) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), ENCODING.UTF_8));
        bw.write(json);
        bw.flush();
        bw.close();
    }
}
