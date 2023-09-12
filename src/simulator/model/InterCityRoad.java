package simulator.model;

public class InterCityRoad extends Road {

	public InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	void reduceTotalContamination() {
		if (getTotalCO2() != 0) {
			int c = (100 - getAtmosphCond()) * getTotalCO2() / 100;
			setTotalCO2(c);
		}
	}

	private int getAtmosphCond() {
		switch (getWeather()) {
		case SUNNY:
			return 2;
		case CLOUDY:
			return 3;
		case RAINY:
			return 10;
		case WINDY:
			return 15;
		case STORM:
			return 20;
		}
		return 0;
	}

	void updateSpeedLimit() {
		if (getTotalCO2() > getContLimit())
			setSpeedLimit(getMaxSpeed() / 2);
		else
			setSpeedLimit(getMaxSpeed());
	}

	int calculateVehicleSpeed(Vehicle v) {
		if (getWeather().equals(Weather.STORM))
			return getSpeedLimit() * 8 / 10;
		return getSpeedLimit();
	}

}