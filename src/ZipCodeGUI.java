import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*************************************************************
 * GUI for a Zip Code Database.
 *
 * @author Scott Grissom
 * @author Ben Payne
 * @version 2017.03.23
 ************************************************************/
public class ZipCodeGUI extends JFrame implements ActionListener {

    /**
     * the database that does all the work
     */
    private ZipCodeDatabase database;

    /**
     * The action buttons
     */
    private JButton distanceButton;
    private JButton findButton;
    private JButton radiusButton;
    private JButton searchButton;
    private JButton furthestButton;

    /**
     * Displays results in this text area
     */
    private JTextArea results;

    /**
     * GUI labels
     */
    private JLabel zip1Label;
    private JLabel zip2Label;
    private JLabel radiusLabel;
    private JLabel nameLabel;

    /**
     * GUI Labels
     */
    private JTextField zip1Field;
    private JTextField zip2Field;
    private JTextField radiusField;
    private JTextField nameField;

    /**
     * menu items
     */
    private JMenuBar menus;
    private JMenu fileMenu;
    private JMenuItem quitItem;
    private JMenuItem openItem;

    private static final int LABEL_PADDING_BOTTOM = 20;

    private static final Insets TEXT_FIELD_PADDING =
        new Insets(5, 5, 5, 5);

    private static final Insets BUTTON_PADDING =
        new Insets(5, 5, 5, 5);

    private static final int TEXT_FIELD_COLUMNS = 10;

    /*****************************************************************
     * Main Method.
     * @param args
     ****************************************************************/
    public static void main(final String[] args) {
        ZipCodeGUI gui = new ZipCodeGUI();
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gui.setTitle("Ben Payne");
        gui.pack();
        gui.setVisible(true);
    }

    /*****************************************************************
     * constructor installs all of the GUI components
     ****************************************************************/
    public ZipCodeGUI() {
        // instantiate the analyzer and read the data file
        database = new ZipCodeDatabase();
        database.readZipCodeData("zipcodes.txt");

        // set the layout to GridBag
        setLayout(new GridBagLayout());
        GridBagConstraints loc;

        // create Results label
        loc = new GridBagConstraints();
        loc.gridx = 0;
        loc.gridy = 0;
        loc.insets.bottom = LABEL_PADDING_BOTTOM;
        add(new JLabel("Results"), loc);

        // create results area to span one column and 11 rows
        results = new JTextArea(20, 20);
        JScrollPane scrollPane = new JScrollPane(results);
        loc = new GridBagConstraints();
        loc.gridx = 0;
        loc.gridy = 1;
        loc.gridheight = 11;
        loc.insets.left = 20;
        loc.insets.right = 20;
        loc.insets.bottom = 20;
        add(scrollPane, loc);

        // create Choices label
        loc = new GridBagConstraints();
        loc.gridx = 1;
        loc.gridy = 0;
        loc.gridwidth = 2;
        loc.insets.bottom = 20;
        add(new JLabel("Choices"), loc);

        // Generate the form fields
        generateFields();

        // Generate the form buttons
        generateButtons();

        // register the button action listeners
        distanceButton.addActionListener((final ActionEvent event) ->
            calculateDistance()
        );

        findButton.addActionListener((final ActionEvent event) ->
            findZipCode()
        );

        radiusButton.addActionListener((final ActionEvent event) ->
            searchWithinRadius()
        );

        searchButton.addActionListener((final ActionEvent event) ->
            searchByZipCodeName()
        );

        furthestButton.addActionListener((final ActionEvent event) ->
            findFurthest()
        );

        // Create a File menu with two menu items
        fileMenu = new JMenu("File");
        quitItem = new JMenuItem("Quit");
        openItem = new JMenuItem("Open...");
        fileMenu.add(openItem);
        fileMenu.add(quitItem);
        menus = new JMenuBar();
        setJMenuBar(menus);
        menus.add(fileMenu);

        // Add menu item event listeners
        openItem.addActionListener((ActionEvent event) -> this.openFile());
        quitItem.addActionListener((ActionEvent evt) -> System.exit(0));
    }

