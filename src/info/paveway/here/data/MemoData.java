package info.paveway.here.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * ここにいるサーバー
 * メモデータクラス
 *
 * @version 1.0 新規作成
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class MemoData {

    /** メモID */
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @PrimaryKey private Long memoId;

    @Persistent private String title;

    @Persistent private String content;

    @Persistent Long updateTime;
}
