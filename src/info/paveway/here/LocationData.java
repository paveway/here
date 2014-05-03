package info.paveway.here;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class LocationData {

    @PrimaryKey private String id;
    @Persistent private String latitude;
    @Persistent private String longitude;

    public LocationData(String id, String latitude, String longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setId(String id) { this.id = id; }
    public void setLatitude(String latitude) { this.latitude = latitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }
    public String getId() { return id; }
    public String getLatitude() { return latitude; }
    public String getLongitude() { return longitude; }
}
