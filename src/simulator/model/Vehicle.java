package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject {

	private List<Junction> itinerary;
	private int maxSpeed;
	private int actSpeed;
	private VehicleStatus status;
	private Road road;
	private int location;
	private int contClass;
	private int contTotal;
	private int disTotal;
	private int junctionInd;

	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws IllegalArgumentException {
		super(id);
		if (maxSpeed <= 0)
			throw new IllegalArgumentException("Max Speed in class Vehicle must be greater than 0. Vehicle: " + id);
		if (contClass < 0 || contClass > 10)
			throw new IllegalArgumentException(
					"Cont class in class Vehicle must be positive and smaller than 10. Vehicle: " + id);
		if (itinerary.size() < 2)
			throw new IllegalArgumentException("Itinerary in class Vehicle must be longer than 2. Vehicle: " + id);
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		this.disTotal = 0;
		this.status = VehicleStatus.PENDING;
		this.junctionInd = 0;
	}

	int min(int x, int y) {
		if (x < y)
			return x;
		else
			return y;
	}

	void setSpeed(int s) throws IllegalArgumentException {
		if (s < 0)
			throw new IllegalArgumentException("Speed in class vehicle must be greater than 0. Vehicle: " + _id);
		if (VehicleStatus.TRAVELING.equals(status))
			this.actSpeed = min(s, maxSpeed);
	}

	void setContClass(int c) throws IllegalArgumentException {
		if (c >= 0 && c <= 10)
			this.contClass = c;
		else
			throw new IllegalArgumentException(
					"Cont class in class Vehicle must be positive and smaller than 10. Vehicle: " + _id);
	}

	void moveToNextRoad() throws IllegalArgumentException {
		if (!VehicleStatus.PENDING.equals(status) && !VehicleStatus.WAITING.equals(status))
			throw new IllegalArgumentException(
					"A vehicle can't move to the next road if it's not pending or waiting. Vehicle: " + _id);

		if (!VehicleStatus.PENDING.equals(status))
			road.exit(this);

		if (junctionInd < itinerary.size() - 1) {
			if (itinerary.get(junctionInd) == null || itinerary.get(junctionInd + 1) == null)
				throw new IllegalArgumentException(
						"The vehicle's itinerary can not have empty elements. Vehicle: " + _id);
			road = itinerary.get(junctionInd).roadTo(itinerary.get(junctionInd + 1));
			location = 0;
			this.setSpeed(0);
			road.enter(this);
			junctionInd++;
			status = VehicleStatus.TRAVELING;
		} else
			status = VehicleStatus.ARRIVED;
	}

	void advance(int time) {
		int locIni = location;
		int adv = 0;
		int c;

		if (status.equals(VehicleStatus.TRAVELING)) {
			location = min(location + actSpeed, road.getLength());
			adv = location - locIni;
			disTotal += adv;
			c = adv * contClass;
			contTotal += c;
			road.addContamination(c);

			if (location >= road.getLength()) {
				actSpeed = 0;
				itinerary.get(junctionInd).enter(this);
				status = VehicleStatus.WAITING;
			}
		}
	}

	public JSONObject report() {
		JSONObject jo1 = new JSONObject();

		jo1.put("id", String.valueOf(_id));
		jo1.put("speed", actSpeed);
		jo1.put("distance", disTotal);
		jo1.put("co2", contTotal);
		jo1.put("class", contClass);
		jo1.put("status", status.toString());
		if (!status.equals(VehicleStatus.PENDING) && !status.equals(VehicleStatus.ARRIVED)) {
			jo1.put("road", road.getId());
			jo1.put("location", location);
		}

		return jo1;
	}

	public int getLocation() {
		return location;
	}

	public int getSpeed() {
		return actSpeed;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getContClass() {
		return contClass;
	}

	public VehicleStatus getStatus() {
		return status;
	}

	public int getTotalCO2() {
		return contTotal;
	}

	public List<Junction> getItinerary() {
		return itinerary;
	}

	public Road getRoad() {
		return road;
	}

	int compareTo(Vehicle v) {
		if (v.getLocation() < getLocation()) {
			return -1;
		} else if (v.getLocation() > getLocation()) {
			return 1;
		} else {
			return 0;
		}
	}

	public String getLocationToString() {
		if (status.equals(VehicleStatus.PENDING))
			return "Pending";
		else if (status.equals(VehicleStatus.WAITING))
			return "Waiting: " + itinerary.get(junctionInd).getId();
		else if (status.equals(VehicleStatus.ARRIVED))
			return "Arrived";
		return road.getId() + ": " + location;
	}
}