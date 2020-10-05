package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import connectors.PSQLUtils;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Login {

	private JFrame frame;
	private static JTextField txtUsername;
	private static JPasswordField txtPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
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
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 564, 419);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);

		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(10, 263, 96, 28);
		frame.getContentPane().add(usernameLabel);

		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(10, 302, 96, 28);
		frame.getContentPane().add(passwordLabel);

		txtUsername = new JTextField();
		txtUsername.setBounds(116, 263, 383, 28);
		frame.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);

		JLabel title = new JLabel("Record View");
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(159, 11, 231, 68);
		frame.getContentPane().add(title);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(116, 302, 383, 28);
		frame.getContentPane().add(txtPassword);

		JLabel centerPicture = new JLabel("");
		centerPicture.setIcon(new ImageIcon("C:\\Users\\juanb\\Desktop\\Java Workspace\\recordsview\\img\\views\\mikecenter.png"));
		centerPicture.setBounds(126, 65, 319, 187);
		frame.getContentPane().add(centerPicture);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String password = txtPassword.getText();
				String username = txtUsername.getText();

				//Query for username and password
				PSQLUtils psql = new PSQLUtils();

				try {
					ResultSet rs = psql.executeGetQuery("SELECT * FROM public.\"user\" WHERE username = '" + username +"'");
					if(!rs.next()) {
						System.out.println("No results found for given query. Please check the code.");
						JOptionPane.showMessageDialog(null, "Invalid Username", "Login Error", JOptionPane.ERROR_MESSAGE);
					} else {
						String dbpass = rs.getString("password");
						if(dbpass.trim().equals(password.toString())) {
							frame.dispose();

							//Record Log in into sessions
							psql.logSession(Integer.parseInt(rs.getString("id")), "LOG IN");

							//Run Main
							String[] args = new String[] {rs.getString("id"), rs.getString("username")}; //Pass ID to main
							RecordMain.main(args);

						} else {
							JOptionPane.showMessageDialog(null, "Wrong Password!", "Login Error", JOptionPane.ERROR_MESSAGE);
						}
					}

				}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				txtPassword.setText(null);
				txtUsername.setText(null);
			}
		});
		btnLogin.setBounds(449, 346, 89, 23);
		frame.getContentPane().add(btnLogin);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtUsername.setText(null);
				txtPassword.setText(null);
			}
		});
		btnClear.setBounds(10, 346, 89, 23);
		frame.getContentPane().add(btnClear);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame frmLoginSystem = new JFrame("EXIT");
				if(JOptionPane.showConfirmDialog(frmLoginSystem, "Are you sure you want to exit?", "Login Systems",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION){
					System.exit(0);
				}
			}
		});
		btnExit.setBounds(0, 0, 62, 23);
		frame.getContentPane().add(btnExit);

		JButton registerBtn = new JButton("Register");
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				Register.main(null);
			}
		});
		registerBtn.setBounds(350, 346, 89, 23);
		frame.getContentPane().add(registerBtn);
	}
}