    /**
     * Calculate the distance between Zip 1 and 2
     */
    private void calculateDistance() {
        try {
            results.setText("Calculating...");
            results.paintImmediately(results.getVisibleRect());

            int zip1 = this.checkValidInteger(zip1Field.getText());
            int zip2 = this.checkValidInteger(zip2Field.getText());

            ZipCode zipCode1 = database.findZip(zip1);
            ZipCode zipCode2 = database.findZip(zip2);

            int distance = database.distance(
                zipCode1.getZipCode(),
                zipCode2.getZipCode()
            );

            String result = "The distance between" + System.lineSeparator();
            result += zipCode1 + " and " + System.lineSeparator();
            result += zipCode2 + " is ";
            result += distance + " miles.";

            results.setText(result);
            System.out.println("Finished");
        } catch (ZipCodeNotFoundException e) {
            results.setText(e.getMessage());
        } catch (Exception e) {
            results.setText("An error has occured.");
        }
    }

    /**
     * Find the zip code data by Zip Code
     */
    private void findZipCode() {
        try {
            int zip = this.checkValidInteger(zip1Field.getText());

            ZipCode zipCode = database.findZip(zip);

            results.setText(zipCode.toString());
        } catch (ZipCodeNotFoundException e) {
            results.setText(e.getMessage());
        } catch (Exception e) {
            results.setText("An error has occurred.");
        }
    }

    /**
     * Find all zip codes within a certain radius
     */
    private void searchWithinRadius() {
        try {
            results.setText("Searching...");
            results.paintImmediately(results.getVisibleRect());

            final int zip1 = this.checkValidInteger(zip1Field.getText());

            final int radius = this.checkValidInteger(radiusField.getText());

            ArrayList<ZipCode> zipCodes = database.withinRadius(
                zip1,
                radius
            );

            String result = "";

            result += "Zip codes within " + radius + " miles of ";
            result += zip1 + "." + System.lineSeparator();

            for (ZipCode zipCode : zipCodes) {
                result += zipCode + System.lineSeparator();
            }

            result += "Total: " + zipCodes.size();

            results.setText(result);
        } catch (Exception e) {
            results.setText("Zip code not found.");
        }
    }

    private void searchByZipCodeName() {
        try {
            results.setText("Searching...");
            results.paintImmediately(results.getVisibleRect());

            String name = nameField.getText();

            ArrayList<ZipCode> zipCodes = database.search(name);

            String output = "";

            output += "ZipCodes that contain '" + name + "':";
            output += System.lineSeparator();

            for (ZipCode zipCode : zipCodes) {
                output += zipCode + System.lineSeparator();
            }

            output += "Total: " + zipCodes.size();

            results.setText(output);
        } catch (Exception e) {
            results.setText(e.getMessage());
        }
    }

    /**
     * Find the furthest Zip Code away from Zip Code 1
     */
    private void findFurthest() {
        try {
            results.setText("Calculating...");
            results.paintImmediately(results.getVisibleRect());

            int zip1 = this.checkValidInteger(zip1Field.getText());

            ZipCode zipCode = database.findZip(zip1);

            ZipCode farthestZipCode = database.findFurthest(zip1);

            int distance = database.distance(
                zip1,
                farthestZipCode.getZipCode()
            );

            String output = "The distance between" + System.lineSeparator();
            output += zipCode + " and" + System.lineSeparator();
            output += farthestZipCode + " is " + distance + " miles";

            results.setText(output);
        } catch (ZipCodeNotFoundException e) {
            results.setText(e.getMessage());
        } catch (Exception e) {
            results.setText("An error has occured.");
        }
    }

