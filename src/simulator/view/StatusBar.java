package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class StatusBar extends JPanel implements TrafficSimObserver {

	private Controller control;
	private int time;
	private JLabel message, timeL;
	private final String timeS = "Time: ", eventS = "Event added: ";

	StatusBar(Controller c) {
		control = c;
		time = 0;
		initGUI();
		control.addObserver(this);
	}

	private void initGUI() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		message = new JLabel("Welcome!");
		timeL = new JLabel(timeS + String.valueOf(time));
		timeL.setPreferredSize(new Dimension(200, 50));

		add(timeL);
		add(message);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {

	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		updateLabels(null, time);

	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		updateLabels(e, time);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		updateLabels(null, time);

	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {

	}

	@Override
	public void onError(String err) {

	}

	private void updateTime(int time) {
		timeL.setText(timeS + String.valueOf(time));
	}

	private void updateLabels(Event e, int time) {
		if (e == null)
			message.setVisible(false);
		else {
			message.setText(eventS + e.toString());
			message.setVisible(true);
		}
		updateTime(time);
	}

}
