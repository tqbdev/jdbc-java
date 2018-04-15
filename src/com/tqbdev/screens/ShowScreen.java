package com.tqbdev.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.UtilDateModel;

import com.tqbdev.funcs.Dialog;
import com.tqbdev.model.HocSinh;

public class ShowScreen extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4913402437790155842L;
	private JTextField maHSField = null;
	private JTextField tenHSField = null;
	private JDatePicker datePicker = null;
	private JTextArea ghiChuField = null;
	private JLabel imageView = null;
	
	private HocSinh hocSinh = null;
	private JFrame parentScreen = null;

	public ShowScreen(JFrame parent, String title, HocSinh hs) {
		super(parent, title, true);
		
		parentScreen = parent;
		hocSinh = hs;

		JPanel mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));

		// image avatar		
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

		mainPane.add(imageView);
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

		setContentPane(mainPane);
		loadData();
		
		this.pack();
		this.setResizable(false);
	}
	
	private void loadData() {
		maHSField.setText(Integer.toString(hocSinh.getMaHS()));
		maHSField.setEditable(false);
		
		tenHSField.setText(hocSinh.getTenHS());
		tenHSField.setEditable(false);
		
		Date birthDay = hocSinh.getNgaySinh();
		Calendar cal = Calendar.getInstance();
		cal.setTime(birthDay);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		datePicker.getModel().setDate(year, month, day);
		datePicker.setEnabled(false);
		
		ghiChuField.setText(hocSinh.getGhichu());
		ghiChuField.setEditable(false);
		
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
}