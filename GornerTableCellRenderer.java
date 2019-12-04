package com.company;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class GornerTableCellRenderer implements TableCellRenderer {

    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();

    private String needle = null;
    private String from = null;
    private String to = null;

    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    public GornerTableCellRenderer() {
        formatter.setMaximumFractionDigits(5);
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        panel.add(label);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int col) {
        String formattedDouble = formatter.format(value);

        label.setText(formattedDouble);

        if ((col == 1 || col == 2 || col == 3) && needle != null && needle.equals(formattedDouble)) {
            panel.setBackground(Color.RED);
        } else {
            if (row % 2 == 0) {
                if (col % 2 == 0) {
                    panel.setBackground(Color.WHITE);
                    label.setForeground(Color.BLACK);
                } else {
                    panel.setBackground(Color.BLACK);
                    label.setForeground(Color.WHITE);
                }
            } else {
                if (col % 2 != 0) {
                    panel.setBackground(Color.WHITE);
                    label.setForeground(Color.BLACK);
                } else {
                    panel.setBackground(Color.BLACK);
                    label.setForeground(Color.WHITE);
                }
            }
        }

        if (from != null & to != null) {
            Double val = Double.parseDouble(formattedDouble);
            Double f = Double.parseDouble(from);
            Double t = Double.parseDouble(to);

            if (val >= f && val <= t) {
                panel.setBackground(Color.magenta);
                label.setForeground(Color.WHITE);
            }

        }
        return panel;
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }

    public void setRanges(String from, String to) {
        this.from = from;
        this.to = to;
    }

}