public class ZipCode {
    private int zipCode;

    private String city;

    private String state;

    private double latitude;

    private double longitude;

    public ZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public ZipCode(
        int zipCode,
        String city,
        String state,
        double latitude,
        double longitude
    ) {
        this.zipCode = zipCode;

        this.city = city;

        this.state = state;

        this.latitude = latitude;

        this.longitude = longitude;
    }

    public String toString() {
        return this.city + ", " + this.state + " " + this.zipCode;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
}