    /**
     * Generate the for fields
     */
    private void generateFields() {
        GridBagConstraints loc;// Zip Code 1 label
        loc = new GridBagConstraints();
        loc.gridx = 1;
        loc.gridy = 2;
        loc.insets = new Insets(0, 0, 0, 1);
        zip1Label = new JLabel("Zip 1");
        add(zip1Label, loc);

        // Zip Code 1 Text Field
        loc = new GridBagConstraints();
        loc.gridx = 2;
        loc.gridy = 2;
        loc.insets = TEXT_FIELD_PADDING;
        zip1Field = new JTextField(TEXT_FIELD_COLUMNS);
        add(zip1Field, loc);

        // Zip Code 2 label
        loc = new GridBagConstraints();
        loc.gridx = 1;
        loc.gridy = 3;
        loc.insets = new Insets(0, 0, 0, 1);
        zip2Label = new JLabel("Zip 2");
        add(zip2Label, loc);

        // Zip Code 2 Text Field
        loc = new GridBagConstraints();
        loc.gridx = 2;
        loc.gridy = 3;
        loc.insets = TEXT_FIELD_PADDING;
        zip2Field = new JTextField(TEXT_FIELD_COLUMNS);
        add(zip2Field, loc);

        // Radius Label
        loc = new GridBagConstraints();
        loc.gridx = 1;
        loc.gridy = 4;
        loc.insets = new Insets(0, 0, 0, 1);
        radiusLabel = new JLabel("radius");
        add(radiusLabel, loc);

        // Radius field
        loc = new GridBagConstraints();
        loc.gridx = 2;
        loc.gridy = 4;
        loc.insets = TEXT_FIELD_PADDING;
        radiusField = new JTextField(TEXT_FIELD_COLUMNS);
        add(radiusField, loc);

        // Name label
        loc = new GridBagConstraints();
        loc.gridx = 1;
        loc.gridy = 5;
        loc.insets = new Insets(0, 0, 0, 1);
        nameLabel = new JLabel("Name");
        add(nameLabel, loc);

        // Name field
        loc = new GridBagConstraints();
        loc.gridx = 2;
        loc.gridy = 5;
        loc.insets = TEXT_FIELD_PADDING;
        nameField = new JTextField(TEXT_FIELD_COLUMNS);
        add(nameField, loc);
    }

    /**
     * Generate the form buttons
     */
    private void generateButtons() {
        GridBagConstraints loc;

        // Distance between Zip 1 and 2
        loc = new GridBagConstraints();
        loc.gridx = 2;
        loc.gridy = 6;
        loc.gridwidth = 2;
        loc.insets = BUTTON_PADDING;
        distanceButton = new JButton("Distance between Zip 1 and 2");
        add(distanceButton, loc);

        // Find zip 1
        loc = new GridBagConstraints();
        loc.gridx = 2;
        loc.gridy = 7;
        loc.gridwidth = 2;
        loc.insets = BUTTON_PADDING;
        findButton = new JButton("Find Zip 1");
        add(findButton, loc);

        // Find a Zip Codes within radius of Zip 1
        loc = new GridBagConstraints();
        loc.gridx = 2;
        loc.gridy = 8;
        loc.gridwidth = 2;
        loc.insets = BUTTON_PADDING;
        radiusButton = new JButton("Within radius of Zip 1");
        add(radiusButton, loc);

        // Search zip codes by name.
        loc = new GridBagConstraints();
        loc.gridx = 2;
        loc.gridy = 9;
        loc.gridwidth = 2;
        loc.insets = BUTTON_PADDING;
        searchButton = new JButton("Search by name");
        add(searchButton, loc);

        // Find the farthest zip codes
        loc = new GridBagConstraints();
        loc.gridx = 2;
        loc.gridy = 10;
        loc.gridwidth = 2;
        loc.insets = BUTTON_PADDING;
        furthestButton = new JButton("Furthest from Zip 1");
        add(furthestButton, loc);
    }

    /*****************************************************************
     * open a data file with the name selected by the user
     ****************************************************************/
    private void openFile() {

        // create File Chooser so that it starts at the current directory
        String userDir = System.getProperty("user.dir");
        JFileChooser fc = new JFileChooser(userDir);

        // show File Chooser and wait for user selection
        int returnVal = fc.showOpenDialog(this);

        // did the user select a file?
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filename = fc.getSelectedFile().getName();
            database.readZipCodeData(filename);
        }
    }

    /*****************************************************************
     * Check if the String contains a valid integer.  Display
     * an appropriate warning if it is not valid.
     *
     * @param numStr - the String to be checked
     * @return true if valid
     ****************************************************************/
    private int checkValidInteger(final String numStr) {
        try {
            return Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter an integer");

            return -1;
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            throw new Exception("Avoid god methods.");
        } catch (Exception e) {
            results.setText(e.getMessage());
        }
    }
}
