package info.paveway.here;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class LocationData {

    @PrimaryKey private String userId;
    @Persistent private Long   roomNo;
    @Persistent private String nickname;
    @Persistent private String latitude;
    @Persistent private String longitude;

    public LocationData() {
    }

    public LocationData(String userId, Long roomNo, String nickname, String latitude, String longitude) {
        this.userId    = userId;
        this.roomNo    = roomNo;
        this.nickname  = nickname;
        this.latitude  = latitude;
        this.longitude = longitude;
    }

    public void setUserId(   String userId   ) { this.userId    = userId;    }
    public void setRoomNo(   Long   roomNo   ) { this.roomNo    = roomNo;    }
    public void setNickname( String nickname ) { this.nickname  = nickname;  }
    public void setLatitude( String latitude ) { this.latitude  = latitude;  }
    public void setLongitude(String longitude) { this.longitude = longitude; }
    public String getUserId()    { return userId;    }
    public Long   getRoomNo()    { return roomNo;    }
    public String getNickname()  { return nickname;  }
    public String getLatitude()  { return latitude;  }
    public String getLongitude() { return longitude; }
}
