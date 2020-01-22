/**
 * 
 */
package sma.layout;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
/**
 * 
 */
public class StatsListModel implements ListModel {
    private String storeName;
    private List<Stats> statsArray;

    public StatsListModel(String storeNme) {
        storeName = storeNme;
        statsArray = new ArrayList<Stats>();
    }

    private List<ListDataListener> listener = new ArrayList<ListDataListener>();

    @Override
    public void addListDataListener(ListDataListener l) {
        listener.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listener.remove(l);
    }

    protected void fireContentsChanged() {
        for (ListDataListener l : listener) {
            l.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, statsArray.size()-1));
        }
    }

    public Object getElementAt(int index) {
        return statsArray.get(index);
    }

    public int getSize() {
        return statsArray.size();
    }

}