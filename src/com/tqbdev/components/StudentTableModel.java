package com.tqbdev.components;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.table.AbstractTableModel;

import com.tqbdev.model.HocSinh;

public class StudentTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6050701234900644062L;
	private HocSinh[] students;
	private String[] columns = { "ID", "Name", "Birthday (yyyy/MM/dd)" };

	public StudentTableModel() {
		this.students = new HocSinh[] {};
	}

	public StudentTableModel(HocSinh[] students) {
		this.students = students;
	}

	public Object getValueAt(int row, int column) {
		HocSinh hs = students[row];
		switch (column) {
		case 0:
			return hs.getMaHS();
		case 1:
			return hs.getTenHS();
		case 2:
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			return df.format(hs.getNgaySinh());
		default:
			System.err.println("Logic Error");
		}
		return "";
	}

	public int getColumnCount() {
		return columns.length;
	}

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return Integer.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		}
		return String.class;
	}

	public String getColumnName(int column) {
		return columns[column];
	}

	public int getRowCount() {
		return students.length;
	}

	public HocSinh getStudent(int row) {
		return students[row];
	}

	public void setList(HocSinh[] students) {
		this.students = students;
		fireTableDataChanged();
	}
}
