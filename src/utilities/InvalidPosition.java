package utilities;

public class InvalidPosition {

    private double latitude;
    private double longitude;
    private long timeStamp;
    private String description;

    public InvalidPosition(double latitude, double longitude, long timeStamp, String description) {
        setLatitude(latitude);
        setLongitude(longitude);
        setTimeStamp(timeStamp);
        setDescription(description);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setDescription(String description){this.description = description;}

    public String getDescription(){return this.description;}
}
