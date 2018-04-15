package com.tqbdev.screens;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.UtilDateModel;

import com.tqbdev.funcs.DatabaseConnection;
import com.tqbdev.funcs.Dialog;
import com.tqbdev.model.HocSinh;

public class ModifyScreen extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4913402437790155842L;
	private File selectedFile = null;
	private JTextField maHSField = null;
	private JTextField tenHSField = null;
	private JDatePicker datePicker = null;
	private JTextArea ghiChuField = null;
	private JLabel imageView = null;
	private HocSinh hocSinh = null;

	private JFrame parentScreen = null;
	private DatabaseConnection dbConnection = null;

	public ModifyScreen(JFrame parent, String title, HocSinh hs, DatabaseConnection connection) {
		super(parent, title, true);
		parentScreen = parent;
		dbConnection = connection;
		hocSinh = hs;

		JPanel mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));

		// Choice image
		JPanel imagePane = new JPanel();
		imagePane.setAlignmentX(Component.LEFT_ALIGNMENT);
		imagePane.setMinimumSize(new Dimension(Integer.MAX_VALUE, 100));
		imagePane.setLayout(new BoxLayout(imagePane, BoxLayout.X_AXIS));

		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResource("/icons/default-avatar.png"));
		} catch (IOException e) {
			Dialog.showErrorMessage(parentScreen, "Error default-avatar", "Error");
			// e.printStackTrace();
		}
		Image dimg = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
		imageView = new JLabel(new ImageIcon(dimg));
		imageView.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		imageView.setMinimumSize(new Dimension(200, 200));
		imagePane.add(imageView);

		imagePane.add(Box.createHorizontalStrut(10));

		JButton choice = new JButton("Choice avatar");
		choice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (*.jpg *.gif *.png *.bmp *.tiff)",
						"jpg", "gif", "png", "bmp", "tiff");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(parent);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					BufferedImage image = null;
					try {
						selectedFile = chooser.getSelectedFile();
						image = ImageIO.read(selectedFile);
					} catch (IOException ex) {
						Dialog.showErrorMessage(parentScreen, ex.getMessage(), "Error load image...");
					}
					Image dimg = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
					imageView.setIcon(new ImageIcon(dimg));
				}
			}
		});
		imagePane.add(choice);

		mainPane.add(imagePane);
		//

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
		contentPane.add(new JLabel("Ma hoc sinh"), c);

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
		contentPane.add(new JLabel("Ten hoc sinh"), c);

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
		contentPane.add(new JLabel("Ngay sinh"), c);

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
		contentPane.add(new JLabel("Ghi chu"), c);

		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = i++;
		c.gridwidth = 2;
		c.gridheight = 4;

		ghiChuField = new JTextArea(4, 2);
		JScrollPane scrollpane = new JScrollPane(ghiChuField);
		contentPane.add(scrollpane, c);

		mainPane.add(contentPane);
		//

		// Buttons
		JPanel controlPane = new JPanel();

		JButton OK = new JButton("Modify");
		OK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				modifyHocSinhToDatabase();
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

		loadData();

		this.pack();
		this.setResizable(false);
	}

	private void loadData() {
		maHSField.setText(Integer.toString(hocSinh.getMaHS()));
		maHSField.setEnabled(false);
		maHSField.setEditable(false);

		tenHSField.setText(hocSinh.getTenHS());

		Date birthDay = hocSinh.getNgaySinh();
		Calendar cal = Calendar.getInstance();
		cal.setTime(birthDay);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		datePicker.getModel().setDate(year, month, day);

		ghiChuField.setText(hocSinh.getGhichu());

		if (hocSinh.getExtInfo_bytes() != null) {
			BufferedImage image = null;
			
			try {
				image = hocSinh.getExtInfo_image();
			} catch (IOException e) {
				Dialog.showErrorMessage(parentScreen, e.getMessage(), "Error load image from database");
			}
			
			Image dimg = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
			imageView.setIcon(new ImageIcon(dimg));
		}
	}

	private void modifyHocSinhToDatabase() {
		String tenHS = tenHSField.getText();
		Date ngaySinh = (Date) datePicker.getModel().getValue();
		String ghiChu = ghiChuField.getText();

		if (tenHS == null || tenHS == "" || tenHS.length() == 0) {
			Dialog.showWarning(parentScreen, "Ban phai nhap ten hoc sinh", "Warning");
			return;
		}

		try {
			byte[] ExtInfo_image = null;

			if (selectedFile != null) {
				ImageInputStream input = ImageIO.createImageInputStream(selectedFile);
				Iterator<ImageReader> readers = ImageIO.getImageReaders(input);

				if (readers.hasNext()) {
					ImageReader reader = readers.next();
					reader.setInput(input);

					BufferedImage image = reader.read(0);
					String format = reader.getFormatName();

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(image, format, baos);
					ExtInfo_image = baos.toByteArray();
				}

				if (ExtInfo_image == hocSinh.getExtInfo_bytes()) {
					ExtInfo_image = null;
				}
			}

			if (tenHS == hocSinh.getTenHS()) {
				tenHS = null;
			}

			if (ghiChu == hocSinh.getGhichu()) {
				ghiChu = null;
			}

			if (ngaySinh.compareTo(hocSinh.getNgaySinh()) == 0) {
				ngaySinh = null;
			}

			dbConnection.modify(hocSinh.getMaHS(), tenHS, ngaySinh, ghiChu, ExtInfo_image);

			Dialog.showInform(parentScreen, "Da sua hoc sinh thanh cong.", "Modify Student Successful");
			closeDialog();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			Dialog.showErrorMessage(parentScreen, e.getMessage(), "Error add student");
		} catch (NumberFormatException e) {
			Dialog.showErrorMessage(parentScreen, "Ma hoc sinh khong phu hop.\r\n" + e.getMessage(),
					"Error add student");
		}
	}

	private void closeDialog() {
		this.dispose();
	}
}
