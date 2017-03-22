import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ZipCodeDatabase {
    private ArrayList<ZipCode> zipCodes;

    public ZipCodeDatabase() {
        this.zipCodes = new ArrayList<>();
    }

    private static final int ZIP_CODE_NOT_FOUND = -1;

    private static final int EARTH_RADIUS = 3959;

    public ZipCode findZip(final int zip) throws Exception {
        ZipCode queriedZipCode = null;

        for (ZipCode zipCode : zipCodes) {
            if (zipCode.getZipCode() == zip) {
                queriedZipCode = zipCode;
            }
        }

        if (queriedZipCode == null) {
            throw new Exception("Zip code not found");
        }

        return queriedZipCode;
    }

    public int distance(final int zip1, final int zip2) {
        // convert both latitudes and longitudes from degrees to radians
        // p1 = cos(lat1) * cos(long1) * cos(lat2) * cos(long2)
        // p2 = cos(lat1) * sin(long1) * cos(lat2) * sin(long2)
        // p3 = sin(lat1) * sin(lat2)
        // distance = arccos(p1+p2+p3) * EARTH_RADIUS

        try {
            ZipCode zipCode1 = this.findZip(zip1);

            ZipCode zipCode2 = this.findZip(zip2);

            double point1 = Math.cos(zipCode1.getLatitude())
                * Math.cos(zipCode1.getLongitude())
                * Math.cos(zipCode2.getLatitude())
                * Math.cos(zipCode2.getLongitude());

            double point2 = Math.cos(zipCode1.getLatitude())
                * Math.sin(zipCode1.getLongitude())
                * Math.cos(zipCode2.getLatitude())
                * Math.sin(zipCode2.getLongitude());

            double point3 = Math.sin(zipCode1.getLatitude())
                * Math.sin(zipCode2.getLatitude());

            double distance = Math.acos(point1 + point2 + point3)
                * EARTH_RADIUS;

            return (int) distance;
        } catch (Exception e) {
            return ZIP_CODE_NOT_FOUND;
        }
    }

    public ArrayList<ZipCode> withinRadius(final int zip, final int radius) {
        ArrayList<ZipCode> queriedZipCodes = new ArrayList<>();

        for (ZipCode zipCode : this.zipCodes) {
            if (this.distance(zip, zipCode.getZipCode()) <= radius) {
                queriedZipCodes.add(zipCode);
            }
        }

        return queriedZipCodes;
    }

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
                int zipCode = inputFileStream.nextInt();
                String city = inputFileStream.next();
                String state = inputFileStream.next();
                double latitude = inputFileStream.nextDouble();
                double longitude = inputFileStream.nextDouble();

                this.zipCodes.add(
                    new ZipCode(zipCode, city, state, latitude, longitude)
                );
            }

            fileByteStream.close();

        // error while reading the file
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}