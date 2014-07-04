package info.paveway.here.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * ここにいるサーバー
 * ルーム使用データクラス
 *
 * @version 1.0 新規作成
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UseRoomData {

    /** ルーム使用ID */
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @PrimaryKey private Key key;

    /** ユーザID */
    @Persistent private Long userId;

    /** ユーザ名 */
    @Persistent private String userName;

    /** 緯度 */
    @Persistent private Double latitude;

    /** 経度 */
    @Persistent private Double longitude;

    /** 更新日時 */
    @Persistent private Long updateTime;

    /**
     * コンストラクタ
     *
     * @param userId ユーザID
     * @param userName ユーザ名
     * @param latitude 緯度
     * @param longitude 経度
     * @param updateTime 更新日時
     */
    public UseRoomData(Long userId, String userName, Double latitude, Double longitude, Long updateTime) {
        this.userId     = userId;
        this.userName   = userName;
        this.latitude   = latitude;
        this.longitude  = longitude;
        this.updateTime = updateTime;
    }

    /**
     * コンストラクタ
     *
     * @param useRoomId ルーム使用ID
     * @param userId ユーザID
     * @param userName ユーザ名
     * @param latitude 緯度
     * @param longitude 経度
     * @param updateTime 更新日時
     */
    public UseRoomData(Key key, Long userId, String userName, Double latitude, Double longitude, Long updateTime) {
        this(userId, userName, latitude, longitude, updateTime);
        this.key = key;
    }

    public void setKey(       Key    key       ) { this.key        = key;        }
    public void setUserId(    Long   userId    ) { this.userId     = userId;     }
    public void setUserName(  String userName  ) { this.userName   = userName;   }
    public void setLatitude(  Double latitude  ) { this.latitude   = latitude;   }
    public void setLongitude( Double longitude ) { this.longitude  = longitude;  }
    public void setUpdateTime(Long   updateTime) { this.updateTime = updateTime; }

    public Key    getKey()        { return key;        }
    public Long   getUserId()     { return userId;     }
    public String getUserName()   { return userName;   }
    public Double getLatitude()   { return latitude;   }
    public Double getLongitude()  { return longitude;  }
    public Long   getUpdateTime() { return updateTime; }
}
