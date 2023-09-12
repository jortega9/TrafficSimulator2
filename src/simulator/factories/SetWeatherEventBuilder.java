package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		JSONArray ws = data.getJSONArray("info");
		List<Pair<String, Weather>> w = new ArrayList<>();
		JSONObject aux;
		for (int i = 0; i < ws.length(); i++) {
			aux = ws.getJSONObject(i);
			if (aux.getString("road").isEmpty())
				throw new IllegalArgumentException("In the set weather event, road can not be empty");
			try {
				w.add(new Pair<String, Weather>(aux.getString("road"), Weather.valueOf(aux.getString("weather"))));
			} catch (Exception e) {
				throw new IllegalArgumentException("In the set weather event weather is not valid");
			}

		}

		return new SetWeatherEvent(time, w);
	}

}
