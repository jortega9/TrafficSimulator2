package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

@SuppressWarnings("serial")
public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {

	private Controller control;

	private List<Vehicle> _vehicles;
	private String[] _colNames = { "id", "Location", "Itinerary", "CO2 class", "Max. Speed", "Speed", "Total CO2",
			"Distance" };

	public VehiclesTableModel() {
		_vehicles = new ArrayList<Vehicle>();
	}

	public VehiclesTableModel(Controller c) {
		control = c;
		_vehicles = new ArrayList<Vehicle>();
		control.addObserver(this);
	}

	public void setVehiclesList(List<Vehicle> vehicles) {
		_vehicles = vehicles;
		update();
	}

	public void update() {
		fireTableDataChanged();
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
		return _vehicles == null ? 0 : _vehicles.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = _vehicles.get(rowIndex).getId();
			break;
		case 1:
			s = _vehicles.get(rowIndex).getLocationToString();
			break;
		case 2:
			s = _vehicles.get(rowIndex).getItinerary();
			break;
		case 3:
			s = _vehicles.get(rowIndex).getContClass();
			break;
		case 4:
			s = _vehicles.get(rowIndex).getMaxSpeed();
			break;
		case 5:
			s = _vehicles.get(rowIndex).getSpeed();
			break;
		case 6:
			s = _vehicles.get(rowIndex).getTotalCO2();
			break;
		case 7:
			s = _vehicles.get(rowIndex).getLocation();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {

	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setVehiclesList(map.getVehicles());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		setVehiclesList(map.getVehicles());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// _vehicles.clear();
		setVehiclesList(map.getVehicles());
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {

	}

	@Override
	public void onError(String err) {

	}

}
