/**
 * ZipCode wrapper class.
 * @author Ben Payne
 * @version 2017.03.28
 */
public class ZipCode {
    private int zipCode;

    private String city;

    private String state;

    private double latitude;

    private double longitude;

    /**
     * Initialize a new ZipCode
     * @param zipCode - the zip code to use.
     */
    public ZipCode(final int zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * ZipCode constructor.
     * @param zipCode - Zip code
     * @param city - City
     * @param state - State
     * @param latitude - Lat
     * @param longitude - Long
     */
    public ZipCode(
        final int zipCode,
        final String city,
        final String state,
        final double latitude,
        final double longitude
    ) {
        this.zipCode = zipCode;

        this.city = city;

        this.state = state;

        this.latitude = latitude;

        this.longitude = longitude;
    }

    /**
     * Overload the prototype.
     * @return String
     */
    public String toString() {
        return this.city + ", " + this.state + " " + this.zipCode;
    }

    /**
     * Get the actual zip code.
     * @return int
     */
    public int getZipCode() {
        return zipCode;
    }

    /**
     * Set the zip code.
     * @param zipCode - the zip code to use.
     */
    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Get the associated city.
     * @return - The Zip Codes City.
     */
    public String getCity() {
        return city;
    }

    /**
     * Set the associated city.
     * @param city - The city name to use.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get the associate state.
     * @return String
     */
    public String getState() {
        return state;
    }

    /**
     * Set the state to use.
     * @param state - The state name to use.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Get the ZipCodes latitude in degrees.
     * @return - the associated latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Set the ZipCode latitude.
     * @param latitude - the latitude to use.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Get the ZipCodes longitude.
     * @return - the assocaited longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Set the ZipCode longitude.
     * @param longitude - the longitude to use.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}