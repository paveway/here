package info.paveway.here;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * 部屋データクラス
 *
 * @version 1.0 新規作成
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RoomData {

    /** ルーム番号 */
    @PrimaryKey private Long roomNo;

    /** 使用中フラグ */
    @Persistent private boolean used;

    /** パスワード */
    @Persistent private String password;

    /** ユーザーID */
    @Persistent private String userId;

    /** ニックネーム */
    @Persistent private String nickname;

    /** 更新日時 */
    @Persistent private Date update;

    /**
     * コンストラクタ
     *
     * @param roomNo ルーム番号
     * @param used 使用中フラグ
     * @param password パスワード
     * @param userId ユーザーID
     * @param nickname ニックネーム
     * @param update 更新日時
     */
    public RoomData(Long roomNo, boolean used, String password, String userId, String nickname, Date update) {
        this.roomNo   = roomNo;
        this.used     = used;
        this.password = password;
        this.userId   = userId;
        this.nickname = nickname;
        this.update   = update;
    }

    public void setRoomNo(Long roomNo)       { this.roomNo   = roomNo;   }
    public void setUsed(boolean used)        { this.used     = used;     }
    public void setPassword(String password) { this.password = password; }
    public void setUserId(String userId)     { this.userId   = userId;   }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setUpdate(Date update)       { this.update   = update;   }
    public Long getRoomNo()     { return roomNo;   }
    public boolean getUsed()    { return used;     }
    public String getPassword() { return password; }
    public String getUserId()   { return userId;   }
    public String getNickname() { return nickname; }
    public Date getUpdate()     { return update;   }
}
