import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

/*************************************************************
 * GUI for a Zip Code Database
 *
 * @author Scott Grissom
 * @author (add your name as co-author)
 * @version October 7, 2015
 ************************************************************/
public class ZipCodeGUI extends JFrame implements ActionListener {

    /**
     * the database that does all the work
     */
    ZipCodeDatabase database;

    //FIX ME: define five buttons

    /**
     * Displays results in this text area
     */
    JTextArea results;

    // FIX ME: define four labels and two text fields
    // two text fields are declared for you but NOT instantiated below
    JTextField zip1;
    JTextField name;

    /**
     * menu items
     */
    JMenuBar menus;
    JMenu fileMenu;
    JMenuItem quitItem;
    JMenuItem openItem;

    /*****************************************************************
     * Main Method
     ****************************************************************/
    public static void main(String args[]) {
        ZipCodeGUI gui = new ZipCodeGUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setTitle("Zip Code Database");
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

        // create Results label
        loc = new GridBagConstraints();
        loc.gridx = 0;
        loc.gridy = 0;
        loc.insets.bottom = 20;
        add(new JLabel("Results"), loc);

        // create Choices label
        loc = new GridBagConstraints();
        loc.gridx = 1;
        loc.gridy = 0;
        loc.gridwidth = 2;
        add(new JLabel("Choices"), loc);
        loc = new GridBagConstraints();

        // FIX ME: create and display labels and textfields


        // FIX ME: create and display buttons


        // FIX ME: register the button action listeners 


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


    }

    /*****************************************************************
     * This method is called when any button is clicked.  The proper
     * internal method is called as needed.
     *
     * @param e the event that was fired
     ****************************************************************/
    public void actionPerformed(ActionEvent e) {

        // FIX ME: react to button presses and menu selections


    }

    /*****************************************************************
     * Search city and state for any match
     ****************************************************************/
    private void searchByName() {

        // retrieve the zip codes with the matching String
        ArrayList<ZipCode> list = database.search(name.getText());

        // dislay the results
        results.setText("city / states that contain '" + name.getText() + "'\n\n");
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
            // FIX ME: Use checkValidInteger to confirm valid entry

            // search for the zip code
            int z1 = Integer.parseInt(zip1.getText());
            ZipCode z = database.findZip(z1);

            // if no zip code found
            if (z == null)
                results.setText("no city found with zip code " + z1);
            else
                results.setText(z.toString());
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
    private boolean checkValidInteger(String numStr, String label) {
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