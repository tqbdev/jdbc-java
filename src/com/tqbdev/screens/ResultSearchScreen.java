package com.tqbdev.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import com.tqbdev.components.StudentTableModel;
import com.tqbdev.components.TableCellRenderer;
import com.tqbdev.components.TableMouseListener;
import com.tqbdev.funcs.DatabaseConnection;
import com.tqbdev.funcs.Dialog;
import com.tqbdev.funcs.Utils;
import com.tqbdev.model.HocSinh;

public class ResultSearchScreen extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2864921348741803948L;
	
	private JFrame parentFrame = null;
	private JTable tableStudent = null;
	private DatabaseConnection connection = null;
	private List<HocSinh> lstHocSinh = null;

	public ResultSearchScreen(JFrame parent, String title, DatabaseConnection connection, List<HocSinh> lst) {
		super(parent, title, true);
		parentFrame = parent;
		this.connection = connection;
		this.lstHocSinh = lst;
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		tableStudent = createTable();
		JScrollPane tableScroll = new JScrollPane(tableStudent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScroll.getViewport().setBackground(Color.WHITE);

		contentPane.add(tableScroll);
		loadStudentTable();
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
				ShowScreen showScreen = new ShowScreen(parentFrame, "Show Student", hs);
				showScreen.setLocationRelativeTo(parentFrame);
				showScreen.setVisible(true);
			}
		});

		JMenuItem modifyItem = new JMenuItem("Modify selected row");
		modifyItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				StudentTableModel model = (StudentTableModel) tableStudent.getModel();
				HocSinh hs = model.getStudent(tableStudent.getSelectedRow());
				ModifyScreen modifyScreen = new ModifyScreen(parentFrame, "Modify Student", hs, connection);
				modifyScreen.setLocationRelativeTo(parentFrame);
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
		StudentTableModel model = (StudentTableModel) tableStudent.getModel();
		model.setList((HocSinh[]) lstHocSinh.toArray(new HocSinh[0]));

		Utils.resizeColumnWidth(tableStudent);
		this.pack();
	}
}
