package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy> {

	public RoundRobinStrategyBuilder() {
		super("round_robin_lss");
	}

	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int time = 1;
		if (data != null && !data.isEmpty()) {
			time = data.getInt("timeslot");
			return new RoundRobinStrategy(time);
		}
		return new RoundRobinStrategy();
	}

}
