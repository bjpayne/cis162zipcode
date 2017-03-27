import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

        generateFields();

        generateButtons();

        // register the button action listeners
        distanceButton.addActionListener((final ActionEvent event) ->
            calculateDistance()
        );

        findButton.addActionListener((final ActionEvent event) -> {
            findZipCode();
        });

        radiusButton.addActionListener((final ActionEvent event) -> {
            searchWithinRadius();
        });

        searchButton.addActionListener((final ActionEvent event) -> {
            try {

            } catch (Exception e) {

            }
        });
        // furthestButton.addActionListener(this);

        // Create a File menu with two menu items
        fileMenu = new JMenu("File");
        quitItem = new JMenuItem("Quit");
        openItem = new JMenuItem("Open...");
        fileMenu.add(openItem);
        fileMenu.add(quitItem);
        menus = new JMenuBar();
        setJMenuBar(menus);
        menus.add(fileMenu);

        // FIX ME: set the action listeners for the menu items
        quitItem.addActionListener(this);
        openItem.addActionListener(this);
    }

    private void findZipCode() {
        try {
            int zip = Integer.parseInt(zip1Field.getText());

            ZipCode zipCode = database.findZip(zip);

            results.setText(zipCode.toString());
        } catch (Exception e) {
            results.setText(e.getMessage());
        }
    }

    private void searchWithinRadius() {
        try {
            results.setText("Searching...");
            results.paintImmediately(results.getVisibleRect());

            final int zip1 = Integer.parseInt(zip1Field.getText());

            final int radius = Integer.parseInt(radiusField.getText());

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

    private void calculateDistance() {
        try {
            System.out.println("Running...");
            int zip1 = Integer.parseInt(zip1Field.getText());
            int zip2 = Integer.parseInt(zip2Field.getText());

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
        } catch (Exception e) {
            results.setText(e.getMessage());
        }
    }

    private void generateButtons() {
        GridBagConstraints loc;

        loc = new GridBagConstraints();
        loc.gridx = 2;
        loc.gridy = 6;
        loc.gridwidth = 2;
        loc.insets = BUTTON_PADDING;
        distanceButton = new JButton("Distance between Zip 1 and 2");
        add(distanceButton, loc);

        loc = new GridBagConstraints();
        loc.gridx = 2;
        loc.gridy = 7;
        loc.gridwidth = 2;
        loc.insets = BUTTON_PADDING;
        findButton = new JButton("Find Zip 1");
        add(findButton, loc);

        loc = new GridBagConstraints();
        loc.gridx = 2;
        loc.gridy = 8;
        loc.gridwidth = 2;
        loc.insets = BUTTON_PADDING;
        radiusButton = new JButton("Within radius of Zip 1");
        add(radiusButton, loc);

        loc = new GridBagConstraints();
        loc.gridx = 2;
        loc.gridy = 9;
        loc.gridwidth = 2;
        loc.insets = BUTTON_PADDING;
        searchButton = new JButton("Search by name");
        add(searchButton, loc);

        loc = new GridBagConstraints();
        loc.gridx = 2;
        loc.gridy = 10;
        loc.gridwidth = 2;
        loc.insets = BUTTON_PADDING;
        furthestButton = new JButton("Furthest from Zip 1");
        add(furthestButton, loc);
    }

    /*****************************************************************
     * This method is called when any button is clicked.  The proper
     * internal method is called as needed.
     *
     * @param e the event that was fired
     ****************************************************************/
    public void actionPerformed(final ActionEvent e) {
        System.out.println(e.getSource());
    }

    /*****************************************************************
     * Search city and state for any match
     ****************************************************************/
    private void searchByName() {

        // retrieve the zip codes with the matching String
        ArrayList<ZipCode> list = database.search(nameField.getText());

        // dislay the results
        results.setText("city / states that contain '" + nameField.getText() + "'\n\n");
        for (ZipCode z : list) {
            results.append(z + "\n");
        }
        results.append("\nTotal: " + list.size());
    }

    /*****************************************************************
     * find a zip code
     ****************************************************************/
    private void findZip() {
        try {
            if (checkValidInteger(zip1Field.getText(), "Zip 1")) {
                int z1 = Integer.parseInt(zip1Field.getText());

                // search for the zip code
                ZipCode z = database.findZip(z1);

                // if no zip code found
                if (z == null) {
                    results.setText("no city found with zip code " + z1);
                } else {
                    results.setText(z.toString());
                }
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
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
     * @param label - the textfield name that contains the String
     * @return true if valid
     ****************************************************************/
    private boolean checkValidInteger(final String numStr, final String label) {
        boolean isValid = true;
        try {
            int val = Integer.parseInt(numStr);

            // display error message if not a valid integer    
        } catch (NumberFormatException e) {
            isValid = false;
            JOptionPane.showMessageDialog(this, "Enter an integer in " + label);

        }
        return isValid;
    }
}
