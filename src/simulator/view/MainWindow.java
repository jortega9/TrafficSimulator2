package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	private Controller control;

	static int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	static int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

	public MainWindow(Controller c) {
		super("Traffic Simulator");
		control = c;
		initGUI();

	}

	private void initGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);

		mainPanel.add(new ControlPanel(control), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(control), BorderLayout.PAGE_END);

		JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);

		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);

		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);

		JTable t = new JTable(new EventsTableModel(control));
		JPanel eventsView = createViewPanel(t, "Events");
		eventsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(eventsView);

		JPanel vehiclesView = createViewPanel(new JTable(new VehiclesTableModel(control)), "Vehicles");
		vehiclesView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(vehiclesView);

		JPanel roadsView = createViewPanel(new JTable(new RoadsTableModel(control)), "Roads");
		roadsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(roadsView);

		JPanel junctionsView = createViewPanel(new JTable(new JunctionsTableModel(control)), "Junctions");
		junctionsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(junctionsView);

		JPanel mapView = createViewPanel(new MapComponent(control), "Map");
		mapView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapView);

		JPanel mapComponentView = createViewPanel(new MapByRoadComponent(control), "Map by Road");
		mapComponentView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapComponentView);

		viewsPanel.add(mapsPanel);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setLocation(ancho / 2 - this.getWidth() / 2, alto / 2 - this.getHeight() / 2);
		this.setVisible(true);
	}

	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(new TitledBorder(title));
		p.add(new JScrollPane(c));
		return p;
	}
}
