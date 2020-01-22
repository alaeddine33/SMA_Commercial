/**
 * 
 */
package sma.layout;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
/**
 * 
 */
public class TableModelDynamique extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private List<Stats> stats = new ArrayList<Stats>();
 
    private final String[] entetes = {"Name", "Status", "Satisfaction", "Satisfaction Average", "Production", "Production Stock", "Price", "Price Average", "Consumption", "Consumption Stock", "Money", "Money Average", "Life Time", "LifeState", "RunningState", "Transaction Init", "Transaction Confirm", "Transaction Cancel"};
 
    public TableModelDynamique() {
        super();
    }
 
    public int getRowCount() {
        return stats.size();
    }
 
    public int getColumnCount() {
        return entetes.length;
    }
 
    public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
 
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                return stats.get(rowIndex).getName();
            case 1:
                return stats.get(rowIndex).getStatus();
            case 2:
                return String.format("%.2f", stats.get(rowIndex).getSatisfaction());
            case 3:
                return String.format("%.2f", stats.get(rowIndex).getAverage_satifaction());
            case 4:
                return stats.get(rowIndex).getProduction();
            case 5:
                return String.format("%.2f", stats.get(rowIndex).getStock_production());
            case 6:
                return String.format("%.2f", stats.get(rowIndex).getPrice());
            case 7:
                return String.format("%.2f", stats.get(rowIndex).getAverage_price());
            case 8:
                return stats.get(rowIndex).getConsumption();
            case 9:
                return String.format("%.2f", stats.get(rowIndex).getStock_consumption());
            case 10:
                return String.format("%.2f", stats.get(rowIndex).getMoney());
            case 11:
                return String.format("%.2f", stats.get(rowIndex).getAverage_money());
            case 12:
                return String.format("%.0f", stats.get(rowIndex).getLife_time());
            case 13:
                return String.format("%.0f", stats.get(rowIndex).getLifeState());
            case 14:
                return String.format("%d", stats.get(rowIndex).getRunningState());
            case 15:
                return String.format("%d", stats.get(rowIndex).getTransactionInit());
            case 16:
                return String.format("%d", stats.get(rowIndex).getTransactionConfirm());
            case 17:
                return String.format("%d", stats.get(rowIndex).getTransactionCancel());
            default:
                return ""; //Ne devrait jamais arriver
        }
    }
 
    public void add(Stats stats) {
        this.stats.add(stats);
 
        fireTableRowsInserted(this.stats.size() -1, this.stats.size() -1);
    }
 
    public void remove(int rowIndex) {
    	this.stats.remove(rowIndex);
 
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
    
    public void setStats(List<Stats> stats){
    	this.stats = stats;
    }
}

