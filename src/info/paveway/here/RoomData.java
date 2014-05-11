package info.paveway.here;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RoomData {

    /** ルーム番号 */
    @PrimaryKey private Long roomNo;
    @Persistent private boolean used;
    @Persistent private String password;
    @Persistent private String ownerId;
    @Persistent private Date update;

    /**
     * コンストラクタ
     *
     * @param roomNo ルーム番号
     * @param used 使用中フラグ
     * @param password パスワード
     * @param ownerId オーナーID
     * @param update 更新日時
     */
    public RoomData(Long roomNo, boolean used, String password, String ownerId, Date update) {
        this.roomNo   = roomNo;
        this.used     = used;
        this.password = password;
        this.ownerId  = ownerId;
        this.update   = update;
    }

    public void setRoomNo(Long roomNo)       { this.roomNo   = roomNo;   }
    public void setUsed(boolean used)        { this.used     = used;     }
    public void setPassword(String password) { this.password = password; }
    public void setOwnerId(String ownerId)   { this.ownerId  = ownerId;  }
    public void setUpdate(Date update)       { this.update   = update;   }
    public Long getRoomNo()      { return roomNo;   }
    public boolean getUsed()    { return used;     }
    public String getPassword() { return password; }
    public String getOwnerId()  { return ownerId;  }
    public Date getUpdate()     { return update;   }
}
