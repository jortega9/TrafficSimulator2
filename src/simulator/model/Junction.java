package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {

	private List<Road> incomingRoad;
	private Map<Junction, Road> outgoingRoad;
	private List<List<Vehicle>> queue;
	private Map<Road, List<Vehicle>> roadQueue;
	private int currGreen, lastSwitchingTime, xCoor, yCoor;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;

	public Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor)
			throws IllegalArgumentException {
		super(id);
		if (lsStrategy == null)
			throw new IllegalArgumentException("Constructor Junction not valid: lsStrategy is null");
		if (dqStrategy == null)
			throw new IllegalArgumentException("Constructor Junction not valid: dqStratey is null");
		if (xCoor < 0)
			throw new IllegalArgumentException("Constructor Junction not valid: coordinate x less than 0");
		if (yCoor < 0)
			throw new IllegalArgumentException("Constructor Junction not valid: coordinate y less than 0");

		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
		lastSwitchingTime = 0;
		incomingRoad = new ArrayList<>();
		outgoingRoad = new HashMap<>();
		queue = new ArrayList<>();
		roadQueue = new HashMap<>();
		currGreen = -1;
	}

	public void addIncommingRoad(Road r) throws IllegalArgumentException {
		if (!r.getDest().equals(this))
			throw new IllegalArgumentException("Not valid Road: " + r.getId());

		incomingRoad.add(r);
		List<Vehicle> auxList = new LinkedList<>();
		queue.add(auxList);
		roadQueue.put(r, auxList);
	}

	public void addOutgoingRoad(Road r) throws IllegalArgumentException {
		if (!r.getSrc().equals(this) || outgoingRoad.containsKey(r.getDest()))
			throw new IllegalArgumentException("Not valid Road: " + r.getId());

		outgoingRoad.put(r.getDest(), r);
	}

	void enter(Vehicle v) {
		List<Vehicle> q = roadQueue.get(v.getRoad());
		q.add(v);
	}

	public Road roadTo(Junction j) {
		if (outgoingRoad.get(j) == null) {
			throw new IllegalArgumentException("There is no outgoing road in junction " + this.getId());
		}
		return outgoingRoad.get(j);
	}

	void advance(int time) {

		if (currGreen != -1) {
			List<Vehicle> movingVehicles = dqStrategy.dequeue(queue.get(currGreen));
			for (int j = 0; j < movingVehicles.size(); j++) {
				movingVehicles.get(j).moveToNextRoad();
			}

			queue.get(currGreen).removeAll(movingVehicles);
			roadQueue.replace(incomingRoad.get(currGreen), queue.get(currGreen));
		}

		int nextRoad = lsStrategy.chooseNextGreen(incomingRoad, queue, currGreen, lastSwitchingTime, time);
		if (nextRoad != currGreen) {
			currGreen = nextRoad;
			lastSwitchingTime = time;
		}
	}

	public JSONObject report() {
		JSONObject jo1 = new JSONObject();
		JSONArray ja = new JSONArray();

		jo1.put("id", this.getId());
		if (currGreen != -1)
			jo1.put("green", incomingRoad.get(currGreen).getId());
		else
			jo1.put("green", "none");

		for (int j = 0; j < queue.size(); j++) {
			JSONObject jo2 = new JSONObject();
			JSONArray ja2 = new JSONArray();
			for (Vehicle v : queue.get(j)) {
				ja2.put(v.getId());
			}

			jo2.put("road", incomingRoad.get(j).getId());
			jo2.put("vehicles", ja2);
			ja.put(jo2);

		}

		jo1.put("queues", ja);
		return jo1;
	}

	public int getX() {
		return xCoor;
	}

	public int getY() {
		return yCoor;
	}

	public int getGreenLightIndex() {
		return currGreen;
	}

	public String getGreenRoad() {
		if (currGreen == -1)
			return "NONE";
		else
			return incomingRoad.get(currGreen).getId();
	}

	public List<Road> getInRoads() {
		return Collections.unmodifiableList(incomingRoad);
	}

	public String getQueue() {
		String str = "";
		for (Road r : incomingRoad) {
			str += r.getId() + ": " + roadQueue.get(r) + " ";
		}

		return str;
	}

}
