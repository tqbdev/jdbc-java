package com.tqbdev.screens;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.UtilDateModel;

import com.tqbdev.funcs.DatabaseConnection;
import com.tqbdev.funcs.Dialog;
import com.tqbdev.model.HocSinh;

public class SearchScreen extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3278449877380874964L;
	private JTextField maHSField = null;
	private JTextField tenHSField = null;
	private JDatePicker datePicker = null;
	private JTextArea ghiChuField = null;

	private ButtonGroup group = null;

	private JFrame parentScreen = null;
	private DatabaseConnection dbConnection = null;

	public SearchScreen(JFrame parent, String title, DatabaseConnection connection) {
		super(parent, title, true);
		parentScreen = parent;
		dbConnection = connection;

		JPanel mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));

		// Information
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 0, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;

		int i = 0;

		c.weightx = 0.1;
		c.gridx = 0;
		c.gridy = i;
		c.gridwidth = 1;
		JRadioButton radioMaHS = new JRadioButton("Ma hoc sinh");
		radioMaHS.setActionCommand("A");
		contentPane.add(radioMaHS, c);

		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = i++;
		c.gridwidth = 2;
		maHSField = new JTextField();
		contentPane.add(maHSField, c);

		c.weightx = 0.1;
		c.gridx = 0;
		c.gridy = i;
		c.gridwidth = 1;
		JRadioButton radioTenHS = new JRadioButton("Ten hoc sinh");
		radioTenHS.setActionCommand("B");
		contentPane.add(radioTenHS, c);

		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = i++;
		c.gridwidth = 2;
		tenHSField = new JTextField();
		contentPane.add(tenHSField, c);

		c.weightx = 0.1;
		c.gridx = 0;
		c.gridy = i;
		c.gridwidth = 1;
		JRadioButton radioNgaySinh = new JRadioButton("Ngay sinh");
		radioNgaySinh.setActionCommand("C");
		contentPane.add(radioNgaySinh, c);

		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = i++;
		c.gridwidth = 2;

		UtilDateModel model = new UtilDateModel();
		LocalDate nowDate = LocalDate.now();
		model.setDate(nowDate.getYear() - 18, nowDate.getMonthValue() - 1, nowDate.getDayOfMonth());
		model.setSelected(true);

		datePicker = new JDatePicker(model);
		contentPane.add(datePicker, c);

		c.weightx = 0.1;
		c.gridx = 0;
		c.gridy = i;
		c.gridwidth = 1;
		JRadioButton radioGhiChu = new JRadioButton("Ghi chu (contains)");
		radioGhiChu.setActionCommand("D");
		contentPane.add(radioGhiChu, c);

		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = i++;
		c.gridwidth = 2;
		c.gridheight = 4;

		ghiChuField = new JTextArea(4, 2);
		JScrollPane scrollpane = new JScrollPane(ghiChuField);
		contentPane.add(scrollpane, c);

		mainPane.add(contentPane);

		group = new ButtonGroup();
		group.add(radioMaHS);
		group.add(radioTenHS);
		group.add(radioNgaySinh);
		group.add(radioGhiChu);

		radioMaHS.setSelected(true);
		//

		// Buttons
		JPanel controlPane = new JPanel();

		JButton OK = new JButton("Search");
		OK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<HocSinh> lst = searchStudent();
				if (lst != null) {
					ResultSearchScreen resultSearchScreen = new ResultSearchScreen(parentScreen, "Result Search", connection, lst);
					resultSearchScreen.setLocationRelativeTo(parentScreen);
					resultSearchScreen.setVisible(true);
				}
			}
		});
		controlPane.add(OK);

		JButton Cancel = new JButton("Cancel");
		Cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
		controlPane.add(Cancel);

		mainPane.add(controlPane);

		setContentPane(mainPane);
		this.pack();
		this.setResizable(false);
	}

	private List<HocSinh> searchStudent() {
		char code = group.getSelection().getActionCommand().charAt(0);

		int maHS = -1;
		String tenHS = null;
		Date ngaySinh = null;
		String ghiChu = null;

		switch (code) {
		case 'A': // MaHS
			String maHSText = maHSField.getText();
			if (maHSText == null || maHSText == "" || maHSText.length() == 0) {
				Dialog.showWarning(parentScreen, "Ban phai nhap ma hoc sinh khi da chon", "Warning");
				return null;
			}

			try {
				maHS = Integer.parseInt(maHSText);
			} catch (NumberFormatException e) {
				Dialog.showErrorMessage(parentScreen, "Ma hoc sinh khong phu hop.\r\n" + e.getMessage(),
						"Error add student");
				return null;
			}
			break;
		case 'B': // TenHS
			tenHS = tenHSField.getText();
			if (tenHS == null || tenHS == "" || tenHS.length() == 0) {
				Dialog.showWarning(parentScreen, "Ban phai nhap ten hoc sinh khi da chon", "Warning");
				return null;
			}
			break;
		case 'C': // NgaySinh
			ngaySinh = (Date) datePicker.getModel().getValue();
			break;
		case 'D': // Ghichu
			ghiChu = ghiChuField.getText();
			if (ghiChu == null || ghiChu == "" || ghiChu.length() == 0) {
				Dialog.showWarning(parentScreen, "Ban phai nhap ghi chu khi da chon", "Warning");
				return null;
			}
			break;
		}
		
		try {
			return dbConnection.query(maHS, tenHS, ngaySinh, ghiChu);
		} catch (SQLException e) {
			Dialog.showErrorMessage(parentScreen, e.getMessage(), "Error add student");
			return null;
		}
	}

	private void closeDialog() {
		this.dispose();
	}
}
