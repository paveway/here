package info.paveway.here;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * �g�p���f�[�^�N���X
 *
 * @version 1.0 �V�K�쐬
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UsedData {

    /** ID */
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @PrimaryKey private Long id;

    /** �����ԍ� */
    @Persistent private Long roomNo;

    /** ���[�U */
    @Persistent private String userId;

    /**
     * �R���X�g���N�^
     *
     * @param roomNo �����ԍ�
     * @param userId ���[�U�[ID
     */
    public UsedData(Long roomNo, String userId) {
        this.roomNo = roomNo;
        this.userId = userId;
    }

    public void setId(    Long id      ) { this.id     = id;     }
    public void setRoomNo(Long roomNo  ) { this.roomNo = roomNo; }
    public void setUserId(String userId) { this.userId = userId; }
    public Long getId()       { return id;     }
    public Long getRoomNo()   { return roomNo; }
    public String getUserId() { return userId; }
}
