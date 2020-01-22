/**
 * 
 */
package sma.layout;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
/**
 * 
 */
public class MyRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

    Color backgroundColor = getBackground();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String val_string;
        double v;
        
        switch (column) {
			case 1:
				val_string = (String) value;
				if(val_string.equals("Alive")){
					setBackground(Color.GREEN.brighter());
				}else{
					setBackground(Color.RED.brighter());
				}
				break;
				
			case 2:
				val_string = (String) value;
				val_string = val_string.replaceAll(",", ".");
				v = Double.parseDouble(val_string);
				if(v >= 100){
					setBackground(backgroundColor);
				}else{
					setBackground(new Color(255, 100, 100));
				}
				break;
				
			case 5:
			case 9:
			case 10:
				val_string = (String) value;
				val_string = val_string.replaceAll(",", ".");
				v = Double.parseDouble(val_string);
				if(v > 0){
					setBackground(backgroundColor);
				}else{
					setBackground(Color.ORANGE);
				}
				break;
				
			case 6:
				val_string = (String) value;
				val_string = val_string.replaceAll(",", ".");
				v = Double.parseDouble(val_string);
				if(v > 0.1){
					setBackground(backgroundColor);
				}else{
					setBackground(new Color(255, 100, 100));
				}
				break;
				
			default:
				setBackground(backgroundColor);
				break;
		}
        
        return c;
    }

}
