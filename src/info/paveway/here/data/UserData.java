package info.paveway.here.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * ここにいるサーバー
 * ユーザデータクラス
 *
 * @version 1.0 新規作成
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UserData {

    /** ユーザID */
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @PrimaryKey private Long userId;

    /** ユーザ名 */
    @Persistent private String userName;

    /** パスワード */
    @Persistent private String password;

    /** ログイン済みフラグ */
    @Persistent private Boolean logged;

    /** 更新日時 */
    @Persistent private Long updateTime;

    /**
     * コンストラクタ
     *
     * @param userName ユーザ名
     * @param password パスワード
     * @param logged ログイン済みフラグ
     * @param updateTime 更新日時
     */
    public UserData(String userName, String password, Boolean logged, Long updateTime) {
        this.userName   = userName;
        this.password   = password;
        this.logged     = logged;
        this.updateTime = updateTime;
    }

    /**
     * コンストラクタ
     *
     * @param userId ユーザーID
     * @param userName ユーザ名
     * @param password パスワード
     * @param logged ログイン済みフラグ
     * @param updateTime 更新日時
     */
    public UserData(Long userId, String userName, String password, Boolean logged, Long updateTime) {
        this(userName, password, logged, updateTime);
        this.userId     = userId;
    }

    public void setUserId(    Long userId    ) { this.userId     = userId;     }
    public void setUserName(  String userName) { this.userName   = userName;   }
    public void setPassword(  String password) { this.password   = password;   }
    public void setLogged(    Boolean logged ) { this.logged     = logged;     }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
    public Long getUserId()        { return userId;     }
    public String  getUserName()   { return userName;   }
    public String  getPassword()   { return password;   }
    public Boolean getLogged()     { return logged;     }
    public Long    getUpdateTime() { return updateTime; }
}
