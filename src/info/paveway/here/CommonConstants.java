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
     * リクエストパラメータキー
     *
     */
    public class ReqParamKey {
        /** エンコーディング */
        public static final String ENCODING = "encoding";

        /** ステータス */
        public static final String STATUS = "status";

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

        /** 使用中カウント */
        public static final String USED_COUNT = "usedCount";

        /** ルーム更新日時 */
        public static final String ROOM_UPDATE_TIME = "roomUpdateTime";

        /** ルーム配列 */
        public static final String ROOMS = "rooms";

        /** 位置情報配列 */
        public static final String LOCATIONS = "locations";
    }

    /**
     * JSONキー
     *
     */
    public class JSONKey {

        /** ステータス */
        public static final String STATUS = ReqParamKey.STATUS;

        /** ユーザID */
        public static final String USER_ID = ReqParamKey.USER_ID;

        /** ユーザ名 */
        public static final String USER_NAME = ReqParamKey.USER_NAME;

        /** ユーザパスワード */
        public static final String USER_PASSWORD = ReqParamKey.USER_PASSWORD;

        /** ユーザログイン済みフラグ */
        public static final String USER_LOGGED = ReqParamKey.USER_LOGGED;

        /** ユーザ更新日時 */
        public static final String USER_UPDATE_TIME = ReqParamKey.USER_UPDATE_TIME;

        /** ルームID */
        public static final String ROOM_ID = ReqParamKey.ROOM_ID;

        /** ルーム名 */
        public static final String ROOM_NAME = ReqParamKey.ROOM_NAME;

        /** ルームキー */
        public static final String ROOM_KEY = ReqParamKey.ROOM_KEY;

        /** オーナーID */
        public static final String OWNER_ID = ReqParamKey.OWNER_ID;

        /** オーナー名 */
        public static final String OWNER_NAME = ReqParamKey.OWNER_NAME;

        /** 使用中カウント */
        public static final String USED_COUNT = ReqParamKey.USED_COUNT;

        /** ルーム更新日時 */
        public static final String ROOM_UPDATE_TIME = ReqParamKey.ROOM_UPDATE_TIME;

        /** ルームデータ配列 */
        public static final String ROOM_DATAS = "roomDatas";

        /** ルームデータ数 */
        public static final String ROOM_DATA_NUM = "roomDataNum";

        /** 位置データ配列 */
        public static final String LOCATION_DATAS = "locationDatas";
    }

    /**
     * エンコーディング
     *
     */
    public class Encoding {
        /** UTF-8 */
        public static final String UTF_8 = "UTF-8";
    }
}
