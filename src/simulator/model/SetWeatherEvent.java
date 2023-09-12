package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event {

	private List<Pair<String, Weather>> ws;

	public SetWeatherEvent(int time, List<Pair<String, Weather>> ws) {
		super(time);
		if (ws == null || ws.isEmpty())
			throw new IllegalArgumentException("SetWeatherEvent constructor: the info in this event is null");
		this.ws = ws;
	}

	@Override
	void execute(RoadMap map) {
		for (Pair<String, Weather> p : ws) {
			map.getRoad(p.getFirst()).setWeather(p.getSecond());
		}

	}

	@Override
	public String toString() {
		return "Change weather road: " + ws.get(0).getFirst().toString() + ' ' + ws.get(0).getSecond().toString();
	}

}
