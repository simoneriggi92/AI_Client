package utilities;

public class InvalidPosition {

    private double latitude;
    private double longitude;
    private long timestamp;
    private String description;

    public InvalidPosition(){ }

    public InvalidPosition(double latitude, double longitude, long timestamp, String description) {
        setLatitude(latitude);
        setLongitude(longitude);
        setTimestamp(timestamp);
        setDescription(description);
    }


    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setDescription(String description){this.description = description;}

    public String getDescription(){return this.description;}
}
