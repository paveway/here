package info.paveway.here;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UseRoomServlet extends HttpServlet {

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // パーシスタンスマネージャーを取得する。
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            // ルーム番号を取得する。
            String roomNoStr = request.getParameter("roomNo");

            // パスワードを取得する。
            String password = request.getParameter("password");

            // ルーム番号が取得できた場合
            if (StringUtil.isNotNullOrEmpty(roomNoStr)) {
                // ルーム番号をlong型に変更する。
                long roomNo = Long.parseLong(roomNoStr);

                Query querys = pm.newQuery(RoomData.class);
                querys.setFilter("roomNo == roomNoParams");
                List<RoomData> roomDataList = (List<RoomData>)querys.execute(roomNo);

                // ルームデータがある場合
                if (1 == roomDataList.size()) {
                    // ルームデータを取得する。
                    RoomData roomData = roomDataList.get(0);

                    // 使用中の場合
                    if (roomData.getUsed()) {
                        // パスワードが等しい場合
                        if (roomData.getPassword().equals(password)) {

                        // パスワードが等しくない場合
                        } else {

                        }

                    // 未使用の場合
                    } else {

                    }

                // ルームデータがない場合
                } else {

                }
            }
        } catch (Exception e) {

        }
    }
}
