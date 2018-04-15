package com.tqbdev.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import com.tqbdev.components.StudentTableModel;
import com.tqbdev.components.TableCellRenderer;
import com.tqbdev.components.TableMouseListener;
import com.tqbdev.funcs.DatabaseConnection;
import com.tqbdev.funcs.Dialog;
import com.tqbdev.funcs.Utils;
import com.tqbdev.model.HocSinh;

public class MainScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3957854703411452939L;
	private JPanel contentPane;
	private JTable tableStudent;
	private JFrame thisFrame = this;

	private DatabaseConnection connection = new DatabaseConnection();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception weTried) {
				}

				try {
					MainScreen frame = new MainScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainScreen() {
		setIconImage((new ImageIcon(getClass().getResource("/icons/main.png"))).getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		while (getConnect(true) == false);
		Dialog.showInform(this, "Connection successful", "Connection...");

		contentPane.add(prepareButtons(), BorderLayout.PAGE_START);

		tableStudent = createTable();
		JScrollPane tableScroll = new JScrollPane(tableStudent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScroll.getViewport().setBackground(Color.WHITE);

		contentPane.add(tableScroll);
		loadStudentTable();
		
		setJMenuBar(prepareMenuBar());
	}

	private JMenuBar prepareMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("Settings");
		menu.setMnemonic(KeyEvent.VK_S);
		menu.getAccessibleContext().setAccessibleDescription("Setting configure connection to database");
		menuBar.add(menu);

		JMenuItem menuItem = new JMenuItem("Configure connection...", KeyEvent.VK_C);
		//menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Reconfigure connection to database");
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				while (getConnect(false) == false);
				Dialog.showInform(thisFrame, "Connection successful", "Connection...");		
			}
		});
		menu.add(menuItem);

		return menuBar;
	}

	private JPanel prepareButtons() {
		JPanel buttonsPane = new JPanel();
		buttonsPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		buttonsPane.setLayout(new BoxLayout(buttonsPane, BoxLayout.X_AXIS));
		buttonsPane.setMinimumSize(new Dimension(Integer.MAX_VALUE, 0));

		JButton addBtn = new JButton("Add student");
		addBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AddScreen addScreen = new AddScreen(thisFrame, "Add Student", connection);
				addScreen.setLocationRelativeTo(thisFrame);
				addScreen.setVisible(true);
				loadStudentTable();
			}
		});
		buttonsPane.add(addBtn);

		buttonsPane.add(Box.createHorizontalStrut(10));

		JButton refreshBtn = new JButton("Refresh table");
		refreshBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadStudentTable();
			}
		});
		buttonsPane.add(refreshBtn);
		
		buttonsPane.add(Box.createHorizontalStrut(10));
		
		JButton searchBtn = new JButton("Search student");
		searchBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SearchScreen searchScreen = new SearchScreen(thisFrame, "Search Student", connection);
				searchScreen.setLocationRelativeTo(thisFrame);
				searchScreen.setVisible(true);
				loadStudentTable();
			}
		});
		buttonsPane.add(searchBtn);

		return buttonsPane;
	}

	private JTable createTable() {
		JTable tableStudent = new JTable();
		tableStudent.setModel(new StudentTableModel());
		tableStudent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableStudent.setAutoCreateRowSorter(true);
		tableStudent.setShowVerticalLines(true);
		tableStudent.setShowHorizontalLines(true);
		tableStudent.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableStudent.getColumnModel().getColumn(0).setResizable(false);
		tableStudent.getTableHeader().setReorderingAllowed(false);

		DefaultTableCellRenderer r1 = new TableCellRenderer(Color.BLUE);
		DefaultTableCellRenderer r2 = new TableCellRenderer(Color.BLACK);

		tableStudent.getColumnModel().getColumn(0).setCellRenderer(r1);
		tableStudent.getColumnModel().getColumn(1).setCellRenderer(r2);
		tableStudent.getColumnModel().getColumn(2).setCellRenderer(r2);
		tableStudent.setFont(new Font("Serif", Font.PLAIN, 16));

		tableStudent.addMouseListener(new TableMouseListener(tableStudent));
		tableStudent.setComponentPopupMenu(tablePopupMenu());

		return tableStudent;
	}

	private JPopupMenu tablePopupMenu() {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem showItem = new JMenuItem("Show selected row");
		showItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				StudentTableModel model = (StudentTableModel) tableStudent.getModel();
				HocSinh hs = model.getStudent(tableStudent.getSelectedRow());
				ShowScreen showScreen = new ShowScreen(thisFrame, "Show Student", hs);
				showScreen.setLocationRelativeTo(thisFrame);
				showScreen.setVisible(true);
			}
		});

		JMenuItem modifyItem = new JMenuItem("Modify selected row");
		modifyItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				StudentTableModel model = (StudentTableModel) tableStudent.getModel();
				HocSinh hs = model.getStudent(tableStudent.getSelectedRow());
				ModifyScreen modifyScreen = new ModifyScreen(thisFrame, "Modify Student", hs, connection);
				modifyScreen.setLocationRelativeTo(thisFrame);
				modifyScreen.setVisible(true);

				loadStudentTable();
			}
		});

		JMenuItem deleteItem = new JMenuItem("Delete selected row");
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StudentTableModel model = (StudentTableModel) tableStudent.getModel();
				HocSinh hs = model.getStudent(tableStudent.getSelectedRow());
				int result = Dialog.showConfirm(null, "Ban co muon xoa hoc sinh - MS: " + hs.getMaHS(),
						"Confirm delete student");

				if (result == JOptionPane.YES_OPTION) {
					try {
						connection.delete(hs.getMaHS());
						loadStudentTable();
					} catch (SQLException e1) {
						Dialog.showErrorMessage(null, e1.getMessage(), "Error from delete student");
					}
				}
			}
		});

		popupMenu.add(showItem);
		popupMenu.add(modifyItem);
		popupMenu.add(deleteItem);

		return popupMenu;
	}

	private void loadStudentTable() {
		List<HocSinh> lstHocSinh = null;
		try {
			lstHocSinh = connection.getTable();
		} catch (SQLException e) {
			Dialog.showErrorMessage(this, "Error to get list student from database" + e.getMessage(),
					"Error database...");
		}

		StudentTableModel model = (StudentTableModel) tableStudent.getModel();
		model.setList((HocSinh[]) lstHocSinh.toArray(new HocSinh[0]));

		Utils.resizeColumnWidth(tableStudent);
		this.pack();
	}

	private boolean getConnect(boolean check) {
		JTextField databaseUrlField = new JTextField(5);
		JTextField portNumberField = new JTextField(5);
		JTextField databaseNameField = new JTextField(5);
		JTextField userNameField = new JTextField(5);
		JPasswordField passWordField = new JPasswordField(5);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(5, 2, 10, 10));

		myPanel.add(new JLabel("Database Url (default localhost):"));
		myPanel.add(databaseUrlField);

		myPanel.add(new JLabel("Port (default 1433):"));
		myPanel.add(portNumberField);

		myPanel.add(new JLabel("Database name:"));
		myPanel.add(databaseNameField);

		myPanel.add(new JLabel("Username:"));
		myPanel.add(userNameField);

		myPanel.add(new JLabel("Password:"));
		myPanel.add(passWordField);

		int result = JOptionPane.showConfirmDialog(this, myPanel, "Connect to database...",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				connection.getConnection(databaseUrlField.getText(), portNumberField.getText(),
						databaseNameField.getText(), userNameField.getText(), new String(passWordField.getPassword()));
			} catch (ClassNotFoundException | SQLException e) {
				Dialog.showErrorMessage(this, e.getMessage() + "\r\nTry again.", "Connection error...");
				return false;
				// e.printStackTrace();
			}
		} else {
			if (check == true) {
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			} else {
				return true;
			}
		}

		return true;
	}
}
