import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Zip Code Database class
 * @author Ben Payne
 * @version 2017.03.28
 */
public class ZipCodeDatabase {
    private ArrayList<ZipCode> zipCodes;

    private static final int ZIP_CODE_NOT_FOUND = -1;

    private static final int EARTH_RADIUS = 3959;

    /**
     * Instantiate a new ZipCodeDatabase
     */
    public ZipCodeDatabase() {
        this.zipCodes = new ArrayList<>();
    }

    /**
     * Find a ZipCode by a zip.
     * @param zip - a zip to find.
     * @return ZipCode
     * @throws ZipCodeNotFoundException - throw if no zip code found.
     */
    public ZipCode findZip(final int zip) throws ZipCodeNotFoundException {
        ZipCode queriedZipCode = null;

        for (ZipCode zipCode : zipCodes) {
            if (zipCode.getZipCode() == zip) {
                queriedZipCode = zipCode;
            }
        }

        if (queriedZipCode == null) {
            throw new ZipCodeNotFoundException("Zip code " + zip + " not found");
        }

        return queriedZipCode;
    }

    /**
     * Calculate the distance between two zip codes.
     * @param zip1 - the base zip.
     * @param zip2 - the long zip.
     * @return int
     */
    public int distance(final int zip1, final int zip2) {
        try {
            ZipCode zipCode1 = this.findZip(zip1);

            ZipCode zipCode2 = this.findZip(zip2);

            // Calculate each point by first converting degrees -> radians
            double point1 = Math.cos(Math.toRadians(zipCode1.getLatitude()))
                * Math.cos(Math.toRadians(zipCode1.getLongitude()))
                * Math.cos(Math.toRadians(zipCode2.getLatitude()))
                * Math.cos(Math.toRadians(zipCode2.getLongitude()));

            double point2 = Math.cos(Math.toRadians(zipCode1.getLatitude()))
                * Math.sin(Math.toRadians(zipCode1.getLongitude()))
                * Math.cos(Math.toRadians(zipCode2.getLatitude()))
                * Math.sin(Math.toRadians(zipCode2.getLongitude()));

            double point3 = Math.sin(Math.toRadians(zipCode1.getLatitude()))
                * Math.sin(Math.toRadians(zipCode2.getLatitude()));

            double distance = Math.acos(point1 + point2 + point3)
                * EARTH_RADIUS;

            return (int) distance;
        } catch (ZipCodeNotFoundException e) {
            return ZIP_CODE_NOT_FOUND;
        }
    }

    /**
     * Find all zip codes within a radius of a base Zip Code.
     * @param zip - the base zip.
     * @param radius - the radius to search withing.
     * @return ArrayList
     */
    public ArrayList<ZipCode> withinRadius(final int zip, final int radius) {
        ArrayList<ZipCode> queriedZipCodes = new ArrayList<>();

        for (ZipCode zipCode : this.zipCodes) {
            if (this.distance(zip, zipCode.getZipCode()) <= radius) {
                queriedZipCodes.add(zipCode);
            }
        }

        return queriedZipCodes;
    }

    /**
     * Find the furthest zip code from a base zip.
     * @param zip - the base zip.
     * @return ZipCode
     */
    public ZipCode findFurthest(final int zip) {
        ZipCode farthestZipCode = this.zipCodes.get(0);

        int furthestDistance = distance(zip, farthestZipCode.getZipCode());

        for (ZipCode zipCode : zipCodes) {
            int distance = this.distance(zip, farthestZipCode.getZipCode());

            if (distance > furthestDistance) {
                farthestZipCode = zipCode;

                furthestDistance = distance;
            }
        }

        return farthestZipCode;
    }

    /**
     * Search for a zip code by name.
     * @param query - the name to search for.
     * @return ArrayList
     */
    public ArrayList<ZipCode> search(final String query) {
        ArrayList<ZipCode> queriedZipCodes = new ArrayList<>();

        for (ZipCode zipCode : zipCodes) {
            if (zipCode.getCity().contains(query)
                || zipCode.getState().contains(query)
            ) {
                queriedZipCodes.add(zipCode);
            }
        }

        return queriedZipCodes;
    }

    /**
     * Read the provided database file.
     * @param filename - the filename of the data file.
     */
    public void readZipCodeData(final String filename) {
        Scanner inputFileStream = null;
        FileInputStream fileByteStream = null;

        try {
            // open the File and set delimiters
            fileByteStream = new FileInputStream(filename);
            inputFileStream = new Scanner(fileByteStream);
            inputFileStream.useDelimiter("[,\r\n]+");

            // continue while there is more data to read
            while (inputFileStream.hasNext()) {
                // read five data elements
                int zip          = inputFileStream.nextInt();
                String city      = inputFileStream.next();
                String state     = inputFileStream.next();
                double latitude  = inputFileStream.nextDouble();
                double longitude = inputFileStream.nextDouble();

                ZipCode zipCode = new ZipCode(
                    zip,
                    city,
                    state,
                    latitude,
                    longitude
                );

                this.zipCodes.add(zipCode);
            }

            fileByteStream.close();

        // error while reading the file
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Get the db ZipCodes
     * @return ArrayList
     */
    public ArrayList<ZipCode> getZipCodes() {
        return zipCodes;
    }
}
