package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import connectors.PSQLUtils;

import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class Register {

	private JFrame frame;
	private JTextField newUsernameTxt;
	private JPasswordField newPasswordTxt;
	private JPasswordField repeatPasswordTxt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register window = new Register();
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
	public Register() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 671, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);

		JLabel registerLabel = new JLabel("Register");
		registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		registerLabel.setBounds(245, 11, 157, 36);
		frame.getContentPane().add(registerLabel);

		JLabel recruiterImg = new JLabel("");
		recruiterImg.setIcon(new ImageIcon("C:\\Users\\juanb\\Desktop\\Java Workspace\\recordsview\\img\\views\\WelcomeToTheGang.png"));
		recruiterImg.setBounds(10, 58, 377, 217);
		frame.getContentPane().add(recruiterImg);

		JLabel onlyTheBestLabel = new JLabel("Only the best please!");
		onlyTheBestLabel.setHorizontalAlignment(SwingConstants.CENTER);
		onlyTheBestLabel.setBounds(437, 140, 157, 36);
		frame.getContentPane().add(onlyTheBestLabel);

		JLabel newUsernameLabel = new JLabel("New Username:");
		newUsernameLabel.setBounds(10, 298, 114, 14);
		frame.getContentPane().add(newUsernameLabel);

		JLabel newPasswordLabel1 = new JLabel("New Password:");
		newPasswordLabel1.setBounds(10, 359, 114, 14);
		frame.getContentPane().add(newPasswordLabel1);

		JLabel repeatPasswordLabel = new JLabel("Repeat Password:");
		repeatPasswordLabel.setBounds(10, 421, 114, 14);
		frame.getContentPane().add(repeatPasswordLabel);

		newUsernameTxt = new JTextField();
		newUsernameTxt.setBounds(134, 295, 370, 28);
		frame.getContentPane().add(newUsernameTxt);
		newUsernameTxt.setColumns(10);

		JButton registerBtn = new JButton("Register");
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String newUsername = newUsernameTxt.getText();
				String newPassword = newPasswordTxt.getText();
				String repeatPassword = repeatPasswordTxt.getText();

				PSQLUtils psql = new PSQLUtils();

				//Check if username exists already and prompt the right error message
				try {
					ResultSet rs = psql.executeGetQuery("SELECT * FROM public.\"user\" WHERE username = '" + newUsername +"'");
					if(rs.next()) {
						System.out.println("Username already exists... Found result from DB containing that user");
						JOptionPane.showMessageDialog(null, "Username taken bud!", "Register Error", JOptionPane.ERROR_MESSAGE);
					} else {
						//Check if repeated passwords match and prompt the right error message
						if (!newPassword.equals(repeatPassword.toString())) {
							JOptionPane.showMessageDialog(null, "Passwords don't match!", "Register Error", JOptionPane.ERROR_MESSAGE);
						} else {
							//If we reach here, passwords match and user name is not taken; commence registry.
							try {
								long id_usertable = psql.insertIntoUserTableQuery(newUsername, newPassword);
								if (id_usertable == -1) {
									System.out.println("Could not insert in DB!");
									JOptionPane.showMessageDialog(null, "Error registering user in DB!", "Register Error", JOptionPane.ERROR_MESSAGE);
								} else {
									//Reached here means we can add this entry without any problems.
									System.out.println("Inserted at row id " + id_usertable + " at user table.");
									newUsernameTxt.setText("");
									newPasswordTxt.setText("");
									repeatPasswordTxt.setText("");
									
									//Try to log these changes as Activity REGISTERED
									psql.logSession((int)id_usertable, "REGISTERED");
								
									JOptionPane.showMessageDialog(null, "User Registered Successfully. Please go back to Main Menu and Login!", "Register Success", JOptionPane.INFORMATION_MESSAGE);
									frame.dispose(); //Dispose
									Login.main(null); //Go to Login Automatically
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		registerBtn.setBounds(514, 380, 131, 66);
		frame.getContentPane().add(registerBtn);

		JButton backMainBtn = new JButton("Back to Menu");
		backMainBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				Login.main(null);
			}
		});
		backMainBtn.setBounds(514, 294, 131, 66);
		frame.getContentPane().add(backMainBtn);
		
		newPasswordTxt = new JPasswordField();
		newPasswordTxt.setBounds(134, 352, 370, 28);
		frame.getContentPane().add(newPasswordTxt);
		
		repeatPasswordTxt = new JPasswordField();
		repeatPasswordTxt.setBounds(134, 403, 370, 28);
		frame.getContentPane().add(repeatPasswordTxt);
	}
}
