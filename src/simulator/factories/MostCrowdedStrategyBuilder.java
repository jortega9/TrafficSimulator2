package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy> {

	public MostCrowdedStrategyBuilder() {
		super("most_crowded_lss");
	}

	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int time = 1;
		if (data != null && !data.isEmpty()) {
			time = data.getInt("timeslot");
			return new MostCrowdedStrategy(time);
		}
		return new MostCrowdedStrategy();
	}

}
