package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event {

	private List<Pair<String, Integer>> cs;

	public SetContClassEvent(int time, List<Pair<String, Integer>> cs) {
		super(time);
		if (cs == null)
			throw new IllegalArgumentException(" In set contamination event: the list can not be null");
		this.cs = cs;
	}

	void execute(RoadMap map) {
		for (Pair<String, Integer> p : cs) {
			map.getVehicle(p.getFirst()).setContClass(p.getSecond());
		}
	}

	@Override
	public String toString() {
		return "Change CO2 class: " + cs.get(0).getFirst().toString() + ' ' + cs.get(0).getSecond().toString();
	}

}
