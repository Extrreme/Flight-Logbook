package dev.extrreme.logbook.ui.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * A simple {@link DefaultTableCellRenderer} that centers the text within each cell
 */
public class CenteredTableCellRenderer extends DefaultTableCellRenderer {

    public CenteredTableCellRenderer() {
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
    }
}
