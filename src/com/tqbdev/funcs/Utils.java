package com.tqbdev.funcs;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class Utils {
	public static void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		
		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 15; // Min width
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);
			}
			if (width < 50 && columnModel.getColumn(column).getHeaderValue() != "")
				width = 50;
			if (width > 300)
				width = 300;
			columnModel.getColumn(column).setPreferredWidth(width + 20);
		}
		
		table.setRowHeight(35);
	}
}
