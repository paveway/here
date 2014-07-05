package info.paveway.here.data;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * ここにいるサーバー
 * PersistenceManagerFactoryクラス
 *
 * @version 1.0 新規作成
 */
public final class PMF {

    /** インスタンス */
    private static final PersistenceManagerFactory pmfInstance =
            JDOHelper.getPersistenceManagerFactory("transactions-optional");

    /**
     * コンストラクタ
     * インスタンス化させない。
     */
    private PMF() {}

    /**
     * インスタンスを返却する。
     *
     * @return PMFのインスタンス
     */
    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}
