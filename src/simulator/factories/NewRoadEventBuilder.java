package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewRoadEvent;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event> {
	protected int time;
	protected String id, srcJun, destJunc;
	protected int length, co2Limit, maxSpeed;
	protected Weather weather;
	protected boolean empty = true;

	public NewRoadEventBuilder(String type) {
		super(type);
	}

	protected Event createTheInstance(JSONObject data) {
		if (data != null && !data.isEmpty()) {
			time = data.getInt("time");
			id = data.getString("id");
			srcJun = data.getString("src");
			destJunc = data.getString("dest");
			length = data.getInt("length");
			co2Limit = data.getInt("co2limit");
			maxSpeed = data.getInt("maxspeed");
			weather = Weather.valueOf(data.getString("weather"));
			empty = false;
		}
		return createTheRoad();
	}

	abstract NewRoadEvent createTheRoad();

}
