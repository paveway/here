package info.paveway.here;

import info.paveway.here.CommonConstants.ENCODING;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class InitServlet extends HttpServlet {

    /** ロガー */
    private static final Logger logger = Logger.getLogger(InitServlet.class.getName());

    private static final int ROOM_MAX = 10;

    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        logger.log(Level.INFO, "IN");

        try {
            // パーシステンスマネージャーを取得する。
            PersistenceManager pm = PMF.get().getPersistenceManager();
            try {
                Query querys = pm.newQuery(RoomData.class);
                List<RoomData> roomDataList = (List<RoomData>)querys.execute();

                if (null == roomDataList) {
                    roomDataList = new ArrayList<RoomData>();
                }

                int size = roomDataList.size();
                if (0 == size) {
                    Date now = new Date();
                    for (int i = 1; i <= ROOM_MAX; i++) {
                        RoomData roomData = new RoomData((long)i, false, "", "", now);
                        pm.makePersistent(roomData);
                        roomDataList.add(roomData);
                    }
                    size = ROOM_MAX;
                }

                JSONObject json = new JSONObject();
                JSONObject[] roomDatas = new JSONObject[size];
                for (int i = 0; i < size; i++) {
                    RoomData roomData = roomDataList.get(i);

                    Map<String, String> roomDataMap = new HashMap<String, String>();
                    roomDataMap.put("roomNo",   String.valueOf(roomData.getRoomNo()) );
                    roomDataMap.put("used",     String.valueOf(roomData.getUsed())   );
                    roomDataMap.put("password",                roomData.getPassword());
                    roomDataMap.put("ownerId",                 roomData.getOwnerId() );
                    roomDataMap.put("update",   String.valueOf(roomData.getUpdate()) );

                    roomDatas[i] = new JSONObject(roomDataMap);

                    try {
                        json.put("rooms", roomDatas);
                    } catch (JSONException e) {
                        logger.log(Level.SEVERE, e.getMessage(), e);
                    }
                }

                BufferedWriter bw =
                        new BufferedWriter(
                                new OutputStreamWriter(response.getOutputStream(), ENCODING.UTF_8));
                try {
                    bw.write(json.toString());
                    bw.flush();
                } finally {
                    bw.close();
                }
            } finally {
                pm.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }

        logger.log(Level.INFO, "OUT(OK)");
    }
}
