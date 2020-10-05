package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import connectors.PSQLUtils;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class ModifyInventory {

	private JFrame frame;
	private JTextField vinTxt;
	private JTextField makeTxt;
	private JTextField yearTxt;
	private JTextField colorTxt;
	private JTextField priceTxt;
	private String loggedInUserID;
	private String loggedInUsername;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModifyInventory window = new ModifyInventory();
					window.loggedInUserID = args[0];
					window.loggedInUsername = args[1];
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ModifyInventory() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 790, 361);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		PSQLUtils psql = new PSQLUtils();

		JButton backBtn = new JButton("Back");
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();

				//log action
				psql.logSession(Integer.parseInt(loggedInUserID), "FROM MOD INVENTORY TO INVENTORY");

				//conserve session
				String[] args = new String[] {loggedInUserID, loggedInUsername};
				Inventory.main(args);
			}
		});
		backBtn.setBounds(10, 11, 89, 23);
		frame.getContentPane().add(backBtn);

		JButton logOutBtn = new JButton("Log Out");
		logOutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();

				//Session store for activity LOG OUT
				psql.logSession(Integer.parseInt(loggedInUserID), "LOG OUT");

				Login.main(null);
			}
		});
		logOutBtn.setBounds(675, 11, 89, 23);
		frame.getContentPane().add(logOutBtn);

		JLabel lblVin = new JLabel("VIN :");
		lblVin.setBounds(10, 110, 101, 23);
		frame.getContentPane().add(lblVin);

		JLabel lblMake = new JLabel("MAKE :");
		lblMake.setBounds(10, 144, 101, 23);
		frame.getContentPane().add(lblMake);

		JLabel lblYear = new JLabel("YEAR :");
		lblYear.setBounds(10, 178, 101, 23);
		frame.getContentPane().add(lblYear);

		JLabel lblColor = new JLabel("COLOR :");
		lblColor.setBounds(10, 212, 101, 23);
		frame.getContentPane().add(lblColor);

		JLabel lblPrice = new JLabel("PRICE :");
		lblPrice.setBounds(10, 246, 101, 23);
		frame.getContentPane().add(lblPrice);

		vinTxt = new JTextField();
		vinTxt.setBounds(85, 111, 309, 20);
		frame.getContentPane().add(vinTxt);
		vinTxt.setColumns(10);

		makeTxt = new JTextField();
		makeTxt.setColumns(10);
		makeTxt.setBounds(85, 145, 309, 20);
		frame.getContentPane().add(makeTxt);

		yearTxt = new JTextField();
		yearTxt.setColumns(10);
		yearTxt.setBounds(85, 179, 309, 20);
		frame.getContentPane().add(yearTxt);

		colorTxt = new JTextField();
		colorTxt.setColumns(10);
		colorTxt.setBounds(85, 213, 309, 20);
		frame.getContentPane().add(colorTxt);

		priceTxt = new JTextField();
		priceTxt.setColumns(10);
		priceTxt.setBounds(85, 246, 309, 20);
		frame.getContentPane().add(priceTxt);

		JButton submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String vin = vinTxt.getText();
				String make = makeTxt.getText();
				String year = yearTxt.getText();
				String color = colorTxt.getText();
				String price = priceTxt.getText();
				
				boolean foundError = false;
				
				//Validate Fields In Succession
				//VIN Check
				if (vin == null || vin.length() != 13) {
					JOptionPane.showMessageDialog(null, "Invalid Vin. Must be 13 characters!", "Vin Error", JOptionPane.ERROR_MESSAGE);
					foundError = true;
				}
				
				//MAKE Check 
				if (make == null || make.equals("") || !make.matches("^[a-zA-Z]*$")) {
					JOptionPane.showMessageDialog(null, "Invalid Make. Make sure its not empty and has only alpha characters!", "Make Error", JOptionPane.ERROR_MESSAGE);
					foundError = true;
				}
				
				//YEAR Check 
				if(year == null || year.length() > 4 || !year.matches("[0-9]+")) {
					JOptionPane.showMessageDialog(null, "Invalid Year.", "Year Error", JOptionPane.ERROR_MESSAGE);
					foundError = true;
				}
				
				//COLOR Check 
				if (color == null || color.equals("") ||  !color.matches("^[a-zA-Z]*$")) {
					JOptionPane.showMessageDialog(null, "Invalid Color. Make sure its not empty and has only alpha characters!", "Color Error", JOptionPane.ERROR_MESSAGE);
					foundError = true;
				}
				
				//PRICE Check 
				if (price == null || price.equals("") || !price.matches("[0-9]+")) {
					JOptionPane.showMessageDialog(null, "Invalid Price. Has to be only numeric characters!", "Color Error", JOptionPane.ERROR_MESSAGE);
					foundError = true;
				}
				
				
				//Only if everything is right, try the insert
				if(!foundError) {
					try {
						long idInventoryTable = psql.insertIntoInventoryTableQuery(vin, make, year, color, price, loggedInUserID);
						if (idInventoryTable == -1) {
							System.out.println("Could not insert in DB!");
							JOptionPane.showMessageDialog(null, "Error inserting inventory in DB!", "Inventory Error", JOptionPane.ERROR_MESSAGE);
						} else {
							//Reached here means we can add this entry without any problems.
							System.out.println("Inserted at row id " + idInventoryTable + " at user table.");
							
							//Try to log these changes as Activity REGISTERED
							psql.logSession(Integer.parseInt(loggedInUserID), "INSERTED INVENTORY");
						
							JOptionPane.showMessageDialog(null, "Inventory Succesfully Inserted!", "Inventory Insert Success", JOptionPane.INFORMATION_MESSAGE);
							frame.dispose(); //Dispose
							
							//log action
							psql.logSession(Integer.parseInt(loggedInUserID), "FROM MOD INVENTORY TO INVENTORY");

							//conserve session
							String[] args = new String[] {loggedInUserID, loggedInUsername};
							Inventory.main(args);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		submitBtn.setBounds(649, 271, 115, 40);
		frame.getContentPane().add(submitBtn);

		JLabel lblTitle = new JLabel("Add Inventory");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(311, 15, 128, 14);
		frame.getContentPane().add(lblTitle);
		
		JLabel lblWarning = new JLabel("If needed to remove a record, please contact DB Admin.");
		lblWarning.setBounds(462, 114, 302, 23);
		frame.getContentPane().add(lblWarning);
	}
}
