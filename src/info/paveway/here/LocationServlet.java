package info.paveway.here;

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

@SuppressWarnings("serial")
public class LocationServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LocationServlet.class.getName());

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
        String id = request.getParameter("id");
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");
        logger.log(Level.CONFIG, "id=[" + id + "] latitude=[" + latitude + "] longitude=[" + longitude + "]");

        // データが取得できた場合
        if (StringUtil.isNotNullOrEmpty(id       ) &&
            StringUtil.isNotNullOrEmpty(latitude ) &&
            StringUtil.isNotNullOrEmpty(longitude)) {

            // パーシステンスマネージャーを取得する。
            PersistenceManager pm = PMF.get().getPersistenceManager();
            try {
                // 取得したロケーションデータを登録/更新する。
                registLocationData(pm, id, latitude, longitude);

                // 登録済みのロケーションデータリストを取得する。
                List<LocationData> locationDataList = getLocationDataList(pm);

                // ロケーションデータリストからJSON文字列を取得する。
                String json = createJsonString(locationDataList);

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
     * @param id ID
     * @param latitude 経度
     * @param longitude 緯度
     */
    private void registLocationData(PersistenceManager pm, String id, String latitude, String longitude) {
        logger.log(Level.CONFIG, "IN id=[" + id + "] latitude=[" + latitude + "] longitude=[" + longitude + "]");

        // 登録済みロケーションデータを取得する。
        LocationData locationData = getLocationData(pm, id);

        // ロケーションデータが登録済みの場合
        if (null != locationData) {
            logger.log(Level.CONFIG, "locationData exist.");

            // 緯度、経度データを更新する。
            locationData.setLatitude(latitude);
            locationData.setLongitude(longitude);

        // ロケーションデータが未登録の場合
        } else {
            logger.log(Level.CONFIG, "locationData not exist.");

            locationData = new LocationData(id, latitude, longitude);
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
     * @param id ID
     * @return ロケーションデータ 未登録の場合はnull
     */
    @SuppressWarnings("unchecked")
    private LocationData getLocationData(PersistenceManager pm, String id) {
        logger.log(Level.CONFIG, "IN id=[" + id + "]");

        Query querys = pm.newQuery(LocationData.class);
        querys.setFilter("id == idParams");
        querys.declareParameters("Long idParams");
        List<LocationData> locationDataList = (List<LocationData>)querys.execute(id);
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
            jsonMap.put("id",        data.getId());
            jsonMap.put("latitude",  data.getLatitude());
            jsonMap.put("longitude", data.getLongitude());
            logger.log(Level.CONFIG, "id=[" + data.getId() + "] latitude=[" + String.valueOf(data.getLatitude()) + "] longitude=[" + String.valueOf(data.getLongitude()) + "]");

            locations[i] = new JSONObject(jsonMap);
        }
        try {
            root.put("locations", locations);
        } catch (JSONException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        logger.log(Level.CONFIG, "OUT(OK)");
        return root.toString();
    }

    private void outputResponse(HttpServletResponse response, String json) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
        bw.write(json);
        bw.flush();
        bw.close();
    }
}
