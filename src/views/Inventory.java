package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import connectors.PSQLUtils;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Inventory {

	private JFrame frame;
	private String loggedInUserID;
	private String loggedInUsername;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Inventory window = new Inventory();
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
	public Inventory() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 850, 663);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		PSQLUtils psql = new PSQLUtils();

		JButton logoutBtn = new JButton("Log Out");
		logoutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();

				//Session store for activity LOG OUT
				psql.logSession(Integer.parseInt(loggedInUserID), "LOG OUT");

				Login.main(null);
			}
		});
		logoutBtn.setBounds(735, 11, 89, 23);
		frame.getContentPane().add(logoutBtn);

		JButton backBtn = new JButton("Back");
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				
				//log action
				psql.logSession(Integer.parseInt(loggedInUserID), "FROM INVENTORY TO MAIN");

				//conserve session
				String[] args = new String[] {loggedInUserID, loggedInUsername};
				RecordMain.main(args);
			}
		});
		backBtn.setBounds(10, 11, 89, 23);
		frame.getContentPane().add(backBtn);

		JLabel inventoryLbl = new JLabel("Inventory");
		inventoryLbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		inventoryLbl.setBounds(10, 45, 235, 46);
		frame.getContentPane().add(inventoryLbl);

		table = new JTable();
		table.setBounds(10, 102, 814, 396);
		frame.getContentPane().add(table);

		DefaultTableModel model = new DefaultTableModel();
		String[] columnNames = {"VIN", "MAKE", "YEAR", "COLOR", "PRICE"};
		model.setColumnIdentifiers(columnNames);

		table.setModel(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		JButton modInvBtn = new JButton("Modify Inventory");
		modInvBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				
				String[] args = new String[] {loggedInUserID, loggedInUsername};
				ModifyInventory.main(args);
			}
		});
		modInvBtn.setBounds(669, 509, 155, 31);
		frame.getContentPane().add(modInvBtn);
		
		JButton refreshTableBtn = new JButton("Refresh Table");
		refreshTableBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				
				String[] args = new String[] {loggedInUserID, loggedInUsername};
				Inventory.main(args);
			}
		});
		refreshTableBtn.setBounds(10, 509, 155, 31);
		frame.getContentPane().add(refreshTableBtn);

		String vinnum = "";
		String make = "";
		String year = "";
		String color = "";
		Integer price = 0;
		
		try {
			//query inventory and fill table
			ResultSet rs = psql.executeGetQuery("SELECT * FROM public.inventory");
			int i = 0;
			//insert column names
			model.addRow(new Object[] {"VIN", "MAKE", "YEAR", "COLOR", "PRICE"});
			
			while (rs.next()) {
				vinnum = rs.getString("vin_num");
				make = rs.getString("make");
				year = rs.getString("year");
				color = rs.getString("color");
				price = rs.getInt("price");
				model.addRow(new Object[] {vinnum, make, year, color, price});
				i++;
			}
			if(i < 1) {
				JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);
			} else if (i > 1) {
				System.out.println(i + " Records Found");
			} else {
				System.out.println("1 Record Found");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
