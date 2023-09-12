package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements TrafficSimObserver {

	private Controller control;
	private boolean _stopped = false;
	private JButton fileChooser, contVehicle, changeRoadWeather, runButton, stopButton;
	private JLabel ticksLabel;
	private JSpinner ticksSpinner;

	ControlPanel(Controller c) {
		control = c;
		initGUI();
		control.addObserver(this);
	}

	private void initGUI() {
		this.setLayout(new GridLayout(1, 2));

		initLeftButtons();

		JPanel right = new JPanel();
		right.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JButton exitButton = new JButton();
		exitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		exitButton.setContentAreaFilled(false);
		exitButton.setBorderPainted(false);
		exitButton.setToolTipText("Exit Traffic Simulator");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog((Frame) SwingUtilities.getWindowAncestor(ControlPanel.this),
						"Do you want to exit?", "Exit simulator: ", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION)
					System.exit(0);

			}

		});

		right.add(exitButton);

		addComponentsToContainer();
		this.add(right);
	}

	private void initLeftButtons() {

		fileChooser = new JButton();
		fileChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser("resources/examples");
				fc.setFileFilter(new FileNameExtensionFilter("JSON File", "json"));
				int seleccion = fc.showOpenDialog(ControlPanel.this);
				if (seleccion == JFileChooser.APPROVE_OPTION) {
					control.reset();
					InputStream in;
					try {
						in = new FileInputStream(fc.getSelectedFile());
						control.loadEvents(in);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog((Frame) SwingUtilities.getWindowAncestor(ControlPanel.this),
								ex.getMessage(), "Error in file", JOptionPane.ERROR_MESSAGE);
					}

				} else if (seleccion == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog((Frame) SwingUtilities.getWindowAncestor(ControlPanel.this),
							"An error occurred", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		contVehicle = new JButton();
		contVehicle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ChangeCO2ClassDialog((Frame) SwingUtilities.getWindowAncestor(ControlPanel.this), control);
			}

		});

		changeRoadWeather = new JButton();
		changeRoadWeather.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ChangeWeatherDialog((Frame) SwingUtilities.getWindowAncestor(ControlPanel.this), control);
			}

		});

		ticksLabel = new JLabel("Ticks:");
		ticksSpinner = new JSpinner();

		runButton = new JButton();
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableToolBar(false);
				_stopped = false;
				run_sim((Integer) ticksSpinner.getValue());
			}

		});

		stopButton = new JButton();
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}

		});

		setAppereance();
	}

	private void setAppereance() {
		fileChooser.setContentAreaFilled(false);
		fileChooser.setBorder(BorderFactory.createEtchedBorder(1));
		fileChooser.setIcon(new ImageIcon("resources/icons/open.png"));
		fileChooser.setToolTipText("Load a file with events");

		contVehicle.setContentAreaFilled(false);
		contVehicle.setBorder(BorderFactory.createEtchedBorder(1));
		contVehicle.setIcon(new ImageIcon("resources/icons/co2class.png"));
		contVehicle.setToolTipText("Change CO2 class of a vehicle");

		changeRoadWeather.setContentAreaFilled(false);
		changeRoadWeather.setBorder(BorderFactory.createEtchedBorder(1));
		changeRoadWeather.setIcon(new ImageIcon("resources/icons/weather.png"));
		changeRoadWeather.setToolTipText("Change weather of road");

		ticksSpinner.setPreferredSize(new Dimension(100, 20));

		runButton.setContentAreaFilled(false);
		runButton.setBorder(BorderFactory.createEtchedBorder(1));
		runButton.setIcon(new ImageIcon("resources/icons/run.png"));

		stopButton.setContentAreaFilled(false);
		stopButton.setBorder(BorderFactory.createEtchedBorder(1));
		stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
	}

	private void addComponentsToContainer() {
		JPanel left = new JPanel();
		left.setLayout(new FlowLayout(FlowLayout.LEFT));

		left.add(fileChooser);
		left.add(contVehicle);
		left.add(changeRoadWeather);
		left.add(runButton);
		left.add(stopButton);
		left.add(ticksLabel);
		left.add(ticksSpinner);

		this.add(left);
	}

	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				control.run(1);
			} catch (Exception e) {
				_stopped = true;
				enableToolBar(true);
				return;
			}

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});

		} else {
			enableToolBar(true);
			_stopped = true;
		}
	}

	private void enableToolBar(boolean b) {
		fileChooser.setEnabled(b);
		contVehicle.setEnabled(b);
		changeRoadWeather.setEnabled(b);
		changeRoadWeather.setEnabled(b);
	}

	private void stop() {
		_stopped = true;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog((Frame) SwingUtilities.getWindowAncestor(ControlPanel.this), err, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

}
