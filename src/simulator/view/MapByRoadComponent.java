package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

@SuppressWarnings("serial")
public class MapByRoadComponent extends JComponent implements TrafficSimObserver {

	private Controller control;

	private RoadMap _map;

	private Image _car;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final int _JRADIUS = 10;
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	private final int x1 = 50;
	private int x2;

	MapByRoadComponent(Controller c) {
		control = c;
		initGUI();
		setPreferredSize(new Dimension(300, 200));
		control.addObserver(this);

	}

	private void initGUI() {
		_car = loadImage("car.png");
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		x2 = getWidth() - 100;

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize();
			drawMap(g);
		}

	}

	private void drawMap(Graphics g) {
		int y;
		Junction src, dst;
		Road r;
		for (int i = 0; i < _map.getRoads().size(); ++i) {
			y = (i + 1) * 50;

			r = _map.getRoads().get(i);
			g.setColor(Color.black);
			g.drawString(r.getId(), x1 - 30, y);
			g.drawLine(x1, y, x2, y);

			src = r.getSrc();
			dst = r.getDest();

			drawJunction(g, src, r, x1, y);
			drawJunction(g, dst, r, x2, y);
			for (Vehicle v : r.getVehicles())
				drawVehicle(g, v, y);

			drawClimateCond(g, r.getWeather(), x2 + 10, y);
			drawContCond(g, r.getTotalCO2(), r.getContLimit(), x2 + 52, y);
		}
	}

	private void drawJunction(Graphics g, Junction j, Road r, int x, int y) {
		if (x != x1)
			g.setColor(junctionColor(j, r));
		else
			g.setColor(_JUNCTION_COLOR);

		g.fillOval(x - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(j.getId(), x - 5, y - 10);
	}

	private void drawVehicle(Graphics g, Vehicle v, int y) {
		int x = x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) v.getRoad().getLength()));

		int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
		g.setColor(new Color(0, vLabelColor, 0));

		g.drawImage(_car, x, y - 12, 16, 16, this);
		g.drawString(v.getId(), x, y - 12);

	}

	private void drawClimateCond(Graphics g, Weather w, int x, int y) {
		Image image = null;
		switch (w) {
		case SUNNY:
			image = loadImage("sun.png");
			break;
		case CLOUDY:
			image = loadImage("cloud.png");
			break;
		case RAINY:
			image = loadImage("rain.png");
			break;
		case WINDY:
			image = loadImage("wind.png");
			break;
		case STORM:
			image = loadImage("storm.png");
			break;
		}
		g.drawImage(image, x, y - 12, 32, 32, this);
	}

	private void drawContCond(Graphics g, int totalCO2, int contLimit, int x, int y) {
		int C = (int) Math.floor(Math.min((double) totalCO2 / (1.0 + (double) contLimit), 1.0) / 0.19);
		Image image = loadImage("cont_" + C + ".png");

		g.drawImage(image, x, y - 12, 32, 32, this);
	}

	private Color junctionColor(Junction j, Road r) {
		if (!j.getGreenRoad().equals(r.getId()))
			return _RED_LIGHT_COLOR;
		else
			return _GREEN_LIGHT_COLOR;
	}

	private void updatePrefferedSize() {
		int maxW = 200;
		int maxH = 200;
		for (int i = 0; i < _map.getRoads().size(); ++i) {
			maxH += 40;
		}

		if (maxW > getWidth() || maxH > getHeight()) {
			setPreferredSize(new Dimension(maxW, maxH));
			setSize(new Dimension(maxW, maxH));
		}
	}

	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		SwingUtilities.invokeLater(() -> {
			_map = map;
			repaint();
		});
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {

	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {

	}

}
