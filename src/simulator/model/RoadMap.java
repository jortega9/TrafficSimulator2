package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {

	private List<Junction> junctions;
	private List<Road> roads;
	private List<Vehicle> vehicles;
	private Map<String, Junction> juncMap;
	private Map<String, Road> roadMap;
	private Map<String, Vehicle> vehicleMap;

	RoadMap() {
		junctions = new ArrayList<>();
		roads = new ArrayList<>();
		vehicles = new ArrayList<>();
		juncMap = new HashMap<>();
		roadMap = new HashMap<>();
		vehicleMap = new HashMap<>();
	}

	void addJunction(Junction j) {
		if (juncMap.containsKey(j._id))
			throw new IllegalArgumentException("Not valid junction: " + j.getId());
		junctions.add(j);
		juncMap.put(j._id, j);
	}

	void addRoad(Road r) {
		if (roadMap.containsKey(r._id))
			throw new IllegalArgumentException("Not valid road: " + r.getId());

		if (!juncMap.containsValue(r.getSrc()) || !juncMap.containsValue(r.getDest()))
			throw new IllegalArgumentException("Not valid road: " + r.getId() + " the junctions are not defined");

		roads.add(r);
		roadMap.put(r._id, r);
	}

	void addVehicle(Vehicle v) {
		if (vehicleMap.containsKey(v._id))
			throw new IllegalArgumentException("Not valid vehicle: the vehicle " + v.getId() + " already exists");

		Junction previous = v.getItinerary().get(0);
		for (Junction current : v.getItinerary()) {
			if (previous != current) {
				if (previous.roadTo(current) == null)
					throw new IllegalArgumentException("Not valid junction: there is no road that connects "
							+ previous.getId() + " and " + current.getId());
			}
			previous = current;
		}
		vehicles.add(v);
		vehicleMap.put(v._id, v);
	}

	public Junction getJunction(String id) {
		return juncMap.get(id);
	}

	public Road getRoad(String id) {
		return roadMap.get(id);
	}

	public Vehicle getVehicle(String id) {
		return vehicleMap.get(id);
	}

	public List<Junction> getJunctions() {
		return Collections.unmodifiableList(junctions);
	}

	public List<Road> getRoads() {
		return Collections.unmodifiableList(roads);
	}

	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehicles);
	}

	void reset() {
		junctions.clear();
		vehicles.clear();
		roads.clear();
		juncMap.clear();
		roadMap.clear();
		vehicleMap.clear();
	}

	public JSONObject report() {
		JSONObject jo1 = new JSONObject();

		JSONArray ja = new JSONArray();

		for (Junction q : junctions) {
			ja.put(q.report());
		}

		jo1.put("junctions", ja);

		JSONArray je = new JSONArray();

		for (Road q : roads) {
			je.put(q.report());
		}

		jo1.put("roads", je);

		JSONArray ji = new JSONArray();

		for (Vehicle q : vehicles) {
			ji.put(q.report());
		}

		jo1.put("vehicles", ji);

		return jo1;
	}
}
