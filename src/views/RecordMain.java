package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import connectors.PSQLUtils;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class RecordMain {

	private JFrame frame;
	private String loggedInUserID;
	private String loggedInUsername;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RecordMain window = new RecordMain();
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
	public RecordMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 858, 355);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		
		JLabel userLbl = new JLabel("Welcome " + loggedInUsername + "!");
		userLbl.setHorizontalAlignment(SwingConstants.LEFT);
		userLbl.setBounds(10, 11, 256, 23);
		frame.getContentPane().add(userLbl);
		
		JButton logoutBtn = new JButton("Log Out");
		logoutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				
				//Session store for activity LOG OUT
				PSQLUtils psql = new PSQLUtils();
				psql.logSession(Integer.parseInt(loggedInUserID), "LOG OUT");
				
				Login.main(null);
			}
		});
		logoutBtn.setBounds(746, 11, 89, 23);
		frame.getContentPane().add(logoutBtn);
		
		JLabel whatDoYouNeedLbl = new JLabel("What do you need?");
		whatDoYouNeedLbl.setFont(new Font("Tahoma", Font.PLAIN, 25));
		whatDoYouNeedLbl.setBounds(10, 60, 236, 34);
		frame.getContentPane().add(whatDoYouNeedLbl);
		
		JButton seeInventoryBtn = new JButton("See Inventory");
		seeInventoryBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				
				PSQLUtils psql = new PSQLUtils();
				psql.logSession(Integer.parseInt(loggedInUserID), "SEE INVENTORY");
				
				String[] args = new String[] {loggedInUserID, loggedInUsername};
				Inventory.main(args);
			}
		});
		seeInventoryBtn.setBounds(10, 128, 160, 42);
		frame.getContentPane().add(seeInventoryBtn);
		
		JButton nextAppointmentsBtn = new JButton("Appointments");
		nextAppointmentsBtn.setBounds(10, 215, 160, 42);
		frame.getContentPane().add(nextAppointmentsBtn);
	}
}
