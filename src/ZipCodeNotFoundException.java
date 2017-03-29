/**
 * Handle zip code exceptions.
 * @author Ben Payne
 * @version 2017.03.28
 */
public class ZipCodeNotFoundException extends Exception {
    public ZipCodeNotFoundException () {
        super("Zip Code Not Found");
    }

    public ZipCodeNotFoundException (final String message) {
        super(message);
    }
}
