/**
 * ZipCode test class
 * @author Ben Payne
 * @version 2017.03.28
 */
public class ZipCodeTest {
    public static void main(final String[] args) {
        System.out.println("Testing started");

        ZipCodeDatabase db = new ZipCodeDatabase();

        ZipCode zipCode1 = new ZipCode(12345);

        ZipCode zipCode2 = new ZipCode(
            12345,
            "Test City",
            "Test State",
            1.1234,
            2.1234
        );

        System.out.println("Testing ZipCodeDatabase");

        db.readZipCodeData("zipcodes.txt");

        assert db.getZipCodes().size() > 19000 : "DB did not load";

        assert db.distance(49506, 49508) >= 5 :
            "ZipCode distance incorrect";

        assert db.withinRadius(49506, 100).size() >= 400 :
            "Radius number incorrect";

        System.out.println("Done");

        System.out.println("Testing ZipCode");

        assert zipCode1.getZipCode() == 12345 : "ZipCode code not set.";

        assert zipCode2.getZipCode() == 12345 : "ZipCode code not set.";

        assert zipCode2.getCity().equals("Test City") : "ZipCode city not set.";

        assert zipCode2.getState().equals("Test State") :
            "ZipCode state not set.";

        assert zipCode2.getLatitude() == 1.1234 : "ZipCode lat not set.";

        assert zipCode2.getLongitude() == 2.1234 : "ZipCode long not set.";

        System.out.println("Done");

        System.out.println("Testing completed. No errors found.");
    }
}
