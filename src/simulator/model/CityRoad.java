package simulator.model;

public class CityRoad extends Road {

	private final int WINDY_STORM = 10;
	private final int NOT_WINDY_STORM = 2;

	public CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	void reduceTotalContamination() {
		if (getTotalCO2() != 0) {
			if (getWeather().equals(Weather.WINDY) || getWeather().equals(Weather.STORM))
				setTotalCO2(getTotalCO2() - WINDY_STORM);
			else
				setTotalCO2(getTotalCO2() - NOT_WINDY_STORM);
		}
	}

	void updateSpeedLimit() {
	}

	int calculateVehicleSpeed(Vehicle v) {
		return ((11 - v.getContClass()) * this.getSpeedLimit()) / 11;
	}

}