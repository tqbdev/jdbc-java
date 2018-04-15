package com.tqbdev.components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class TableCellRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6935965343593676285L;
	
	private Color color = Color.BLACK;
	
	public TableCellRenderer(Color color) {
		this.color = color;
	}
	
	@Override
    public Component getTableCellRendererComponent(JTable table, Object
        value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);
        setForeground(color);
        setHorizontalAlignment(JLabel.RIGHT);
        setBorder(new EmptyBorder(0, 0, 0, 10));
        return this;
    }
}
