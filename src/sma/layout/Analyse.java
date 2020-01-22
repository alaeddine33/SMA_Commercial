/**
 * 
 */
package sma.layout;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import sma.agent.SmaAgent;

/**
 * Classe d'aperçu de la simulation
 */
public class Analyse extends JFrame {

	private static final long serialVersionUID = 1L;
	private static Analyse instance;
	
	protected HashMap<String, AgentRepresentation> agents;
	protected HashMap<String, Stats> agent_stats;
	
	private JTabbedPane tabbedPane;
	private JPanel panel_stats;
	private JPanel panel_graphe;
	private Box panel_charts;
	private JTable jtable;
	private TableModelDynamique tableModel;

	@SuppressWarnings("unused")
	private int count = 0;
	private long last_update = System.currentTimeMillis();


	public Analyse() throws HeadlessException {
		super();
		init();
	}

	public static Analyse getInstance() {
		if(instance == null){
			instance = new Analyse();
		}
		return instance;
	}
	
	private void init(){
		agents = new HashMap<String, AgentRepresentation>(2);
		agent_stats = new HashMap<String, Stats>(2);
		
		tabbedPane = new JTabbedPane();
		
		panel_stats = new JPanel();
		panel_stats.setLayout(new BorderLayout());
		tableModel = new TableModelDynamique();
		
		jtable = new JTable(tableModel);
		jtable.setDefaultRenderer(Object.class, new MyRenderer());
		
		panel_stats.add(jtable.getTableHeader(), BorderLayout.NORTH);
		panel_stats.add(jtable, BorderLayout.CENTER);
		
		panel_graphe = new JPanel();
		panel_graphe.setLayout(new CircleLayout());

		panel_charts = Box.createVerticalBox();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tabbedPane.addTab("Simulation", panel_graphe);
		tabbedPane.addTab("Stats", panel_stats);
		getContentPane().add(tabbedPane);
		setSize(1024, 700);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}
	
	public void agent_setup(SmaAgent smaAgent){
		AgentRepresentation agentRepresentation = new AgentRepresentation();
		panel_graphe.add(agentRepresentation);
		agents.put(smaAgent.getName(), agentRepresentation);
		
		agent_stats.put(smaAgent.getName(), new Stats(smaAgent));
		tableModel.setStats( new ArrayList<Stats>(agent_stats.values()) );
		
		panel_graphe.setSize(getPreferredSize().width, getPreferredSize().height);
	}
	
	public void agent_update(SmaAgent agentCommercial){
		agent_stats.get(agentCommercial.getName()).update(agentCommercial);
		tableModel.fireTableDataChanged();
		agents.get(agentCommercial.getName()).update(agentCommercial);
		agent_stats.get(agentCommercial.getName()).setStatus("Alive");

		if (System.currentTimeMillis() - last_update >= 500) {
			for (Entry<String, Stats> e : Analyse.getInstance().agent_stats.entrySet()) {
			}
			count++;
			panel_charts.invalidate();
			panel_charts.revalidate();
			panel_charts.repaint();
			last_update = System.currentTimeMillis();
		}
	}
	
	public void agent_dead(SmaAgent agentCommercial){
		agent_stats.get(agentCommercial.getName()).update(agentCommercial);
		tableModel.fireTableDataChanged();
		agents.get(agentCommercial.getName()).update(agentCommercial);
		agent_stats.get(agentCommercial.getName()).setStatus("Dead");
	}

}
