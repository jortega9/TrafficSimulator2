package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	public NewVehicleEventBuilder() {
		super("new_vehicle");

	}

	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		int maxSpeed = data.getInt("maxspeed");
		int contClass = data.getInt("class");
		JSONArray it = data.getJSONArray("itinerary");
		List<String> itinerary = new ArrayList<>();

		for (Object i : it)
			itinerary.add(i.toString());

		return new NewVehicleEvent(time, id, maxSpeed, contClass, itinerary);
	}

}
