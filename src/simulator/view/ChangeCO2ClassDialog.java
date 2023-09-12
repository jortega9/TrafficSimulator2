package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

@SuppressWarnings("serial")
public class ChangeCO2ClassDialog extends JDialog implements TrafficSimObserver {
	private Controller control;

	private JComboBox<String> vehicles;
	private int _time;

	ChangeCO2ClassDialog(Frame p, Controller control) {
		super(p, "Change CO2 Class", true);
		this.control = control;
		_time = 0;
		initGUI();
		control.addObserver(this);
		this.setVisible(true);
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		JPanel info = new JPanel();

		JLabel infoLabel = new JLabel(
				"Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.");

		info.add(infoLabel);

		JPanel eventPanel = new JPanel();
		eventPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

		JLabel vehicleLabel = new JLabel("Vehicle:");
		JLabel co2Label = new JLabel("CO2 Class:");
		JLabel ticksLabel = new JLabel("Ticks:");

		vehicles = new JComboBox<String>();
		vehicles.setPreferredSize(new Dimension(60, 20));

		JSpinner co2Spinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
		co2Spinner.setPreferredSize(new Dimension(60, 20));

		JSpinner ticksSpinner = new JSpinner();
		ticksSpinner.setPreferredSize(new Dimension(60, 20));
		ticksSpinner.setModel(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));

		eventPanel.add(vehicleLabel);
		eventPanel.add(vehicles);
		eventPanel.add(co2Label);
		eventPanel.add(co2Spinner);
		eventPanel.add(ticksLabel);
		eventPanel.add(ticksSpinner);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		okButton.setPreferredSize(cancelButton.getPreferredSize());

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeCO2ClassDialog.this.dispose();
			}

		});

		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Pair<String, Integer>> l = new ArrayList<>();

				l.add(new Pair<String, Integer>(vehicles.getSelectedItem().toString(),
						(Integer) co2Spinner.getValue()));

				control.addEvent(new SetContClassEvent((Integer) ticksSpinner.getValue() + _time, l));
				ChangeCO2ClassDialog.this.dispose();
			}

		});
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);

		this.add(info, BorderLayout.PAGE_START);
		this.add(eventPanel, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.PAGE_END);

		this.setResizable(false);
		this.pack();
		this.setLocation(MainWindow.ancho / 2 - this.getWidth() / 2, MainWindow.alto / 2 - this.getHeight() / 2);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {

	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_time = time;
		updateVehicles(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_time = time;
		updateVehicles(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_time = time;
		updateVehicles(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_time = time;
		updateVehicles(map);
	}

	@Override
	public void onError(String err) {
	}

	private void updateVehicles(RoadMap map) {
		String[] vehicles = new String[map.getVehicles().size()];
		List<Vehicle> v = map.getVehicles();

		for (int i = 0; i < v.size(); ++i) {
			vehicles[i] = v.get(i).getId();
		}

		updateModel(vehicles);
	}

	private void updateModel(String[] v) {

		vehicles.setModel(new DefaultComboBoxModel<String>(v));
	}
}
