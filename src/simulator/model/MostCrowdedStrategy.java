package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {

	private int _timeSlot;
	private final static int posIni = 0;

	public MostCrowdedStrategy() {
	}

	public MostCrowdedStrategy(int timeSlot) {
		_timeSlot = timeSlot;
	}

	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		if (roads.isEmpty())
			return -1;
		if (currGreen == -1)
			return largestQueue(qs, posIni);
		if (currTime - lastSwitchingTime < _timeSlot)
			return currGreen;
		return largestQueue(qs, (currGreen + 1) % roads.size());
	}

	private int largestQueue(List<List<Vehicle>> qs, int iniPos) {
		int look;
		int msp = iniPos;
		for (int i = 1; i < qs.size(); i++) {
			look = (iniPos + i) % qs.size();
			if (qs.get(msp).size() < qs.get(look).size())
				msp = look;
		}
		return msp;
	}

}