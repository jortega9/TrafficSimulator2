package simulator.factories;

import simulator.model.NewCityRoadEvent;

public class NewCityRoadEventBuilder extends NewRoadEventBuilder {

	public NewCityRoadEventBuilder() {
		super("new_city_road");
	}

	NewCityRoadEvent createTheRoad() {
		if (empty)
			return new NewCityRoadEvent();
		return new NewCityRoadEvent(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}

}
