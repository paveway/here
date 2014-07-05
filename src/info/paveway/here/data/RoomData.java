package info.paveway.here.data;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * ここにいるサーバー
 * ルームデータクラス
 *
 * @version 1.0 新規作成
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RoomData {

    /** ルームID */
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @PrimaryKey private Long roomId;

    /** ルーム名 */
    @Persistent private String roomName;

    /** ルームキー */
    @Persistent private String roomKey;

    /** オーナーID */
    @Persistent private Long ownerId;

    /** オーナー名 */
    @Persistent private String ownerName;

    /** 更新日時 */
    @Persistent private Long updateTime;

    /** ルーム使用データリスト */
    @Persistent private List<UseRoomData> useRoomDataList = new ArrayList<UseRoomData>();

    /**
     * コンストラクタ
     *
     * @param roomName ユーザ名
     * @param roomKey パスワード
     * @param ownerId オーナーID
     * @param ownerName オーナー名
     * @param updateTime 更新日時
     */
    public RoomData(String roomName, String roomKey, Long ownerId, String ownerName, Long updateTime) {
        this.roomName   = roomName;
        this.roomKey    = roomKey;
        this.ownerId    = ownerId;
        this.ownerName  = ownerName;
        this.updateTime = updateTime;
    }

    /**
     * コンストラクタ
     *
     * @param roomId ルームID
     * @param roomName ルーム名
     * @param roomKey パスワード
     * @param ownerId オーナーID
     * @param ownerName オーナー名
     * @param updateTime 更新日時
     */
    public RoomData(Long roomId, String roomName, String roomKey, Long ownerId, String ownerName, Long updateTime) {
        this(roomName, roomKey, ownerId, ownerName, updateTime);
        this.roomId = roomId;
    }

    public void setRoomId(    Long   roomId    ) { this.roomId     = roomId;     }
    public void setRoomName(  String roomName  ) { this.roomName   = roomName;   }
    public void setRoomKey(   String roomKey   ) { this.roomKey    = roomKey;    }
    public void setOwnerId(   Long   ownerId   ) { this.ownerId    = ownerId;    }
    public void setOwnerName( String ownerName ) { this.ownerName  = ownerName;  }
    public void setUpdateTime(Long   updateTime) { this.updateTime = updateTime; }
    public void setUseRoomDataList(List<UseRoomData> useRoomDataList) { this.useRoomDataList = useRoomDataList; }
    public Long   getRoomId()     { return roomId;     }
    public String getRoomName()   { return roomName;   }
    public String getRoomKey()    { return roomKey;    }
    public Long   getOwnerId()    { return ownerId;    }
    public String getOwnerName()  { return ownerName;  }
    public Long   getUpdateTime() { return updateTime; }
    public List<UseRoomData> getUseRoomDataList() { return useRoomDataList; }
}
