package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private Controller control;

	private List<Road> _roads;
	private String[] _colNames = { "id", "Length", "Weather", "Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit" };

	public RoadsTableModel() {
		_roads = new ArrayList<Road>();
	}

	public RoadsTableModel(Controller c) {
		control = c;
		_roads = new ArrayList<Road>();
		control.addObserver(this);
	}

	public void update() {
		fireTableDataChanged();
	}

	public void setRoadsList(List<Road> roads) {
		_roads = roads;
		update();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public String getColumnName(int col) {
		return _colNames[col];
	}

	@Override
	public int getColumnCount() {
		return _colNames.length;
	}

	@Override
	public int getRowCount() {
		return _roads == null ? 0 : _roads.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = _roads.get(rowIndex).getId();
			break;
		case 1:
			s = _roads.get(rowIndex).getLength();
			break;
		case 2:
			s = _roads.get(rowIndex).getWeather().toString();
			break;
		case 3:
			s = _roads.get(rowIndex).getMaxSpeed();
			break;
		case 4:
			s = _roads.get(rowIndex).getSpeedLimit();
			break;
		case 5:
			s = _roads.get(rowIndex).getTotalCO2();
			break;
		case 6:
			s = _roads.get(rowIndex).getContLimit();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {

	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setRoadsList(map.getRoads());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		setRoadsList(map.getRoads());

	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// _roads.clear();
		setRoadsList(map.getRoads());

	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {

	}

	@Override
	public void onError(String err) {

	}

}
