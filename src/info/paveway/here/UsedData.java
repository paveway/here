package info.paveway.here;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UsedData {

    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @PrimaryKey private Long id;
    @Persistent private Long roomNo;
    @Persistent private String userId;

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
