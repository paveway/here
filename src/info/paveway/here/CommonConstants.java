package info.paveway.here;

/**
 * ここにいるサーバー
 * 共通定数クラス
 *
 * @version 1.0 新規作成
 */
public class CommonConstants {

    /**
     * リクエストアトリビュートキー
     *
     */
    public class ReqAttrKey {
        /** ユーザー */
        public static final String USER = "user";

        /** ニックネーム */
        public static final String NICKNAME = "nickname";
    }

    /**
     * パラメータキー
     *
     */
    public class ParamKey {
        /** エンコーディング */
        public static final String ENCODING = "encoding";

        /** ユーザID */
        public static final String USER_ID = "userId";

        /** ユーザ名 */
        public static final String USER_NAME = "userName";

        /** ユーザパスワード */
        public static final String USER_PASSWORD = "userPassword";

        /** ユーザログイン済みフラグ */
        public static final String USER_LOGGED = "userLogged";

        /** ユーザ更新日時 */
        public static final String USER_UPDATE_TIME = "userUpdateTime";

        /** ルームID */
        public static final String ROOM_ID = "roomId";

        /** ルーム名 */
        public static final String ROOM_NAME = "roomName";

        /** ルームキー */
        public static final String ROOM_KEY = "roomKey";

        /** オーナーID */
        public static final String OWNER_ID = "ownerId";

        /** オーナー名 */
        public static final String OWNER_NAME = "ownerName";

        /** 緯度 */
        public static final String LATITUDE = "latitude";

        /** 経度 */
        public static final String LONGITUDE = "longitude";

        /** ルーム更新日時 */
        public static final String ROOM_UPDATE_TIME = "roomUpdateTime";

        /** 位置データ更新日時 */
        public static final String LOCATION_UPDATE_TIME = "locationUpdateTime";

        /** ステータス */
        public static final String STATUS = "status";

        /** ルームデータ配列 */
        public static final String ROOM_DATAS = "roomDatas";

        /** ルームデータ数 */
        public static final String ROOM_DATA_NUM = "roomDataNum";

        /** 位置データ配列 */
        public static final String LOCATION_DATAS = "locationDatas";

        /** 位置データ数 */
        public static final String LOCATION_DATA_NUM = "locationDataNum";
    }

    /**
     * エンコーディング
     *
     */
    public class Encoding {
        /** UTF-8 */
        public static final String UTF_8 = "UTF-8";
    }

    /**
     * ステータス値
     *
     */
    public class Status {
        /** 正常終了 */
        public static final int SUCCESS = 0;

        /** その他エラー */
        public static final int ERROR = 1;

        /** パラメータエラー */
        public static final int PARAM_EROR = 2;

        /** DBエラー */
        public static final int DB_ERROR = 3;

        /** ユーザ登録済みエラー */
        public static final int USER_REGISTED_ERROR = 4;

        /** ユーザ存在無エラー */
        public static final int NO_USER_ERROR = 5;

        /** ユーザパスワードエラー */
        public static final int USER_PASSWORD_ERROR = 6;

        /** ログイン済みエラー */
        public static final int LOGINED_ERROR = 7;

        /** ルーム登録済みエラー */
        public static final int ROOM_REGISTED_ERROR = 8;
    }
}
